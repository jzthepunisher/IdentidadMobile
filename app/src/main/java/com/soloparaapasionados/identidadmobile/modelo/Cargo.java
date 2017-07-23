package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

/**
 * Created by USUARIO on 13/05/2017.
 */
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;

public class Cargo {

    private String idCargo;
    private String descripcion;

    public Cargo(String idCargo,String descripcion) {
        this.idCargo = idCargo;
        this.descripcion = descripcion;
    }

    public Cargo(Cursor cursor) {
        idCargo = cursor.getString(cursor.getColumnIndex(Cargos.ID_CARGO));
        descripcion = cursor.getString(cursor.getColumnIndex(Cargos.DESCRIPCION));
    }

    public String getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(String idCargo) {
        this.idCargo = idCargo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Cargos.ID_CARGO, idCargo);
        values.put(Cargos.DESCRIPCION, descripcion);
        return values;
    }
}
