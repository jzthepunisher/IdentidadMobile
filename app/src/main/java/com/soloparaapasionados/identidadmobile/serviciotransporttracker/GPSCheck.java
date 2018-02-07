package com.soloparaapasionados.identidadmobile.serviciotransporttracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Created by USUARIO on 04/02/2018.
 */

public class GPSCheck extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);


        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(context, "Has habilitado GPS", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context, "Has deshabilitado GPS", Toast.LENGTH_LONG).show();
        }
    }

}