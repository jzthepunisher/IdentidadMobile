package com.soloparaapasionados.identidadmobile.antenasReceptoras;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.ProgramacionRastreadosGpsServicioLocal;
import com.soloparaapasionados.identidadmobile.ServicioLocal.RastreadorServicio;

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

            Toast.makeText(context, "ProgramacionRastreadosGpsBroadcastReceiver onReceive 0", Toast.LENGTH_LONG).show();
            Intent intentInicio = new Intent(context, ProgramacionRastreadosGpsServicioLocal.class);
            intentInicio.setAction(ProgramacionRastreadosGpsServicioLocal.ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE);
            context.startService(intentInicio);
            Toast.makeText(context, "ProgramacionRastreadosGpsBroadcastReceiver onReceive 1", Toast.LENGTH_LONG).show();
        }
    }

   /* private boolean isServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }*/
}
