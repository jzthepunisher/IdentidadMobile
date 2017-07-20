package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposUnidadReaccion;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class TipoUnidadReaccion {


    private String IdTipoUnidadReaccion;
    private String Descripcion;
    private String Foto;

    public TipoUnidadReaccion(String idTipoUnidadReaccion,String descripcion,String foto) {
        this.IdTipoUnidadReaccion = idTipoUnidadReaccion;
        this.Descripcion = descripcion;
        this.Foto = foto;
    }

    public TipoUnidadReaccion(Cursor cursor) {
        IdTipoUnidadReaccion = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION));
        Descripcion = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.DESCRIPCION));
        Foto = cursor.getString(cursor.getColumnIndex(TiposUnidadReaccion.FOTO));
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(TiposUnidadReaccion.ID_TIPO_UNIDAD_REACCION, IdTipoUnidadReaccion);
        values.put(TiposUnidadReaccion.DESCRIPCION, Descripcion);
        values.put(TiposUnidadReaccion.FOTO, Foto);
        return values;
    }

    public String getIdTipoUnidadReaccion() {
        return IdTipoUnidadReaccion;
    }

    public void setIdTipoUnidadReaccion(String idTipoUnidadReaccion) {
        IdTipoUnidadReaccion = idTipoUnidadReaccion;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }


}
