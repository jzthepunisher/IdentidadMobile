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

import com.soloparaapasionados.identidadmobile.ServicioRemoto.TurnoUnidadReaccionUbicacionServicioRemoto;
import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Turno_UnidadReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos_UnidadesReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TurnoServiceLocal extends IntentService {
    private static final String TAG = TurnoServiceLocal.class.getSimpleName();

    public static final String ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE";
    //public static final String ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE   = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE";

    public static final String EXTRA_MI_TURNO_UNIDAD_REACCION_UBICACION = "extra_mi_turno_unidad_reaccion_ubicacion";
    public static final String EXTRA_ID_TURNO = "extra_id_turno";
    public static final String EXTRA_ID_UNIDAD_REACCION="extra_id_unidad_reaccion";
    //public static final String EXTRA_ID_DISPOSITIVO = "extra_id_dispositivo";

    public TurnoServiceLocal() {
        super("TurnoServiceLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();

            if (TurnoServiceLocal.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE.equals(action)) {

                Turno_UnidadReaccionUbicacion turno_unidadReaccionUbicacion=(Turno_UnidadReaccionUbicacion)intent.getSerializableExtra(TurnoServiceLocal.EXTRA_MI_TURNO_UNIDAD_REACCION_UBICACION);

                actualizarTurnoUnidadReaccionUbicacionLocal(turno_unidadReaccionUbicacion);
            }

            /*if (DispositivoServicioLocal.ACCION_ELIMINAR_DISPOSITIVO_EMPLEADO_TEMPORAL_ISERVICE.equals(action)) {

                String imei=intent.getStringExtra(DispositivoServicioLocal.EXTRA_ID_DISPOSITIVO);

                eliminarDispositoEmpleadoTemporalLocal(imei);
            }*/
        }
    }

    private void actualizarTurnoUnidadReaccionUbicacionLocal(Turno_UnidadReaccionUbicacion turnoUnidadReaccionUbicacion){
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

            ops.add(ContentProviderOperation.newUpdate(Turnos.crearUriTurno_UnidadesReaccionUbicacion_Ubicacion(turnoUnidadReaccionUbicacion.getIdTurno(),turnoUnidadReaccionUbicacion.getIdUnidadReaccion()))
                    .withValue(Turnos_UnidadesReaccionUbicacion.LATITUD,turnoUnidadReaccionUbicacion.getLatitud())
                    .withValue(Turnos_UnidadesReaccionUbicacion.LONGITUD,turnoUnidadReaccionUbicacion.getLongitud())
                    .withValue(Turnos_UnidadesReaccionUbicacion.DIRECCION,turnoUnidadReaccionUbicacion.getDireccion())
                    .withValue(Turnos_UnidadesReaccionUbicacion.PENDIENTE_PETICION, ContratoCotizacion.EstadoRegistro.ACTUALIZADO_LOCALMENTE)
                    .withValue(Turnos_UnidadesReaccionUbicacion.ESTADO_SINCRONIZACION, ContratoCotizacion.EstadoRegistro.ESTADO_OK)
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

        actualizarTurnosUnidadesReaccionUbicacionRemotamente();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio Local destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private void actualizarTurnosUnidadesReaccionUbicacionRemotamente()
    {
        Intent intent = new Intent(this, TurnoUnidadReaccionUbicacionServicioRemoto.class);
        intent.setAction(TurnoUnidadReaccionUbicacionServicioRemoto.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE);
        //intent.putExtra(EmpleadoServicioRemoto.EXTRA_MI_EMPLEADO, empleado);
        this.startService(intent);
    }

}
