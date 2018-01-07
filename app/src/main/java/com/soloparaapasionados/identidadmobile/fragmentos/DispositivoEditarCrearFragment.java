package com.soloparaapasionados.identidadmobile.fragmentos;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.DispositivoServicioLocal;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.modelo.Dispositivo;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;




import java.util.ArrayList;
import java.util.regex.Pattern;

public class DispositivoEditarCrearFragment extends Fragment
{
    private static final String ARGUMENTO_IMEI = "argumento_imei";

    private TextInputLayout textInputLayoutCelular;
    private TextInputLayout textInputLayoutDescripcion;

    EditText editTextIMEI;
    EditText editTextIdCardSim;
    EditText editTextDescripcion;
    EditText editTextCelular;

    SwitchCompat switchCompatMensajeEnviado;
    SwitchCompat switchCompatMensajeRecibido;
    SwitchCompat switchCompatMensajeValidado;

    Button buttonAceptar;
    Button buttonCancelar;

    CollapsingToolbarLayout collapser;

    LinearLayout linearLayoutContenedor;

    PendingIntent sentPI, deliveredPI;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver, smsEntrandoReceiver;

    private String mImei;
    private int offSetInicial=0;
    public static final int REQUEST_SELECCION_EMPLEADO = 3;

    public DispositivoEditarCrearFragment() {
        // Required empty public constructor
    }

    public static DispositivoEditarCrearFragment newInstance(String imei)
    {
        DispositivoEditarCrearFragment fragment = new DispositivoEditarCrearFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_IMEI, imei);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImei = getArguments().getString(ARGUMENTO_IMEI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dispositivo_editar_crear, container, false);

        EstablecerEventos(root);
        MostrarInformacionCelular();
        editTextCelular.setText("997253205");

        sentPI = PendingIntent.getBroadcast(getActivity(), 0,new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,new Intent(DELIVERED), 0);

        // Carga de datos
        cargaDispositivo();

        return root;
    }

    private void EstablecerEventos(View root)
    {
        textInputLayoutCelular=(TextInputLayout) root.findViewById(R.id.textInputLayoutCelular);
        textInputLayoutDescripcion=(TextInputLayout) root.findViewById(R.id.textInputLayoutDescripcion);

        editTextIMEI=(EditText) root.findViewById(R.id.editTextIMEI);
        editTextIdCardSim=(EditText)root.findViewById(R.id.editTextIdCardSim);
        editTextDescripcion=(EditText)root.findViewById(R.id.editTextDescripcion);
        editTextCelular=(EditText)root.findViewById(R.id.editTextCelular);

        switchCompatMensajeEnviado=(SwitchCompat)root.findViewById(R.id.switchCompatMensajeEnviado);
        switchCompatMensajeRecibido=(SwitchCompat)root.findViewById(R.id.switchCompatMensajeRecibido);
        switchCompatMensajeValidado=(SwitchCompat)root.findViewById(R.id.switchCompatMensajeValidado);
        switchCompatMensajeEnviado.setChecked(false);
        switchCompatMensajeRecibido.setChecked(false);
        switchCompatMensajeValidado.setChecked(false);

        linearLayoutContenedor=(LinearLayout) root.findViewById(R.id.linearLayoutContenedor);

        //Establecer eventos TextWatcher.
        editTextDescripcion.addTextChangedListener(new MiTextWatcher(editTextDescripcion));
        editTextCelular.addTextChangedListener(new MiTextWatcher(editTextCelular));

        // Setear escucha al FAB
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonSalvar);
        fab.setOnClickListener(
            new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // validarDatos()
                    ValidaYSalvaRegistro();
                }
            }
        );

    }

    private void MostrarInformacionCelular()
    {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        //---get the IMEI number---
        String IMEI = tm.getDeviceId();
        if (IMEI != null){
            //Toast.makeText(this, "IMEI number: " + IMEI,  Toast.LENGTH_LONG).show();
            editTextIMEI.setText(IMEI);
        }

        //---get the SIM card ID---
        String simID = tm.getSimSerialNumber();
        if (simID != null)
        {
            //Toast.makeText(this, "SIM card ID: " + simID,Toast.LENGTH_LONG).show();
            editTextIdCardSim.setText(simID);
        }

        /*
       //---get the phone number---
        String telNumber = tm.getLine1Number();
        //String telNumber = tm.get();
        if (telNumber != null)
        {
            //Toast.makeText(this, "Phone number: " + telNumber, Toast.LENGTH_LONG).show();
            editTextCelular.setText(telNumber);
        }*/

    }

    public void insertarDispositivoLocalmente()
    {
        Intent intent = new Intent(getActivity(), DispositivoServicioLocal.class);
        intent.setAction(DispositivoServicioLocal.ACCION_INSERTAR_DISPOSITIVO_ISERVICE);
        Dispositivo dispositivo = generarEntidadDispositivo();
        intent.putExtra(DispositivoServicioLocal.EXTRA_MI_DISPOSITIVO, dispositivo);
        getActivity().startService(intent);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //---create the BroadcastReceiver when the SMS is sent---
        smsSentReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1)
            {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        ActualizarMensajeEnviado();
                        Toast.makeText(getActivity().getBaseContext(), "SMS Enviado Satisfactoriamente", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getActivity().getBaseContext(), "Generic failure",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getActivity().getBaseContext(), "No service",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getActivity().getBaseContext(), "Null PDU",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getActivity().getBaseContext(), "Radio off",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        //---create the BroadcastReceiver when the SMS is delivered---
        smsDeliveredReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        ActualizarMensajeRecibido();
                        Toast.makeText(getActivity().getBaseContext(), "SMS Recibido Satisfactoriamente", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getActivity().getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };

        //---crea el BroadcasReceier cuando el mensaje de texto entra---
        smsEntrandoReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                //---get the SMS message passed in---
                Bundle bundle = intent.getExtras();
                SmsMessage[] msgs = null;
                String str = "SMS from ";
                if (bundle != null)
                {
                    //---retrieve the SMS message received---
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        if (i==0) {
                            //---get the sender address/phone number---
                            str += msgs[i].getOriginatingAddress();
                            str += ": ";
                        }
                        //---get the message body---
                        str += msgs[i].getMessageBody().toString();
                    }

                    String[] mensajeTrozo= str.split(":");
                    String numeroCelularEntrante;
                    String IMEI_Entrante;

                    numeroCelularEntrante=mensajeTrozo[2];
                    IMEI_Entrante=mensajeTrozo[4];

                    ActualizarMensajeValidado(IMEI_Entrante,numeroCelularEntrante);

                    //---display the new SMS message---
                    Toast.makeText(context, numeroCelularEntrante + "  " + IMEI_Entrante, Toast.LENGTH_LONG).show();
                    //---prevent this SMS message from being broadcasted---
                    abortBroadcast();
                    Log.d("SMSReceiver", str);
                }
            }
        };

        //---register the two BroadcastReceivers---
        getActivity().registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        getActivity().registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
        getActivity().registerReceiver(smsEntrandoReceiver, new IntentFilter(RECEIVED));
    }

    private void ActualizarMensajeEnviado()
    {
        ContentResolver r = getActivity().getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        Log.d("MensajeEnviado", "ActualizarMensajeEnviado");
        DatabaseUtils.dumpCursor(r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==1)
        {
            ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(Dispositivos.ENVIADO, 1)
                    .build());
        }

        try
        {
            if (cursorDispositivo.getCount()==1)
            {
                r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
                switchCompatMensajeEnviado.setChecked(true);
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
    }

    private void ActualizarMensajeRecibido(){
        ContentResolver r = getActivity().getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("MensajeEnviado", "ActualizarMensajeEnviado");
        DatabaseUtils.dumpCursor(r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==1)   {
            ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(Dispositivos.RECIBIDO, 1)
                    .build());
        }

        try {
            if (cursorDispositivo.getCount()==1){
                r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
                switchCompatMensajeRecibido.setChecked(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public void ActualizarMensajeValidado(String IMEI_Entrante,String numeroCelularEntrante) {
        ContentResolver r = getActivity().getContentResolver();
        String numeroCelularGrabado=null;
        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("Cabeceras de pedido", "ActualizarMensajeValidado");
        DatabaseUtils.dumpCursor(r.query(Dispositivos.crearUriDispositivo(IMEI_Entrante), null, null, null, null));

        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(IMEI_Entrante), null, null, null, null);

        if (!cursorDispositivo.moveToFirst())
            return;

        if (cursorDispositivo.getCount()==1)   {
            numeroCelularGrabado=cursorDispositivo.getString(cursorDispositivo.getColumnIndexOrThrow(Dispositivos.NUMERO_CELULAR));

            if (numeroCelularGrabado.equals(numeroCelularEntrante)){
                ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                        .withValue(Dispositivos.VALIDADO, 1)
                        .build());
            }
        }

        try {
            if (cursorDispositivo.getCount()==1 && numeroCelularGrabado.equals(numeroCelularEntrante))   {
                r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
                switchCompatMensajeValidado.setChecked(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    //Validar datos del dispositivo
    private void ValidaYSalvaRegistro()
    {
        boolean error = false;

        if (!esDescripcionDispositivoValido())
        {
            error = true;
        }

        if (!esCelularValido())
        {
            error = true;
        }

        if (error)
        {
            return;
        }

        ContentResolver r = getActivity().getContentResolver();
        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==0)
        {
            insertarDispositivoLocalmente();
        }
        else
        {
            actualizarDispositivoLocalmente();
        }

        String mensajeTextoValidacion="";
        mensajeTextoValidacion= "Boxer-Verificacion-Celular:" + editTextCelular.getText().toString() + ":IMEI:" + editTextIMEI.getText().toString();

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(editTextCelular.getText().toString(),   null, mensajeTextoValidacion, sentPI, deliveredPI);

        Toast.makeText(getActivity(), "Thank You!", Toast.LENGTH_SHORT).show();
        /*if (mIdEmpleado != null) {
            actualizarEmpleadoLocalmente();
            muestraPantallaDetalleEmpleados();
        }else {
            insertarEmpleadoLocalmente();
            muestraPantallaDetalleEmpleados();
        }*/

    }

    //Descripcion del Dispositivo
    private boolean esDescripcionDispositivoValido()
    {
        String descripcionDispositivo=editTextDescripcion.getText().toString().trim();

        if (descripcionDispositivo.isEmpty())
        {
            textInputLayoutDescripcion.setError(getString(R.string.error_campo_vacio));
            textInputLayoutDescripcion.requestFocus();
            return false;
        }

        if (descripcionDispositivo.length() > 30)
        {
            textInputLayoutDescripcion.setError(getString(R.string.error_campo_grande));
            textInputLayoutDescripcion.requestFocus();
            return false;
        }

        textInputLayoutDescripcion.setErrorEnabled(false);

        return true;
    }

    //Celular del Dispositivo
    private boolean esCelularValido()
    {
        String celular=editTextCelular.getText().toString().trim();

        if (celular.isEmpty())
        {
            textInputLayoutCelular.setError(getString(R.string.error_campo_vacio));
            textInputLayoutCelular.requestFocus();
            return false;
        }

        if (celular.length() > 15)
        {
            textInputLayoutCelular.setError(getString(R.string.error_campo_grande));
            textInputLayoutCelular.requestFocus();
            return false;
        }

        textInputLayoutCelular.setErrorEnabled(false);

        return true;
    }

    //Generar entidad empleado
    private Dispositivo generarEntidadDispositivo()
    {
        Dispositivo dispositivo =new Dispositivo();

        dispositivo.setImei(this.editTextIMEI.getText().toString().trim());
        dispositivo.setIdSimCard(this.editTextIdCardSim.getText().toString().trim());
        dispositivo.setDescripcion(this.editTextDescripcion.getText().toString().trim());
        dispositivo.setNumeroCelular(this.editTextCelular.getText().toString().trim());
        dispositivo.setEnviado(false);
        dispositivo.setRecibido(false);
        dispositivo.setValidado(false);
        dispositivo.setEstadoSincronizacion(EstadoRegistro.REGISTRADO_LOCALMENTE);

        return dispositivo;
    }

    private void actualizarDispositivoLocalmente()
    {
        Intent intent = new Intent(getActivity(), DispositivoServicioLocal.class);
        intent.setAction(DispositivoServicioLocal.ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE);
        Dispositivo dispositivo=generarEntidadDispositivo();
        intent.putExtra(DispositivoServicioLocal.EXTRA_MI_DISPOSITIVO, dispositivo);
        getActivity().startService(intent);
    }

    private void cargaDispositivo()
    {
        ContentResolver r = getActivity().getContentResolver();
        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if( cursorDispositivo != null && cursorDispositivo.moveToLast())
        {
            if (cursorDispositivo.getCount()>0)
            {
                muestraDispositivo(cursorDispositivo);

                MostrarInformacionCelular();
            }
        }

    }

    private void muestraDispositivo(Cursor cursorDispositivo)
    {
        this.editTextIdCardSim.setText(cursorDispositivo.getString(cursorDispositivo.getColumnIndex(Dispositivos.ID_SIM_CARD)));
        this.editTextDescripcion.setText(cursorDispositivo.getString(cursorDispositivo.getColumnIndex(Dispositivos.DESCRIPCION)));
        this.editTextCelular.setText(cursorDispositivo.getString(cursorDispositivo.getColumnIndex(Dispositivos.NUMERO_CELULAR)));

        //Mensaje Enviado
        boolean enviado=false;
        enviado=cursorDispositivo.getInt(cursorDispositivo.getColumnIndex(Dispositivos.ENVIADO))==1?true:false;

        this.switchCompatMensajeEnviado.setChecked(enviado);

        //Mensaje Recibido
        boolean recibido=false;
        recibido=cursorDispositivo.getInt(cursorDispositivo.getColumnIndex(Dispositivos.RECIBIDO))==1?true:false;

        this.switchCompatMensajeRecibido.setChecked(recibido);

        //Mensaje Validado
        boolean validado=false;
        validado=cursorDispositivo.getInt(cursorDispositivo.getColumnIndex(Dispositivos.VALIDADO))==1?true:false;

        this.switchCompatMensajeValidado.setChecked(validado);
    }

    //Validar datos del empleado online
    private class MiTextWatcher implements TextWatcher
    {

        private View view;

        private MiTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable)
        {
            int identificadorVista=view.getId();
            switch (identificadorVista)
            {
                case R.id.editTextDescripcion:
                    esDescripcionDispositivoValido();
                    break;
                case R.id.editTextCelular:
                    esCelularValido();
                    break;

            }
        }
    }

}
