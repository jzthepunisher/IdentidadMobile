package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class Turno {

    private String idTurno;
    private String descripcion;
    private String horaInicio;
    private String horaFin;

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Turno(String idTurno,String descripcion,String horaInicio,String horaFin) {
        this.idTurno = idTurno;
        this.descripcion = descripcion;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public Turno(Cursor cursor) {
        idTurno = cursor.getString(cursor.getColumnIndex(Turnos.ID_TURNO));
        descripcion = cursor.getString(cursor.getColumnIndex(Turnos.DESCRIPCION));
        horaInicio = cursor.getString(cursor.getColumnIndex(Turnos.HORA_INICIO));
        horaFin = cursor.getString(cursor.getColumnIndex(Turnos.HORA_FIN));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Turnos.ID_TURNO, idTurno);
        values.put(Turnos.DESCRIPCION, descripcion);
        values.put(Turnos.HORA_INICIO, horaInicio);
        values.put(Turnos.HORA_FIN, horaFin);
        return values;
    }

}
