package com.soloparaapasionados.identidadmobile.alarmas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class AlarmScheduler
{
    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    public void setAlarm(Context context, long alarmTime, Uri reminderTask,String activar_desactivar)
    {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask,activar_desactivar);

        if (Build.VERSION.SDK_INT >= 23)
        {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        }
        else
        {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime,String activar_desactivar)
    {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask,activar_desactivar);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);
    }

    public void cancelAlarm(Context context, Uri reminderTask,String activar_desactivar)
    {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask,activar_desactivar);

        manager.cancel(operation);

    }

}