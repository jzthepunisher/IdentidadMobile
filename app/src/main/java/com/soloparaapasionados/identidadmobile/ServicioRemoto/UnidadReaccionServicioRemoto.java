package com.soloparaapasionados.identidadmobile.ServicioRemoto;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
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
import com.soloparaapasionados.identidadmobile.modelo.UnidadReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UnidadesReaccion;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaUnidadesReaccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UnidadReaccionServicioRemoto extends IntentService {
    private static final String TAG = UnidadReaccionServicioRemoto.class.getSimpleName();

    public static final String ACCION_LEER_UNIDAD_REACCION_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_LEER_UNIDAD_REACCION_ISERVICE";
    //public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    //public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    //public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    //public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    SyncResult syncResult;

    private final String[] proyUnidadReaccion = new String[]{
            BaseColumns._ID,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.ID_UNIDAD_REACCION,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.ID_TIPO_UNIDAD_REACCION,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.DESCRIPCION,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.PLACA,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.MARCA,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.MODELO,
            Tablas.UNIDAD_REACCION + "." + UnidadesReaccion.COLOR};

    public UnidadReaccionServicioRemoto() {
        super("UnidadReaccionServicioRemoto");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (UnidadReaccionServicioRemoto.ACCION_LEER_UNIDAD_REACCION_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                leerUnidadesReaccionServicioRemotoRemoto();
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

    private void leerUnidadesReaccionServicioRemotoRemoto(){
        //try {
        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando lectura de registros de unidades de reacción ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudUnidadesReaccionGet();

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
    public void solicitudUnidadesReaccionGet()
    {

        GsonRequest gsonRequest=new GsonRequest<ListaUnidadesReaccion>(
                Constantes.GET_UNIDADES_REACCION,
                ListaUnidadesReaccion.class,
                null,
                new Response.Listener<ListaUnidadesReaccion>() {
                    @Override
                    public void onResponse(ListaUnidadesReaccion response) {
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
     * @param responseListaUnidadesReaccion Objeto Json con la respuesta
     */
    private void procesarRespuesta(ListaUnidadesReaccion responseListaUnidadesReaccion)
    {
        try{
            // Obtener atributo "estado"
            String estado = responseListaUnidadesReaccion.getEstado();

            switch (estado)
            {
                case "1": // EXITO
                    int cantidadUnidadesReaccion=responseListaUnidadesReaccion.getItems().size();
                    Toast.makeText(getBaseContext(), String.valueOf(cantidadUnidadesReaccion),Toast.LENGTH_LONG).show();

                    if (cantidadUnidadesReaccion>0)
                    {
                        sincronizarUnidadesdReaccion(responseListaUnidadesReaccion.getItems());
                    }

                    break;
                case "2": // FALLIDO
                    String mensaje2 = responseListaUnidadesReaccion.getMensaje();
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
    private void sincronizarUnidadesdReaccion(List<UnidadReaccion> listaUnidadesReaccion) {
        syncResult= new SyncResult();
        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas de los registros
        HashMap<String, UnidadReaccion> hashMapUnidadesReaccionEntrantes = new HashMap<String, UnidadReaccion>();
        for (UnidadReaccion t : listaUnidadesReaccion) {
            hashMapUnidadesReaccionEntrantes.put(t.getIdUnidadReaccion() , t);
        }

        // Consultar registros remotos actuales
        Uri uri =  UnidadesReaccion.crearUriUnidadReaccionLista("desactivado") ;
        /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/

        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(uri, proyUnidadReaccion, null, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        while (c.moveToNext())
        {
            syncResult.stats.numEntries++;

            UnidadReaccion unidadReaccionLocal= new UnidadReaccion(c);

            UnidadReaccion unidadReaccionMatch = hashMapUnidadesReaccionEntrantes.get(unidadReaccionLocal.getIdTipoUnidadReaccion());

            if (unidadReaccionMatch != null)
            {
                // Esta entrada existe, por lo que se remueve del mapeado
                hashMapUnidadesReaccionEntrantes.remove(unidadReaccionLocal.getIdTipoUnidadReaccion());

                Uri existingUri = UnidadesReaccion.crearUriUnidadReaccion(unidadReaccionLocal.getIdUnidadReaccion());

                // Comprobar si el gasto necesita ser actualizado
                boolean b = !unidadReaccionMatch.getIdTipoUnidadReaccion().equals(unidadReaccionLocal.getIdTipoUnidadReaccion());
                boolean b1 = unidadReaccionMatch.getDescripcion() != null && !unidadReaccionMatch.getDescripcion().equals(unidadReaccionLocal.getDescripcion());
                boolean b2 = unidadReaccionMatch.getPlaca() != null && !unidadReaccionMatch.getPlaca().equals(unidadReaccionLocal.getPlaca());
                boolean b3 = unidadReaccionMatch.getMarca() != null && !unidadReaccionMatch.getMarca().equals(unidadReaccionLocal.getMarca());
                boolean b4 = unidadReaccionMatch.getModelo() != null && !unidadReaccionMatch.getModelo().equals(unidadReaccionLocal.getModelo());
                boolean b5 = unidadReaccionMatch.getColor() != null && !unidadReaccionMatch.getColor().equals(unidadReaccionLocal.getColor());


                if (b || b1 || b2 || b3 || b4 || b5)
                {
                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(UnidadesReaccion.ID_UNIDAD_REACCION, unidadReaccionMatch.getIdUnidadReaccion())
                            .withValue(UnidadesReaccion.ID_TIPO_UNIDAD_REACCION, unidadReaccionMatch.getIdTipoUnidadReaccion())
                            .withValue(UnidadesReaccion.DESCRIPCION, unidadReaccionMatch.getDescripcion())
                            .withValue(UnidadesReaccion.PLACA, unidadReaccionMatch.getPlaca())
                            .withValue(UnidadesReaccion.MARCA, unidadReaccionMatch.getMarca())
                            .withValue(UnidadesReaccion.MODELO, unidadReaccionMatch.getModelo())
                            .withValue(UnidadesReaccion.COLOR, unidadReaccionMatch.getColor())
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
                Uri deleteUri = UnidadesReaccion.crearUriUnidadReaccion(unidadReaccionLocal.getIdUnidadReaccion());
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (UnidadReaccion unidadReaccionRemoto : hashMapUnidadesReaccionEntrantes.values())
        {
            Log.i(TAG, "Programando inserción de: " + unidadReaccionRemoto.getIdTipoUnidadReaccion());
            ops.add(ContentProviderOperation.newInsert(UnidadesReaccion.crearUriUnidadReaccion(unidadReaccionRemoto.getIdUnidadReaccion()))
                    .withValue(UnidadesReaccion.ID_UNIDAD_REACCION, unidadReaccionRemoto.getIdUnidadReaccion())
                    .withValue(UnidadesReaccion.ID_TIPO_UNIDAD_REACCION, unidadReaccionRemoto.getIdTipoUnidadReaccion())
                    .withValue(UnidadesReaccion.DESCRIPCION, unidadReaccionRemoto.getDescripcion())
                    .withValue(UnidadesReaccion.PLACA, unidadReaccionRemoto.getPlaca())
                    .withValue(UnidadesReaccion.MARCA, unidadReaccionRemoto.getMarca())
                    .withValue(UnidadesReaccion.MODELO, unidadReaccionRemoto.getModelo())
                    .withValue(UnidadesReaccion.COLOR, unidadReaccionRemoto.getColor())
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

            resolver.notifyChange(UnidadesReaccion.crearUriUnidadReaccionLista("desactivado"), null, false);

            Log.i(TAG, "Sincronización finalizada.");
        }
        else
        {
            Log.i(TAG, "No se requiere sincronización");
        }

        leerTurnosUnidadesReaccionUbicacionRemotamente();

    }

    private void leerTurnosUnidadesReaccionUbicacionRemotamente()
    {
        Intent intent = new Intent(this, TurnoUnidadReaccionUbicacionServicioRemoto.class);
        intent.setAction(TurnoUnidadReaccionUbicacionServicioRemoto.ACCION_LEER_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE);
        //intent.putExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO, empleado);
        this.startService(intent);
    }
}
