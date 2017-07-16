package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;

/**
 * Created by USUARIO on 15/07/2017.
 */

public class Cliente
{
    private String idCliente;
    private String nombresCliente;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String razonSocialCliente;
    private String rucCliente;
    private String direccionCliente;
    private String latitudCliente;
    private String longitudCliente;
    private Boolean monitoreoActivo;

    public Cliente(String idCliente,String nombresCliente,String apellidoPaterno,String apellidoMaterno,
                   String razonSocialCliente,String rucCliente,String direccionCliente,String latitudCliente,
                    String longitudCliente,Boolean monitoreoActivo)
    {
        this.idCliente=idCliente;
        this.nombresCliente=nombresCliente;
        this.apellidoPaterno=apellidoPaterno;
        this.apellidoMaterno=apellidoMaterno;
        this.razonSocialCliente=razonSocialCliente;
        this.rucCliente=rucCliente;
        this.direccionCliente=direccionCliente;
        this.latitudCliente=latitudCliente;
        this.longitudCliente=longitudCliente;
        this.monitoreoActivo=monitoreoActivo;
    }

    public Cliente(Cursor cursor)
    {
        this.idCliente=cursor.getString(cursor.getColumnIndex(Clientes.ID_CLIENTE));
        this.nombresCliente=cursor.getString(cursor.getColumnIndex(Clientes.NOMBRES_CLIENTE));
        this.apellidoPaterno=cursor.getString(cursor.getColumnIndex(Clientes.APELLIDO_PATERNO));
        this.apellidoMaterno=cursor.getString(cursor.getColumnIndex(Clientes.APELLIDO_MATERNO));
        this.razonSocialCliente=cursor.getString(cursor.getColumnIndex(Clientes.RAZON_SOCIAL_CLIENTE));
        this.rucCliente=cursor.getString(cursor.getColumnIndex(Clientes.RUC_CLIENTE));
        this.direccionCliente=cursor.getString(cursor.getColumnIndex(Clientes.DIRECCION_CLIENTE));
        this.latitudCliente=cursor.getString(cursor.getColumnIndex(Clientes.LATITUD_CLIENTE));
        this.longitudCliente=cursor.getString(cursor.getColumnIndex(Clientes.LONGITUD_CLIENTE));
        this.monitoreoActivo=Boolean.valueOf( cursor.getString(cursor.getColumnIndex(Clientes.MONITOREO_ACTIVO)));
    }


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Clientes.ID_CLIENTE, idCliente);
        values.put(Clientes.NOMBRES_CLIENTE, nombresCliente);
        values.put(Clientes.APELLIDO_PATERNO, apellidoPaterno);
        values.put(Clientes.APELLIDO_MATERNO, apellidoMaterno);
        values.put(Clientes.RAZON_SOCIAL_CLIENTE, razonSocialCliente);
        values.put(Clientes.RUC_CLIENTE, rucCliente);
        values.put(Clientes.DIRECCION_CLIENTE, direccionCliente);
        values.put(Clientes.LATITUD_CLIENTE, latitudCliente);
        values.put(Clientes.LONGITUD_CLIENTE, longitudCliente);
        values.put(Clientes.MONITOREO_ACTIVO, monitoreoActivo);

        return values;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }

    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }

    public String getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(String rucCliente) {
        this.rucCliente = rucCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getLatitudCliente() {
        return latitudCliente;
    }

    public void setLatitudCliente(String latitudCliente) {
        this.latitudCliente = latitudCliente;
    }

    public String getLongitudCliente() {
        return longitudCliente;
    }

    public void setLongitudCliente(String longitudCliente) {
        this.longitudCliente = longitudCliente;
    }

    public Boolean getMonitoreoActivo() {
        return monitoreoActivo;
    }

    public void setMonitoreoActivo(Boolean monitoreoActivo) {
        this.monitoreoActivo = monitoreoActivo;
    }


}
