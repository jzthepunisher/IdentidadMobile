package com.soloparaapasionados.identidadmobile.aplicacion;

import android.app.Application;
import android.support.compat.BuildConfig;

import com.facebook.stetho.Stetho;

/**
 * Created by USUARIO on 13/05/2017.
 */

public class DeviceDatabaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
       // if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
       // }
    }
}