package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.GruposTabla;

/**
 * Created by USUARIO on 17/02/2018.
 */

public class Grupo
{
    private String IdGrupo;
    private String Descripcion;
    private Boolean RastreoGps;
    private Boolean VerEnMapa;
    private String FechaHoraUltimaUbicacion;
    private String DireccionUltimaUbicacion;
    private String FechaCreacion;
    private String IdProgramacionRastreoGps;
    private String EstadoSincronizacion;

    public Grupo(String IdGrupo,String Descripcion,Boolean RastreoGps, Boolean VerEnMapa, String FechaHoraUltimaUbicacion,
        String DireccionUltimaUbicacion, String FechaCreacion, String IdProgramacionRastreoGps)
    {
        this.IdGrupo=IdGrupo;
        this.Descripcion=Descripcion;
        this.RastreoGps=RastreoGps;
        this.VerEnMapa=VerEnMapa;
        this.FechaHoraUltimaUbicacion=FechaHoraUltimaUbicacion;
        this.DireccionUltimaUbicacion=DireccionUltimaUbicacion;
        this.FechaCreacion=FechaCreacion;
        this.IdProgramacionRastreoGps=IdProgramacionRastreoGps;
    }

    public Grupo(Cursor cursor)
    {
        this.IdGrupo= cursor.getString(cursor.getColumnIndex(GruposTabla.ID_GRUPO));
        this.Descripcion= cursor.getString(cursor.getColumnIndex(GruposTabla.DESCRIPCION));

        String valor=cursor.getString(cursor.getColumnIndex(GruposTabla.RASTREO_GPS));
        this.RastreoGps =valor.equals("1") ? true : false;

        valor=cursor.getString(cursor.getColumnIndex(GruposTabla.VER_EN_MAPA));
        this.VerEnMapa =valor.equals("1") ? true : false;

        this.FechaHoraUltimaUbicacion= cursor.getString(cursor.getColumnIndex(GruposTabla.FECHA_HORA_ULTIMA_UBICACION));
        this.DireccionUltimaUbicacion= cursor.getString(cursor.getColumnIndex(GruposTabla.DIRECCION_ULTIMA_UBICACION));
        this.FechaCreacion= cursor.getString(cursor.getColumnIndex(GruposTabla.FECHA_HORA_CREACION));
        this.IdProgramacionRastreoGps= cursor.getString(cursor.getColumnIndex(GruposTabla.ID_PROGRAMACION_RASTREO_GPS));
    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(GruposTabla.ID_GRUPO, this.IdGrupo);
        values.put(GruposTabla.DESCRIPCION, this.Descripcion);
        values.put(GruposTabla.RASTREO_GPS, this.RastreoGps);
        values.put(GruposTabla.VER_EN_MAPA, this.VerEnMapa);
        values.put(GruposTabla.FECHA_HORA_ULTIMA_UBICACION, this.FechaHoraUltimaUbicacion);
        values.put(GruposTabla.DIRECCION_ULTIMA_UBICACION, this.DireccionUltimaUbicacion);
        values.put(GruposTabla.FECHA_HORA_CREACION, this.FechaCreacion);
        values.put(GruposTabla.ID_PROGRAMACION_RASTREO_GPS, this.IdProgramacionRastreoGps);
        return values;
    }

    public String getIdGrupo() {
        return IdGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        IdGrupo = idGrupo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Boolean getRastreoGps() {
        return RastreoGps;
    }

    public void setRastreoGps(Boolean rastreoGps) {
        RastreoGps = rastreoGps;
    }

    public Boolean getVerEnMapa() {
        return VerEnMapa;
    }

    public void setVerEnMapa(Boolean verEnMapa) {
        VerEnMapa = verEnMapa;
    }

    public String getFechaHoraUltimaUbicacion() {
        return FechaHoraUltimaUbicacion;
    }

    public void setFechaHoraUltimaUbicacion(String fechaHoraUltimaUbicacion) {
        FechaHoraUltimaUbicacion = fechaHoraUltimaUbicacion;
    }

    public String getDireccionUltimaUbicacion() {
        return DireccionUltimaUbicacion;
    }

    public void setDireccionUltimaUbicacion(String direccionUltimaUbicacion) {
        DireccionUltimaUbicacion = direccionUltimaUbicacion;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getIdProgramacionRastreoGps() {
        return IdProgramacionRastreoGps;
    }

    public void setIdProgramacionRastreoGps(String idProgramacionRastreoGps) {
        IdProgramacionRastreoGps = idProgramacionRastreoGps;
    }

    public String getEstadoSincronizacion() {
        return EstadoSincronizacion;
    }

    public void setEstadoSincronizacion(String estadoSincronizacion) {
        EstadoSincronizacion = estadoSincronizacion;
    }
}




