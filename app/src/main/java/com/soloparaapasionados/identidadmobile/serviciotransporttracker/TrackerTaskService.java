package com.soloparaapasionados.identidadmobile.serviciotransporttracker;

/**
 * Created by USUARIO on 03/02/2018.
 */
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import android.content.Intent;
import android.util.Log;

public class TrackerTaskService extends GcmTaskService
{
    public static final String TAG = TrackerTaskService.class.getSimpleName();

    @Override
    public int onRunTask(TaskParams taskParams)
    {
        Log.i(TAG, "onRunTask");

        Intent start = new Intent(this, TrackerActivity.class);
        start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(start);

        return GcmNetworkManager.RESULT_SUCCESS;
    }

}