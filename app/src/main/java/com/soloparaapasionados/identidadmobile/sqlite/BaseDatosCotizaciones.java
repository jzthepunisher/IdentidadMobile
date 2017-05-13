package com.soloparaapasionados.identidadmobile.sqlite;

import com.soloparaapasionados.identidadmobile.modelo.Cargo;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by USUARIO on 25/03/2017.
 */

public class BaseDatosCotizaciones extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "cotizaciones.db";

    private static final int VERSION_ACTUAL = 2;

    private final Context contexto;

    interface Tablas {
        String DISPOSITIVO = "dispositivo";
        String EMPLEADO    = "empleado";
        String CARGO       = "cargo";
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

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL)",
            Tablas.CARGO, BaseColumns._ID,
            Cargos.ID_CARGO,Cargos.DESCRIPCION));

        /*db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            "%s TEXT UNIQUE NOT NULL,%s INTEGER NOT NULL,%s TEXT NOT NULL,%s TEXT NULL," +
            "%s INTEGER NOT NULL ,%s INTEGER NOT NULL,%s INTEGER NOT NULL)",
            Tablas.EMPLEADO, BaseColumns._ID,
            Empleados.ID_CARGO,Dispositivos.ID_TIPO_DISPOSITIVO, Dispositivos.ID_SIM_CARD,Dispositivos.NUMERO_CELULAR,
            Dispositivos.ENVIADO, Dispositivos.RECIBIDO, Dispositivos.VALIDADO));*/

        mockData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DISPOSITIVO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.EMPLEADO);

        onCreate(db);
    }

    // Insertar datos ficticios para prueba inicial
    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockLawyer(sqLiteDatabase, new Cargo("C001", "Vendedor"));
        mockLawyer(sqLiteDatabase, new Cargo("C002", "Técnico Instalador"));
        mockLawyer(sqLiteDatabase, new Cargo("C003", "Técnico Servicio"));
        mockLawyer(sqLiteDatabase, new Cargo("C004", "Halcon"));
        mockLawyer(sqLiteDatabase, new Cargo("C005", "Supervisor"));
    }

    public long mockLawyer(SQLiteDatabase db, Cargo cargo) {
        return db.insert(
                Tablas.CARGO,
                null,
                cargo.toContentValues());
    }
}
