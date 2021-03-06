package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.BuildConfig;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.actividades.RastreadorActivity;
import com.soloparaapasionados.identidadmobile.modelo.CorrelativoTabla;
import com.soloparaapasionados.identidadmobile.modelo.Grupo;
import com.soloparaapasionados.identidadmobile.modelo.ProgramacionRastreoGpsDetalle;
import com.soloparaapasionados.identidadmobile.modelo.UbicacionDispositivoGps;
import com.soloparaapasionados.identidadmobile.serviciotransporttracker.TrackerTaskService;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.CorrelativosTabla;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.GruposTabla;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.ProgramacionesRastregoGpsDetalleTabla;



import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by USUARIO on 10/02/2018.
 */

public class RastreadorServicio extends Service implements LocationListener
{
    private static final String TAG = RastreadorServicio.class.getSimpleName();
    public static final String STATUS_INTENT = "status";

    private static final int NOTIFICATION_ID = 2;
    private static final int FOREGROUND_SERVICE_ID = 2;
    private static final int CONFIG_CACHE_EXPIRY = 600;  // 10 minutes.

    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mFirebaseTransportRef;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private LinkedList<Map<String, Object>> mTransportStatuses = new LinkedList<>();
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotificationBuilder;
    private PowerManager.WakeLock mWakelock;

    private SharedPreferences mPrefs;

    private int mDiaSemana=0;

    public RastreadorServicio()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        buildNotification();

        setStatusMessage(R.string.connecting);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        mPrefs = getSharedPreferences(getString(R.string.prefs), MODE_PRIVATE);
        String email = mPrefs.getString(getString(R.string.email), "");
        String password = mPrefs.getString(getString(R.string.password), "");

        if (validaRangoInicioFin() == true)
        {
            Toast.makeText(this, "11111111111111111", Toast.LENGTH_LONG).show();
            fetchRemoteConfig();
            //////loadPreviousStatuses();
            startLocationTracking();
        }
        else
        {
            Toast.makeText(this, "222222222222222222", Toast.LENGTH_LONG).show();
            stopSelf();
        }

        //////authenticate(email, password);
    }

    @Override
    public void onDestroy()
    {
        // Set activity title to not tracking.
        setStatusMessage(R.string.not_tracking);
        // Stop the persistent notification.
        mNotificationManager.cancel(NOTIFICATION_ID);
        // Stop receiving location updates.
        if (mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,RastreadorServicio.this);
        }
        // Release the wakelock
        if (mWakelock != null)
        {
            mWakelock.release();
        }

        super.onDestroy();
    }

    private void authenticate(String email, String password)
    {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(Task<AuthResult> task)
                {
                    Log.i(TAG, "authenticate: " + task.isSuccessful());
                    if (task.isSuccessful())
                    {
                        fetchRemoteConfig();
                        loadPreviousStatuses();
                    }
                    else
                    {
                        Toast.makeText(RastreadorServicio.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        stopSelf();
                    }
                }
            });
    }

    private void fetchRemoteConfig()
    {
        long cacheExpiration = CONFIG_CACHE_EXPIRY;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled())
        {
            cacheExpiration = 0;
        }

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Log.i(TAG, "Remote config fetched");
                        mFirebaseRemoteConfig.activateFetched();
                    }
                });
    }

    /**
     * Loads previously stored statuses from Firebase, and once retrieved,
     * start location tracking.
     */
    private void loadPreviousStatuses()
    {
        String transportId = mPrefs.getString(getString(R.string.transport_id), "");
        FirebaseAnalytics.getInstance(this).setUserProperty("transportID", transportId);
        String path = getString(R.string.firebase_path) + transportId;

        mFirebaseTransportRef = FirebaseDatabase.getInstance().getReference(path);
        mFirebaseTransportRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                if (snapshot != null)
                {
                    for (DataSnapshot transportStatus : snapshot.getChildren())
                    {
                        mTransportStatuses.add(Integer.parseInt(transportStatus.getKey()), (Map<String, Object>) transportStatus.getValue());
                    }
                }

                startLocationTracking();
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // TODO: Handle gracefully
            }
        });
    }

    private GoogleApiClient.ConnectionCallbacks mLocationRequestCallback = new GoogleApiClient.ConnectionCallbacks()
    {
        @Override
        public void onConnected(Bundle bundle)
        {
            LocationRequest request = new LocationRequest();
            request.setInterval(mFirebaseRemoteConfig.getLong("LOCATION_REQUEST_INTERVAL"));
            request.setFastestInterval(mFirebaseRemoteConfig.getLong("LOCATION_REQUEST_INTERVAL_FASTEST"));
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            //&& ActivityCompat.checkSelfPermission(TrackerService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            if ( ContextCompat.checkSelfPermission(RastreadorServicio.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, RastreadorServicio.this);
                setStatusMessage(R.string.tracking);
            }

            // Hold a partial wake lock to keep CPU awake when the we're tracking location.
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            mWakelock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakelockTag");
            mWakelock.acquire();
        }

        @Override
        public void onConnectionSuspended(int reason)
        {
            // TODO: Handle gracefully
        }
    };

    /**
     * Starts location tracking by creating a Google API client, and
     * requesting location updates.
     */
    private void startLocationTracking()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(mLocationRequestCallback)
            .addApi(LocationServices.API)
            .build();

        mGoogleApiClient.connect();
    }

    /**
     * Determines if the current location is approximately the same as the location
     * for a particular status. Used to check if we'll add a new status, or
     * update the most recent status of we're stationary.
     */
    private boolean locationIsAtStatus(Location location, int statusIndex)
    {
        if (mTransportStatuses.size() <= statusIndex)
        {
            return false;
        }

        Map<String, Object> status = mTransportStatuses.get(statusIndex);
        Location locationForStatus = new Location("");

        locationForStatus.setLatitude((double) status.get("lat"));
        locationForStatus.setLongitude((double) status.get("lng"));

        float distance = location.distanceTo(locationForStatus);
        Log.d(TAG, String.format("Distance from status %s is %sm", statusIndex, distance));
        return distance < mFirebaseRemoteConfig.getLong("LOCATION_MIN_DISTANCE_CHANGED");
    }

    private float getBatteryLevel()
    {
        Intent batteryStatus = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int batteryLevel = -1;
        int batteryScale = 1;

        if (batteryStatus != null)
        {
            batteryLevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, batteryLevel);
            batteryScale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, batteryScale);
        }

        return batteryLevel / (float) batteryScale * 100;
    }

    private void logStatusToStorage(Map<String, Object> transportStatus) {
        try {
            File path = new File(Environment.getExternalStoragePublicDirectory(""),
                    "transport-tracker-log.txt");
            if (!path.exists()) {
                path.createNewFile();
            }
            FileWriter logFile = new FileWriter(path.getAbsolutePath(), true);
            logFile.append(transportStatus.toString() + "\n");
            logFile.close();
        } catch (Exception e) {
            Log.e(TAG, "Log file error", e);
        }
    }

    private void shutdownAndScheduleStartup(int when)
    {
        Log.i(TAG, "overnight shutdown, seconds to startup: " + when);

        com.google.android.gms.gcm.Task task = new OneoffTask.Builder()
                .setService(TrackerTaskService.class)
                .setExecutionWindow(when, when + 60)
                .setUpdateCurrent(true)
                .setTag(TrackerTaskService.TAG)
                .setRequiredNetwork(com.google.android.gms.gcm.Task.NETWORK_STATE_ANY)
                .setRequiresCharging(false)
                .build();

        GcmNetworkManager.getInstance(this).schedule(task);

        stopSelf();
    }

    /**
     * Pushes a new status to Firebase when location changes.
     */
    @Override
    public void onLocationChanged(Location location)
    {
        fetchRemoteConfig();

        long hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int startupSeconds = (int) (mFirebaseRemoteConfig.getDouble("SLEEP_HOURS_DURATION") * 3600);

        /*if (hour == mFirebaseRemoteConfig.getLong("SLEEP_HOUR_OF_DAY"))
        {
            shutdownAndScheduleStartup(startupSeconds);
            return;
        }*/

        Map<String, Object> transportStatus = new HashMap<>();
        transportStatus.put("lat", location.getLatitude());
        transportStatus.put("lng", location.getLongitude());
        transportStatus.put("time", new Date().getTime());
        transportStatus.put("power", getBatteryLevel());

        long time=new Date().getTime();
        String fechaHoraUbicacionGps=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(time);

        Date miFecha = Calendar.getInstance().getTime();
        String fechaUbicacionGps=new SimpleDateFormat("dd/MM/yyyy").format(miFecha);
        float batteryLevel=getBatteryLevel();

        insertarUbicacionDispositivoGpsLocalmente(location,fechaHoraUbicacionGps,batteryLevel);

        /*if (locationIsAtStatus(location, 1) && locationIsAtStatus(location, 0))
        {
            // If the most recent two statuses are approximately at the same
            // location as the new current location, rather than adding the new
            // location, we update the latest status with the current. Two statuses
            // are kept when the locations are the same, the earlier representing
            // the time the location was arrived at, and the latest representing the
            // current time.
            mTransportStatuses.set(0, transportStatus);
            // Only need to update 0th status, so we can save bandwidth.
            mFirebaseTransportRef.child("0").setValue(transportStatus);
        }
        else
        {
            // Maintain a fixed number of previous statuses.
            while (mTransportStatuses.size() >= mFirebaseRemoteConfig.getLong("MAX_STATUSES"))
            {
                mTransportStatuses.removeLast();
            }
            mTransportStatuses.addFirst(transportStatus);
            // We push the entire list at once since each key/index changes, to
            // minimize network requests.
            mFirebaseTransportRef.setValue(mTransportStatuses);
        }*/

        if (BuildConfig.DEBUG)
        {
            logStatusToStorage(transportStatus);
        }

        NetworkInfo info = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        boolean connected = info != null && info.isConnectedOrConnecting();
        //////setStatusMessage(connected ? R.string.tracking : R.string.not_tracking);
    }

    private void buildNotification()
    {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RastreadorActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.bus_white)
                .setColor(getColor(R.color.colorPrimary))
                .setContentTitle(getString(R.string.app_name))
                .setOngoing(true)
                .setContentIntent(resultPendingIntent);

        startForeground(FOREGROUND_SERVICE_ID, mNotificationBuilder.build());
    }

    /**
     * Sets the current status message (connecting/tracking/not tracking).
     */
    private void setStatusMessage(int stringId)
    {
        mNotificationBuilder.setContentText(getString(stringId));
        mNotificationManager.notify(NOTIFICATION_ID, mNotificationBuilder.build());

        // Also display the status message in the activity.
        Intent intent = new Intent(STATUS_INTENT);
        intent.putExtra(getString(R.string.status), stringId);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void insertarUbicacionDispositivoGpsLocalmente(Location location,String fechaHoraUbicacionGps,float batteryLevel)
    {
        Intent intent = new Intent(this, UbicacionDispositivoGpsServicioLocal.class);
        intent.setAction(UbicacionDispositivoGpsServicioLocal.ACCION_INSERTAR_UBICACION_DISPOSITIVO_GPS_ISERVICE);
        UbicacionDispositivoGps ubicacionDispositivoGps = generarEntidadUbicacionDispositivoGps(location,fechaHoraUbicacionGps,batteryLevel);
        intent.putExtra(UbicacionDispositivoGpsServicioLocal.EXTRA_MI_UBICACION_DISPOSITIVO_GPS, ubicacionDispositivoGps);
        startService(intent);
    }

    //Generar entidad Ubicacion Dispositivo Gps
    private UbicacionDispositivoGps generarEntidadUbicacionDispositivoGps(Location location,String fechaHoraUbicacionGps
            ,float batteryLevel)
    {
        UbicacionDispositivoGps ubicacionDispositivoGps =new UbicacionDispositivoGps();

        ubicacionDispositivoGps.setIdUbicacion(0);
        ubicacionDispositivoGps.setDireccionUbicacion("");
        ubicacionDispositivoGps.setLatitud(location.getLatitude());
        ubicacionDispositivoGps.setLongitud(location.getLongitude());
        ubicacionDispositivoGps.setFechaHoraUbicacion(fechaHoraUbicacionGps);
        ubicacionDispositivoGps.setBateria(batteryLevel);

        return ubicacionDispositivoGps;
    }

    private boolean validaRangoInicioFin()
    {
        int mYear, mMonth, mHoraHoy=0, mMinutoHoy=0, mDay=0,mDiaSemana;
        int mHoraInicio=0, mMinutoInicio=0, mHoraFin=0, mMinutoFin=0;
        String mTime;
        String mDate;

        try
        {
            Boolean rastreoGpsDia=false;

            ContentResolver resolver = getContentResolver();

            Uri uri =  GruposTabla.crearUriGrupoTablaLista();

            Cursor cursorGrupo = resolver.query(uri, null, null, null, null);
            assert cursorGrupo != null;

            Log.i(TAG, "Se encontraron " + cursorGrupo.getCount() + " registros locales.");
            // Encontrar datos obsoletos
            if (cursorGrupo.moveToNext()== true)
            {
                Grupo grupo = new Grupo(cursorGrupo);

                // Create a new notification
                if (grupo.getRastreoGps() == true)
                {
                    rastreoGpsDia=true;
                    Calendar mCalendarHoy;
                    mCalendarHoy = Calendar.getInstance();
                    mDiaSemana = mCalendarHoy.get(Calendar.DAY_OF_WEEK);

                    Uri uriProgramacionRastreoGpsDetalle =  ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTabla(grupo.getIdProgramacionRastreoGps(),obtieneDiaSemana(mDiaSemana));

                    Cursor cursorProgramacionRastreoGpsDetalle = resolver.query(uriProgramacionRastreoGpsDetalle, null, null, null, null);
                    assert cursorProgramacionRastreoGpsDetalle != null;

                    if (cursorProgramacionRastreoGpsDetalle.moveToNext()== true)
                    {
                        ProgramacionRastreoGpsDetalle programacionRastreoGpsDetalle = new ProgramacionRastreoGpsDetalle(cursorProgramacionRastreoGpsDetalle);

                        if (programacionRastreoGpsDetalle.getRastreoGps() == true)
                        {
                            mYear = mCalendarHoy.get(Calendar.YEAR);
                            mMonth = mCalendarHoy.get(Calendar.MONTH) + 1;
                            mDay = mCalendarHoy.get(Calendar.DATE);
                            mHoraHoy = mCalendarHoy.get(Calendar.HOUR_OF_DAY);
                            mMinutoHoy = mCalendarHoy.get(Calendar.MINUTE);

                            mDate = mDay + "/" + mMonth + "/" + mYear;

                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                            /////////////////Configura Inicio de Rastreo
                            Date fechaRangoHoraInicio = format.parse( mDate + " " + programacionRastreoGpsDetalle.getRangoHoraInicio());

                            Calendar mCalendarRangoHoraInicio;
                            mCalendarRangoHoraInicio = Calendar.getInstance();
                            mCalendarRangoHoraInicio.setTime(fechaRangoHoraInicio);

                            mHoraInicio= mCalendarRangoHoraInicio.get(Calendar.HOUR_OF_DAY);
                            mMinutoInicio=mCalendarRangoHoraInicio.get(Calendar.MINUTE);

                            /////////////////Configura Fin de Rastreo
                            Date fechaRangoHoraFin = format.parse( mDate + " " + programacionRastreoGpsDetalle.getRangoHoraFinal());

                            Calendar mCalendarRangoHoraFin;
                            mCalendarRangoHoraFin = Calendar.getInstance();
                            mCalendarRangoHoraFin.setTime(fechaRangoHoraFin);

                            mHoraFin = mCalendarRangoHoraFin.get(Calendar.HOUR_OF_DAY);
                            mMinutoFin = mCalendarRangoHoraFin.get(Calendar.MINUTE);
                        }
                    }
                    cursorProgramacionRastreoGpsDetalle.close();
                }
            }
            cursorGrupo.close();

            if (rastreoGpsDia == true)
            {
                if((mHoraHoy >= mHoraInicio)  && (mHoraHoy <= mHoraFin ) )
                {
                    if(((mHoraHoy == mHoraInicio && mMinutoHoy < mMinutoInicio) || (mHoraHoy == mHoraFin && mMinutoHoy > mMinutoFin ) ))
                    {
                        return false;
                    }
                    return true;
                }
                else
                {
                    return false;
                }
            }

        }
        catch ( ParseException e)
        {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    private String obtieneDiaSemana(int dia)
    {
        switch (dia)
        {
            case 1 : //Domingo
                return "Domingo";
            case 2 : //Lunes
                return "Lunes";
            case 3 : //Martes
                return "Martes";
            case 4 : //Miercoles
                return "Miercoles";
            case 5 : //Jueves
                return "Jueves";
            case 6 : //Viernes
                return "Viernes";
            case 7 : //Sabado
                return "Sabado";
        }
        return "Lunes";
    }
}
