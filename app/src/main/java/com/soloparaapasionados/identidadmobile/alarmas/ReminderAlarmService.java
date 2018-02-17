package com.soloparaapasionados.identidadmobile.alarmas;

import android.Manifest;
import android.app.ActivityManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.ProgramacionRastreadosGpsServicioLocal;
import com.soloparaapasionados.identidadmobile.ServicioLocal.RastreadorServicio;
import com.soloparaapasionados.identidadmobile.actividades.RastreadorActivity;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class ReminderAlarmService  extends IntentService
{
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    private static final int PERMISSIONS_REQUEST = 1;
    private static String[] PERMISSIONS_REQUIRED = new String[]
            {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri,String activar_desactivar)
    {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        action.putExtra(ProgramacionRastreadosGpsServicioLocal.EXTRA_ACTIVAR_DESACTIVAR_SERVICIO_RASTREO,activar_desactivar);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri uri = intent.getData();
        String activar_desactivar= intent.getStringExtra(ProgramacionRastreadosGpsServicioLocal.EXTRA_ACTIVAR_DESACTIVAR_SERVICIO_RASTREO);

        if(activar_desactivar.equals(ProgramacionRastreadosGpsServicioLocal.ACTIVAR))
        {
            if (isServiceRunning(RastreadorServicio.class))
            {
                // If service already running, simply update UI.
                //////setTrackingStatus(R.string.tracking);

            //////} else if (transportID.length() > 0 && email.length() > 0 && password.length() > 0)
            } else if (true)
            {
                // Inputs have previously been stored, start validation.
                checkLocationPermission();
            } else
            {
                // First time running - check for inputs pre-populated from build.
                /////mTransportIdEditText.setText(getString(R.string.build_transport_id));
                /////mEmailEditText.setText(getString(R.string.build_email));
                /////mPasswordEditText.setText(getString(R.string.build_password));
            }

            //Display a notification to view the task details
            Intent action = new Intent(this, RastreadorActivity.class);
            action.setData(uri);
            PendingIntent operation = TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(action)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            //Grab the task description
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            String titulo = "Progrmación rastreo GPS";
            String description = "Se ha iniciado o finalizado rastreo";
            //////try {
            //////    if (cursor != null && cursor.moveToFirst()) {
            //////        description = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            //////    }
            //////} finally {
            //////    if (cursor != null) {
            //////        cursor.close();
            //////    }
            //////}

            Notification note = new NotificationCompat.Builder(this)
                    .setContentTitle(titulo)
                    .setContentText(description)
                    .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                    .setContentIntent(operation)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .build();

            manager.notify(NOTIFICATION_ID, note);

        }

        if(activar_desactivar.equals(ProgramacionRastreadosGpsServicioLocal.DESACTIVAR))
        {
            if (isServiceRunning(RastreadorServicio.class))
            {
                stopLocationService();
            }
        }


    }

    private boolean isServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    private void checkLocationPermission()
    {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (locationPermission != PackageManager.PERMISSION_GRANTED || storagePermission != PackageManager.PERMISSION_GRANTED)
        {
           ////// ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST);
        }
        else
        {
            checkGpsEnabled();
        }
    }

    /**
     * Third and final validation check - ensures GPS is enabled, and if not, prompts to
     * enable it, otherwise all checks pass so start the location tracking service.
     */
    private void checkGpsEnabled()
    {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //////reportGpsError();
        }
        else
        {
            //////resolveGpsError();
            startLocationService();
        }
    }


    private void startLocationService()
    {
        // Before we start the service, confirm that we have extra power usage privileges.
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        Intent intent = new Intent();

        if (!pm.isIgnoringBatteryOptimizations(getPackageName()))
        {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }

        startService(new Intent(this, RastreadorServicio.class));
    }

    private void stopLocationService()
    {
        stopService(new Intent(this, RastreadorServicio.class));
    }
}