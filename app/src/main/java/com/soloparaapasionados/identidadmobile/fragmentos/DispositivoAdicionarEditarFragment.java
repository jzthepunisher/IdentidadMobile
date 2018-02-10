package com.soloparaapasionados.identidadmobile.fragmentos;

import android.app.Activity;
import android.app.ActivityOptions;
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
import android.os.Bundle;
import android.os.RemoteException;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.actividades.DispostivoAdicionarEditarActivity;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoAdicionarEditarActivity;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoDetalleActivity;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoListadoActivity;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadoSeleccionadoAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosSugerenciaListaAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

import java.util.ArrayList;

/**
 * Created by USUARIO on 27/05/2017.
 */

public class DispositivoAdicionarEditarFragment extends Fragment
    implements EmpleadosListaAdaptador.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String ARGUMENTO_IMEI = "argumento_imei";

    TextView editTextIMEI;
    TextView editTextIdCardSim;
    EditText editTextCelular;
    TextView textViewCantidadEmpleadosAsignados;

    SwitchCompat switchCompatMensajeEnviado;
    SwitchCompat switchCompatMensajeRecibido;
    SwitchCompat switchCompatMensajeValidado;

    Button buttonAceptar;
    Button buttonCancelar;

    CollapsingToolbarLayout collapser;

    private RecyclerView recyclerViewListadoEmpleado;
    private LinearLayoutManager linearLayoutManager;
    private EmpleadosListaAdaptador empleadosListaAdaptador;

    LinearLayout linearLayoutAnadirEmpleado;
    LinearLayout linearLayoutContenedor;

    PendingIntent sentPI, deliveredPI;
    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver, smsEntrandoReceiver;

    private String mImei;
    private int offSetInicial=0;
    public static final int REQUEST_SELECCION_EMPLEADO = 3;

    public DispositivoAdicionarEditarFragment()
    {
    }

    public static DispositivoAdicionarEditarFragment newInstance(String imei) {
        DispositivoAdicionarEditarFragment fragment = new DispositivoAdicionarEditarFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_IMEI, imei);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImei = getArguments().getString(ARGUMENTO_IMEI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_dispositivo_adicionar_editar, container, false);

        EstablecerEventos(root);
        MostrarInformacionCelular();
        editTextCelular.setText("997253205");

        sentPI = PendingIntent.getBroadcast(getActivity(), 0,new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(getActivity(), 0,new Intent(DELIVERED), 0);

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);

        return root;
    }

    private void EstablecerEventos(View root){

        editTextIMEI=(TextView)root.findViewById(R.id.editTextIMEI);
        editTextIdCardSim=(TextView)root.findViewById(R.id.editTextIdCardSim);
        editTextCelular=(EditText)root.findViewById(R.id.editTextCelular);
        textViewCantidadEmpleadosAsignados=(TextView)root.findViewById(R.id.textViewCantidadEmpleadosAsignados);

        switchCompatMensajeEnviado=(SwitchCompat)root.findViewById(R.id.switchCompatMensajeEnviado);
        switchCompatMensajeRecibido=(SwitchCompat)root.findViewById(R.id.switchCompatMensajeRecibido);
        switchCompatMensajeValidado=(SwitchCompat)root.findViewById(R.id.switchCompatMensajeValidado);
        switchCompatMensajeEnviado.setChecked(false);
        switchCompatMensajeRecibido.setChecked(false);
        switchCompatMensajeValidado.setChecked(false);

        linearLayoutAnadirEmpleado=(LinearLayout) root.findViewById(R.id.linearLayoutAnadirEmpleado);
        linearLayoutContenedor=(LinearLayout) root.findViewById(R.id.linearLayoutContenedor);

        // Preparar lista
        recyclerViewListadoEmpleado = (RecyclerView) root.findViewById(R.id.recyclerViewListadoEmpleado);
        recyclerViewListadoEmpleado.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoEmpleado.setLayoutManager(linearLayoutManager);

        empleadosListaAdaptador = new EmpleadosListaAdaptador(getActivity(), this);
        recyclerViewListadoEmpleado.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoEmpleado.setAdapter(empleadosListaAdaptador);

        linearLayoutAnadirEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImei=editTextIMEI.getText().toString();
                muestraListadoEmpleado(mImei);
            }
        });

        // Setear escucha al FAB
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonSalvar);
        fab.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // validarDatos()
                        insertarDispositivo();

                        String mensajeTextoValidacion="";
                        mensajeTextoValidacion= "Boxer-Verificacion-Celular:" + editTextCelular.getText().toString() + ":IMEI:" + editTextIMEI.getText().toString();

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(editTextCelular.getText().toString(),   null, mensajeTextoValidacion, sentPI, deliveredPI);

                    }
                }
        );

    }

    private void muestraListadoEmpleado(String imei){

        Intent intent = new Intent(getActivity(), EmpleadoListadoActivity.class);
        intent.putExtra(DispostivoAdicionarEditarActivity.EXTRA_IMEI, imei);

        //ActivityOptions options0 = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        //getActivity().startActivity(intent, options0.toBundle());

        //startActivityForResult(intent, REQUEST_SELECCION_EMPLEADO, options0.toBundle());
        startActivityForResult(intent, REQUEST_SELECCION_EMPLEADO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_SELECCION_EMPLEADO:
                    // Iniciar loader
                   // getActivity().getSupportLoaderManager().restartLoader(1, null,  this);
                    break;
            }
        }
    }

    @Override
    public void onClick(EmpleadosListaAdaptador.ViewHolder holder, String idEmpleado,int position) {
        //muestraPantallaDetalle(idEmpleado);
    }

    @Override
    public void onResume() {
        super.onResume();

        //---create the BroadcastReceiver when the SMS is sent---
        smsSentReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
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
        smsDeliveredReceiver = new BroadcastReceiver(){
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

    @Override
    public void onPause() {
        super.onPause();
        //---unregister the two BroadcastReceivers---
        getActivity().unregisterReceiver(smsSentReceiver);
        getActivity().unregisterReceiver(smsDeliveredReceiver);
        getActivity().unregisterReceiver(smsEntrandoReceiver);
    }

    public void insertarDispositivo()
    {
        ContentResolver r = getActivity().getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        Log.d("Cabeceras de pedido", "Cabeceras de pedido");
        DatabaseUtils.dumpCursor(r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==0)   {
            ops.add(ContentProviderOperation.newInsert(ContratoCotizacion.Dispositivos.URI_CONTENIDO)
                    .withValue(ContratoCotizacion.Dispositivos.IMEI, this.editTextIMEI.getText().toString())

                    .withValue(ContratoCotizacion.Dispositivos.ID_SIM_CARD, this.editTextIdCardSim.getText().toString())
                    .withValue(ContratoCotizacion.Dispositivos.NUMERO_CELULAR, this.editTextCelular.getText().toString())
                    .withValue(ContratoCotizacion.Dispositivos.ENVIADO, 0)
                    .withValue(ContratoCotizacion.Dispositivos.RECIBIDO, 0)
                    .withValue(ContratoCotizacion.Dispositivos.VALIDADO, 0)
                    .build());
        }else{
            ops.add(ContentProviderOperation.newUpdate(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(ContratoCotizacion.Dispositivos.IMEI, this.editTextIMEI.getText().toString())

                    .withValue(ContratoCotizacion.Dispositivos.ID_SIM_CARD, this.editTextIdCardSim.getText().toString())
                    .withValue(ContratoCotizacion.Dispositivos.NUMERO_CELULAR, this.editTextCelular.getText().toString())
                    .withValue(ContratoCotizacion.Dispositivos.ENVIADO, 0)
                    .withValue(ContratoCotizacion.Dispositivos.RECIBIDO, 0)
                    .withValue(ContratoCotizacion.Dispositivos.VALIDADO, 0)
                    .build());
        }

        try {
            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void ActualizarMensajeEnviado(){
        ContentResolver r = getActivity().getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("MensajeEnviado", "ActualizarMensajeEnviado");
        DatabaseUtils.dumpCursor(r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==1)   {
            ops.add(ContentProviderOperation.newUpdate(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(ContratoCotizacion.Dispositivos.ENVIADO, 1)
                    .build());
        }

        try {
            if (cursorDispositivo.getCount()==1){
                r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
                switchCompatMensajeEnviado.setChecked(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void MostrarInformacionCelular(){

        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        //---get the IMEI number---
        String IMEI = tm.getDeviceId();
        if (IMEI != null){
            //Toast.makeText(this, "IMEI number: " + IMEI,  Toast.LENGTH_LONG).show();
            editTextIMEI.setText(IMEI);
        }

        //---get the SIM card ID---
        String simID = tm.getSimSerialNumber();
        if (simID != null){
            //Toast.makeText(this, "SIM card ID: " + simID,Toast.LENGTH_LONG).show();
            editTextIdCardSim.setText(simID);
        }


        //---get the phone number---
        String telNumber = tm.getLine1Number();
        //String telNumber = tm.get();
        if (telNumber != null){
            //Toast.makeText(this, "Phone number: " + telNumber, Toast.LENGTH_LONG).show();
            editTextCelular.setText(telNumber);
        }

    }

    private void ActualizarMensajeRecibido(){
        ContentResolver r = getActivity().getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("MensajeEnviado", "ActualizarMensajeEnviado");
        DatabaseUtils.dumpCursor(r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==1)   {
            ops.add(ContentProviderOperation.newUpdate(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(ContratoCotizacion.Dispositivos.RECIBIDO, 1)
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
        DatabaseUtils.dumpCursor(r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(IMEI_Entrante), null, null, null, null));

        Cursor cursorDispositivo=r.query(ContratoCotizacion.Dispositivos.crearUriDispositivo(IMEI_Entrante), null, null, null, null);

        if (!cursorDispositivo.moveToFirst())
            return;

        if (cursorDispositivo.getCount()==1)   {
            numeroCelularGrabado=cursorDispositivo.getString(cursorDispositivo.getColumnIndexOrThrow(ContratoCotizacion.Dispositivos.NUMERO_CELULAR));

            if (numeroCelularGrabado.equals(numeroCelularEntrante)){
                ops.add(ContentProviderOperation.newUpdate(ContratoCotizacion.Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                        .withValue(ContratoCotizacion.Dispositivos.VALIDADO, 1)
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

    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case 1:
                //return new CursorLoader(getActivity(), ContratoCotizacion.Empleados.crearUriEmpleadoOffSet(String.valueOf(offSetInicial)), null, null, null, null);
                // return new CursorLoader(getActivity(), ContratoCotizacion.DispositivosEmpleados.crearUriDispositivoEmpleado(editTextIMEI.getText().toString()), null, null, null, null);
                return new CursorLoader(getActivity(), ContratoCotizacion.DispositivosEmpleadosTemporal.crearUriDispositivoEmpleadoTemporal(editTextIMEI.getText().toString()), null, null, null, null);

          }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Creando Adaptador para CargoSpinner

        switch (loader.getId()){
            case 1:
                if(data!=null){
                    if (empleadosListaAdaptador != null) {
                        empleadosListaAdaptador.swapCursor(data);
                        textViewCantidadEmpleadosAsignados.setText(String.valueOf(empleadosListaAdaptador.getItemCount()));

                    }
                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
}
