package com.soloparaapasionados.identidadmobile.ServicioRemoto;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.TipoUnidadReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposUnidadReaccion;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaTiposUnidadReaccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by USUARIO on 19/07/2017.
 */

public class TipoUnidadReaccionServicioRemoto extends IntentService {
    private static final String TAG = TipoUnidadReaccionServicioRemoto.class.getSimpleName();

    public static final String ACCION_LEER_TIPO_UNIDAD_REACCION_TURNO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_LEER_TIPO_UNIDAD_REACCION_ISERVICE";
    //public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    //public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    //public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    //public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    SyncResult syncResult;

    private final String[] proyTipoUnidadReaccion = new String[]{
            BaseColumns._ID,
            Tablas.TIPO_UNIDAD_REACCION + "." + TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION,
            Tablas.TIPO_UNIDAD_REACCION + "." + TiposUnidadReaccion.DESCRIPCION,
            Tablas.TIPO_UNIDAD_REACCION + "." + TiposUnidadReaccion.FOTO};

    public TipoUnidadReaccionServicioRemoto() {
        super("TipoUnidadReaccionServicioRemoto");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (TipoUnidadReaccionServicioRemoto.ACCION_LEER_TIPO_UNIDAD_REACCION_TURNO_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                leerTiposUnidadReaccionServicioRemotoRemoto();
            }

            /*if (EmpleadoServicioRemoto.ACCION_INSERTAR_EMPLEADO_ISERVICE.equals(action)) {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO);

                insertarEmpleadoRemoto(empleado);
            }*/

            /*if (EmpleadoServicioRemoto.ACCION_INSERTAR_EMPLEADO_ISERVICE.equals(action)) {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                insertarEmpleadoLocal(empleado);
            }

            if (EmpleadoServicioRemoto.ACCION_ACTUALIZAR_EMPLEADO_ISERVICE.equals(action)) {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                actualizarEmpleadoLocal(empleado);
            }

            if (EmpleadoServicioRemoto.ACCION_ELIMINAR_EMPLEADO_ISERVICE.equals(action)) {

                String idEmpleado = intent.getStringExtra(EmpleadoServicioLocal.EXTRA_ID_EMPLEADO);

                eliminarEmpleadoLocal(idEmpleado);
            }*/


        }
    }

    private void leerTiposUnidadReaccionServicioRemotoRemoto(){
        //try {
        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando lectura de registros de tipos de unidad de reacción ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudTiposUnidadReaccionGet();

        // Quitar de primer plano
        builder.setProgress( 2, 2, false);
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Remoto destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        /*Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);*/
    }

    /**
     * Carga el adaptador con las metas obtenidas
     * en la respuesta
     */
    public void solicitudTiposUnidadReaccionGet()
    {

        GsonRequest gsonRequest=new GsonRequest<ListaTiposUnidadReaccion>(
                Constantes.GET_TIPOS_UNIDAD_REACCION,
                ListaTiposUnidadReaccion.class,
                null,
                new Response.Listener<ListaTiposUnidadReaccion>() {
                    @Override
                    public void onResponse(ListaTiposUnidadReaccion response) {
                        // Procesar la respuesta Json
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Volley: " + error.toString());
                        Toast.makeText(getBaseContext(),"Error Volley" + error.toString() ,Toast.LENGTH_LONG).show();
                    }
                });

        gsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(gsonRequest);
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param responseListaTiposUnidadReaccion Objeto Json con la respuesta
     */
    private void procesarRespuesta(ListaTiposUnidadReaccion responseListaTiposUnidadReaccion)
    {
        try{
            // Obtener atributo "estado"
            String estado = responseListaTiposUnidadReaccion.getEstado();

            switch (estado)
            {
                case "1": // EXITO
                    int cantidadTiposUnidadReaccion=responseListaTiposUnidadReaccion.getItems().size();
                    Toast.makeText(getBaseContext(), String.valueOf(cantidadTiposUnidadReaccion),Toast.LENGTH_LONG).show();

                    if (cantidadTiposUnidadReaccion>0)
                    {
                        sincronizarTiposUnidadReaccion(responseListaTiposUnidadReaccion.getItems());
                    }

                    break;
                case "2": // FALLIDO
                    String mensaje2 = responseListaTiposUnidadReaccion.getMensaje();
                    Toast.makeText(getBaseContext(), mensaje2,Toast.LENGTH_LONG).show();
                    break;
            }

        }
        catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
        }
    }

    //Procesador de nuevo registros para empleados
    private void sincronizarTiposUnidadReaccion(List<TipoUnidadReaccion> listaTiposUnidadReaccion) {
        syncResult= new SyncResult();
        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas de los registros
        HashMap<String, TipoUnidadReaccion> hashMapTiposUnidadReaccionEntrantes = new HashMap<String, TipoUnidadReaccion>();
        for (TipoUnidadReaccion t : listaTiposUnidadReaccion) {
            hashMapTiposUnidadReaccionEntrantes.put(t.getIdTipoUnidadReaccion(), t);
        }

        // Consultar registros remotos actuales
        Uri uri =  TiposUnidadReaccion.crearUriTipoUnidadReaccionLista("desactivado") ;
        /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/

        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(uri, proyTipoUnidadReaccion, null, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        while (c.moveToNext())
        {
            syncResult.stats.numEntries++;

            TipoUnidadReaccion tipoUnidadReaccionLocal= new TipoUnidadReaccion(c);

            TipoUnidadReaccion tipoUnidadReaccionMatch = hashMapTiposUnidadReaccionEntrantes.get(tipoUnidadReaccionLocal.getIdTipoUnidadReaccion());

            if (tipoUnidadReaccionMatch != null)
            {
                // Esta entrada existe, por lo que se remueve del mapeado
                hashMapTiposUnidadReaccionEntrantes.remove(tipoUnidadReaccionLocal.getIdTipoUnidadReaccion());

                Uri existingUri = TiposUnidadReaccion.crearUriTipoUnidadReaccion(tipoUnidadReaccionLocal.getIdTipoUnidadReaccion());

                // Comprobar si el gasto necesita ser actualizado
                boolean b = !tipoUnidadReaccionMatch.getDescripcion().equals(tipoUnidadReaccionLocal.getDescripcion());
                boolean b1 = tipoUnidadReaccionMatch.getFoto() != null && !tipoUnidadReaccionMatch.getFoto().equals(tipoUnidadReaccionLocal.getFoto());

                if (b || b1)
                {
                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(TiposUnidadReaccion.DESCRIPCION, tipoUnidadReaccionMatch.getDescripcion())
                            .withValue(TiposUnidadReaccion.FOTO, tipoUnidadReaccionMatch.getFoto())
                            .build());
                    syncResult.stats.numUpdates++;
                }
                else
                {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            }
            else
            {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = TiposUnidadReaccion.crearUriTipoUnidadReaccion(tipoUnidadReaccionLocal.getIdTipoUnidadReaccion());
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (TipoUnidadReaccion tipoUnidadReaccionRemoto : hashMapTiposUnidadReaccionEntrantes.values()) {
            Log.i(TAG, "Programando inserción de: " + tipoUnidadReaccionRemoto.getIdTipoUnidadReaccion());
            ops.add(ContentProviderOperation.newInsert(TiposUnidadReaccion.crearUriTipoUnidadReaccion(tipoUnidadReaccionRemoto.getIdTipoUnidadReaccion()))
                    .withValue(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION, tipoUnidadReaccionRemoto.getIdTipoUnidadReaccion())
                    .withValue(TiposUnidadReaccion.DESCRIPCION, tipoUnidadReaccionRemoto.getDescripcion())
                    .withValue(TiposUnidadReaccion.FOTO, tipoUnidadReaccionRemoto.getFoto())
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0)
        {
            Log.i(TAG, "Aplicando operaciones...");
            try
            {
                resolver.applyBatch(ContratoCotizacion.AUTORIDAD, ops);
            }
            catch (RemoteException | OperationApplicationException e)
            {
                e.printStackTrace();
            }

            resolver.notifyChange(TiposUnidadReaccion.crearUriTipoUnidadReaccionLista("desactivado"), null, false);

            Log.i(TAG, "Sincronización finalizada.");
        }
        else
        {
            Log.i(TAG, "No se requiere sincronización");
        }

    }
}
