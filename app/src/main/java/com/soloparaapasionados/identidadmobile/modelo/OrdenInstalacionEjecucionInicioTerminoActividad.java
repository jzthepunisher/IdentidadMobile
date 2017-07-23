package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionInicioTerminoActividad;

/**
 * Created by USUARIO on 22/07/2017.
 */

public class OrdenInstalacionEjecucionInicioTerminoActividad  {

    private String FechaInicioTerminadoEjecucion;
    private String IdOrdenInstlacion;
    private String IdActividad;
    private boolean Iniciado;
    private String FechaHoraInicio;
    private double LatitudInicio;
    private double LongitudInicio;
    private String DireccionInicio;
    private boolean Terminado;
    private String FechaHoraTermino;
    private double LatitudTermino;
    private double LongitudTermino;
    private String DireccionTermino;


    public OrdenInstalacionEjecucionInicioTerminoActividad(String FechaInicioTerminadoEjecucion,String IdOrdenInstlacion,
        String IdActividad, boolean Iniciado,String FechaHoraInicio, double LatitudInicio, double LongitudInicio,
        String DireccionInicio,boolean Terminado,String FechaHoraTermino, double LatitudTermino, double LongitudTermino,
        String DireccionTermino)
    {
        this.FechaInicioTerminadoEjecucion=FechaInicioTerminadoEjecucion;
        this.IdOrdenInstlacion=IdOrdenInstlacion;
        this.IdActividad=IdActividad;
        this.Iniciado=Iniciado;
        this.FechaHoraInicio=FechaHoraInicio;
        this.LatitudInicio=LatitudInicio;
        this.LongitudInicio=LongitudInicio;
        this.DireccionInicio=DireccionInicio;
        this.Terminado=Terminado;
        this.FechaHoraTermino=FechaHoraTermino;
        this.LatitudTermino=LatitudTermino;
        this.LongitudTermino=LongitudTermino;
        this.DireccionTermino=DireccionTermino;
    }

    public OrdenInstalacionEjecucionInicioTerminoActividad(Cursor cursor)
    {
        this.FechaInicioTerminadoEjecucion=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_INICIO_TERMINADO_EJECUCION));
        this.IdOrdenInstlacion=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.ID_ORDEN_INSTALACION));
        this.IdActividad=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.ID_ACTIVIDAD));
        this.Iniciado=Boolean.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.INICIADO)));
        this.FechaHoraInicio=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_INICIO));
        this.LatitudInicio=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.LATITUD_INICIO)));
        this.LongitudInicio=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.LONGITUD_INICIO)));
        this.DireccionInicio=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_INICIO));
        this.Terminado=Boolean.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.TERMINADO)));
        this.FechaHoraTermino=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_TERMINO));
        this.LatitudTermino=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.LATITUD_TERMINO)));
        this.LongitudTermino=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.LONGITUD_TERMINO)));
        this.DireccionTermino=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_TERMINO));

    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_INICIO_TERMINADO_EJECUCION, FechaInicioTerminadoEjecucion);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.ID_ORDEN_INSTALACION, IdOrdenInstlacion);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.ID_ACTIVIDAD, IdActividad);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.INICIADO, Iniciado);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_INICIO, FechaHoraInicio);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.LATITUD_INICIO, LatitudInicio);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.LONGITUD_INICIO, LongitudInicio);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_INICIO, DireccionInicio);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.TERMINADO, Terminado);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_TERMINO, FechaHoraTermino);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.LATITUD_TERMINO, LatitudTermino);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.LONGITUD_TERMINO, LongitudTermino);
        values.put(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_TERMINO, DireccionTermino);


        return values;
    }
}
