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
    private String idTurno;
    private String idUnidadReaccion;
    private Double latitud;
    private Double longitud;
    private String direccion;

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getIdUnidadReaccion() {
        return idUnidadReaccion;
    }

    public void setIdUnidadReaccion(String idUnidadReaccion) {
        this.idUnidadReaccion = idUnidadReaccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Turno_UnidadReaccionUbicacion(){

    }

    public Turno_UnidadReaccionUbicacion(String idTurno,String idUnidadReaccion,Double latitud,Double longitud
            ,String direccion) {

        this.idTurno= idTurno;
        this.idUnidadReaccion= idUnidadReaccion;
        this.latitud= latitud;
        this.longitud= longitud;
        this.direccion= direccion;
    }

    public Turno_UnidadReaccionUbicacion(Cursor cursor) {
        idTurno = cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.ID_TURNO));
        idUnidadReaccion = cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION));
        latitud = Double.valueOf(cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LATITUD)));
        longitud = Double.valueOf(cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LONGITUD)));
        direccion = cursor.getString(cursor.getColumnIndex(Turnos_UnidadesReaccionUbicacion.DIRECCION));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();

        values.put(Turnos_UnidadesReaccionUbicacion.ID_TURNO, idTurno);
        values.put(Turnos_UnidadesReaccionUbicacion.ID_UNIDAD_REACCION, idUnidadReaccion);
        values.put(Turnos_UnidadesReaccionUbicacion.LATITUD, latitud);
        values.put(Turnos_UnidadesReaccionUbicacion.LONGITUD, longitud);
        values.put(Turnos_UnidadesReaccionUbicacion.DIRECCION, direccion);

        return values;
    }
}