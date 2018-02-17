package com.soloparaapasionados.identidadmobile.alarmas;

import android.app.AlarmManager;
import android.content.Context;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class AlarmManagerProvider
{
    private static final String TAG = AlarmManagerProvider.class.getSimpleName();

    private static AlarmManager sAlarmManager;

    public static synchronized void injectAlarmManager(AlarmManager alarmManager)
    {
        if (sAlarmManager != null)
        {
            throw new IllegalStateException("Alarm Manager Already Set");
        }
        sAlarmManager = alarmManager;
    }

    /*package*/
    static synchronized AlarmManager getAlarmManager(Context context)
    {
        if (sAlarmManager == null)
        {
            sAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return sAlarmManager;
    }
}
