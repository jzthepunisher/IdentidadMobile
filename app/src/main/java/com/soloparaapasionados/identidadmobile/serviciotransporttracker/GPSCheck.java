package com.soloparaapasionados.identidadmobile.serviciotransporttracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.ServicioLocal.RastreadorServicio;

/**
 * Created by USUARIO on 04/02/2018.
 */

public class GPSCheck extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().matches("android.location.GPS_ENABLED_CHANGE"))
        {
            boolean enabled = intent.getBooleanExtra("enabled",false);

            Toast.makeText(context, "GPS : " + enabled,
                    Toast.LENGTH_SHORT).show();
        }

       /* LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);


        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Intent intentInicio = new Intent(context, RastreadorServicio.class);
            intentInicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentInicio);
            Toast.makeText(context, "Has habilitado GPS", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, "Has deshabilitado GPS", Toast.LENGTH_LONG).show();
        }*/
    }

}