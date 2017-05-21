package com.soloparaapasionados.identidadmobile.observadores;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by USUARIO on 20/05/2017.
 */

public class MiObervador extends ContentObserver {
    public MiObervador(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange,null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        //Write your code here
        //Whatever is written here will be
        //executed whenever a change is made
        int a;
        a=0;
    }
}
