package com.soloparaapasionados.identidadmobile.ServicioRemoto;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.soloparaapasionados.identidadmobile.ServicioLocal.EmpleadoServicioLocal;
import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaEmpleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by USUARIO on 04/06/2017.
 */

public class EmpleadoServicioRemoto extends IntentService {
    private static final String TAG = EmpleadoServicioLocal.class.getSimpleName();

    public static final String ACCION_LEER_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_LEER_EMPLEADO_ISERVICE";
    public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    //public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    //public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    public EmpleadoServicioRemoto() {
        super("EmpleadoServicioRemoto");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (EmpleadoServicioRemoto.ACCION_LEER_EMPLEADO_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                leerEmpleadosRemoto();
            }

            if (EmpleadoServicioRemoto.ACCION_INSERTAR_EMPLEADO_ISERVICE.equals(action)) {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO);

                insertarEmpleadoRemoto(empleado);
            }

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
    private void leerEmpleadosRemoto(){
        //try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Remoto en segundo plano")
                    .setContentText("Procesando registros de empleado ...");

            builder.setProgress( 2, 1, false);
            startForeground( 1, builder.build());

            solicitudEmpleadosGet();

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

            /*ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            Date miFecha = Calendar.getInstance().getTime();

            String miFechaCadena=new SimpleDateFormat("dd/MM/yyyy").format(miFecha);

            ops.add(ContentProviderOperation.newInsert(ContratoCotizacion.Empleados.URI_CONTENIDO)
                    .withValue(ContratoCotizacion.Empleados.ID_EMPLEADO, empleado.getIdEmpleado())
                    .withValue(ContratoCotizacion.Empleados.NOMBRES, empleado.getNombres())
                    .withValue(ContratoCotizacion.Empleados.APELLIDO_PATERNO, empleado.getApellidoPaterno())
                    .withValue(ContratoCotizacion.Empleados.APELLIDO_MAERNO, empleado.getApellidoMaterno())
                    .withValue(ContratoCotizacion.Empleados.DIRECCION, empleado.getDireccion())
                    .withValue(ContratoCotizacion.Empleados.DNI, empleado.getDNI())
                    .withValue(ContratoCotizacion.Empleados.CELULAR, empleado.getCelular())
                    .withValue(ContratoCotizacion.Empleados.EMAIL, empleado.getEmail())
                    .withValue(ContratoCotizacion.Empleados.FECHA_NACIMIENTO, empleado.getFechaNacimiento())
                    .withValue(ContratoCotizacion.Empleados.ID_CARGO, empleado.getIdCargo())
                    .withValue(ContratoCotizacion.Empleados.FECHA_INGRESO, empleado.getFechaIngreso())
                    .withValue(ContratoCotizacion.Empleados.FECHA_BAJA, empleado.getFechaBaja())
                    .withValue(ContratoCotizacion.Empleados.FECHA_CREACION, miFechaCadena)
                    .withValue(ContratoCotizacion.Empleados.FOTO, empleado.getFoto())
                    .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }*/
    }

    private void insertarEmpleadoRemoto(Empleado empleado){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando inserción de registros de empleado ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudEmpleadosPost(empleado);

        // Quitar de primer plano
        builder.setProgress( 2, 2, false);
        stopForeground(true);
    }

    private void insertarEmpleadoLocal(Empleado empleado){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando inserción de empleado...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            Date miFecha = Calendar.getInstance().getTime();

            String miFechaCadena=new SimpleDateFormat("dd/MM/yyyy").format(miFecha);

            ops.add(ContentProviderOperation.newInsert(ContratoCotizacion.Empleados.URI_CONTENIDO)
                    .withValue(ContratoCotizacion.Empleados.ID_EMPLEADO, empleado.getIdEmpleado())
                    .withValue(ContratoCotizacion.Empleados.NOMBRES, empleado.getNombres())
                    .withValue(ContratoCotizacion.Empleados.APELLIDO_PATERNO, empleado.getApellidoPaterno())
                    .withValue(ContratoCotizacion.Empleados.APELLIDO_MAERNO, empleado.getApellidoMaterno())
                    .withValue(ContratoCotizacion.Empleados.DIRECCION, empleado.getDireccion())
                    .withValue(ContratoCotizacion.Empleados.DNI, empleado.getDNI())
                    .withValue(ContratoCotizacion.Empleados.CELULAR, empleado.getCelular())
                    .withValue(ContratoCotizacion.Empleados.EMAIL, empleado.getEmail())
                    .withValue(ContratoCotizacion.Empleados.FECHA_NACIMIENTO, empleado.getFechaNacimiento())
                    .withValue(ContratoCotizacion.Empleados.ID_CARGO, empleado.getIdCargo())
                    .withValue(ContratoCotizacion.Empleados.FECHA_INGRESO, empleado.getFechaIngreso())
                    .withValue(ContratoCotizacion.Empleados.FECHA_BAJA, empleado.getFechaBaja())
                    .withValue(ContratoCotizacion.Empleados.FECHA_CREACION, miFechaCadena)
                    .withValue(ContratoCotizacion.Empleados.FOTO, empleado.getFoto())
                    .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void actualizarEmpleadoLocal(Empleado empleado){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando edición de empleado...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [ACTUALIZACIONES]
            ops.add(ContentProviderOperation.newUpdate(ContratoCotizacion.Empleados.crearUriEmpleado( empleado.getIdEmpleado()))
                    .withValue(ContratoCotizacion.Empleados.NOMBRES, empleado.getNombres())
                    .withValue(ContratoCotizacion.Empleados.APELLIDO_PATERNO, empleado.getApellidoPaterno())
                    .withValue(ContratoCotizacion.Empleados.APELLIDO_MAERNO, empleado.getApellidoMaterno())
                    .withValue(ContratoCotizacion.Empleados.DIRECCION, empleado.getDireccion())
                    .withValue(ContratoCotizacion.Empleados.DNI, empleado.getDNI())
                    .withValue(ContratoCotizacion.Empleados.CELULAR, empleado.getCelular())
                    .withValue(ContratoCotizacion.Empleados.EMAIL, empleado.getEmail())
                    .withValue(ContratoCotizacion.Empleados.FECHA_NACIMIENTO, empleado.getFechaNacimiento())
                    .withValue(ContratoCotizacion.Empleados.ID_CARGO, empleado.getIdCargo())
                    .withValue(ContratoCotizacion.Empleados.FECHA_INGRESO, empleado.getFechaIngreso())
                    .withValue(ContratoCotizacion.Empleados.FECHA_BAJA, empleado.getFechaBaja())
                    .withValue(ContratoCotizacion.Empleados.FOTO, empleado.getFoto())
                    .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void eliminarEmpleadoLocal(String idEmpleado){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando eliminación de empleado...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [ACTUALIZACIONES]
            ops.add(ContentProviderOperation.newDelete(ContratoCotizacion.Empleados.crearUriEmpleado(idEmpleado))
                    .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Remoto destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    /**
     * Carga el adaptador con las metas obtenidas
     * en la respuesta
     */
    public void solicitudEmpleadosGet() {

        GsonRequest gsonRequest=new GsonRequest<ListaEmpleados>(
                Constantes.EMPLEADOS_GET,
                ListaEmpleados.class,
                null,
                new Response.Listener<ListaEmpleados>() {
                    @Override
                    public void onResponse(ListaEmpleados response) {
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
    private void procesarRespuesta(ListaEmpleados response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getEstado();

            switch (estado) {
                case "1": // EXITO

                    int cantidadEmpleados=response.getItems().size();
                    Toast.makeText(getBaseContext(), String.valueOf(cantidadEmpleados),Toast.LENGTH_LONG).show();

                    if (cantidadEmpleados>0)
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
    private void actualizarEmpleados(List<Empleado> listaEmpleados) {

        for (Empleado empleado : listaEmpleados) {
            //Instrucciones
        }
    }

    public void solicitudEmpleadosPost(Empleado empleado) {

        Gson gson = new Gson();
        JSONObject jsonObject=null;

        String jsonString=gson.toJson(empleado);

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
                    actualizarEstadoEmpleado(idEmpleado,EstadoRegistro.REGISTRADO_REMOTALMENTE);
                    // Enviar código de éxito
                    //getActivity().setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    //getActivity().finish();
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

    private void actualizarEstadoEmpleado(String idEmpleado,String estadoRegistro){
        Intent intent = new Intent(this, EmpleadoServicioLocal.class);
        intent.setAction(EmpleadoServicioLocal.ACCION_ACTUALIZAR_ESTADO_EMPLEADO_ISERVICE);
        intent.putExtra(EmpleadoServicioLocal.EXTRA_ID_EMPLEADO, idEmpleado);
        intent.putExtra(EmpleadoServicioLocal.EXTRA_ESTADO_EMPLEADO, estadoRegistro);
        startService(intent);
    }

}
