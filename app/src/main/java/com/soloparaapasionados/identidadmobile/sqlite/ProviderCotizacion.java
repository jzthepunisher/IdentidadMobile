package com.soloparaapasionados.identidadmobile.sqlite;

import com.soloparaapasionados.identidadmobile.ServicioLocal.EmpleadoServicioLocal;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.EmpleadoServicioRemoto;
import com.soloparaapasionados.identidadmobile.modelo.DispositivoEmpleado;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleadosTemporal;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class ProviderCotizacion extends ContentProvider {

    public static final String TAG = "ProviderCotizacion";
    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private BaseDatosCotizaciones helper;

    private ContentResolver resolver;

    public ProviderCotizacion() {
    }

    // [URI_MATCHER]
    public static final UriMatcher uriMatcher;

    // Casos
    public static final int DISPOSITIVOS = 100;
    public static final int DISPOSITIVOS_ID = 101;
    public static final int CARGOS = 200;
    public static final int EMPLEADOS=300;
    public static final int EMPLEADOS_ID=301;
    public static final int DISPOSITIVOS_EMPLEADOS=400;
    public static final int DISPOSITIVOS_EMPLEADOS_ID=401;
    public static final int DISPOSITIVOS_EMPLEADOS_TEMPORAL=500;
    public static final int DISPOSITIVOS_EMPLEADOS_TEMPORAL_ID=501;

    public static final String AUTORIDAD = "com.soloparaapasionados.identidadmobile";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTORIDAD, "dispositivos"             , DISPOSITIVOS);
        uriMatcher.addURI(AUTORIDAD, "dispositivos/#"           , DISPOSITIVOS_ID);
        uriMatcher.addURI(AUTORIDAD, "cargos"                   , CARGOS);
        uriMatcher.addURI(AUTORIDAD, "empleados"                , EMPLEADOS);
        uriMatcher.addURI(AUTORIDAD, "empleados/*"              , EMPLEADOS_ID);
        uriMatcher.addURI(AUTORIDAD, "dispositivos_empleados"   , DISPOSITIVOS_EMPLEADOS);
        uriMatcher.addURI(AUTORIDAD, "dispositivos_empleados/*" , DISPOSITIVOS_EMPLEADOS_ID);
        uriMatcher.addURI(AUTORIDAD, "dispositivos_empleados_temporal"   , DISPOSITIVOS_EMPLEADOS_TEMPORAL);
        uriMatcher.addURI(AUTORIDAD, "dispositivos_empleados_temporal/#" , DISPOSITIVOS_EMPLEADOS_TEMPORAL_ID);
    }
    // [/URI_MATCHER]

    // [CAMPOS_AUXILIARES DE PROYECCCIONES]
    private final String[] proyDispositivo = new String[]{
           Tablas.DISPOSITIVO + "." + ContratoCotizacion.Dispositivos.IMEI,
            ContratoCotizacion.Dispositivos.ID_TIPO_DISPOSITIVO,
            ContratoCotizacion.Dispositivos.ID_SIM_CARD,
            ContratoCotizacion.Dispositivos.NUMERO_CELULAR,
            ContratoCotizacion.Dispositivos.ENVIADO,
            ContratoCotizacion.Dispositivos.RECIBIDO,
            ContratoCotizacion.Dispositivos.VALIDADO};

    private final String[] proyCargo = new String[]{
            BaseColumns._ID,
            Tablas.CARGO + "." + Cargos.ID_CARGO,
            Cargos.DESCRIPCION};

    private final String[] proyEmpleado = new String[]{
            BaseColumns._ID,
            Tablas.EMPLEADO + "." + Empleados.ID_EMPLEADO,
            Empleados.NOMBRES,Empleados.APELLIDO_PATERNO,
            Empleados.APELLIDO_MAERNO,
            Empleados.FOTO};

    private final String[] proyDispositivoEmpleado = new String[]{
            Tablas.DISPOSITIVO_EMPLEADO + "." + BaseColumns._ID,
            Tablas.DISPOSITIVO_EMPLEADO + "." + DispositivosEmpleados.IMEI,
            Tablas.DISPOSITIVO_EMPLEADO + "." + DispositivosEmpleados.ID_EMPLEADO,
            Tablas.EMPLEADO + "." + Empleados.NOMBRES,
            Tablas.EMPLEADO + "." + Empleados.APELLIDO_PATERNO,
            Tablas.EMPLEADO + "." + Empleados.APELLIDO_MAERNO,
            Tablas.EMPLEADO + "." + Empleados.FOTO,
            Tablas.CARGO + "." + Cargos.DESCRIPCION};

    private final String[] proyDispositivoEmpleadoTemporal = new String[]{
            Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL + "." + BaseColumns._ID,
            Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL + "." + DispositivosEmpleados.IMEI,
            Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL + "." + DispositivosEmpleados.ID_EMPLEADO,
            Tablas.EMPLEADO + "." + Empleados.NOMBRES,
            Tablas.EMPLEADO + "." + Empleados.APELLIDO_PATERNO,
            Tablas.EMPLEADO + "." + Empleados.APELLIDO_MAERNO,
            Tablas.EMPLEADO + "." + Empleados.FOTO,
            Tablas.CARGO + "." + Cargos.DESCRIPCION};

    // [/CAMPOS_AUXILIARES]


    private static final String EMPLEADO_CARGO =
            "empleado INNER JOIN cargo " +
                    "ON empleado.id_cargo = cargo.id_cargo";

    private static final String DISPOSITIVO_EMPLEADO =
            "dispositivo INNER JOIN dispositivo_empleado " +
                    " ON dispositivo.imei = dispositivo_empleado.imei" +
            " INNER JOIN empleado " +
                    " ON dispositivo_empleado.id_empleado = empleado.id_empleado" +
            " INNER JOIN cargo " +
                    " ON empleado.id_cargo = cargo.id_cargo";

    private static final String DISPOSITIVO_EMPLEADO_TEMPORAL =
            "dispositivo INNER JOIN dispositivo_empleado_temporal " +
                    " ON dispositivo.imei = dispositivo_empleado_temporal.imei" +
                    " INNER JOIN empleado " +
                    " ON dispositivo_empleado_temporal.id_empleado = empleado.id_empleado" +
                    " INNER JOIN cargo " +
                    " ON empleado.id_cargo = cargo.id_cargo";

    int offSet=30;

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case DISPOSITIVOS:
                return ContratoCotizacion.generarMime("dispositivos");
            case DISPOSITIVOS_ID:
                return ContratoCotizacion.generarMimeItem("dispositivos");
            case CARGOS:
                return ContratoCotizacion.generarMime("cargos");
            case EMPLEADOS:
                return  ContratoCotizacion.generarMime("empleados");
            case EMPLEADOS_ID:
                return ContratoCotizacion.generarMimeItem("empleados");
            case DISPOSITIVOS_EMPLEADOS:
                return  ContratoCotizacion.generarMime("dispositivos_empleados");
            case DISPOSITIVOS_EMPLEADOS_ID:
                return ContratoCotizacion.generarMimeItem("dispositivos_empleados");
            case DISPOSITIVOS_EMPLEADOS_TEMPORAL:
                return  ContratoCotizacion.generarMime("dispositivos_empleados_temporal");
            case DISPOSITIVOS_EMPLEADOS_TEMPORAL_ID:
                return ContratoCotizacion.generarMimeItem("dispositivos_empleados_temporal");
            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase bd = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (uriMatcher.match(uri)) {
            case EMPLEADOS_ID:
                id = Empleados.obtenerIdEmpleado(uri);
                String seleccion = String.format("%s=? ", Empleados.ID_EMPLEADO);
                String[] argumentos={id};

                ContentValues values=new ContentValues();
                values.put(Empleados.ELIMINADO,1);

                afectados = bd.update(Tablas.EMPLEADO, values, seleccion, argumentos);
                break;
            case DISPOSITIVOS_EMPLEADOS_TEMPORAL_ID:
                // Consultando una Dispositivo Empleado
                id = DispositivosEmpleadosTemporal.obtenerIdDispostivoEmpleadoTemporal(uri);
                seleccion = String.format("%s=? ", DispositivosEmpleadosTemporal.IMEI);
                String[] argumentosDos={id};

                afectados = bd.delete(Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL,seleccion,argumentosDos);

                break;
            case DISPOSITIVOS_EMPLEADOS_ID:
                // Consultando una Dispositivo Empleado
                id = DispositivosEmpleados.obtenerIdDispostivoEmpleado(uri);
                seleccion = String.format("%s=? ", DispositivosEmpleados.IMEI);
                String[] argumentosTres={id};

                afectados = bd.delete(Tablas.DISPOSITIVO_EMPLEADO,seleccion,argumentosTres);

                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

        notificarCambio(uri);

        return afectados;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Log.d(TAG, "Inserción en " + uri + "( " + values.toString() + " )\n");

        SQLiteDatabase bd = helper.getWritableDatabase();
        String id = null;

        switch (uriMatcher.match(uri)) {
            case DISPOSITIVOS:
                id = values.getAsString(Dispositivos.IMEI);
                bd.insertOrThrow(Tablas.DISPOSITIVO, null, values);
                notificarCambio(uri);
                return Dispositivos.crearUriDispositivo(id);
            case EMPLEADOS:
                id = values.getAsString(Empleados.ID_EMPLEADO);
                bd.insertOrThrow(Tablas.EMPLEADO,null,values);
                notificarCambio(uri);

                actualizaIntentoInsercionEmpleadoRemotamente(bd,id);
                insertarEmpleadoRemotamente(values);

                return Empleados.crearUriEmpleado(id);
            case DISPOSITIVOS_EMPLEADOS_ID:
                String[] claves = DispositivosEmpleados.obtenerIdDispositivoEmpleado(uri);
                String imei=claves[0];
                String idEmpleado=claves[1];

                /*String seleccion = String.format("%s=? AND %s=?",
                        DispositivosEmpleados.IMEI, DispositivosEmpleados.ID_EMPLEADO);*/

                ContentValues valuesDos = new ContentValues();
                valuesDos.put(DispositivosEmpleados.IMEI,imei);
                valuesDos.put(DispositivosEmpleados.ID_EMPLEADO,idEmpleado);

                bd.insertOrThrow(Tablas.DISPOSITIVO_EMPLEADO, null,valuesDos);
                notificarCambio(uri);
                return DispositivosEmpleados.crearUriDispositivoEmpleado(id,idEmpleado);
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

    }

    @Override
    public boolean onCreate() {
        helper = new BaseDatosCotizaciones(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Obtener base de datos
        SQLiteDatabase bd = helper.getReadableDatabase();

        // Comparar Uri
        int match = uriMatcher.match(uri);

        // string auxiliar para los ids
        String id;

        Cursor c=null;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        switch (match) {
            case DISPOSITIVOS:
                // Consultando todas las cabeceras de pedido
                builder.setTables(Tablas.DISPOSITIVO);
                c = builder.query(bd, proyDispositivo,
                        null, null, null, null,null);
                break;
            case DISPOSITIVOS_ID:
                // Consultando una cabecera de pedido
                id = Dispositivos.obtenerIdDispostivo(uri);
                builder.setTables(Tablas.DISPOSITIVO);
                c = builder.query(bd, proyDispositivo,
                        Dispositivos.IMEI + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case CARGOS:
                // Consultando todos los cargos
                builder.setTables(Tablas.CARGO);
                c = builder.query(bd, proyCargo,
                        null, null, null, null,null);
                break;
            case EMPLEADOS:
                // Obtener filtro
                int offSetActual=0;

                if (Empleados.tieneOffSet(uri)){

                    leerEmpleadosRemotamente();

                    offSetActual=Empleados.tieneOffSet(uri)? Integer.valueOf( uri.getQueryParameter("off_set")) : 0;

                    String query = "select " + Tablas.EMPLEADO + "." + BaseColumns._ID + "," + Empleados.ID_EMPLEADO + "," + Empleados.NOMBRES;
                    query += "," + Empleados.APELLIDO_PATERNO + "," + Empleados.APELLIDO_MAERNO + "," + Empleados.FOTO;
                    query += "," + Tablas.CARGO + "." + Cargos.DESCRIPCION;
                    query += " from " + EMPLEADO_CARGO + " where " + Empleados.ELIMINADO + " = 0 limit ?,?";

                    c = bd.rawQuery(query, new String[]{String.valueOf(offSetActual),String.valueOf(offSet)});
                }

                if (Empleados.tieneFiltroBusqueda(uri)){
                    String parametroConsulta="";
                    parametroConsulta=String.valueOf( uri.getQueryParameter(Empleados.PARAMETRO_FILTRO_BUSQUEDA)) ;

                    String query = "select " + Tablas.EMPLEADO + "." + BaseColumns._ID + "," + Empleados.ID_EMPLEADO + "," + Empleados.NOMBRES;
                    query += "," + Empleados.APELLIDO_PATERNO + "," + Empleados.APELLIDO_MAERNO + "," + Empleados.FOTO;
                    query += "," + Tablas.CARGO + "." + Cargos.DESCRIPCION;
                    query += " from " + EMPLEADO_CARGO + " where " + Empleados.ELIMINADO + " = 0 ";
                    query += " and (  (" + Empleados.NOMBRES + " like \'%" + parametroConsulta + "%\' ";
                    query += "           or " + Empleados.APELLIDO_PATERNO + " like \'%" + parametroConsulta + "%\' ";
                    query += "           or " + Empleados.APELLIDO_MAERNO + " like \'%" + parametroConsulta + "%\'  ) ";
                    query += "        or  ((" + Empleados.NOMBRES + " || " + "\' \'" + " || " + Empleados.APELLIDO_PATERNO + " || " + "\' \'" + " || " + Empleados.APELLIDO_MAERNO + ") like \'%" + parametroConsulta + "%\' ) ) ";
                    query += " limit ?,?";

                    c = bd.rawQuery(query, new String[]{String.valueOf(offSetActual),String.valueOf(offSet)});
                }

                break;
            case EMPLEADOS_ID:
                // Consultando un empleado
                id = Empleados.obtenerIdEmpleado(uri);
                builder.setTables(EMPLEADO_CARGO);
                c = builder.query(bd, null,
                        Empleados.ID_EMPLEADO + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;
            case DISPOSITIVOS_EMPLEADOS_ID:
                // Consultando una Dispositivo Empleado
                id = DispositivosEmpleados.obtenerIdDispostivoEmpleado(uri);
                builder.setTables(DISPOSITIVO_EMPLEADO);
                c = builder.query(bd, proyDispositivoEmpleado,
                        Tablas.DISPOSITIVO_EMPLEADO + '.' + DispositivosEmpleados.IMEI + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;

            case DISPOSITIVOS_EMPLEADOS_TEMPORAL_ID:
                // Consultando una Dispositivo Empleado
                id = DispositivosEmpleadosTemporal.obtenerIdDispostivoEmpleadoTemporal(uri);

                String query = "delete from " +  Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL;

                bd.execSQL(query);

                query = "INSERT INTO " +  Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL + "(" + DispositivosEmpleadosTemporal.IMEI + "," +  DispositivosEmpleadosTemporal.ID_EMPLEADO +")";
                query += " SELECT " + DispositivosEmpleados.IMEI + "," +  DispositivosEmpleados.ID_EMPLEADO ;
                query += " from " + Tablas.DISPOSITIVO_EMPLEADO ;
                query += " where " + Tablas.DISPOSITIVO_EMPLEADO + '.' + DispositivosEmpleados.IMEI + "=" + "\'" + id + "\'";

                bd.execSQL(query);

                builder.setTables(DISPOSITIVO_EMPLEADO_TEMPORAL);
                c = builder.query(bd, proyDispositivoEmpleadoTemporal,
                        Tablas.DISPOSITIVO_EMPLEADO_TEMPORAL + '.' + DispositivosEmpleadosTemporal.IMEI + "=" + "\'" + id + "\'"
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

        c.setNotificationUri(resolver, uri);

        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase bd = helper.getWritableDatabase();
        String id;
        int afectados;

        switch (uriMatcher.match(uri)) {

            case DISPOSITIVOS_ID:

                id = Dispositivos.obtenerIdDispostivo(uri);
                String seleccion = String.format("%s=? ", Dispositivos.IMEI);
                String[] argumentos={id};

                afectados = bd.update(Tablas.DISPOSITIVO, values, seleccion, argumentos);
                break;
            case EMPLEADOS_ID:
                id = Empleados.obtenerIdEmpleado(uri);
                seleccion = String.format("%s=? ", Empleados.ID_EMPLEADO);
                String[] argumentosDos={id};

                if (Empleados.tieneEstadoRegistro(uri)){
                    String estado =Empleados.tieneEstadoRegistro(uri)? uri.getQueryParameter(ContratoCotizacion.Empleados.PARAMETRO_ESTADO_REGISTRO) : "";
                    values=new ContentValues();
                    values.put(Empleados.ESTADO,estado);

                    afectados = bd.update(Tablas.EMPLEADO, values, seleccion, argumentosDos);
                }
                else
                {
                    afectados = bd.update(Tablas.EMPLEADO, values, seleccion, argumentosDos);
                }


                break;
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

        notificarCambio(uri);

        return afectados;
    }

    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null);
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    private void leerEmpleadosRemotamente(){
        Intent intent = new Intent(getContext(), EmpleadoServicioRemoto.class);
        intent.setAction(EmpleadoServicioRemoto.ACCION_LEER_EMPLEADO_ISERVICE);
        //intent.putExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO, empleado);
        getContext().startService(intent);
    }

    private void insertarEmpleadoRemotamente(ContentValues values){
        Intent intent = new Intent(getContext(), EmpleadoServicioRemoto.class);
        intent.setAction(EmpleadoServicioRemoto.ACCION_INSERTAR_EMPLEADO_ISERVICE);
        Empleado empleado=new Empleado(values);
        intent.putExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO, empleado);
        getContext().startService(intent);
    }

    private void actualizaIntentoInsercionEmpleadoRemotamente(SQLiteDatabase bd,String idEmpleado){

        String seleccion = String.format("%s=? ", Empleados.ID_EMPLEADO);
        String[] argumentos={idEmpleado};

        ContentValues values=new ContentValues();
        values.put(Empleados.ESTADO, EstadoRegistro.REGISTRANDO_REMOTAMENTE);

        int afectados=0;
        afectados = bd.update(Tablas.EMPLEADO, values, seleccion, argumentos);
    }

}
