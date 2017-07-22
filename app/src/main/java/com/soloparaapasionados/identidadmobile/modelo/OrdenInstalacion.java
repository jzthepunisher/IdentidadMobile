package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacion;

/**
 * Created by USUARIO on 22/07/2017.
 */

public class OrdenInstalacion {
    private String IdOrdenInstlacion;
    private String FechaEmision;
    private String IdCliente;
    private String IdTipoOrdenInstalacion;

    public OrdenInstalacion()
    {
    }

    public OrdenInstalacion(String idOrdenInstlacion,String fechaEmision,String idCliente,String idTipoOrdenInstalacion)
    {
        this.IdOrdenInstlacion=idOrdenInstlacion;
        this.FechaEmision=fechaEmision;
        this.IdCliente=idCliente;
        this.IdTipoOrdenInstalacion=idTipoOrdenInstalacion;
    }

    public OrdenInstalacion(Cursor cursor){
        IdOrdenInstlacion=cursor.getString(cursor.getColumnIndex(OrdenesInstalacion.ID_ORDEN_INSTALACION));
        FechaEmision=cursor.getString(cursor.getColumnIndex(OrdenesInstalacion.FECHA_EMISION));
        IdCliente=cursor.getString(cursor.getColumnIndex(OrdenesInstalacion.ID_CLIENTE));
        IdTipoOrdenInstalacion=cursor.getString(cursor.getColumnIndex(OrdenesInstalacion.ID_TIPO_ORDEN_INSTALACION));
    }

    public OrdenInstalacion(ContentValues contentValues){
        IdOrdenInstlacion=contentValues.getAsString(OrdenesInstalacion.ID_ORDEN_INSTALACION);
        FechaEmision=contentValues.getAsString(OrdenesInstalacion.FECHA_EMISION);
        IdCliente=contentValues.getAsString(OrdenesInstalacion.ID_CLIENTE);
        IdTipoOrdenInstalacion=contentValues.getAsString(OrdenesInstalacion.ID_TIPO_ORDEN_INSTALACION);
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(OrdenesInstalacion.ID_ORDEN_INSTALACION, IdOrdenInstlacion);
        values.put(OrdenesInstalacion.FECHA_EMISION, FechaEmision);
        values.put(OrdenesInstalacion.ID_CLIENTE, IdCliente);
        values.put(OrdenesInstalacion.ID_TIPO_ORDEN_INSTALACION, IdTipoOrdenInstalacion);

        return values;
    }

    public String getIdOrdenInstlacion() {
        return IdOrdenInstlacion;
    }

    public void setIdOrdenInstlacion(String idOrdenInstlacion) {
        IdOrdenInstlacion = idOrdenInstlacion;
    }

    public String getFechaEmision() {
        return FechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        FechaEmision = fechaEmision;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getIdTipoOrdenInstalacion() {
        return IdTipoOrdenInstalacion;
    }

    public void setIdTipoOrdenInstalacion(String idTipoOrdenInstalacion) {
        IdTipoOrdenInstalacion = idTipoOrdenInstalacion;
    }
}
