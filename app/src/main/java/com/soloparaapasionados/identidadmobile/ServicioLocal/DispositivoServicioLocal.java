package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Dispositivo;
import com.soloparaapasionados.identidadmobile.modelo.DispositivoEmpleado;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USUARIO on 03/06/2017.
 */

public class DispositivoServicioLocal extends IntentService
{
    private static final String TAG = DispositivoServicioLocal.class.getSimpleName();

    public static final String ACCION_INSERTAR_DISPOSITIVO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_DISPOSITIVO_ISERVICE";
    public static final String ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE";
    public static final String ACCION_ACTUALIZAR_ESTADO_DISPOSITIVO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_ESTADO_DISPOSITIVO_ISERVICE";
    public static final String ACCION_INSERTAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE";
    public static final String ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE";

    public static final String EXTRA_MI_DISPOSITIVO = "extra_mi_dispositivo";
    public static final String EXTRA_MI_DISPOSITIVO_EMPLEADO = "extra_mi_dispositivo_empleado";
    public static final String EXTRA_ID_DISPOSITIVO = "extra_id_dispositivo";
    public static final String EXTRA_ESTADO_DISPOSITIVO = "extra_estado_dispositivo";

    public DispositivoServicioLocal() {
        super("DispositivoServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();

            if (DispositivoServicioLocal.ACCION_INSERTAR_DISPOSITIVO_ISERVICE.equals(action))
            {
                Dispositivo dispositivo=(Dispositivo)intent.getSerializableExtra(DispositivoServicioLocal.EXTRA_MI_DISPOSITIVO);

                insertarDispositivoLocal(dispositivo);
            }

            if (DispositivoServicioLocal.ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE.equals(action))
            {
                Dispositivo dispositivo=(Dispositivo)intent.getSerializableExtra(DispositivoServicioLocal.EXTRA_MI_DISPOSITIVO);

                actualizarDispositivoLocal(dispositivo);
            }

            if (DispositivoServicioLocal.ACCION_ACTUALIZAR_ESTADO_DISPOSITIVO_ISERVICE.equals(action))
            {
                String idDispositivo = intent.getStringExtra(DispositivoServicioLocal.EXTRA_ID_DISPOSITIVO);
                String estadoRegistro = intent.getStringExtra(DispositivoServicioLocal.EXTRA_ESTADO_DISPOSITIVO);

                actualizarEstadoEmpleadoLocal(idDispositivo,estadoRegistro);
            }

        }
    }

    private void insertarDispositivoLocal(Dispositivo dispositivo)
    {
        try
        {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando inserción de dispositivo...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            Date miFecha = Calendar.getInstance().getTime();

            String miFechaCadena=new SimpleDateFormat("dd/MM/yyyy").format(miFecha);

            ops.add(ContentProviderOperation.newInsert(Dispositivos.URI_CONTENIDO)
                    .withValue(Dispositivos.IMEI, dispositivo.getImei())
                    .withValue(Dispositivos.ID_SIM_CARD, dispositivo.getIdSimCard())
                    .withValue(Dispositivos.DESCRIPCION,dispositivo.getDescripcion())
                    .withValue(Dispositivos.NUMERO_CELULAR, dispositivo.getNumeroCelular())
                    .withValue(Dispositivos.ENVIADO, dispositivo.getEnviado())
                    .withValue(Dispositivos.RECIBIDO, dispositivo.getRecibido())
                    .withValue(Dispositivos.VALIDADO, dispositivo.getValidado())
                    .withValue(Dispositivos.ESTADO_SINCRONIZACION, EstadoRegistro.REGISTRADO_LOCALMENTE)
                    .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
    }

    private void actualizarDispositivoLocal(Dispositivo dispositivo)
    {
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando edición de dispositivo...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [ACTUALIZACIONES]
            ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivo( dispositivo.getImei()))
                    .withValue(Dispositivos.IMEI, dispositivo.getImei())
                    .withValue(Dispositivos.ID_SIM_CARD, dispositivo.getIdSimCard())
                    .withValue(Dispositivos.DESCRIPCION,dispositivo.getDescripcion())
                    .withValue(Dispositivos.NUMERO_CELULAR, dispositivo.getNumeroCelular())
                    .withValue(Dispositivos.ENVIADO, dispositivo.getEnviado())
                    .withValue(Dispositivos.RECIBIDO, dispositivo.getRecibido())
                    .withValue(Dispositivos.VALIDADO, dispositivo.getValidado())
                    .withValue(Dispositivos.ESTADO_SINCRONIZACION, EstadoRegistro.ACTUALIZADO_LOCALMENTE)
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

    private void actualizarEstadoEmpleadoLocal(String idDispositivo,String estadoRegistro)
    {
        try
        {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando actualizacion estado de dispositivo...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [ACTUALIZACIONES]
            ops.add(ContentProviderOperation.newUpdate(Dispositivos.crearUriDispositivoConEstado(idDispositivo, estadoRegistro))
                    .withValue(Dispositivos.ESTADO_SINCRONIZACION, estadoRegistro)
                    .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "Servicio Local destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}
