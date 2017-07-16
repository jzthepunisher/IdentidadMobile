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
import com.soloparaapasionados.identidadmobile.modelo.DispositivoEmpleado;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleados;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USUARIO on 03/06/2017.
 */

public class DispositivoServicioLocal extends IntentService {
    private static final String TAG = DispositivoServicioLocal.class.getSimpleName();

    public static final String ACCION_INSERTAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE";
    public static final String ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE";

    public static final String EXTRA_MI_DISPOSITIVO_EMPLEADO = "extra_mi_dispositivo_empleado";
    public static final String EXTRA_ID_DISPOSITIVO = "extra_id_dispositivo";

    public DispositivoServicioLocal() {
        super("DispositivoServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (DispositivoServicioLocal.ACCION_INSERTAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE.equals(action)) {

                DispositivoEmpleado dispositivoEmpleado=(DispositivoEmpleado)intent.getSerializableExtra(DispositivoServicioLocal.EXTRA_MI_DISPOSITIVO_EMPLEADO);

                insertarDispositivoEmpleadoTemporalLocal(dispositivoEmpleado);
            }

            if (DispositivoServicioLocal.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE.equals(action)) {

                String imei=intent.getStringExtra(DispositivoServicioLocal.EXTRA_ID_DISPOSITIVO);

                eliminarDispositoEmpleadoTemporalLocal(imei);
            }
        }
    }

    private void insertarDispositivoEmpleadoTemporalLocal(DispositivoEmpleado dispositivoEmpleado){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando inserción de empleados asignados a un dispositivo...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            Date miFecha = Calendar.getInstance().getTime();

            String miFechaCadena=new SimpleDateFormat("dd/MM/yyyy").format(miFecha);


            String imei;
            imei=dispositivoEmpleado.getImei();
            ops.add(ContentProviderOperation.newDelete(ContratoCotizacion.DispositivosEmpleadosTemporal.crearUriDispositivoEmpleadoTemporal(imei))
                    .build());
            ops.add(ContentProviderOperation.newDelete(ContratoCotizacion.DispositivosEmpleados.crearUriDispositivoEmpleado(imei))
                    .build());

            for ( Empleado empleado : dispositivoEmpleado.getEmpleados() ) {

                String idEmpleado;
                imei=dispositivoEmpleado.getImei();
                idEmpleado=empleado.getIdEmpleado();

                ops.add(ContentProviderOperation.newInsert(DispositivosEmpleados.crearUriDispositivoEmpleado(imei,idEmpleado))
                        .withValue(DispositivosEmpleados.IMEI, imei)
                        .withValue(DispositivosEmpleados.ID_EMPLEADO, idEmpleado)
                        .build());

            }

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

    private void eliminarDispositoEmpleadoTemporalLocal(String imei){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando eliminación de empleados asignados al dispositivo...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [ACTUALIZACIONES]
            ops.add(ContentProviderOperation.newDelete(ContratoCotizacion.DispositivosEmpleadosTemporal.crearUriDispositivoEmpleadoTemporal(imei))
                    .build());
            ops.add(ContentProviderOperation.newDelete(ContratoCotizacion.DispositivosEmpleados.crearUriDispositivoEmpleado(imei))
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
        Toast.makeText(this, "Servicio Local destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}
