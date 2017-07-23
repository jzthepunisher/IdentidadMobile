package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionActividad;

/**
 * Created by USUARIO on 23/07/2017.
 */

public class OrdenInstalacionEjecucionActividad   {

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


    public OrdenInstalacionEjecucionActividad(String IdOrdenInstlacion,
                                                           String IdActividad, boolean Iniciado,String FechaHoraInicio, double LatitudInicio, double LongitudInicio,
                                                           String DireccionInicio,boolean Terminado,String FechaHoraTermino, double LatitudTermino, double LongitudTermino,
                                                           String DireccionTermino)
    {

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

    public OrdenInstalacionEjecucionActividad(Cursor cursor)
    {
        this.IdOrdenInstlacion=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.ID_ORDEN_INSTALACION));
        this.IdActividad=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.ID_ACTIVIDAD));
        this.Iniciado=Boolean.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.INICIADO)));
        this.FechaHoraInicio=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.FECHA_HORA_INICIO));
        this.LatitudInicio=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.LATITUD_INICIO)));
        this.LongitudInicio=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.LONGITUD_INICIO)));
        this.DireccionInicio=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.DIRECCION_INICIO));
        this.Terminado=Boolean.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.TERMINADO)));
        this.FechaHoraTermino=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.FECHA_HORA_TERMINO));
        this.LatitudTermino=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.LATITUD_TERMINO)));
        this.LongitudTermino=Double.valueOf(cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.LONGITUD_TERMINO)));
        this.DireccionTermino=cursor.getString(cursor.getColumnIndex(OrdenesInstalacionEjecucionActividad.DIRECCION_TERMINO));

    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(OrdenesInstalacionEjecucionActividad.ID_ORDEN_INSTALACION, IdOrdenInstlacion);
        values.put(OrdenesInstalacionEjecucionActividad.ID_ACTIVIDAD, IdActividad);
        values.put(OrdenesInstalacionEjecucionActividad.INICIADO, Iniciado);
        values.put(OrdenesInstalacionEjecucionActividad.FECHA_HORA_INICIO, FechaHoraInicio);
        values.put(OrdenesInstalacionEjecucionActividad.LATITUD_INICIO, LatitudInicio);
        values.put(OrdenesInstalacionEjecucionActividad.LONGITUD_INICIO, LongitudInicio);
        values.put(OrdenesInstalacionEjecucionActividad.DIRECCION_INICIO, DireccionInicio);
        values.put(OrdenesInstalacionEjecucionActividad.TERMINADO, Terminado);
        values.put(OrdenesInstalacionEjecucionActividad.FECHA_HORA_TERMINO, FechaHoraTermino);
        values.put(OrdenesInstalacionEjecucionActividad.LATITUD_TERMINO, LatitudTermino);
        values.put(OrdenesInstalacionEjecucionActividad.LONGITUD_TERMINO, LongitudTermino);
        values.put(OrdenesInstalacionEjecucionActividad.DIRECCION_TERMINO, DireccionTermino);

        return values;
    }
}

