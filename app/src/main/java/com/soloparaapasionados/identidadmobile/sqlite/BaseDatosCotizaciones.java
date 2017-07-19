package com.soloparaapasionados.identidadmobile.sqlite;

import com.soloparaapasionados.identidadmobile.modelo.Cargo;
import com.soloparaapasionados.identidadmobile.modelo.Cliente;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.modelo.DispositivoEmpleado;

import com.soloparaapasionados.identidadmobile.modelo.TipoUnidadReaccion;
import com.soloparaapasionados.identidadmobile.modelo.Turno;
import com.soloparaapasionados.identidadmobile.modelo.Turno_UnidadReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.modelo.UnidadReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposUnidadReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UnidadesReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos_UnidadesReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;

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

    private static final int VERSION_ACTUAL = 42;

    private final Context contexto;

    public static interface Tablas {
        String DISPOSITIVO                     = "dispositivo";
        String EMPLEADO                        = "empleado";
        String CARGO                           = "cargo";
        String DISPOSITIVO_EMPLEADO            = "dispositivo_empleado";
        String DISPOSITIVO_EMPLEADO_TEMPORAL   = "dispositivo_empleado_temporal";
        String TURNO                           = "turno";
        String TIPO_UNIDAD_REACCION            = "tipo_unidad_reaccion";
        String UNIDAD_REACCION                 = "unidad_reaccion";
        String TURNO_UNIDAD_REACCION_UBICACION = "turno_unidad_reaccion_ubicacion";
        String CLIENTE                         = "cliente";
    }

    interface Referencias {
        String ID_CARGO                = String.format("REFERENCES %s(%s) ", Tablas.CARGO               , Cargos.ID_CARGO);
        String IMEI                    = String.format("REFERENCES %s(%s) ", Tablas.DISPOSITIVO         , DispositivosEmpleados.IMEI);
        String ID_EMPLEADO             = String.format("REFERENCES %s(%s) ", Tablas.EMPLEADO            , Empleados.ID_EMPLEADO);
        String ID_TIPO_UNIDAD_REACCION = String.format("REFERENCES %s(%s) ", Tablas.TIPO_UNIDAD_REACCION, TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION);
        String ID_TURNO                = String.format("REFERENCES %s(%s) ", Tablas.TURNO               , Turnos.ID_TURNO);
        String ID_UNIDAD_REACCION      = String.format("REFERENCES %s(%s) ", Tablas.UNIDAD_REACCION     , UnidadesReaccion.ID_UNIDAD_REACCION);
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
                        "%s DATETIME NULL ,%s TEXT NULL,%s BOOLEAN DEFAULT 0,%s TEXT DEFAULT 'Registrado.Localmente')",
                Tablas.EMPLEADO, BaseColumns._ID,
                Empleados.ID_EMPLEADO,Empleados.NOMBRES, Empleados.APELLIDO_PATERNO,Empleados.APELLIDO_MAERNO,
                Empleados.DIRECCION, Empleados.DNI, Empleados.CELULAR, Empleados.EMAIL,
                Empleados.FECHA_NACIMIENTO, Empleados.ID_CARGO,Referencias.ID_CARGO, Empleados.FECHA_INGRESO, Empleados.FECHA_BAJA,
                Empleados.FECHA_CREACION,Empleados.FOTO,Empleados.ELIMINADO,Empleados.ESTADO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s ,%s TEXT UNIQUE NOT NULL %s" +
                " , UNIQUE (%s, %s) ON CONFLICT REPLACE)",
                Tablas.DISPOSITIVO_EMPLEADO, BaseColumns._ID,
                DispositivosEmpleados.IMEI,Referencias.IMEI,DispositivosEmpleados.ID_EMPLEADO,Referencias.ID_EMPLEADO,
                DispositivosEmpleados.IMEI,DispositivosEmpleados.ID_EMPLEADO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s ,%s TEXT UNIQUE NOT NULL %s" +
                        " , UNIQUE (%s, %s) ON CONFLICT REPLACE)",
                Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL, BaseColumns._ID,
                DispositivosEmpleados.IMEI,Referencias.IMEI,DispositivosEmpleados.ID_EMPLEADO,Referencias.ID_EMPLEADO,
                DispositivosEmpleados.IMEI,DispositivosEmpleados.ID_EMPLEADO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL)",
                Tablas.TURNO, BaseColumns._ID,
                Turnos.ID_TURNO,Turnos.DESCRIPCION,
                Turnos.HORA_INICIO,Turnos.HORA_FIN));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT NOT NULL)",
                Tablas.TIPO_UNIDAD_REACCION, BaseColumns._ID,
                TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION,TiposUnidadReaccion.DESCRIPCION,
                TiposUnidadReaccion.FOTO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NOT NULL %s,"+
                        "%s TEXT NOT NULL,%s TEXT NOT NULL," +
                        "%s TEXT NULL,%s TEXT NULL," +
                        "%s TEXT NULL)",
                Tablas.UNIDAD_REACCION, BaseColumns._ID,
                UnidadesReaccion.ID_UNIDAD_REACCION,TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION,Referencias.ID_TIPO_UNIDAD_REACCION,
                UnidadesReaccion.DESCRIPCION,UnidadesReaccion.PLACA,
                UnidadesReaccion.MARCA,UnidadesReaccion.MODELO,
                UnidadesReaccion.COLOR));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL %s,%s TEXT NOT NULL %s,"+
                        "%s TEXT NULL,%s TEXT NULL," +
                        "%s TEXT NULL," +
                        " UNIQUE (%s, %s) ON CONFLICT REPLACE)",
                Tablas.TURNO_UNIDAD_REACCION_UBICACION, BaseColumns._ID,
                Turnos_UnidadesReaccionUbicacion.ID_TURNO,Referencias.ID_TURNO,Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION,Referencias.ID_UNIDAD_REACCION,
                Turnos_UnidadesReaccionUbicacion.LATITUD,Turnos_UnidadesReaccionUbicacion.LONGITUD,
                Turnos_UnidadesReaccionUbicacion.DIRECCION,
                Turnos_UnidadesReaccionUbicacion.ID_TURNO,Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL,%s TEXT NULL,%s TEXT NULL,%s TEXT NULL," +
                        "%s TEXT NULL ,%s TEXT NULL,%s TEXT NULL,%s TEXT NULL,"+
                        "%s TEXT NULL ,%s BOOLEAN DEFAULT 0)",
                Tablas.CLIENTE, BaseColumns._ID,
                Clientes.ID_CLIENTE,Clientes.NOMBRES_CLIENTE, Clientes.APELLIDO_PATERNO,Clientes.APELLIDO_MATERNO,
                Clientes.RAZON_SOCIAL_CLIENTE, Clientes.RUC_CLIENTE, Clientes.DIRECCION_CLIENTE, Clientes.LATITUD_CLIENTE,
                Clientes.LONGITUD_CLIENTE, Clientes.MONITOREO_ACTIVO));

        mockData(db);
        //mockDataTurnos(db);
        //mockDataTiposUnidadReaccion(db);
        //mockDataUnidadesReaccion(db);
        //mockDataTurno_UnidadesReaccionUbicacion(db);
        //mockDataClientes(db);
        //mockDataEmpleados(db);
        //mockDataDispositivosEmpleados(db);
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
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TURNO);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TIPO_UNIDAD_REACCION);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.UNIDAD_REACCION);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.TURNO_UNIDAD_REACCION_UBICACION);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLIENTE);

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

    // Insertar datos ficticios para prueba inicial
    private void mockDataTurnos(SQLiteDatabase sqLiteDatabase) {
        mockTurno(sqLiteDatabase, new Turno("01", "Turno Mañana","08:00:00","16:00:00"));
        mockTurno(sqLiteDatabase, new Turno("02", "Turno Tarde","16:00:00","00:00:00"));
        mockTurno(sqLiteDatabase, new Turno("03", "Turno Noche","00:00:00","08:00:00"));
    }

    private void mockDataTiposUnidadReaccion(SQLiteDatabase sqLiteDatabase) {
        mockTipoUnidadReaccion(sqLiteDatabase, new TipoUnidadReaccion("01", "Halcón","ic_motorcycle_black_24dp.png"));
        mockTipoUnidadReaccion(sqLiteDatabase, new TipoUnidadReaccion("02", "Cóndor","ic_directions_car_black_24dp.png"));
    }

    private void mockDataUnidadesReaccion(SQLiteDatabase sqLiteDatabase) {
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("01", "01","Halcón 1","XP-8709","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("02", "01","Halcón 2","ER-3122","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("03", "01","Halcón 3","YU-4444","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("04", "01","Halcón 4","IO-5555","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("05", "01","Halcón 5","OP-6666","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("06", "01","Halcón 6","AS-7777","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("07", "01","Halcón 7","SD-8888","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("08", "01","Halcón 8","DF-9999","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("09", "01","Halcón 9","GH-5564","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("10", "01","Halcón 10","JK-4443","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("11", "01","Halcón 11","KJ-6777","Honda","Terreno","Azul"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("12", "01","Halcón 12","VC-8666","Honda","Terreno","Azul"));

        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("13", "02","Cóndor 13","XP-8709","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("14", "02","Cóndor 14","ER-3122","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("15", "02","Cóndor 15","YU-4444","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("16", "02","Cóndor 16","IO-5555","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("17", "02","Cóndor 17","OP-6666","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("18", "02","Cóndor 18","AS-7777","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("19", "02","Cóndor 19","SD-8888","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("20", "02","Cóndor 20","DF-9999","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("21", "02","Cóndor 21","GH-5564","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("22", "02","Cóndor 22","JK-4443","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("23", "02","Cóndor 23","KJ-6777","Nissan","Terreno","Blanco"));
        mockUnidadReaccion(sqLiteDatabase, new UnidadReaccion("24", "02","Cóndor 24","VC-8666","Nissan","Terreno","Blanco"));

    }

    private void mockDataTurno_UnidadesReaccionUbicacion(SQLiteDatabase sqLiteDatabase) {
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","01",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","02",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","03",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","04",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","05",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","06",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","07",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("01","08",Double.valueOf(0),Double.valueOf(0),""));

        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","01",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","02",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","03",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","04",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","05",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","06",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","07",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("02","08",Double.valueOf(0),Double.valueOf(0),""));

        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","13",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","14",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","15",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","16",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","17",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","18",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","19",Double.valueOf(0),Double.valueOf(0),""));
        mockTurno_UnidadesReaccionUbicacion(sqLiteDatabase, new Turno_UnidadReaccionUbicacion("03","20",Double.valueOf(0),Double.valueOf(0),""));
    }

    private void mockDataClientes(SQLiteDatabase sqLiteDatabase)
    {
        mockCliente(sqLiteDatabase, new Cliente("11111111111","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.063355","-77.028431",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111112","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.063746","-77.028479",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111113","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.065328","-77.034696",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111114","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.075864","-77.034296",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111115","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.069483","-77.037914",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111116","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074459","-77.027633",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111117","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.079473","-77.033952",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111118","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.080648","-77.033180",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111119","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.081446","-77.033781",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111110","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.080774","-77.030347",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111211","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.082411","-77.030567",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111212","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.079012","-77.028717",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111213","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.079515","-77.026785",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111214","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.078876","-77.026689",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111215","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.079421","-77.026796",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111216","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.077931","-77.024071",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111217","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.076410","-77.022880",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111218","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.082243","-77.022150",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111219","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.075319","-77.021807",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111220","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.076913","-77.020906",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111221","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.084887","-77.021550",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111222","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.078928","-77.020262",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111223","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.081487","-77.019833",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111224","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.080858","-77.016786",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111225","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.073682","-77.015499",true));

        mockCliente(sqLiteDatabase, new Cliente("11111111226","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.103791","-76.962974",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111227","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.100913","-76.964946",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111228","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.104533","-76.964946",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111229","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.105720","-76.966099",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111230","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.106758","-76.967950",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111231","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.100272","-76.969772",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111232","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.100797","-76.971725",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111233","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.101090","-76.971317",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111234","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.101657","-76.971682",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111235","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.103293","-76.971918",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111236","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.102244","-76.972304",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111237","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.101846","-76.971382",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111238","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.103398","-76.972948",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111239","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.104972","-76.972991",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111240","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.105790","-76.973420",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111241","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.107490","-76.971639",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111242","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.106545","-76.973828",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111243","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.107490","-76.968506",true));

        mockCliente(sqLiteDatabase, new Cliente("11111111244","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.076528","-77.093597",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111245","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.077535","-77.093844",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111246","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.078039","-77.092031",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111247","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.075112","-77.092471",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111248","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074073","-77.092578",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111249","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074010","-77.092267",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111250","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074125","-77.091956",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111251","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074524","-77.090357",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111252","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074167","-77.090143",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111253","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074566","-77.089542",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111254","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.077462","-77.089735",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111255","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.077546","-77.085991",true));
        mockCliente(sqLiteDatabase, new Cliente("11111111256","Nombre 11111111111","Apellido Paterno 11111111111","Apellido Materno 11111111111" ,"","","","-12.074846","-77.083170",true));

    }
/////////////////////////////////////////////////////////////////////////////////////////////
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

    public long mockTurno(SQLiteDatabase db, Turno turno) {
        return db.insert(
                Tablas.TURNO,
                null,
                turno.toContentValues());
    }

    public long mockTipoUnidadReaccion(SQLiteDatabase db, TipoUnidadReaccion tipoUnidadReaccion) {
        return db.insert(
                Tablas.TIPO_UNIDAD_REACCION,
                null,
                tipoUnidadReaccion.toContentValues());
    }

    public long mockUnidadReaccion(SQLiteDatabase db, UnidadReaccion unidadReaccion) {
        return db.insert(
                Tablas.UNIDAD_REACCION,
                null,
                unidadReaccion.toContentValues());
    }

    public long mockTurno_UnidadesReaccionUbicacion(SQLiteDatabase db, Turno_UnidadReaccionUbicacion turno_unidadReaccionUbicacion) {
        return db.insert(
                Tablas.TURNO_UNIDAD_REACCION_UBICACION,
                null,
                turno_unidadReaccionUbicacion.toContentValues());
    }

    public long mockCliente(SQLiteDatabase db, Cliente cliente) {
        return db.insert(
                Tablas.CLIENTE,
                null,
                cliente.toContentValues());
    }
}
