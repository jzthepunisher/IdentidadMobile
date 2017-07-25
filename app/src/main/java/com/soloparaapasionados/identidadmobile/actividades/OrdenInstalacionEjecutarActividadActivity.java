package com.soloparaapasionados.identidadmobile.actividades;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.EjecucionActividadInicioTerminoServicioLocal;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.Constants;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.FetchAddressIntentService;
import com.soloparaapasionados.identidadmobile.dialogos.SimpleDialog;
import com.soloparaapasionados.identidadmobile.fragmentos.OrdenInstalacionEjecutarActividadFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OrdenInstalacionEjecutarActividadActivity extends AppCompatActivity
implements OrdenInstalacionEjecutarActividadFragment.OrdenInstalacionEjecutarActividadFragmentListener ,
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener,
        SimpleDialog.OnSimpleDialogListener {

    private final String LOG_TAG = "EjecutarActividades";
    private String idOrdenInstalacion;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    public static String mFechaInicioTerminadoEjecucion;
    public static String mIdOrdenInstalacion;

    public static double mLongitudInicio_TerminoInicio;
    public static double mLatitudInicio_TerminoInicio;
    public static String mdireccionInicio_TerminoInicio;
    public static String mFechaHoraInicio_TerminoInicio;

    public static double mLongitudTermino_TerminoInicio;
    public static double mLatitudTermino_TerminoInicio;
    public static String mdireccionTermino_TerminoInicio;
    public static String mFechaHoraTermino_TerminoInicio;

    Boolean mOnButtonActividadIniciada_IT_Clicked=false;
    Boolean mOnButtonActividadTerminada_IT_Clicked=false;

    private AddressResultReceiver mResultReceiver;
    private String mAddressOutput;
    private Location mLastLocation;
    private boolean mAddressRequested;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_instalacion_ejecutar_actividad);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API).build();

        mResultReceiver = new AddressResultReceiver(new Handler());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("toca para iniciar o terminar la actividad");


        idOrdenInstalacion = getIntent().getStringExtra(OrdenInstalacionListadoActivity.EXTRA_ID_ORDEN_INSTALACION);

        OrdenInstalacionEjecutarActividadFragment ordenInstalacionEjecutarActividadFragment = (OrdenInstalacionEjecutarActividadFragment)
                getSupportFragmentManager().findFragmentById(R.id.ordenes_instalacion_ejecuta_actividad_container);

        if (ordenInstalacionEjecutarActividadFragment == null) {
            ordenInstalacionEjecutarActividadFragment = OrdenInstalacionEjecutarActividadFragment.newInstance(idOrdenInstalacion);
            getSupportFragmentManager().beginTransaction().add(R.id.ordenes_instalacion_ejecuta_actividad_container, ordenInstalacionEjecutarActividadFragment)
                    .commit();
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onButtonActividadIniciada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position) {
        mFechaInicioTerminadoEjecucion=fechaInicioTerminadoEjecucion;
        mIdOrdenInstalacion=idOrdenInstalacion;
        mOnButtonActividadIniciada_IT_Clicked=true;
        mOnButtonActividadTerminada_IT_Clicked=false;

        // Obtención del manejador de fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        new SimpleDialog().show(fragmentManager, "SimpleDialog");
    }

    @Override
    public void onPossitiveButtonClick() {
        obtienGeoReferencia();
    }

    @Override
    public void onNegativeButtonClick() {
       /* Toast.makeText(
                this,
                "Botón Negativo Pulsado",
                Toast.LENGTH_LONG)
                .show();*/
    }

    @Override
    public void onButtonActividadTerminada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position) {
        mOnButtonActividadTerminada_IT_Clicked=true;
        mOnButtonActividadIniciada_IT_Clicked=false;
        mFechaInicioTerminadoEjecucion=fechaInicioTerminadoEjecucion;
        mIdOrdenInstalacion=idOrdenInstalacion;

        // Obtención del manejador de fragmentos
        FragmentManager fragmentManager = getSupportFragmentManager();
        new SimpleDialog().show(fragmentManager, "SimpleDialog");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //googleApiClient.connect();

    }

    @Override
    protected void onStop() {

        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.

    }

    private void obtienGeoReferencia()
    {
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);// Actualiza ubicacion cada segundo

        try{
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }catch (SecurityException se){
            Log.i(LOG_TAG,"GoogleApiClient conexion ha sido ");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG,"GoogleApiClient conexion ha sido suspendido");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG,"GoogleApiClient conexion ha sido fallida");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(LOG_TAG,location.toString());


        mLatitudInicio_TerminoInicio=location.getLatitude();
        mLongitudInicio_TerminoInicio=location.getLongitude();
        if (mLongitudInicio_TerminoInicio!=0.0 && mLatitudInicio_TerminoInicio!=0.0)
        {
            Date miFecha = Calendar.getInstance().getTime();
            String miFechaCadena=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(miFecha);
            mFechaHoraInicio_TerminoInicio=miFechaCadena;
            mFechaHoraTermino_TerminoInicio=miFechaCadena;
            Toast.makeText(this,miFechaCadena + "  Latitud : " + String.valueOf(location.getLatitude())
                            + " Longitud : " + String.valueOf(location.getLongitude()),
                    Toast.LENGTH_LONG ).show();


            mLastLocation=new Location("");
            mLastLocation.setLatitude(mLatitudInicio_TerminoInicio);
            mLastLocation.setLongitude(mLongitudInicio_TerminoInicio);
            startIntentService();

            googleApiClient.disconnect();


        }

        //txtOutput.setText(location.toString());
    }

    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(FetchAddressIntentService.EXTRA_ID_TURNO,"");
        intent.putExtra(FetchAddressIntentService.EXTRA_ID_UNIDAD_REACCION,"");

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        this.startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
                OrdenInstalacionEjecutarActividadActivity.mdireccionInicio_TerminoInicio=mAddressOutput;
                OrdenInstalacionEjecutarActividadActivity.mdireccionTermino_TerminoInicio=mAddressOutput;
                //MapaTermicoAgrupacionFragment.setSubTitulo();
            }

            if (mOnButtonActividadIniciada_IT_Clicked==true)
            {
                actualizarOrdenInstalacionEjecucionInicioTermino_Inicio_ActividadLocalmente();
            }
            else
            {
                actualizarOrdenInstalacionEjecucionInicioTermino_Termino_ActividadLocalmente();
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            //updateUIWidgets();
        }
    }

    private void displayAddressOutput() {
        //mLocationAddressTextView.setText(mAddressOutput);
        showToast(mAddressOutput);
    }

    private void showToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void actualizarOrdenInstalacionEjecucionInicioTermino_Inicio_ActividadLocalmente()
    {
        Intent intent = new Intent(this, EjecucionActividadInicioTerminoServicioLocal.class);
        intent.setAction(EjecucionActividadInicioTerminoServicioLocal.ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_INICIO_ISERVICE);

        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_INICIO_TERMINADO_EJECUCION,this.mFechaInicioTerminadoEjecucion);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_ID_ORDEN_INSTALACION,this.mIdOrdenInstalacion);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_INICIADO,true);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_HORA_INICIO,this.mFechaHoraInicio_TerminoInicio);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LATITUD_INICIO,mLatitudInicio_TerminoInicio);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LONGITUD_INICIO,mLongitudInicio_TerminoInicio);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_DIRECCION_INICIO,this.mdireccionInicio_TerminoInicio);

        this.startService(intent);
    }

    private void actualizarOrdenInstalacionEjecucionInicioTermino_Termino_ActividadLocalmente()
    {
        Intent intent = new Intent(this, EjecucionActividadInicioTerminoServicioLocal.class);
        intent.setAction(EjecucionActividadInicioTerminoServicioLocal.ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_TERMINO_ISERVICE);

        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_INICIO_TERMINADO_EJECUCION,this.mFechaInicioTerminadoEjecucion);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_ID_ORDEN_INSTALACION,this.mIdOrdenInstalacion);

        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_TERMINADO,true);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_HORA_TERMINO,this.mFechaHoraTermino_TerminoInicio);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LATITUD_TERMINO,mLatitudTermino_TerminoInicio);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LONGITUD_TERMINO,mLongitudTermino_TerminoInicio);
        intent.putExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_DIRECCION_TERMINO,this.mdireccionTermino_TerminoInicio);

        this.startService(intent);
    }
}
