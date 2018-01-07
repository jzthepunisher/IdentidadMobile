package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;

import java.io.Serializable;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;

/**
 * Created by USUARIO on 06/01/2018.
 */

public class Dispositivo implements Serializable
{
    public Dispositivo()
    {}

    public Dispositivo(ContentValues contentValues)
    {
        Imei=contentValues.getAsString(Dispositivos.IMEI);
        IdSimCard=contentValues.getAsString(Dispositivos.ID_SIM_CARD);
        Descripcion=contentValues.getAsString(Dispositivos.DESCRIPCION);
        NumeroCelular=contentValues.getAsString(Dispositivos.NUMERO_CELULAR);

        //Mensaje Enviado
        boolean enviado=false;
        enviado=contentValues.getAsBoolean(Dispositivos.ENVIADO)==true?true:false;
        Enviado=enviado;

        //Mensaje Recibido
        boolean recibido=false;
        recibido=contentValues.getAsBoolean(Dispositivos.RECIBIDO)==true?true:false;
        Recibido=enviado;

        //Mensaje Validado
        boolean validado=false;
        validado=contentValues.getAsBoolean(Dispositivos.VALIDADO)==true?true:false;
        Validado=validado;

        EstadoSincronizacion=contentValues.getAsString(Dispositivos.ESTADO_SINCRONIZACION);

    }
   /* public ContentValues toContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(Dispositivos.IMEI, Imei);
        values.put(Dispositivos.ID_EMPLEADO, IdEmpleado);

        return values;
    }*/

    private static final long serialVersionUID=1L;
    private String Imei;
    private String IdSimCard;
    private String Descripcion;
    private String NumeroCelular;
    private Boolean Enviado;
    private Boolean Recibido;
    private Boolean Validado;
    private String EstadoSincronizacion;

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getIdSimCard() {
        return IdSimCard;
    }

    public void setIdSimCard(String idSimCard) {
        IdSimCard = idSimCard;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getNumeroCelular() {
        return NumeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        NumeroCelular = numeroCelular;
    }

    public Boolean getEnviado() {
        return Enviado;
    }

    public void setEnviado(Boolean enviado) {
        Enviado = enviado;
    }

    public Boolean getRecibido() {
        return Recibido;
    }

    public void setRecibido(Boolean recibido) {
        Recibido = recibido;
    }

    public Boolean getValidado() {
        return Validado;
    }

    public void setValidado(Boolean validado) {
        Validado = validado;
    }

    public String getEstadoSincronizacion() {
        return EstadoSincronizacion;
    }

    public void setEstadoSincronizacion(String estadoSincronizacion) {
        EstadoSincronizacion = estadoSincronizacion;
    }
}
