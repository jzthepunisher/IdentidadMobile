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
import com.soloparaapasionados.identidadmobile.modelo.Turno_UnidadReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos_UnidadesReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaTurnosUnidadesReaccionUbicacion;

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
public class TurnoUnidadReaccionUbicacionServicioRemoto extends IntentService {
    private static final String TAG = TurnoUnidadReaccionUbicacionServicioRemoto.class.getSimpleName();

    public static final String ACCION_LEER_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_LEER_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE";
    public static final String ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE";

    //public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    //public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    //public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    //public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    SyncResult syncResult;

    private final String[] proyTurnoUnidadReaccionUbicacion = new String[]{
            BaseColumns._ID,
            Tablas.TURNO_UNIDAD_REACCION_UBICACION + "." + Turnos_UnidadesReaccionUbicacion.ID_TURNO,
            Tablas.TURNO_UNIDAD_REACCION_UBICACION + "." + Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION,
            Tablas.TURNO_UNIDAD_REACCION_UBICACION + "." + Turnos_UnidadesReaccionUbicacion.LATITUD,
            Tablas.TURNO_UNIDAD_REACCION_UBICACION + "." + Turnos_UnidadesReaccionUbicacion.LONGITUD,
            Tablas.TURNO_UNIDAD_REACCION_UBICACION + "." + Turnos_UnidadesReaccionUbicacion.DIRECCION};


    public TurnoUnidadReaccionUbicacionServicioRemoto() {
        super("TurnoUnidadReaccionUbicacionServicioRemoto");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (TurnoUnidadReaccionUbicacionServicioRemoto.ACCION_LEER_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                leerTurnoUnidadReaccionUbicacionServicioRemoto();
            }

            if (TurnoUnidadReaccionUbicacionServicioRemoto.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                actualizarTurnoUnidadReaccionUbicacionServicioRemoto();
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

    private void leerTurnoUnidadReaccionUbicacionServicioRemoto()
    {
        //try {
        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando lectura de registros de ubicación de unidades de reacción ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudTurnosUnidadesReaccionUbicacionGet();

        // Quitar de primer plano
        builder.setProgress( 2, 2, false);
        stopForeground(true);
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "Servicio Remoto destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        /*Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);*/
    }

    public void solicitudTurnosUnidadesReaccionUbicacionGet()
    {

        GsonRequest gsonRequest=new GsonRequest<ListaTurnosUnidadesReaccionUbicacion>(
                Constantes.GET_TURNOS_UNIDADES_REACCION_UBICACION,
                ListaTurnosUnidadesReaccionUbicacion.class,
                null,
                new Response.Listener<ListaTurnosUnidadesReaccionUbicacion>() {
                    @Override
                    public void onResponse(ListaTurnosUnidadesReaccionUbicacion response) {
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

    private void procesarRespuesta(ListaTurnosUnidadesReaccionUbicacion responseListaTurnosUnidadesReaccionUbicacion)
    {
        try
        {
            // Obtener atributo "estado"
            String estado = responseListaTurnosUnidadesReaccionUbicacion.getEstado();

            switch (estado)
            {
                case "1": // EXITO
                    int cantidadTurnosUnidadesReaccionUbicacion=responseListaTurnosUnidadesReaccionUbicacion.getItems().size();
                    Toast.makeText(getBaseContext(), String.valueOf(cantidadTurnosUnidadesReaccionUbicacion),Toast.LENGTH_LONG).show();

                    if (cantidadTurnosUnidadesReaccionUbicacion>0)
                    {
                        sincronizarTurnosUnidadesReaccionUbicacion(responseListaTurnosUnidadesReaccionUbicacion.getItems());
                    }

                    break;
                case "2": // FALLIDO
                    String mensaje2 = responseListaTurnosUnidadesReaccionUbicacion.getMensaje();
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
    private void sincronizarTurnosUnidadesReaccionUbicacion(List<Turno_UnidadReaccionUbicacion> listaTurnosUnidadesReaccionUbicacion)
    {
        syncResult= new SyncResult();
        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas de los registros
        HashMap<String, Turno_UnidadReaccionUbicacion> hashMapTurnosUnidadesReaccionUbicacionEntrantes = new HashMap<String, Turno_UnidadReaccionUbicacion>();
        for (Turno_UnidadReaccionUbicacion t : listaTurnosUnidadesReaccionUbicacion) {
            hashMapTurnosUnidadesReaccionUbicacionEntrantes.put(t.getIdTurno() + t.getIdUnidadReaccion() , t);
        }

        // Consultar registros remotos actuales
        Uri uri =  Turnos_UnidadesReaccionUbicacion.crearUriTurnoUnidadReaccionUbicacionLista("desactivado") ;
        /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/

        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(uri, proyTurnoUnidadReaccionUbicacion, null, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        while (c.moveToNext())
        {
            syncResult.stats.numEntries++;

            Turno_UnidadReaccionUbicacion turno_UnidadReaccionUbicacionLocal= new Turno_UnidadReaccionUbicacion(c);

            Turno_UnidadReaccionUbicacion turno_UnidadReaccionUbicacionLocalMatch = hashMapTurnosUnidadesReaccionUbicacionEntrantes.get(
                    turno_UnidadReaccionUbicacionLocal.getIdTurno()+turno_UnidadReaccionUbicacionLocal.getIdUnidadReaccion());

            if (turno_UnidadReaccionUbicacionLocalMatch != null)
            {
                // Esta entrada existe, por lo que se remueve del mapeado
                hashMapTurnosUnidadesReaccionUbicacionEntrantes.remove(turno_UnidadReaccionUbicacionLocal.getIdTurno()+turno_UnidadReaccionUbicacionLocal.getIdUnidadReaccion());

                Uri existingUri = Turnos_UnidadesReaccionUbicacion.crearUriTurnoUnidadReaccionUbicacion(
                        turno_UnidadReaccionUbicacionLocal.getIdTurno()+turno_UnidadReaccionUbicacionLocal.getIdUnidadReaccion());

                // Comprobar si el gasto necesita ser actualizado
                boolean b = !turno_UnidadReaccionUbicacionLocalMatch.getLatitud().equals(turno_UnidadReaccionUbicacionLocal.getLatitud());
                boolean b1 = turno_UnidadReaccionUbicacionLocalMatch.getLongitud() != null && !turno_UnidadReaccionUbicacionLocalMatch.getLongitud().equals(turno_UnidadReaccionUbicacionLocal.getLongitud());
                boolean b2 = turno_UnidadReaccionUbicacionLocalMatch.getDireccion() != null && !turno_UnidadReaccionUbicacionLocalMatch.getDireccion().equals(turno_UnidadReaccionUbicacionLocal.getDireccion());

                if (b || b1 || b2 )
                {
                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(Turnos_UnidadesReaccionUbicacion.ID_TURNO, turno_UnidadReaccionUbicacionLocalMatch.getIdTurno())
                            .withValue(Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION, turno_UnidadReaccionUbicacionLocalMatch.getIdUnidadReaccion())
                            .withValue(Turnos_UnidadesReaccionUbicacion.LATITUD, turno_UnidadReaccionUbicacionLocalMatch.getLatitud())
                            .withValue(Turnos_UnidadesReaccionUbicacion.LONGITUD, turno_UnidadReaccionUbicacionLocalMatch.getLongitud())
                            .withValue(Turnos_UnidadesReaccionUbicacion.DIRECCION, turno_UnidadReaccionUbicacionLocalMatch.getDireccion())
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
                Uri deleteUri = Turnos_UnidadesReaccionUbicacion.crearUriTurnoUnidadReaccionUbicacion(
                        turno_UnidadReaccionUbicacionLocalMatch.getIdTurno()+turno_UnidadReaccionUbicacionLocalMatch.getIdUnidadReaccion());
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Turno_UnidadReaccionUbicacion turno_UnidadReaccionUbicacionRemoto : hashMapTurnosUnidadesReaccionUbicacionEntrantes.values())
        {
            Log.i(TAG, "Programando inserción de: " + turno_UnidadReaccionUbicacionRemoto.getIdTurno()+turno_UnidadReaccionUbicacionRemoto.getIdUnidadReaccion());
            ops.add(ContentProviderOperation.newInsert(Turnos_UnidadesReaccionUbicacion.crearUriTurnoUnidadReaccionUbicacion(turno_UnidadReaccionUbicacionRemoto.getIdTurno()+turno_UnidadReaccionUbicacionRemoto.getIdUnidadReaccion()))
                    .withValue(Turnos_UnidadesReaccionUbicacion.ID_TURNO, turno_UnidadReaccionUbicacionRemoto.getIdTurno())
                    .withValue(Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION, turno_UnidadReaccionUbicacionRemoto.getIdUnidadReaccion())
                    .withValue(Turnos_UnidadesReaccionUbicacion.LATITUD, turno_UnidadReaccionUbicacionRemoto.getLatitud())
                    .withValue(Turnos_UnidadesReaccionUbicacion.LONGITUD, turno_UnidadReaccionUbicacionRemoto.getLongitud())
                    .withValue(Turnos_UnidadesReaccionUbicacion.DIRECCION, turno_UnidadReaccionUbicacionRemoto.getDireccion())
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

            resolver.notifyChange(ContratoCotizacion.Turnos.crearUriTurno_UnidadesReaccionUbicacion("xx","desactivado"), null, false);

            Log.i(TAG, "Sincronización finalizada.");
        }
        else
        {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    private void actualizarTurnoUnidadReaccionUbicacionServicioRemoto()
    {
        //try {
        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando actualizaccion de registros de ubicación de unidades de reacción ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudTurnosUnidadesReaccionUbicacionPut();

        // Quitar de primer plano
        builder.setProgress( 2, 2, false);
        stopForeground(true);
    }

    public void solicitudTurnosUnidadesReaccionUbicacionPut()
    {

        GsonRequest gsonRequest=new GsonRequest<ListaTurnosUnidadesReaccionUbicacion>(
                Constantes.GET_TURNOS_UNIDADES_REACCION_UBICACION,
                ListaTurnosUnidadesReaccionUbicacion.class,
                null,
                new Response.Listener<ListaTurnosUnidadesReaccionUbicacion>() {
                    @Override
                    public void onResponse(ListaTurnosUnidadesReaccionUbicacion response) {
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

}
