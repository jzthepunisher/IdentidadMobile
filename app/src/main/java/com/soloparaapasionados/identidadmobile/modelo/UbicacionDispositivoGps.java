package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UbicacionesDispositvoGps;

import java.io.Serializable;

/**
 * Created by USUARIO on 10/02/2018.
 */

public class UbicacionDispositivoGps implements Serializable
{
    private static final long serialVersionUID=1L;

    private long IdUbicacion;
    private String DireccionUbicacion;
    private Double Latitud;
    private Double Longitud;
    private String FechaHoraUbicacion;
    private String FechaHoraCreacion;
    private float Bateria;

    public UbicacionDispositivoGps()
    {
    }

    public UbicacionDispositivoGps(long idUbicacion,String direccionUbicacion,Double latitud,Double longitud,
                                   String fechaHoraUbicacion, String fechaHoraCreacion,int bateria)
    {
        this.IdUbicacion = idUbicacion;
        this.DireccionUbicacion = direccionUbicacion;
        this.Latitud = latitud;
        this.Longitud = longitud;
        this.FechaHoraUbicacion = fechaHoraUbicacion;
        this.FechaHoraCreacion = fechaHoraCreacion;
        this.Bateria = bateria;
    }

    public UbicacionDispositivoGps(Cursor cursor)
    {
        IdUbicacion = cursor.getLong(cursor.getColumnIndex(UbicacionesDispositvoGps.ID_UBICACION));
        DireccionUbicacion = cursor.getString(cursor.getColumnIndex(UbicacionesDispositvoGps.DIRECCION_UBICACION));
        Latitud = cursor.getDouble(cursor.getColumnIndex(UbicacionesDispositvoGps.LATITUD));
        Longitud = cursor.getDouble(cursor.getColumnIndex(UbicacionesDispositvoGps.LONGITUD));
        FechaHoraUbicacion = cursor.getString(cursor.getColumnIndex(UbicacionesDispositvoGps.FECHA_HORA_UBICACION));
        FechaHoraCreacion = cursor.getString(cursor.getColumnIndex(UbicacionesDispositvoGps.FECHA_HORA_CREACION));
        Bateria = cursor.getInt(cursor.getColumnIndex(UbicacionesDispositvoGps.BATERIA));
    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(UbicacionesDispositvoGps.ID_UBICACION, IdUbicacion);
        values.put(UbicacionesDispositvoGps.DIRECCION_UBICACION, DireccionUbicacion);
        values.put(UbicacionesDispositvoGps.LATITUD, Latitud);
        values.put(UbicacionesDispositvoGps.LONGITUD, Longitud);
        values.put(UbicacionesDispositvoGps.FECHA_HORA_UBICACION, FechaHoraUbicacion);
        values.put(UbicacionesDispositvoGps.BATERIA, Bateria);

        return values;
    }

    public long getIdUbicacion() {
        return IdUbicacion;
    }

    public void setIdUbicacion(long idUbicacion) {
        IdUbicacion = idUbicacion;
    }

    public String getDireccionUbicacion() {
        return DireccionUbicacion;
    }

    public void setDireccionUbicacion(String direccionUbicacion) {
        DireccionUbicacion = direccionUbicacion;
    }

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }

    public float getBateria()
    {
        return Bateria;
    }

    public String getFechaHoraUbicacion() {
        return FechaHoraUbicacion;
    }

    public void setFechaHoraUbicacion(String fechaHoraUbicacion) {
        FechaHoraUbicacion = fechaHoraUbicacion;
    }

    public String getFechaHoraCreacion() {
        return FechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        FechaHoraCreacion = fechaHoraCreacion;
    }

    public void setBateria(float bateria) {
        Bateria = bateria;
    }
}
