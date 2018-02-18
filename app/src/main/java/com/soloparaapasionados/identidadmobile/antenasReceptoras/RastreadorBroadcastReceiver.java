package com.soloparaapasionados.identidadmobile.antenasReceptoras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.ServicioLocal.RastreadorServicio;
import com.soloparaapasionados.identidadmobile.actividades.RastreadorActivity;

/**
 * Created by USUARIO on 10/02/2018.
 */

public class RastreadorBroadcastReceiver extends BroadcastReceiver
{
    public RastreadorBroadcastReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Toast.makeText(context, "RastreadorBroadcastReceiver onReceive -1", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Toast.makeText(context, "RastreadorBroadcastReceiver onReceive 0", Toast.LENGTH_LONG).show();
            Intent intentInicio = new Intent(context, RastreadorServicio.class);
            ////intentInicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(intentInicio);
            Toast.makeText(context, "RastreadorBroadcastReceiver onReceive 1", Toast.LENGTH_LONG).show();
        }
    }
}
