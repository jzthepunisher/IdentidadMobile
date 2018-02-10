package com.soloparaapasionados.identidadmobile.antenasReceptoras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent intentInicio = new Intent(context, RastreadorActivity.class);
            intentInicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentInicio);
        }
    }
}
