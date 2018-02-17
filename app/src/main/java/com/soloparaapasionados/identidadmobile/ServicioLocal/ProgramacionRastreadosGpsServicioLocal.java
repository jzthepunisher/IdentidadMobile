package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.alarmas.AlarmScheduler;
import com.soloparaapasionados.identidadmobile.modelo.ProgramacionRastreoGpsDetalle;
import com.soloparaapasionados.identidadmobile.sqlite.BaseDatosCotizaciones.Tablas;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.ProgramacionesRastregoGpsDetalleTabla;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by USUARIO on 17/02/2018.
 */

public class ProgramacionRastreadosGpsServicioLocal extends IntentService
{
    private static final String TAG = EmpleadoServicioLocal.class.getSimpleName();

    public static final String ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE";
    public static final String ACCION_ACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_ACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE";

    public static final String EXTRA_ACTIVAR_DESACTIVAR_SERVICIO_RASTREO = "extra_activar_desactivar_servicio_rastreo";
    public static final String ACTIVAR="activar";
    public static final String DESACTIVAR="desactivar";

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    long mRepeatTime = 1 * milWeek;

    public ProgramacionRastreadosGpsServicioLocal()
    {
        super("ProgramacionRastreadosGpsServicioLocal");
    }

    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();

            if (ProgramacionRastreadosGpsServicioLocal.ACCION_REACTIVAR_PROGRAMACION_RASTREO_GPS_ISERVICE.equals(action))
            {

                //////Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                desactivarAlarmasProgramacionRastreoGps();
                activarAlarmasProgramacionRastreoGps();
            }

        }
    }

    private void desactivarAlarmasProgramacionRastreoGps()
    {
        /*String[] proyProgramacionRastreoGpsDetalle= new String[]
        {
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + BaseColumns._ID,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.ID_PROGRAMACION_RASTREO_GPS,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.DIA,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.RASTREO_GPS,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.RANGO_HORA_INICIO,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.RANGO_HORA_FINAL,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.INTERVALO_HORA_CANTIDAD,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.INTERVALO_MINUTO_CANTIDAD,
            Tablas.PROGRAMACION_RASTREO_GPS_DETALLE + "." + ProgramacionesRastregoGpsDetalleTabla.INTERVALO_SEGUNDO_CANTIDAD
        };*/

        try
        {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Local en segundo plano")
                .setContentText("Procesando desactivación de alarma programación rastreo gps...");

            builder.setProgress( 2, 1, false);

            startForeground(1, builder.build());

            ContentResolver resolver = getContentResolver();

            Uri uri =  ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTablaLista();
            /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/


            Cursor cursorProgramacionRastreoGpsDetalle = resolver.query(uri, null, null, null, null);
            assert cursorProgramacionRastreoGpsDetalle != null;

            Log.i(TAG, "Se encontraron " + cursorProgramacionRastreoGpsDetalle.getCount() + " registros locales.");
            // Encontrar datos obsoletos
            while (cursorProgramacionRastreoGpsDetalle.moveToNext())
            {

                ProgramacionRastreoGpsDetalle programacionRastreoGpsDetalle = new ProgramacionRastreoGpsDetalle(cursorProgramacionRastreoGpsDetalle);

                Uri uriProgramacionRastreoGpsDetalleTabla = ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTabla(programacionRastreoGpsDetalle.getIdProgramacionRastreoGps(),programacionRastreoGpsDetalle.getDia());

                new AlarmScheduler().cancelAlarm(getApplicationContext(), uriProgramacionRastreoGpsDetalleTabla,ProgramacionRastreadosGpsServicioLocal.ACTIVAR);
                new AlarmScheduler().cancelAlarm(getApplicationContext(), uriProgramacionRastreoGpsDetalleTabla,ProgramacionRastreadosGpsServicioLocal.DESACTIVAR);

            }
            cursorProgramacionRastreoGpsDetalle.close();
            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            /*Date miFecha = Calendar.getInstance().getTime();

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
                    .build());*/

            resolver.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

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

    private void activarAlarmasProgramacionRastreoGps()
    {
        try
        {
            int mYear, mMonth, mHour, mMinute, mDay,mDiaSemana;
            String mTime;
            String mDate;

            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Servicio Local en segundo plano")
                .setContentText("Procesando activación de alarma programación rastreo gps...");

            builder.setProgress( 2, 1, false);

            startForeground(1, builder.build());

            ContentResolver resolver = getContentResolver();

            Uri uri =  ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTablaLista();
            /******String select = ContractParaGastos.Columnas.ID_REMOTA + " IS NOT NULL";*/


            Cursor cursorProgramacionRastreoGpsDetalle = resolver.query(uri, null, null, null, null);
            assert cursorProgramacionRastreoGpsDetalle != null;

            Log.i(TAG, "Se encontraron " + cursorProgramacionRastreoGpsDetalle.getCount() + " registros locales.");
            // Encontrar datos obsoletos
            while (cursorProgramacionRastreoGpsDetalle.moveToNext())
            {

                ProgramacionRastreoGpsDetalle programacionRastreoGpsDetalle = new ProgramacionRastreoGpsDetalle(cursorProgramacionRastreoGpsDetalle);

                // Create a new notification
                if (programacionRastreoGpsDetalle.getRastreoGps() == true)
                {
                    Uri uriProgramacionRastreoGpsDetalle =  ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTabla(programacionRastreoGpsDetalle.getIdProgramacionRastreoGps(),programacionRastreoGpsDetalle.getDia());

                    Calendar mCalendarHoy;
                    mCalendarHoy = Calendar.getInstance();
                    mDiaSemana = mCalendarHoy.get(Calendar.DAY_OF_WEEK);

                    while(!obtieneDiaSemana(mDiaSemana).equals(programacionRastreoGpsDetalle.getDia().toString()))
                    {
                        mCalendarHoy.add(Calendar.DATE, 1);
                        mDiaSemana = mCalendarHoy.get(Calendar.DAY_OF_WEEK);
                    }

                    mYear = mCalendarHoy.get(Calendar.YEAR);
                    mMonth = mCalendarHoy.get(Calendar.MONTH) + 1;
                    mDay = mCalendarHoy.get(Calendar.DATE);

                    mDate = mDay + "/" + mMonth + "/" + mYear;

                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    mRepeatTime = 1 * milWeek;

                    /////////////////Configura Inicio de Rastreo
                    Date fechaRangoHoraInicio = format.parse( mDate + " " + programacionRastreoGpsDetalle.getRangoHoraInicio());

                    Calendar mCalendarRangoHoraInicio;
                    mCalendarRangoHoraInicio = Calendar.getInstance();
                    mCalendarRangoHoraInicio.setTime(fechaRangoHoraInicio);

                    long selectedTimestamp =  mCalendarRangoHoraInicio.getTimeInMillis();

                    new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp,
                        uriProgramacionRastreoGpsDetalle, mRepeatTime,
                        ProgramacionRastreadosGpsServicioLocal.ACTIVAR);

                    ///////////////////Configura Fin de Rastreo
                    Date fechaRangoHoraFin = format.parse( mDate + " " + programacionRastreoGpsDetalle.getRangoHoraFinal());

                    Calendar mCalendarRangoHoraFin;
                    mCalendarRangoHoraFin = Calendar.getInstance();
                    mCalendarRangoHoraFin.setTime(fechaRangoHoraFin);

                    selectedTimestamp =  mCalendarRangoHoraFin.getTimeInMillis();

                    new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp,
                        uriProgramacionRastreoGpsDetalle, mRepeatTime,
                        ProgramacionRastreadosGpsServicioLocal.DESACTIVAR);

                    Toast.makeText(this, "Alarm time is " + selectedTimestamp,Toast.LENGTH_LONG).show();
                }

                ////////Uri uriProgramacionRastreoGpsDetalleTabla = ProgramacionesRastregoGpsDetalleTabla.crearUriProgramacionRastreoGpsDetalleTabla(programacionRastreoGpsDetalle.getIdProgramacionRastreoGps(),programacionRastreoGpsDetalle.getDia());

                ///////new AlarmScheduler().cancelAlarm(getApplicationContext(), uriProgramacionRastreoGpsDetalleTabla);

            }
            cursorProgramacionRastreoGpsDetalle.close();
            // Lista de operaciones
            ArrayList<ContentProviderOperation> ops = new ArrayList<>();

            // [INSERCIONES]
            //Date miFecha = new Date();
            /*Date miFecha = Calendar.getInstance().getTime();

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
                    .build());*/

            resolver.applyBatch(ContratoCotizacion.AUTORIDAD, ops);

            // Quitar de primer plano
            builder.setProgress( 2, 2, false);
            stopForeground(true);

        }
        catch ( ParseException e)
        {
            e.printStackTrace();
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
    }

    private String obtieneDiaSemana(int dia)
    {
        switch (dia)
        {
            case 1 : //Domingo
                return "Domingo";
            case 2 : //Lunes
                return "Lunes";
            case 3 : //Martes
                return "Martes";
            case 4 : //Miercoles
                return "Miercoles";
            case 5 : //Jueves
                return "Jueves";
            case 6 : //Viernes
                return "Viernes";
            case 7 : //Sabado
                return "Sabado";
        }
        return "Lunes";
    }
}
