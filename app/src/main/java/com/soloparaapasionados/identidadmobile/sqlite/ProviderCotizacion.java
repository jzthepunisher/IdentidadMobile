package com.soloparaapasionados.identidadmobile.sqlite;


import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
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

    public static final String AUTORIDAD = "com.soloparaapasionados.identidadmobile";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTORIDAD, "dispositivos"  , DISPOSITIVOS);
        uriMatcher.addURI(AUTORIDAD, "dispositivos/#", DISPOSITIVOS_ID);
        uriMatcher.addURI(AUTORIDAD, "cargos"        , CARGOS);
        uriMatcher.addURI(AUTORIDAD, "empleados"     , EMPLEADOS);
        uriMatcher.addURI(AUTORIDAD, "empleados/*"   , EMPLEADOS_ID);
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
            Empleados.NOMBRES,Empleados.APELLIDO_PATERNO,Empleados.APELLIDO_MAERNO,
            Empleados.FOTO};

    // [/CAMPOS_AUXILIARES]


    private static final String EMPLEADO_CARGO =
            "empleado INNER JOIN cargo " +
                    "ON empleado.id_cargo = cargo.id_cargo";


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
            default:
                throw new UnsupportedOperationException("Uri desconocida =>" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
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
                return Empleados.crearUriEmpleado(id);
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

        Cursor c;

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
                offSetActual=Empleados.tieneOffSet(uri)? Integer.valueOf( uri.getQueryParameter("off_set")) : 0;

                //String clausulaWhere=BaseColumns._ID  + ">=" + "" + offSetActual + " AND " +  BaseColumns._ID + " <= " + (offSetActual + this.offSet);
                //String clausulaWhere=" 1 = 1 limit " + offSetActual + "," + this.offSet;

                // Consultando todos los Empleados con offset
                //builder.setTables(Tablas.EMPLEADO);
                //c = builder.query(bd, proyEmpleado,
                //        clausulaWhere , null, null, null,null);

                String query = "select " + Tablas.EMPLEADO + "." + BaseColumns._ID + "," + Empleados.ID_EMPLEADO + "," + Empleados.NOMBRES;
                query += "," + Empleados.APELLIDO_PATERNO + "," + Empleados.APELLIDO_MAERNO + "," + Empleados.FOTO;
                query += "," + Tablas.CARGO + "." + Cargos.DESCRIPCION;
                query += " from " + EMPLEADO_CARGO + " limit ?,?";

                c = bd.rawQuery(query, new String[]{String.valueOf(offSetActual),String.valueOf(offSet)});

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
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }


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
}
