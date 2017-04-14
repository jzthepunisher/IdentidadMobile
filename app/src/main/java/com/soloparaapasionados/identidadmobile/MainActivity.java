package com.soloparaapasionados.identidadmobile;

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
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView editTextIMEI;
    TextView editTextIdCardSim;
    EditText editTextCelular;

    SwitchCompat switchCompatMensajeEnviado;
    SwitchCompat switchCompatMensajeRecibido;
    SwitchCompat switchCompatMensajeValidado;

    Button buttonAceptar;
    Button buttonCancelar;

    CollapsingToolbarLayout collapser;

    String SENT = "SMS_SENT";
    String DELIVERED = "SMS_DELIVERED";
    String RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    PendingIntent sentPI, deliveredPI;
    BroadcastReceiver smsSentReceiver, smsDeliveredReceiver, smsEntrandoReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        setToolbar();// Añadir action bar


        EstablecerEventos();
        MostrarInformacionCelular();
        editTextCelular.setText("997253205");

        sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0,new Intent(DELIVERED), 0);


        //getApplicationContext().deleteDatabase("cotizaciones.db");

    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Registro de Dispositivo");


        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void EstablecerEventos(){

        editTextIMEI=(TextView)findViewById(R.id.editTextIMEI);
        editTextIdCardSim=(TextView)findViewById(R.id.editTextIdCardSim);
        editTextCelular=(EditText)findViewById(R.id.editTextCelular);

        switchCompatMensajeEnviado=(SwitchCompat)findViewById(R.id.switchCompatMensajeEnviado);
        switchCompatMensajeRecibido=(SwitchCompat)findViewById(R.id.switchCompatMensajeRecibido);
        switchCompatMensajeValidado=(SwitchCompat)findViewById(R.id.switchCompatMensajeValidado);
        switchCompatMensajeEnviado.setChecked(false);
        switchCompatMensajeRecibido.setChecked(false);
        switchCompatMensajeValidado.setChecked(false);

// Setear escucha al FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonSalvar);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // validarDatos()

                        insertarDispositivo();

                        String mensajeTextoValidacion="";
                        mensajeTextoValidacion= "Boxer-Verificacion-Celular:" + editTextCelular.getText().toString();

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(editTextCelular.getText().toString(),   null, mensajeTextoValidacion, sentPI, deliveredPI);


                    }
                }
        );
        /*buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validarDatos()

                insertarDispositivo();

                String mensajeTextoValidacion="";
                mensajeTextoValidacion= "Boxer-Verificacion-Celular:" + editTextCelular.getText().toString();
                
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(editTextCelular.getText().toString(),   null, mensajeTextoValidacion, sentPI, deliveredPI);


            }
        });*/

        /*buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validarDatos()
            }
        });*/
    }

    private void MostrarInformacionCelular(){

        TelephonyManager tm = (TelephonyManager)  getSystemService(Context.TELEPHONY_SERVICE);

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
                        Toast.makeText(getBaseContext(), "SMS Enviado Satisfactoriamente", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
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
                        Toast.makeText(getBaseContext(), "SMS Recibido Satisfactoriamente", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
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

                    ActualizarMensajeValidado(numeroCelularEntrante,IMEI_Entrante);

                    //---display the new SMS message---
                    Toast.makeText(context, numeroCelularEntrante + "  " + IMEI_Entrante, Toast.LENGTH_LONG).show();
                    //---prevent this SMS message from being broadcasted---
                    abortBroadcast();
                    Log.d("SMSReceiver", str);
                }
            }
        };

        //---register the two BroadcastReceivers---
        registerReceiver(smsSentReceiver, new IntentFilter(SENT));
        registerReceiver(smsDeliveredReceiver, new IntentFilter(DELIVERED));
        registerReceiver(smsEntrandoReceiver, new IntentFilter(RECEIVED));
    }

    @Override
    public void onPause() {
        super.onPause();
        //---unregister the two BroadcastReceivers---
        unregisterReceiver(smsSentReceiver);
        unregisterReceiver(smsDeliveredReceiver);
        unregisterReceiver(smsEntrandoReceiver);
    }

    public void insertarDispositivo() {
        ContentResolver r = getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("Cabeceras de pedido", "Cabeceras de pedido");
        DatabaseUtils.dumpCursor(r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==0)   {
            ops.add(ContentProviderOperation.newInsert(Dispositivos.URI_CONTENIDO)
                    .withValue(Dispositivos.IMEI, this.editTextIMEI.getText().toString())
                    .withValue(Dispositivos.ID_TIPO_DISPOSITIVO, 1)
                    .withValue(Dispositivos.ID_SIM_CARD, this.editTextIdCardSim.getText().toString())
                    .withValue(Dispositivos.NUMERO_CELULAR, this.editTextCelular.getText().toString())
                    .withValue(Dispositivos.ENVIADO, 0)
                    .withValue(Dispositivos.RECIBIDO, 0)
                    .withValue(Dispositivos.VALIDADO, 0)
                    .build());
        }else{
            ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(Dispositivos.IMEI, this.editTextIMEI.getText().toString())
                    .withValue(Dispositivos.ID_TIPO_DISPOSITIVO, 1)
                    .withValue(Dispositivos.ID_SIM_CARD, this.editTextIdCardSim.getText().toString())
                    .withValue(Dispositivos.NUMERO_CELULAR, this.editTextCelular.getText().toString())
                    .withValue(Dispositivos.ENVIADO, 0)
                    .withValue(Dispositivos.RECIBIDO, 0)
                    .withValue(Dispositivos.VALIDADO, 0)
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
        ContentResolver r = getContentResolver();

        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("MensajeEnviado", "ActualizarMensajeEnviado");
        DatabaseUtils.dumpCursor(r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null));

        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()), null, null, null, null);

        if (cursorDispositivo.getCount()==1)   {
            ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                    .withValue(Dispositivos.ENVIADO, 1)
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

    private void ActualizarMensajeRecibido(){
        ContentResolver r = getContentResolver();

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
        ContentResolver r = getContentResolver();
        String numeroCelularGrabado="";
        // Lista de operaciones
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();


        Log.d("Cabeceras de pedido", "Cabeceras de pedido");
        DatabaseUtils.dumpCursor(r.query(Dispositivos.crearUriDispositivo(IMEI_Entrante), null, null, null, null));

        Cursor cursorDispositivo=r.query(Dispositivos.crearUriDispositivo(IMEI_Entrante), null, null, null, null);

        if (cursorDispositivo.getCount()==1)   {
            numeroCelularGrabado=cursorDispositivo.getString(cursorDispositivo.getColumnIndexOrThrow(Dispositivos.NUMERO_CELULAR));

            if (numeroCelularGrabado==numeroCelularEntrante){
                ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo(this.editTextIMEI.getText().toString()))
                        .withValue(Dispositivos.VALIDADO, 1)
                        .build());
            }
        }

        try {
            if (cursorDispositivo.getCount()==1 && numeroCelularGrabado==numeroCelularEntrante)   {
                r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
                switchCompatMensajeValidado.setChecked(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
}
