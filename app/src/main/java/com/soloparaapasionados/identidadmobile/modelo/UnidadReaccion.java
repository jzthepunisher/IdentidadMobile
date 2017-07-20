package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UnidadesReaccion;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class UnidadReaccion  {



    private String IdUnidadReaccion;
    private String IdTipoUnidadReaccion;
    private String Descripcion;
    private String Placa;
    private String Marca;
    private String Modelo;
    private String Color;

    public UnidadReaccion(String idUnidadReaccion,String idTipoUnidadReaccion,String descripcion,String placa
            ,String marca,String modelo,String color) {

        this.IdUnidadReaccion= idUnidadReaccion;
        this.IdTipoUnidadReaccion= idTipoUnidadReaccion;
        this.Descripcion= descripcion;
        this.Placa= placa;
        this.Marca= marca;
        this.Modelo= modelo;
        this.Color= color;
    }

    public UnidadReaccion(Cursor cursor) {
        this.IdUnidadReaccion = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.ID_UNIDAD_REACCION));
        this.IdTipoUnidadReaccion = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.ID_TIPO_UNIDAD_REACCION));
        this.Descripcion = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.DESCRIPCION));
        this.Placa = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.PLACA));
        this.Marca = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.MARCA));
        this.Modelo = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.MODELO));
        this.Color = cursor.getString(cursor.getColumnIndex(UnidadesReaccion.COLOR));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(UnidadesReaccion.ID_UNIDAD_REACCION, this.IdUnidadReaccion);
        values.put(UnidadesReaccion.ID_TIPO_UNIDAD_REACCION, this.IdTipoUnidadReaccion);
        values.put(UnidadesReaccion.DESCRIPCION, this.Descripcion);
        values.put(UnidadesReaccion.PLACA, this.Placa);
        values.put(UnidadesReaccion.MARCA, this.Marca);
        values.put(UnidadesReaccion.MODELO, this.Modelo);
        values.put(UnidadesReaccion.COLOR, this.Color);
        return values;
    }

    public String getIdUnidadReaccion() {
        return IdUnidadReaccion;
    }

    public void setIdUnidadReaccion(String idUnidadReaccion) {
        IdUnidadReaccion = idUnidadReaccion;
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

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

}
