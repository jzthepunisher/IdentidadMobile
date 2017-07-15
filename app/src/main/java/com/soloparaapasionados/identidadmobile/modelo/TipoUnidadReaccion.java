package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposUnidadReaccion;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class TipoUnidadReaccion {

    private String idTipoUnidadReaccion;
    private String descripcion;

    public TipoUnidadReaccion(String idTipoUnidadReaccion,String descripcion) {
        this.idTipoUnidadReaccion = idTipoUnidadReaccion;
        this.descripcion = descripcion;
    }

    public TipoUnidadReaccion(Cursor cursor) {
        idTipoUnidadReaccion = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION));
        descripcion = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.DESCRIPCION));
    }

    public String getIdTipoUnidadReaccion() {
        return idTipoUnidadReaccion;
    }

    public void setIdTipoUnidadReaccion(String idTipoUnidadReaccion) {
        this.idTipoUnidadReaccion = idTipoUnidadReaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION, idTipoUnidadReaccion);
        values.put(TiposUnidadReaccion.DESCRIPCION, descripcion);
        return values;
    }

}
