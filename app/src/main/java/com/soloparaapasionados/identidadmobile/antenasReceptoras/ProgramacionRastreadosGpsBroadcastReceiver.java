package com.soloparaapasionados.identidadmobile.antenasReceptoras;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.soloparaapasionados.identidadmobile.ServicioLocal.ProgramacionRastreadosGpsServicioLocal;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class ProgramacionRastreadosGpsBroadcastReceiver extends BroadcastReceiver
{
    public ProgramacionRastreadosGpsBroadcastReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Intent intentInicio = new Intent(context, ProgramacionRastreadosGpsServicioLocal.class);
            intentInicio.setAction(ProgramacionRastreadosGpsServicioLocal.ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE);
            context.startService(intentInicio);
        }
    }
}
