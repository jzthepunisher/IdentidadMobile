package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;

/**
 * Created by USUARIO on 15/07/2017.
 */

public class Cliente
{
    private String IdCliente;
    private String NombresCliente;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String RazonSocialCliente;
    private String RucCliente;
    private String DireccionCliente;
    private Double LatitudCliente;
    private Double LongitudCliente;
    private Boolean MonitoreoActivo;

    public Cliente(String idCliente,String nombresCliente,String apellidoPaterno,String apellidoMaterno,
                   String razonSocialCliente,String rucCliente,String direccionCliente,Double latitudCliente,
                   Double longitudCliente,Boolean monitoreoActivo)
    {
        this.IdCliente=idCliente;
        this.NombresCliente=nombresCliente;
        this.ApellidoPaterno=apellidoPaterno;
        this.ApellidoMaterno=apellidoMaterno;
        this.RazonSocialCliente=razonSocialCliente;
        this.RucCliente=rucCliente;
        this.DireccionCliente=direccionCliente;
        this.LatitudCliente=latitudCliente;
        this.LongitudCliente=longitudCliente;
        this.MonitoreoActivo=monitoreoActivo;
    }

    public Cliente(Cursor cursor)
    {
        this.IdCliente=cursor.getString(cursor.getColumnIndex(Clientes.ID_CLIENTE));
        this.NombresCliente=cursor.getString(cursor.getColumnIndex(Clientes.NOMBRES_CLIENTE));
        this.ApellidoPaterno=cursor.getString(cursor.getColumnIndex(Clientes.APELLIDO_PATERNO));
        this.ApellidoMaterno=cursor.getString(cursor.getColumnIndex(Clientes.APELLIDO_MATERNO));
        this.RazonSocialCliente=cursor.getString(cursor.getColumnIndex(Clientes.RAZON_SOCIAL_CLIENTE));
        this.RucCliente=cursor.getString(cursor.getColumnIndex(Clientes.RUC_CLIENTE));
        this.DireccionCliente=cursor.getString(cursor.getColumnIndex(Clientes.DIRECCION_CLIENTE));
        this.LatitudCliente=Double.valueOf(cursor.getString(cursor.getColumnIndex(Clientes.LATITUD_CLIENTE)));
        this.LongitudCliente=Double.valueOf(cursor.getString(cursor.getColumnIndex(Clientes.LONGITUD_CLIENTE)));
        this.MonitoreoActivo=Boolean.valueOf( cursor.getString(cursor.getColumnIndex(Clientes.MONITOREO_ACTIVO)));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(Clientes.ID_CLIENTE, IdCliente);
        values.put(Clientes.NOMBRES_CLIENTE, NombresCliente);
        values.put(Clientes.APELLIDO_PATERNO, ApellidoPaterno);
        values.put(Clientes.APELLIDO_MATERNO, ApellidoMaterno);
        values.put(Clientes.RAZON_SOCIAL_CLIENTE, RazonSocialCliente);
        values.put(Clientes.RUC_CLIENTE, RucCliente);
        values.put(Clientes.DIRECCION_CLIENTE, DireccionCliente);
        values.put(Clientes.LATITUD_CLIENTE, LatitudCliente);
        values.put(Clientes.LONGITUD_CLIENTE, LongitudCliente);
        values.put(Clientes.MONITOREO_ACTIVO, MonitoreoActivo);

        return values;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getNombresCliente() {
        return NombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        NombresCliente = nombresCliente;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getRazonSocialCliente() {
        return RazonSocialCliente;
    }

    public void setRazonSocialCliente(String razonSocialCliente) {
        RazonSocialCliente = razonSocialCliente;
    }

    public String getRucCliente() {
        return RucCliente;
    }

    public void setRucCliente(String rucCliente) {
        RucCliente = rucCliente;
    }

    public String getDireccionCliente() {
        return DireccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        DireccionCliente = direccionCliente;
    }

    public Double getLatitudCliente() {
        return LatitudCliente;
    }

    public void setLatitudCliente(Double latitudCliente) {
        LatitudCliente = latitudCliente;
    }

    public Double getLongitudCliente() {
        return LongitudCliente;
    }

    public void setLongitudCliente(Double longitudCliente) {
        LongitudCliente = longitudCliente;
    }

    public Boolean getMonitoreoActivo() {
        return MonitoreoActivo;
    }

    public void setMonitoreoActivo(Boolean monitoreoActivo) {
        MonitoreoActivo = monitoreoActivo;
    }



}
