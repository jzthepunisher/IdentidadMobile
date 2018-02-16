package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.ProgramacionesRastregoGpsDetalleTabla;

import java.io.Serializable;

/**
 * Created by USUARIO on 15/02/2018.
 */

public class ProgramacionRastreoGpsDetalle implements Serializable
{
    private static final long serialVersionUID=1L;

    private String IdProgramacionRastreoGps;
    private String Dia;
    private Boolean RastreoGps;
    private String RangoHoraInicio;
    private String RangoHoraFinal;
    private int IntervaloHoraCantidad;
    private int IntervaloMinutoCantidad;
    private int IntervaloSegundoCantidad;

    public ProgramacionRastreoGpsDetalle(String idProgramacionRastreoGps,String dia, Boolean rastreoGps,String rangoHoraInicio
        ,String rangoHoraFinal,int intervaloHoraCantidad,int intervaloMinutoCantidad,int intervaloSegundoCantidad)
    {
        this.IdProgramacionRastreoGps = idProgramacionRastreoGps;
        this.Dia= dia;
        this.RastreoGps= rastreoGps;
        this.RangoHoraInicio= rangoHoraInicio;
        this.RangoHoraFinal= rangoHoraFinal;
        this.IntervaloHoraCantidad= intervaloHoraCantidad;
        this.IntervaloMinutoCantidad= intervaloMinutoCantidad;
        this.IntervaloSegundoCantidad= intervaloSegundoCantidad;
    }

    public ProgramacionRastreoGpsDetalle(Cursor cursor)
    {
        this.IdProgramacionRastreoGps = cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.ID_PROGRAMACION_RASTREO_GPS));
        this.Dia = cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.DIA));
        this.RastreoGps = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.RASTREO_GPS)));
        this.RangoHoraInicio = cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.RANGO_HORA_INICIO));
        this.RangoHoraFinal = cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.RANGO_HORA_FINAL));
        this.IntervaloHoraCantidad = cursor.getInt(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.INTERVALO_HORA_CANTIDAD));
        this.IntervaloMinutoCantidad = cursor.getInt(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.INTERVALO_MINUTO_CANTIDAD));
        this.IntervaloSegundoCantidad = cursor.getInt(cursor.getColumnIndex(ProgramacionesRastregoGpsDetalleTabla.INTERVALO_SEGUNDO_CANTIDAD));

    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(ProgramacionesRastregoGpsDetalleTabla.ID_PROGRAMACION_RASTREO_GPS, this.IdProgramacionRastreoGps);
        values.put(ProgramacionesRastregoGpsDetalleTabla.DIA,  this.Dia);
        values.put(ProgramacionesRastregoGpsDetalleTabla.RASTREO_GPS, this.RastreoGps);
        values.put(ProgramacionesRastregoGpsDetalleTabla.RANGO_HORA_INICIO, this.RangoHoraInicio);
        values.put(ProgramacionesRastregoGpsDetalleTabla.RANGO_HORA_FINAL,  this.RangoHoraFinal);
        values.put(ProgramacionesRastregoGpsDetalleTabla.INTERVALO_HORA_CANTIDAD, this.IntervaloHoraCantidad);
        values.put(ProgramacionesRastregoGpsDetalleTabla.INTERVALO_MINUTO_CANTIDAD, this.IntervaloMinutoCantidad);
        values.put(ProgramacionesRastregoGpsDetalleTabla.INTERVALO_SEGUNDO_CANTIDAD,  this.IntervaloSegundoCantidad);
        return values;
    }
}
