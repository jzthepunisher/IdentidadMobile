package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class Turno {



    private String IdTurno;
    private String Descripcion;
    private String HoraInicio;
    private String HoraFin;

    public String getIdTurno() {
        return IdTurno;
    }

    public void setIdTurno(String idTurno) {
        IdTurno = idTurno;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraFin() {
        return HoraFin;
    }

    public void setHoraFin(String horaFin) {
        HoraFin = horaFin;
    }


    public Turno(String idTurno,String descripcion,String horaInicio,String horaFin) {
        this.IdTurno = idTurno;
        this.Descripcion = descripcion;
        this.HoraInicio = horaInicio;
        this.HoraFin = horaFin;
    }

    public Turno(Cursor cursor) {
        IdTurno = cursor.getString(cursor.getColumnIndex(Turnos.ID_TURNO));
        Descripcion = cursor.getString(cursor.getColumnIndex(Turnos.DESCRIPCION));
        HoraInicio = cursor.getString(cursor.getColumnIndex(Turnos.HORA_INICIO));
        HoraFin = cursor.getString(cursor.getColumnIndex(Turnos.HORA_FIN));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Turnos.ID_TURNO, IdTurno);
        values.put(Turnos.DESCRIPCION, Descripcion);
        values.put(Turnos.HORA_INICIO, HoraInicio);
        values.put(Turnos.HORA_FIN, HoraFin);
        return values;
    }

}
