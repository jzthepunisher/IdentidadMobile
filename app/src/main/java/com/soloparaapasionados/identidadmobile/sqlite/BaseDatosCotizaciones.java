package com.soloparaapasionados.identidadmobile.sqlite;

import com.soloparaapasionados.identidadmobile.modelo.Cargo;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.modelo.DispositivoEmpleado;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

/**
 * Created by USUARIO on 25/03/2017.
 */

public class BaseDatosCotizaciones extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "cotizaciones.db";

    private static final int VERSION_ACTUAL = 20;

    private final Context contexto;

    interface Tablas {
        String DISPOSITIVO          = "dispositivo";
        String EMPLEADO             = "empleado";
        String CARGO                = "cargo";
        String DISPOSITIVO_EMPLEADO = "dispositivo_empleado";
    }

    interface Referencias {
        String ID_CARGO = String.format("REFERENCES %s(%s) ", Tablas.CARGO, Cargos.ID_CARGO);
        String IMEI = String.format("REFERENCES %s(%s) ", Tablas.DISPOSITIVO, DispositivosEmpleados.IMEI);
        String ID_EMPLEADO = String.format("REFERENCES %s(%s) ", Tablas.EMPLEADO, Empleados.ID_EMPLEADO);
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

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL,%s TEXT NULL,%s TEXT NULL," +
                        "%s TEXT NULL ,%s TEXT NULL,%s TEXT NULL,%s TEXT NULL,"+
                        "%s DATETIME NULL ,%s TEXT NULL %s ,%s DATETIME NULL,%s DATETIME NULL,"+
                        "%s DATETIME NULL ,%s TEXT NULL,%s BOOLEAN DEFAULT 0)",
                Tablas.EMPLEADO, BaseColumns._ID,
                Empleados.ID_EMPLEADO,Empleados.NOMBRES, Empleados.APELLIDO_PATERNO,Empleados.APELLIDO_MAERNO,
                Empleados.DIRECCION, Empleados.DNI, Empleados.CELULAR, Empleados.EMAIL,
                Empleados.FECHA_NACIMIENTO, Empleados.ID_CARGO,Referencias.ID_CARGO, Empleados.FECHA_INGRESO, Empleados.FECHA_BAJA,
                Empleados.FECHA_CREACION,Empleados.FOTO,Empleados.ELIMINADO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s ,%s TEXT UNIQUE NOT NULL %s" +
                " , UNIQUE (%s, %s) ON CONFLICT REPLACE)",
                Tablas.DISPOSITIVO_EMPLEADO, BaseColumns._ID,
                DispositivosEmpleados.IMEI,Referencias.IMEI,DispositivosEmpleados.ID_EMPLEADO,Referencias.ID_EMPLEADO,
                DispositivosEmpleados.IMEI,DispositivosEmpleados.ID_EMPLEADO));

        mockData(db);
        mockDataEmpleados(db);
        mockDataDispositivosEmpleados(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DISPOSITIVO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CARGO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.EMPLEADO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DISPOSITIVO_EMPLEADO);

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

    private void mockDataEmpleados(SQLiteDatabase sqLiteDatabase) {

        Empleado empleado=new Empleado();
        empleado.setIdEmpleado("EM001");
        empleado.setNombres("Andres");
        empleado.setApellidoPaterno("Léido");
        empleado.setApellidoMaterno("Cueva");
        empleado.setFoto("carlos_perez.jpg");
        empleado.setIdCargo("C001");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM002");
        empleado.setNombres("Brenda");
        empleado.setApellidoPaterno("Rosas");
        empleado.setApellidoMaterno("Licuona");
        empleado.setFoto("lucia_aristizabal.jpg");
        empleado.setIdCargo("C002");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM003");
        empleado.setNombres("Diana");
        empleado.setApellidoPaterno("Yarasca");
        empleado.setApellidoMaterno("Berrospi");
        empleado.setFoto("marina_acosta.jpg");
        empleado.setIdCargo("C003");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM004");
        empleado.setNombres("Eliana");
        empleado.setApellidoPaterno("Trujillo");
        empleado.setApellidoMaterno("Baza");
        empleado.setFoto("olga_ortiz.jpg");
        empleado.setIdCargo("C004");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM005");
        empleado.setNombres("Geraldine");
        empleado.setApellidoPaterno("Rivas");
        empleado.setApellidoMaterno("Huamaní");
        empleado.setFoto("pamela_briger.jpg");
        empleado.setIdCargo("C005");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM006");
        empleado.setNombres("Giancarlo");
        empleado.setApellidoPaterno("Aquije");
        empleado.setApellidoMaterno("Milachay");
        empleado.setFoto("daniel_samper.jpg");
        empleado.setIdCargo("C001");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM007");
        empleado.setNombres("Gianfranco");
        empleado.setApellidoPaterno("Vela");
        empleado.setApellidoMaterno("Benavides");
        empleado.setFoto("rodrigo_benavidez.jpg");
        empleado.setIdCargo("C002");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM008");
        empleado.setNombres("Giannia");
        empleado.setApellidoPaterno("Lopez");
        empleado.setApellidoMaterno("Torres");
        empleado.setFoto("pamela_briger.jpg");
        empleado.setIdCargo("C003");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM009");
        empleado.setNombres("Isaac");
        empleado.setApellidoPaterno("Alvarez");
        empleado.setApellidoMaterno("Alvarez");
        empleado.setFoto("tom_bonz.jpg");
        empleado.setIdCargo("C004");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM010");
        empleado.setNombres("Leo");
        empleado.setApellidoPaterno("Angúlo");
        empleado.setApellidoMaterno("Ferroñay");
        empleado.setFoto("carlos_perez.jpg");
        empleado.setIdCargo("C005");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM011");
        empleado.setNombres("Luis");
        empleado.setApellidoPaterno("Rivera");
        empleado.setApellidoMaterno("Nuñez");
        empleado.setFoto("daniel_samper.jpg");
        empleado.setIdCargo("C001");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM012");
        empleado.setNombres("Marina");
        empleado.setApellidoPaterno("Rayme");
        empleado.setApellidoMaterno("Rayme");
        empleado.setFoto("lucia_aristizabal.jpg");
        empleado.setIdCargo("C002");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM013");
        empleado.setNombres("María Cristina");
        empleado.setApellidoPaterno("Rojas");
        empleado.setApellidoMaterno("Rojas");
        empleado.setFoto("marina_acosta.jpg");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM014");
        empleado.setNombres("Melissa");
        empleado.setApellidoPaterno("Elera");
        empleado.setApellidoMaterno("García");
        empleado.setFoto("olga_ortiz.jpg");
        empleado.setIdCargo("C003");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM015");
        empleado.setNombres("Paola");
        empleado.setApellidoPaterno("Ramos");
        empleado.setApellidoMaterno("Ramos");
        empleado.setFoto("pamela_briger.jpg");
        empleado.setIdCargo("C004");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM016");
        empleado.setNombres("Rosario");
        empleado.setApellidoPaterno("Zapata");
        empleado.setApellidoMaterno("Huamaní");
        empleado.setFoto("lucia_aristizabal.jpg");
        empleado.setIdCargo("C005");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM017");
        empleado.setNombres("Sheyla");
        empleado.setApellidoPaterno("Sánchez");
        empleado.setApellidoMaterno("Condemarín");
        empleado.setFoto("marina_acosta.jpg");
        empleado.setIdCargo("C001");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM018");
        empleado.setNombres("Sofía");
        empleado.setApellidoPaterno("Sánchez");
        empleado.setApellidoMaterno("Cardenas");
        empleado.setFoto("olga_ortiz.jpg");
        empleado.setIdCargo("C002");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM019");
        empleado.setNombres("Victor Ronald");
        empleado.setApellidoPaterno("Rivero");
        empleado.setApellidoMaterno("Pumachagua");
        empleado.setFoto("rodrigo_benavidez.jpg");
        empleado.setIdCargo("C003");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM020");
        empleado.setNombres("Violeta");
        empleado.setApellidoPaterno("Cabezudo");
        empleado.setApellidoMaterno("Cabezudo");
        empleado.setFoto("pamela_briger.jpg");
        empleado.setIdCargo("C004");
        mockEmpleado(sqLiteDatabase, empleado);

        empleado.setIdEmpleado("EM021");
        empleado.setNombres("Diego Elmuth");
        empleado.setApellidoPaterno("Vera");
        empleado.setApellidoMaterno("Bueno");
        empleado.setFoto("tom_bonz.jpg");
        empleado.setIdCargo("C005");
        mockEmpleado(sqLiteDatabase, empleado);


    }

    public long mockLawyer(SQLiteDatabase db, Cargo cargo) {
        return db.insert(
                Tablas.CARGO,
                null,
                cargo.toContentValues());
    }

    public long mockEmpleado(SQLiteDatabase db, Empleado empleado) {
        return db.insertOrThrow(
                Tablas.EMPLEADO,
                null,
                empleado.toContentValues());
    }

    private void mockDataDispositivosEmpleados(SQLiteDatabase sqLiteDatabase) {

        DispositivoEmpleado dispositivoEmpleado=new DispositivoEmpleado();
        dispositivoEmpleado.setImei("352430073391446");
        dispositivoEmpleado.setIdEmpleado("EM001");
        mockDispositivoEmpleado(sqLiteDatabase, dispositivoEmpleado);

        dispositivoEmpleado=new DispositivoEmpleado();
        dispositivoEmpleado.setImei("352430073391446");
        dispositivoEmpleado.setIdEmpleado("EM003");
        mockDispositivoEmpleado(sqLiteDatabase, dispositivoEmpleado);

        dispositivoEmpleado=new DispositivoEmpleado();
        dispositivoEmpleado.setImei("352430073391446");
        dispositivoEmpleado.setIdEmpleado("EM005");
        mockDispositivoEmpleado(sqLiteDatabase, dispositivoEmpleado);

    }

    public long mockDispositivoEmpleado(SQLiteDatabase db, DispositivoEmpleado dispositivoEmpleado) {
        return db.insertOrThrow(
                Tablas.DISPOSITIVO_EMPLEADO,
                null,
                dispositivoEmpleado.toContentValues());
    }
}
