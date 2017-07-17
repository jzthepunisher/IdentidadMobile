package com.soloparaapasionados.identidadmobile.ServicioRemoto;

import android.app.IntentService;
import android.content.Intent;
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
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaTurnos;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void solicitudTurnosGet() {

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
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuesta(ListaTurnos response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getEstado();

            switch (estado) {
                case "1": // EXITO

                    int cantidadTurnos=response.getItems().size();
                    Toast.makeText(getBaseContext(), String.valueOf(cantidadTurnos),Toast.LENGTH_LONG).show();

                    if (cantidadTurnos>0)
                    {
                        actualizarEmpleados(response.getItems());
                    }

                    //adapter = new MetaAdapter(response.getItems(), getActivity());
                    // Setear adaptador a la lista
                    //lista.setAdapter(adapter);
                    break;
                case "2": // FALLIDO
                    String mensaje2 = response.getMensaje();
                    Toast.makeText(getBaseContext(), mensaje2,Toast.LENGTH_LONG).show();
                    break;
            }

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    //Procesador de nuevo registros para empleados
    private void actualizarEmpleados(List<Turno> listaTurnos) {

        for (Turno empleado : listaTurnos) {
            //Instrucciones
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
