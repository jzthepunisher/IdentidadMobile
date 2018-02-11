package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.modelo.UbicacionDispositivoGps;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.CorrelativosTabla;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.EstadoRegistro;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UbicacionesDispositvoGps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by USUARIO on 10/02/2018.
 */

public class UbicacionDispositivoGpsServicioLocal extends IntentService
{
    private static final String TAG = UbicacionDispositivoGpsServicioLocal.class.getSimpleName();

    public static final String ACCION_INSERTAR_UBICACION_DISPOSITIVO_GPS_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_UBICACION_DISPOSITIVO_GPS_ISERVICE";

    public static final String EXTRA_MI_UBICACION_DISPOSITIVO_GPS = "extra_mi_ubicacion_dispositivo_gps";

    public UbicacionDispositivoGpsServicioLocal()
    {
        super("UbicacionDispositivoGpsServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();

            if (UbicacionDispositivoGpsServicioLocal.ACCION_INSERTAR_UBICACION_DISPOSITIVO_GPS_ISERVICE.equals(action))
            {
                UbicacionDispositivoGps ubicacionDispositivoGps = (UbicacionDispositivoGps)intent.getSerializableExtra(UbicacionDispositivoGpsServicioLocal.EXTRA_MI_UBICACION_DISPOSITIVO_GPS);

                insertarDispositivoLocal(ubicacionDispositivoGps);
            }

            /*if (DispositivoServicioLocal.ACCION_ACTUALIZAR_DISPOSITIVO_ISERVICE.equals(action))
            {
                Dispositivo dispositivo=(Dispositivo)intent.getSerializableExtra(DispositivoServicioLocal.EXTRA_MI_DISPOSITIVO);

                actualizarDispositivoLocal(dispositivo);
            }

            if (DispositivoServicioLocal.ACCION_ACTUALIZAR_ESTADO_DISPOSITIVO_ISERVICE.equals(action))
            {
                String idDispositivo = intent.getStringExtra(DispositivoServicioLocal.EXTRA_ID_DISPOSITIVO);
                String estadoRegistro = intent.getStringExtra(DispositivoServicioLocal.EXTRA_ESTADO_DISPOSITIVO);

                actualizarEstadoEmpleadoLocal(idDispositivo,estadoRegistro);
            }*/

        }
    }

    private void insertarDispositivoLocal(UbicacionDispositivoGps ubicacionDispositivoGps)
    {
        try
        {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Local en segundo plano")
                .setContentText("Procesando inserción de ubicación dispositivo gps...");

            builder.setProgress( 2, 1, false);
            startForeground(1, builder.build());

            ContentResolver r = getContentResolver();

            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            Date miFecha = Calendar.getInstance().getTime();
            String miFechaCadena=new SimpleDateFormat("dd/MM/yyyy").format(miFecha);

            long correlativoTabla=generaCorrelativoTabla(Tablas.CORRELATIVO_TABLA);

            ops.add(ContentProviderOperation.newInsert(UbicacionesDispositvoGps.crearUriUbicacionDispositivoGps("0"))
                .withValue(UbicacionesDispositvoGps.ID_UBICACION, correlativoTabla)
                .withValue(UbicacionesDispositvoGps.DIRECCION_UBICACION, ubicacionDispositivoGps.getDireccionUbicacion())
                .withValue(UbicacionesDispositvoGps.LATITUD,ubicacionDispositivoGps.getLatitud())
                .withValue(UbicacionesDispositvoGps.LONGITUD, ubicacionDispositivoGps.getLongitud())
                .withValue(UbicacionesDispositvoGps.FECHA_HORA_UBICACION, ubicacionDispositivoGps.getFechaHoraUbicacion())
                .withValue(UbicacionesDispositvoGps.BATERIA, ubicacionDispositivoGps.getBateria())
                .withValue(UbicacionesDispositvoGps.FECHA_HORA_CREACION, miFechaCadena)
                .withValue(UbicacionesDispositvoGps.ESTADO_SINCRONIZACION, EstadoRegistro.REGISTRADO_LOCALMENTE)
                .build());

            // [ACTUALIZACIONES]
            ops.add(ContentProviderOperation.newUpdate(CorrelativosTabla.crearUriCorrelativoTabla(Tablas.UBICACION_DISPOSITIVO_GPS))
                .withValue(CorrelativosTabla.CORRELATIVO,correlativoTabla)
                .build());

            r.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "Servicio Local destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        //////Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        //////LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    private long generaCorrelativoTabla(String tabla)
    {
        // Instancia de helper
        BaseDatosCotizaciones mBaseDatosCotizaciones = new BaseDatosCotizaciones(this);
        long correlativo=0;

        String columns[] = new String[]{CorrelativosTabla.CORRELATIVO};
        String selection = CorrelativosTabla.TABLA + " LIKE ?"; // WHERE id LIKE ?
        String selectionArgs[] = new String[]{Tablas.UBICACION_DISPOSITIVO_GPS};

        Cursor c = mBaseDatosCotizaciones.getWritableDatabase().query(
                Tablas.CORRELATIVO_TABLA, columns, selection, selectionArgs,null,null,null
        );

        while(c.moveToNext())
        {
            correlativo = c.getLong(c.getColumnIndex(CorrelativosTabla.CORRELATIVO));
            // Acciones...
        }

        return correlativo+1;
    }

}
