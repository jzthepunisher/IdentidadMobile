package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UnidadesReaccion;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class UnidadReaccion  {

    private String idUnidadReaccion;
    private String idTipoUnidadReaccion;
    private String descripcion;
    private String placa;
    private String marca;
    private String modelo;
    private String color;

    public UnidadReaccion(String idUnidadReaccion,String idTipoUnidadReaccion,String descripcion,String placa
            ,String marca,String modelo,String color) {

        this.idUnidadReaccion= idUnidadReaccion;
        this.idTipoUnidadReaccion= idTipoUnidadReaccion;
        this.descripcion= descripcion;
        this.placa= placa;
        this.marca= marca;
        this.modelo= modelo;
        this.color= color;
    }

    public UnidadReaccion(Cursor cursor) {
        idUnidadReaccion = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.ID_UNIDAD_REACCION));
        idTipoUnidadReaccion = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.ID_TIPO_UNIDAD_REACCION));
        descripcion = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.DESCRIPCION));
        placa = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.PLACA));
        marca = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.MARCA));
        modelo = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.MODELO));
        color = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.COLOR));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(UnidadesReaccion.ID_UNIDAD_REACCION, idUnidadReaccion);
        values.put(UnidadesReaccion.ID_TIPO_UNIDAD_REACCION, idTipoUnidadReaccion);
        values.put(UnidadesReaccion.DESCRIPCION, descripcion);
        values.put(UnidadesReaccion.PLACA, placa);
        values.put(UnidadesReaccion.MARCA, marca);
        values.put(UnidadesReaccion.MODELO, modelo);
        values.put(UnidadesReaccion.COLOR, color);
        return values;
    }

}
