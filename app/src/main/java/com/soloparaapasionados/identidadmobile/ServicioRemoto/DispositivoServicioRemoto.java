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
import com.soloparaapasionados.identidadmobile.ServicioLocal.DispositivoServicioLocal;
import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Dispositivo;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by USUARIO on 06/01/2018.
 */

public class DispositivoServicioRemoto  extends IntentService
{
    private static final String TAG = DispositivoServicioRemoto.class.getSimpleName();

    public static final String ACCION_INSERTAR_DISPOSITIVO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioRemoto.action.ACCION_INSERTAR_DISPOSITIVO_ISERVICE";
    public static final String ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioRemoto.action.ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE";

    public static final String EXTRA_MI_DISPOSITIVO = "extra_mi_dispositivo";

    public DispositivoServicioRemoto()
    {
        super("DispositivoServicioRemoto");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        if (intent != null)
        {
            final String action = intent.getAction();

            if (DispositivoServicioRemoto.ACCION_INSERTAR_DISPOSITIVO_ISERVICE.equals(action))
            {
                Dispositivo dispositivo = (Dispositivo) intent.getSerializableExtra(DispositivoServicioRemoto.EXTRA_MI_DISPOSITIVO);

                insertarDispositivoRemoto(dispositivo);
            }

            if (DispositivoServicioRemoto.ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE.equals(action))
            {
                Dispositivo dispositivo = (Dispositivo) intent.getSerializableExtra(DispositivoServicioRemoto.EXTRA_MI_DISPOSITIVO);

                actualizarDispositivoRemoto(dispositivo);
            }

        }
    }

    private void insertarDispositivoRemoto(Dispositivo dispositivo)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando inserción de registros de dispositivo ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudDispositivosPost(dispositivo);

        // Quitar de primer plano
        builder.setProgress( 2, 2, false);
        stopForeground(true);
    }

    private void actualizarDispositivoRemoto(Dispositivo dispositivo)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando actualización de registros de dispositivo ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudDispositivosPut(dispositivo);

        // Quitar de primer plano
        builder.setProgress( 2, 2, false);
        stopForeground(true);
    }

    public void solicitudDispositivosPost(Dispositivo dispositivo)
    {
        Gson gson = new Gson();
        JSONObject jsonObject=null;

        String jsonString=gson.toJson(dispositivo);

        try
        {
            // Crear nuevo objeto Json basado en el mapa
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e)
        {
            Log.d(TAG, e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.DISPOSITIVOS_POST,
                jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        // Procesar la respuesta del servidor
                        procesarRespuestaDispositivoPost(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d(TAG, "Error Volley: " + error.getMessage());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8" ;
            }
        };

        jsonObjectRequest.setRetryPolicy(new RetryPolicy()
        {
            @Override
            public int getCurrentTimeout()
            {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount()
            {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void solicitudDispositivosPut(Dispositivo dispositivo)
    {
        Gson gson = new Gson();
        JSONObject jsonObject=null;

        String jsonString=gson.toJson(dispositivo);

        try
        {
            // Crear nuevo objeto Json basado en el mapa
            jsonObject = new JSONObject(jsonString);
        }catch (JSONException e)
        {
            Log.d(TAG, e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Constantes.DISPOSITIVOS_POST,
                jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        // Procesar la respuesta del servidor
                        procesarRespuestaDispositivoPut(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d(TAG, "Error Volley: " + error.getMessage());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8" ;
            }
        };

        jsonObjectRequest.setRetryPolicy(new RetryPolicy()
        {
            @Override
            public int getCurrentTimeout()
            {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount()
            {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError
            {

            }
        });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void procesarRespuestaDispositivoPost(JSONObject response)
    {
        try
        {
            // Obtener estado
            /////String estado = response.getString("Estado");
            // Obtener mensaje
            //////String mensaje = response.getString("Mensaje");
            // Obtener idEmpleado
            //String imei = response.getString("Imei");
            // loop array
            JSONObject jsonObjectDispositivo = (JSONObject) response.get("resultado");
            String imei = jsonObjectDispositivo.getString("Imei");


            //////switch (estado)
            //////{
            //////    case "1":
                    // Mostrar mensaje

                    Toast.makeText(getBaseContext(),imei,Toast.LENGTH_LONG).show();
                    actualizarEstadoDispositivo(imei, ContratoCotizacion.EstadoRegistro.REGISTRADO_REMOTAMENTE);

                    // Enviar código de éxito
                    //getActivity().setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    //getActivity().finish();
           //////         break;
           //////     case "2":
                    // Mostrar mensaje
          ///////          Toast.makeText( getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    //getActivity().setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    //getActivity().finish();
           ///////         break;
           /////// }
        } catch (Exception e)
       ////// } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void procesarRespuestaDispositivoPut(JSONObject response)
    {
        try
        {
            JSONObject jsonObjectDispositivo = (JSONObject) response.get("resultado");
            String imei = jsonObjectDispositivo.getString("Imei");

            Toast.makeText(getBaseContext(),imei,Toast.LENGTH_LONG).show();
            actualizarEstadoDispositivo(imei, ContratoCotizacion.EstadoRegistro.ACTUALIZADO_REMOTAMENTE);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void actualizarEstadoDispositivo(String imei,String estadoRegistro)
    {
        Intent intent = new Intent(this, DispositivoServicioLocal.class);
        intent.setAction(DispositivoServicioLocal.ACCION_ACTUALIZAR_ESTADO_DISPOSITIVO_ISERVICE);
        intent.putExtra(DispositivoServicioLocal.EXTRA_ID_DISPOSITIVO, imei);
        intent.putExtra(DispositivoServicioLocal.EXTRA_ESTADO_DISPOSITIVO, estadoRegistro);
        startService(intent);
    }

}
