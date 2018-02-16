package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.ProgramacionesRastregoGpsTabla;

import java.io.Serializable;

/**
 * Created by USUARIO on 15/02/2018.
 */

public class ProgramacionRastreoGps implements Serializable
{
    private static final long serialVersionUID=1L;

    private String IdProgramacionRastreoGps;
    private String Descripcion;

    public ProgramacionRastreoGps(String idProgramacionRastreoGps,String descripcion)
    {
        this.IdProgramacionRastreoGps = idProgramacionRastreoGps;
        this.Descripcion = descripcion;
    }

    public ProgramacionRastreoGps(Cursor cursor)
    {
        IdProgramacionRastreoGps = cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsTabla.ID_PROGRAMACION_RASTREO_GPS));
        Descripcion = cursor.getString(cursor.getColumnIndex(ProgramacionesRastregoGpsTabla.DESCRIPCION));
    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(ProgramacionesRastregoGpsTabla.ID_PROGRAMACION_RASTREO_GPS, IdProgramacionRastreoGps);
        values.put(ProgramacionesRastregoGpsTabla.DESCRIPCION, Descripcion);
        return values;
    }
}
