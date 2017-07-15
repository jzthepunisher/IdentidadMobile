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
    private String foto;

    public TipoUnidadReaccion(String idTipoUnidadReaccion,String descripcion,String foto) {
        this.idTipoUnidadReaccion = idTipoUnidadReaccion;
        this.descripcion = descripcion;
        this.foto = foto;
    }

    public TipoUnidadReaccion(Cursor cursor) {
        idTipoUnidadReaccion = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION));
        descripcion = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.DESCRIPCION));
        foto = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.FOTO));
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION, idTipoUnidadReaccion);
        values.put(TiposUnidadReaccion.DESCRIPCION, descripcion);
        values.put(TiposUnidadReaccion.FOTO, foto);
        return values;
    }

}
