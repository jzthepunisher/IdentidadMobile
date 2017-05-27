package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

/**
 * Created by USUARIO on 27/05/2017.
 */

public class DispositivoEmpleado {

    private String Imei;
    private String IdEmpleado;

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        IdEmpleado = idEmpleado;
    }

    public ContentValues toContentValues() {

        ContentValues values = new ContentValues();
        values.put(ContratoCotizacion.DispositivosEmpleados.IMEI, Imei);
        values.put(ContratoCotizacion.DispositivosEmpleados.ID_EMPLEADO, IdEmpleado);

        return values;
    }

}
