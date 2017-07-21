package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos_UnidadesReaccionUbicacion;

import java.io.Serializable;

/**
 * Created by USUARIO on 15/07/2017.
 */

public class Turno_UnidadReaccionUbicacion implements Serializable {
    private static final long serialVersionUID=1L;

    private String IdTurno;
    private String IdUnidadReaccion;
    private Double Latitud;
    private Double Longitud;
    private String Direccion;

    public Turno_UnidadReaccionUbicacion()
    {
    }

    public Turno_UnidadReaccionUbicacion(String idTurno,String idUnidadReaccion,Double latitud,Double longitud
            ,String direccion) {

        this.IdTurno= idTurno;
        this.IdUnidadReaccion= idUnidadReaccion;
        this.Latitud= latitud;
        this.Longitud= longitud;
        this.Direccion= direccion;
    }

    public Turno_UnidadReaccionUbicacion(Cursor cursor) {
        IdTurno = cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.ID_TURNO));
        IdUnidadReaccion = cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION));
        Latitud = Double.valueOf(cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LATITUD)));
        Longitud = Double.valueOf(cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LONGITUD)));
        Direccion = cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.DIRECCION));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(Turnos_UnidadesReaccionUbicacion.ID_TURNO, IdTurno);
        values.put(Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION,IdUnidadReaccion);
        values.put(Turnos_UnidadesReaccionUbicacion.LATITUD, Latitud);
        values.put(Turnos_UnidadesReaccionUbicacion.LONGITUD, Longitud);
        values.put(Turnos_UnidadesReaccionUbicacion.DIRECCION, Direccion);

        return values;
    }

    public String getIdTurno() {
        return IdTurno;
    }

    public void setIdTurno(String idTurno) {
        IdTurno = idTurno;
    }

    public String getIdUnidadReaccion() {
        return IdUnidadReaccion;
    }

    public void setIdUnidadReaccion(String idUnidadReaccion) {
        IdUnidadReaccion = idUnidadReaccion;
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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
}