package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Actividades;

/**
 * Created by USUARIO on 22/07/2017.
 */

public class Actividad {

    private String IdActividad;
    private String Descripcion;

    public Actividad(String idCargo,String descripcion) {
        this.IdActividad = idCargo;
        this.Descripcion = descripcion;
    }

    public Actividad(Cursor cursor) {
        IdActividad = cursor.getString(cursor.getColumnIndex(Actividades.ID_ACTIVIDAD));
        Descripcion = cursor.getString(cursor.getColumnIndex(Actividades.DESCRIPCION));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Actividades.ID_ACTIVIDAD, IdActividad);
        values.put(Actividades.DESCRIPCION, Descripcion);
        return values;
    }
}
