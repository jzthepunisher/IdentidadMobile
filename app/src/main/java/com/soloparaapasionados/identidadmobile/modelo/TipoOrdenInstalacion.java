package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposOrdenInstalacion;

/**
 * Created by USUARIO on 22/07/2017.
 */

public class TipoOrdenInstalacion {

    private String IdTipoOrdenInstalacion;
    private String Descripcion;

    public TipoOrdenInstalacion(String idTipoOrdenInstalacion,String descripcion) {
        this.IdTipoOrdenInstalacion = idTipoOrdenInstalacion;
        this.Descripcion = descripcion;
    }

    public TipoOrdenInstalacion(Cursor cursor) {
        this.IdTipoOrdenInstalacion = cursor.getString(cursor.getColumnIndex(TiposOrdenInstalacion.ID_TIPO_ORDEN_INSTALACION));
        this.Descripcion = cursor.getString(cursor.getColumnIndex(TiposOrdenInstalacion.DESCRIPCION));
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TiposOrdenInstalacion.ID_TIPO_ORDEN_INSTALACION, IdTipoOrdenInstalacion);
        values.put(TiposOrdenInstalacion.DESCRIPCION, Descripcion);
        return values;
    }

    public String getIdTipoOrdenInstalacion() {
        return IdTipoOrdenInstalacion;
    }

    public void setIdTipoOrdenInstalacion(String idTipoOrdenInstalacion) {
        IdTipoOrdenInstalacion = idTipoOrdenInstalacion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

}
