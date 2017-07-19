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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.soloparaapasionados.identidadmobile.ServicioLocal.TurnoServiceLocal;
import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Turno;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaTurnos;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USUARIO on 16/07/2017.
 */

public class TurnoServicioRemoto extends IntentService {
    private static final String TAG = TurnoServicioRemoto.class.getSimpleName();

    public static final String ACCION_LEER_TURNO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_LEER_TURNO_ISERVICE";
    //public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    //public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    //public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    //public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    SyncResult syncResult;

    private final String[] proyTurno = new String[]{
            BaseColumns._ID,
            Tablas.TURNO + "." + Turnos.ID_TURNO,
            Tablas.TURNO + "." + Turnos.DESCRIPCION,
            Tablas.TURNO + "." + Turnos.HORA_INICIO,
            Tablas.TURNO + "." + Turnos.HORA_FIN};

    public TurnoServicioRemoto() {
        super("TurnoServicioRemoto");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (TurnoServicioRemoto.ACCION_LEER_TURNO_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                leerTurnosRemoto();
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

    private void leerTurnosRemoto(){
        //try {
        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando lectura de registros de turnos ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudTurnosGet();

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
    public void solicitudTurnosGet()
    {

        GsonRequest gsonRequest=new GsonRequest<ListaTurnos>(
                Constantes.GET_TURNOS,
                ListaTurnos.class,
                null,
                new Response.Listener<ListaTurnos>() {
                    @Override
                    public void onResponse(ListaTurnos response) {
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
     * @param responseListaTurnos Objeto Json con la respuesta
     */
    private void procesarRespuesta(ListaTurnos responseListaTurnos)
    {
        try{
            // Obtener atributo "estado"
            String estado = responseListaTurnos.getEstado();

            switch (estado)
            {
            case "1": // EXITO
                int cantidadTurnos=responseListaTurnos.getItems().size();
                Toast.makeText(getBaseContext(), String.valueOf(cantidadTurnos),Toast.LENGTH_LONG).show();

                if (cantidadTurnos>0)
                {
                    sincronizarTurnos(responseListaTurnos.getItems());
                }

                break;
            case "2": // FALLIDO
                String mensaje2 = responseListaTurnos.getMensaje();
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
    private void sincronizarTurnos(List<Turno> listaTurnos) {
        syncResult= new SyncResult();
        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas de los registros
        HashMap<String, Turno> hashMapTurnosEntrantes = new HashMap<String, Turno>();
        for (Turno t : listaTurnos) {
            hashMapTurnosEntrantes.put(t.getIdTurno(), t);
        }

        // Consultar registros remotos actuales
        Uri uri =  Turnos.crearUriTurnoListaSincronizacion() ;
        /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/

        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(uri, proyTurno, null, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        while (c.moveToNext())
        {
            syncResult.stats.numEntries++;

            Turno turnoLocal= new Turno(c);

            Turno turnoMatch = hashMapTurnosEntrantes.get(turnoLocal.getIdTurno());

            if (turnoMatch != null)
            {
                // Esta entrada existe, por lo que se remueve del mapeado
                hashMapTurnosEntrantes.remove(turnoLocal.getIdTurno());

                Uri existingUri = Turnos.crearUriTurno(turnoLocal.getIdTurno());

                // Comprobar si el gasto necesita ser actualizado
                boolean b = !turnoMatch.getDescripcion().equals(turnoLocal.getDescripcion());
                boolean b1 = turnoMatch.getHoraInicio() != null && !turnoMatch.getHoraInicio().equals(turnoLocal.getHoraInicio());
                boolean b2 = turnoMatch.getHoraFin() != null && !turnoMatch.getHoraFin().equals(turnoLocal.getHoraFin());

                if (b || b1 || b2)
                {
                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                        .withValue(Turnos.DESCRIPCION, turnoMatch.getDescripcion())
                        .withValue(Turnos.HORA_INICIO, turnoMatch.getHoraInicio())
                        .withValue(Turnos.HORA_FIN, turnoMatch.getHoraFin())
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
                Uri deleteUri = Turnos.crearUriTurno(turnoLocal.getIdTurno());
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Turno turnoRemoto : hashMapTurnosEntrantes.values()) {
            Log.i(TAG, "Programando inserción de: " + turnoRemoto.getIdTurno());
            ops.add(ContentProviderOperation.newInsert(Turnos.crearUriTurno(turnoRemoto.getIdTurno()))
                    .withValue(Turnos.ID_TURNO, turnoRemoto.getIdTurno())
                    .withValue(Turnos.DESCRIPCION, turnoRemoto.getDescripcion())
                    .withValue(Turnos.HORA_INICIO, turnoRemoto.getHoraInicio())
                    .withValue(Turnos.HORA_FIN, turnoRemoto.getHoraFin())
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

            resolver.notifyChange(Turnos.crearUriTurnoListaSincronizacion(), null, false);

            Log.i(TAG, "Sincronización finalizada.");
        }
        else
        {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    public void solicitudEmpleadosPost(Turno turno) {

        Gson gson = new Gson();
        JSONObject jsonObject=null;

        String jsonString=gson.toJson(turno);

        try {
            // Crear nuevo objeto Json basado en el mapa
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.EMPLEADOS_POST,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta del servidor
                        procesarRespuestaEmpleadoPost(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Volley: " + error.getMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8" ;
            }
        };

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
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

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuestaEmpleadoPost(JSONObject response) {
        try {
            // Obtener estado
            String estado = response.getString("Estado");
            // Obtener mensaje
            String mensaje = response.getString("Mensaje");
            // Obtener idEmpleado
            String idEmpleado = response.getString("IdEmpleado");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(getBaseContext(),mensaje,Toast.LENGTH_LONG).show();
                    /*actualizarEstadoEmpleado(idEmpleado, EstadoRegistro.REGISTRADO_REMOTALMENTE);*/

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText( getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    //getActivity().setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    //getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /*private void actualizarEstadoEmpleado(String idEmpleado,String estadoRegistro){
        Intent intent = new Intent(this, TurnoServiceLocal.class);
        intent.setAction(TurnoServiceLocal.ACCION_ACTUALIZAR_ESTADO_EMPLEADO_ISERVICE);
        intent.putExtra(TurnoServiceLocal.EXTRA_ID_EMPLEADO, idEmpleado);
        intent.putExtra(TurnoServiceLocal.EXTRA_ESTADO_EMPLEADO, estadoRegistro);
        startService(intent);
    }*/


}
