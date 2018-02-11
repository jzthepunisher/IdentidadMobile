package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.CorrelativosTabla;

/**
 * Created by USUARIO on 10/02/2018.
 */

public class CorrelativoTabla
{
    private String Tabla;
    private long Correlativo;

    public CorrelativoTabla(String tabla,long correlativo)
    {
        this.Tabla = tabla;
        this.Correlativo = correlativo;
    }

    public CorrelativoTabla(Cursor cursor)
    {
        Tabla = cursor.getString(cursor.getColumnIndex(CorrelativosTabla.TABLA));
        Correlativo = cursor.getLong(cursor.getColumnIndex(CorrelativosTabla.CORRELATIVO));
    }

    public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(CorrelativosTabla.TABLA, Tabla);
        values.put(CorrelativosTabla.CORRELATIVO, Correlativo);
        return values;
    }
}
