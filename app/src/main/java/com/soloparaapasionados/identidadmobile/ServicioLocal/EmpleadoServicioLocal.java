package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USUARIO on 13/05/2017.
 */

public class EmpleadoServicioLocal extends IntentService {
    private static final String TAG = EmpleadoServicioLocal.class.getSimpleName();

    public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    public static final String ACCION_ACTUALIZAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_EMPLEADO_ISERVICE";
    public static final String ACCION_ELIMINAR_EMPLEADO_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_EMPLEADO_ISERVICE";
    public static final String ACCION_ACTUALIZAR_ESTADO_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_ESTADO_EMPLEADO_ISERVICE";
    public static final String EXTRA_MI_EMPLEADO = "extra_mi_empleado";
    public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";
    public static final String EXTRA_ESTADO_EMPLEADO = "extra_estado_empleado";

    public EmpleadoServicioLocal() {
        super("EmpleadoServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();

            if (EmpleadoServicioLocal.ACCION_INSERTAR_EMPLEADO_ISERVICE.equals(action)) {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                insertarEmpleadoLocal(empleado);
            }

            if (EmpleadoServicioLocal.ACCION_ACTUALIZAR_EMPLEADO_ISERVICE.equals(action))
            {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                actualizarEmpleadoLocal(empleado);
            }

            if (EmpleadoServicioLocal.ACCION_ELIMINAR_EMPLEADO_ISERVICE.equals(action))
            {

                String idEmpleado = intent.getStringExtra(EmpleadoServicioLocal.EXTRA_ID_EMPLEADO);

                eliminarEmpleadoLocal(idEmpleado);
            }

            if (EmpleadoServicioLocal.ACCION_ACTUALIZAR_ESTADO_EMPLEADO_ISERVICE.equals(action))
            {
                String idEmpleado = intent.getStringExtra(EmpleadoServicioLocal.EXTRA_ID_EMPLEADO);
                String estadoRegistro = intent.getStringExtra(EmpleadoServicioLocal.EXTRA_ESTADO_EMPLEADO);

                actualizarEstadoEmpleadoLocal(idEmpleado,estadoRegistro);
            }

        }
    }

    private void insertarEmpleadoLocal(Empleado empleado)
    {
        try
        {
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

            ops.add(ContentProviderOperation.newInsert(Empleados.URI_CONTENIDO)
                    .withValue(Empleados.ID_EMPLEADO, empleado.getIdEmpleado())
                    .withValue(Empleados.NOMBRES, empleado.getNombres())
                    .withValue(Empleados.APELLIDO_PATERNO, empleado.getApellidoPaterno())
                    .withValue(Empleados.APELLIDO_MAERNO, empleado.getApellidoMaterno())
                    .withValue(Empleados.DIRECCION, empleado.getDireccion())
                    .withValue(Empleados.DNI, empleado.getDNI())
                    .withValue(Empleados.CELULAR, empleado.getCelular())
                    .withValue(Empleados.EMAIL, empleado.getEmail())
                    .withValue(Empleados.FECHA_NACIMIENTO, empleado.getFechaNacimiento())
                    .withValue(Empleados.ID_CARGO, empleado.getIdCargo())
                    .withValue(Empleados.FECHA_INGRESO, empleado.getFechaIngreso())
                    .withValue(Empleados.FECHA_BAJA, empleado.getFechaBaja())
                    .withValue(Empleados.FECHA_CREACION, miFechaCadena)
                    .withValue(Empleados.FOTO, empleado.getFoto())
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

    private void actualizarEmpleadoLocal(Empleado empleado)
    {
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
            ops.add(ContentProviderOperation.newUpdate(Empleados.crearUriEmpleado( empleado.getIdEmpleado()))
                    .withValue(Empleados.NOMBRES, empleado.getNombres())
                    .withValue(Empleados.APELLIDO_PATERNO, empleado.getApellidoPaterno())
                    .withValue(Empleados.APELLIDO_MAERNO, empleado.getApellidoMaterno())
                    .withValue(Empleados.DIRECCION, empleado.getDireccion())
                    .withValue(Empleados.DNI, empleado.getDNI())
                    .withValue(Empleados.CELULAR, empleado.getCelular())
                    .withValue(Empleados.EMAIL, empleado.getEmail())
                    .withValue(Empleados.FECHA_NACIMIENTO, empleado.getFechaNacimiento())
                    .withValue(Empleados.ID_CARGO, empleado.getIdCargo())
                    .withValue(Empleados.FECHA_INGRESO, empleado.getFechaIngreso())
                    .withValue(Empleados.FECHA_BAJA, empleado.getFechaBaja())
                    .withValue(Empleados.FOTO, empleado.getFoto())
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
            ops.add(ContentProviderOperation.newDelete(Empleados.crearUriEmpleado(idEmpleado))
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

    private void actualizarEstadoEmpleadoLocal(String idEmpleado,String estadoRegistro)
    {
        try
        {
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
            ops.add(ContentProviderOperation.newUpdate(Empleados.crearUriEmpleadoConEstado(idEmpleado, estadoRegistro))
                    .withValue(Empleados.ESTADO, estadoRegistro)
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
    public void onDestroy() {
        Toast.makeText(this, "Servicio Local destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}
