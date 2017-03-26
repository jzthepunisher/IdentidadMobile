package com.soloparaapasionados.identidadmobile.sqlite;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by USUARIO on 25/03/2017.
 */

public class BaseDatosCotizaciones extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "cotizaciones.db";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    interface Tablas {
        String DISPOSITIVO = "dispositivo";
    }

    public BaseDatosCotizaciones(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s INTEGER NOT NULL,%s TEXT NOT NULL,%s TEXT NULL," +
                        "%s INTEGER NOT NULL ,%s INTEGER NOT NULL,%s INTEGER NOT NULL)",
                Tablas.DISPOSITIVO, BaseColumns._ID,
                Dispositivos.IMEI,Dispositivos.ID_TIPO_DISPOSITIVO, Dispositivos.ID_SIM_CARD,Dispositivos.NUMERO_CELULAR,
                Dispositivos.ENVIADO, Dispositivos.RECIBIDO, Dispositivos.VALIDADO));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DISPOSITIVO);

        onCreate(db);
    }
}
