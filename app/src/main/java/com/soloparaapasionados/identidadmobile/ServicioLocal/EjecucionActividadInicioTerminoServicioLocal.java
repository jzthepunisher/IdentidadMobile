package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionInicioTerminoActividad;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacion;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class EjecucionActividadInicioTerminoServicioLocal extends IntentService {
    private static final String TAG = EjecucionActividadInicioTerminoServicioLocal.class.getSimpleName();

    public static final String ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_INICIO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_INICIO_ISERVICE";
    public static final String ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_TERMINO_ISERVICE= "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_TERMINO_ISERVICE";
    //public static final String ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE";

    public static final String EXTRA_MI_TURNO_UNIDAD_REACCION_UBICACION = "extra_mi_turno_unidad_reaccion_ubicacion";

    public static final String  EXTRA_FECHA_INICIO_TERMINADO_EJECUCION = "extra_fecha_inicio_terminado_ejecucion";
    public static final String  EXTRA_ID_ORDEN_INSTALACION             = "extra_id_orden_instalacion";
    public static final String  EXTRA_ID_ACTIVIDAD                     = "extra_id_actividad";

    public static final String  EXTRA_INICIADO                         = "extra_iniciado";
    public static final String  EXTRA_FECHA_HORA_INICIO                = "extra_fecha_hora_inicio";
    public static final String  EXTRA_LATITUD_INICIO                   = "extra_latitud_inicio";
    public static final String  EXTRA_LONGITUD_INICIO                  = "extra_longitud_inicio";
    public static final String  EXTRA_DIRECCION_INICIO                 = "extra_direccion_inicio";

    public static final String  EXTRA_TERMINADO                         = "extra_terminado";
    public static final String  EXTRA_FECHA_HORA_TERMINO                = "extra_fecha_hora_terminado";
    public static final String  EXTRA_LATITUD_TERMINO                   = "extra_latitud_terminado";
    public static final String  EXTRA_LONGITUD_TERMINO                  = "extra_longitud_terminado";
    public static final String  EXTRA_DIRECCION_TERMINO                 = "extra_direccion_terminado";


    public EjecucionActividadInicioTerminoServicioLocal() {
        super("EjecucionActividadInicioTerminoServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (EjecucionActividadInicioTerminoServicioLocal.ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_INICIO_ISERVICE.equals(action)) {

                String  FechaInicioTerminadoEjecucion=intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_INICIO_TERMINADO_EJECUCION);
                String  IdOrdenInstlacion            =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_ID_ORDEN_INSTALACION);
                String  IdActividad                  =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_ID_ACTIVIDAD);
                boolean Iniciado                     =intent.getBooleanExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_INICIADO,false);
                String  FechaHoraInicio              =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_HORA_INICIO);
                double  LatitudInicio                =intent.getDoubleExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LATITUD_INICIO,0.0);
                double  LongitudInicio               =intent.getDoubleExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LONGITUD_INICIO,0.0);
                String  DireccionInicio              =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_DIRECCION_INICIO);

                actualizarEjecucionActividadInicioTerminoInicioLocal(FechaInicioTerminadoEjecucion,
                        IdOrdenInstlacion, IdActividad,Iniciado, FechaHoraInicio,
                        LatitudInicio, LongitudInicio, DireccionInicio  );
            }

            if (EjecucionActividadInicioTerminoServicioLocal.ACCION_ACTUALIZAR_EJECUCION_ACTIVIDAD_INICIO_TERMINO_TERMINO_ISERVICE.equals(action)) {

                String  FechaInicioTerminadoEjecucion=intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_INICIO_TERMINADO_EJECUCION);
                String  IdOrdenInstlacion            =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_ID_ORDEN_INSTALACION);
                String  IdActividad                  =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_ID_ACTIVIDAD);

                boolean Terminado                     =intent.getBooleanExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_TERMINADO,false);
                String  FechaHoraTermino             =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_FECHA_HORA_TERMINO);
                double  LatitudTermino                =intent.getDoubleExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LATITUD_TERMINO,0.0);
                double  LongitudTermino               =intent.getDoubleExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_LONGITUD_TERMINO,0.0);
                String  DireccionTermino              =intent.getStringExtra(EjecucionActividadInicioTerminoServicioLocal.EXTRA_DIRECCION_TERMINO);

                actualizarEjecucionActividadInicioTerminoTerminoLocal(FechaInicioTerminadoEjecucion,
                        IdOrdenInstlacion, IdActividad,Terminado, FechaHoraTermino,
                        LatitudTermino, LongitudTermino, DireccionTermino  );
            }



            /*if (DispositivoServicioLocal.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE.equals(action)) {

                String imei=intent.getStringExtra(DispositivoServicioLocal.EXTRA_ID_DISPOSITIVO);

                eliminarDispositoEmpleadoTemporalLocal(imei);
            }*/
        }
    }

    private void actualizarEjecucionActividadInicioTerminoInicioLocal(String FechaInicioTerminadoEjecucion,
        String IdOrdenInstlacion, String IdActividad, boolean Iniciado, String FechaHoraInicio,
        double LatitudInicio, double LongitudInicio, String DireccionInicio ){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando actualización de ubicación de unidad de reacción...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            ops.add(ContentProviderOperation.newUpdate(OrdenesInstalacion.crearUriOrdenesInstalacion_InicioTerminoActividades(IdOrdenInstlacion,FechaInicioTerminadoEjecucion,"activado"))
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.INICIADO,Iniciado)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_INICIO,FechaHoraInicio)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.LATITUD_INICIO,LatitudInicio)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.LONGITUD_INICIO, LongitudInicio)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_INICIO, DireccionInicio)
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

       // actualizarTurnosUnidadesReaccionUbicacionRemotamente();
    }

    private void actualizarEjecucionActividadInicioTerminoTerminoLocal(String FechaInicioTerminadoEjecucion,
                                                                      String IdOrdenInstlacion, String IdActividad, boolean Terminado, String FechaHoraTermino,
                                                                      double LatitudTermino, double LongitudTermino, String DireccionTermino ){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando actualización de ubicación de unidad de reacción...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            ops.add(ContentProviderOperation.newUpdate(OrdenesInstalacion.crearUriOrdenesInstalacion_InicioTerminoActividades(IdOrdenInstlacion,FechaInicioTerminadoEjecucion,"activado"))
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.TERMINADO,Terminado)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_TERMINO,FechaHoraTermino)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.LATITUD_TERMINO,LatitudTermino)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.LONGITUD_TERMINO, LongitudTermino)
                    .withValue(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_TERMINO, DireccionTermino)
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

        // actualizarTurnosUnidadesReaccionUbicacionRemotamente();
    }


    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Local destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    /*private void actualizarTurnosUnidadesReaccionUbicacionRemotamente()
    {
        Intent intent = new Intent(this, TurnoUnidadReaccionUbicacionServicioRemoto.class);
        intent.setAction(TurnoUnidadReaccionUbicacionServicioRemoto.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE);
        //intent.putExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO, empleado);
        this.startService(intent);
    }*/

}
