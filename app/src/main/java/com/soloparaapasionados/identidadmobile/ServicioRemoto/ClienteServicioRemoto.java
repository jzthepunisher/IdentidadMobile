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
import com.soloparaapasionados.identidadmobile.modelo.Cliente;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;
import com.soloparaapasionados.identidadmobile.web.VolleySingleton;
import com.soloparaapasionados.identidadmobile.web.request.GsonRequest;
import com.soloparaapasionados.identidadmobile.web.request.ListaClientes;

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
public class ClienteServicioRemoto extends IntentService {
    private static final String TAG = ClienteServicioRemoto.class.getSimpleName();

    public static final String ACCION_LEER_CLIENTE_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_LEER_CLIENTE_ISERVICE";
    //public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    //public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    //public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    //public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    SyncResult syncResult;

    private final String[] proyCliente = new String[]{
            BaseColumns._ID,
            Tablas.CLIENTE + "." + Clientes.ID_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.NOMBRES_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.APELLIDO_PATERNO,
            Tablas.CLIENTE + "." + Clientes.APELLIDO_MATERNO,
            Tablas.CLIENTE + "." + Clientes.RAZON_SOCIAL_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.RUC_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.DIRECCION_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.LATITUD_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.LONGITUD_CLIENTE,
            Tablas.CLIENTE + "." + Clientes.MONITOREO_ACTIVO
    };

    public ClienteServicioRemoto()
    {
        super("ClienteServicioRemoto");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (ClienteServicioRemoto.ACCION_LEER_CLIENTE_ISERVICE.equals(action)) {

                //Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                leerClientesRemoto();
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

    private void leerClientesRemoto(){
        //try {
        // Se construye la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Remoto en segundo plano")
                .setContentText("Procesando lectura de registros de clientes ...");

        builder.setProgress( 2, 1, false);
        startForeground( 1, builder.build());

        solicitudClientesGet();

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
    public void solicitudClientesGet()
    {
        GsonRequest gsonRequest=new GsonRequest<ListaClientes>(
                Constantes.GET_CLIENTES,
                ListaClientes.class,
                null,
                new Response.Listener<ListaClientes>() {
                    @Override
                    public void onResponse(ListaClientes response) {
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


    private void procesarRespuesta(ListaClientes responseListaClientes)
    {
        try
        {
            // Obtener atributo "estado"
            String estado = responseListaClientes.getEstado();

            switch (estado)
            {
                case "1": // EXITO
                    int cantidadClientes=responseListaClientes.getItems().size();
                    Toast.makeText(getBaseContext(), String.valueOf(cantidadClientes),Toast.LENGTH_LONG).show();

                    if (cantidadClientes>0)
                    {
                        sincronizarClientes(responseListaClientes.getItems());
                    }

                    break;
                case "2": // FALLIDO
                    String mensaje2 = responseListaClientes.getMensaje();
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
    private void sincronizarClientes(List<Cliente> listaClientes) {
        syncResult= new SyncResult();
        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas de los registros
        HashMap<String, Cliente> hashMapClientesEntrantes = new HashMap<String, Cliente>();
        for (Cliente t : listaClientes) {
            hashMapClientesEntrantes.put(t.getIdCliente(), t);
        }

        // Consultar registros remotos actuales
        Uri uri =  Clientes.crearUriClienteListado("desactivado") ;
        /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/

        ContentResolver resolver = getContentResolver();
        Cursor c = resolver.query(uri, proyCliente, null, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        while (c.moveToNext())
        {
            syncResult.stats.numEntries++;

            Cliente clienteLocal= new Cliente(c);

            Cliente clienteMatch = hashMapClientesEntrantes.get(clienteLocal.getIdCliente());

            if (clienteMatch != null)
            {
                // Esta entrada existe, por lo que se remueve del mapeado
                hashMapClientesEntrantes.remove(clienteLocal.getIdCliente());

                Uri existingUri = Clientes.crearUriCliente(clienteLocal.getIdCliente());

                // Comprobar si el gasto necesita ser actualizado
                boolean b = !clienteMatch.getNombresCliente().equals(clienteLocal.getNombresCliente());
                boolean b1 = clienteMatch.getApellidoPaterno() != null && !clienteMatch.getApellidoPaterno().equals(clienteLocal.getApellidoPaterno());
                boolean b2 = clienteMatch.getApellidoMaterno() != null && !clienteMatch.getApellidoMaterno().equals(clienteLocal.getApellidoMaterno());
                boolean b3 = clienteMatch.getRazonSocialCliente() != null && !clienteMatch.getRazonSocialCliente().equals(clienteLocal.getRazonSocialCliente());
                boolean b4 = clienteMatch.getRucCliente() != null && !clienteMatch.getRucCliente().equals(clienteLocal.getRucCliente());
                boolean b5 = clienteMatch.getDireccionCliente() != null && !clienteMatch.getDireccionCliente().equals(clienteLocal.getDireccionCliente());
                boolean b6 = clienteMatch.getLatitudCliente() != null && !clienteMatch.getLatitudCliente().equals(clienteLocal.getLatitudCliente());
                boolean b7 = clienteMatch.getLongitudCliente() != null && !clienteMatch.getLongitudCliente().equals(clienteLocal.getLongitudCliente());
                boolean b8 = clienteMatch.getMonitoreoActivo() != null && !clienteMatch.getMonitoreoActivo().equals(clienteLocal.getMonitoreoActivo());

                if (b || b1 || b2|| b3 || b4|| b5 || b6|| b7 || b8)
                {
                    Log.i(TAG, "Programando actualización de: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(Clientes.NOMBRES_CLIENTE, clienteMatch.getNombresCliente())
                            .withValue(Clientes.APELLIDO_PATERNO, clienteMatch.getApellidoPaterno())
                            .withValue(Clientes.APELLIDO_MATERNO, clienteMatch.getApellidoMaterno())
                            .withValue(Clientes.RAZON_SOCIAL_CLIENTE, clienteMatch.getRazonSocialCliente())
                            .withValue(Clientes.RUC_CLIENTE, clienteMatch.getRucCliente())
                            .withValue(Clientes.DIRECCION_CLIENTE, clienteMatch.getDireccionCliente())
                            .withValue(Clientes.LATITUD_CLIENTE, clienteMatch.getLatitudCliente())
                            .withValue(Clientes.LONGITUD_CLIENTE, clienteMatch.getLongitudCliente())
                            .withValue(Clientes.MONITOREO_ACTIVO, clienteMatch.getMonitoreoActivo())
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
                Uri deleteUri = Clientes.crearUriCliente(clienteLocal.getIdCliente());
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Cliente clienteRemoto : hashMapClientesEntrantes.values()) {
            Log.i(TAG, "Programando inserción de: " + clienteRemoto.getIdCliente());
            ops.add(ContentProviderOperation.newInsert(Clientes.crearUriCliente(clienteRemoto.getIdCliente()))
                    .withValue(Clientes.ID_CLIENTE, clienteRemoto.getIdCliente())
                    .withValue(Clientes.NOMBRES_CLIENTE, clienteRemoto.getNombresCliente())
                    .withValue(Clientes.APELLIDO_PATERNO, clienteRemoto.getApellidoPaterno())
                    .withValue(Clientes.APELLIDO_MATERNO, clienteRemoto.getApellidoMaterno())
                    .withValue(Clientes.RAZON_SOCIAL_CLIENTE, clienteRemoto.getRazonSocialCliente())
                    .withValue(Clientes.RUC_CLIENTE, clienteRemoto.getRucCliente())
                    .withValue(Clientes.DIRECCION_CLIENTE, clienteRemoto.getDireccionCliente())
                    .withValue(Clientes.LATITUD_CLIENTE, clienteRemoto.getLatitudCliente())
                    .withValue(Clientes.LONGITUD_CLIENTE, clienteRemoto.getLongitudCliente())
                    .withValue(Clientes.MONITOREO_ACTIVO, clienteRemoto.getMonitoreoActivo())
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

            resolver.notifyChange(Clientes.crearUriClienteListado("desactivado"), null, false);

            Log.i(TAG, "Sincronización finalizada.");
        }
        else
        {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

}
