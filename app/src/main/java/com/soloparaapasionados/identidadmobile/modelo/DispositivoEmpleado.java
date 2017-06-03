package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.DispositivosEmpleados;

import java.io.Serializable;
import java.util.List;

/**
 * Created by USUARIO on 27/05/2017.
 */

public class DispositivoEmpleado implements Serializable {
    private static final long serialVersionUID=1L;
    private String Imei;
    private String IdEmpleado;
    List<Empleado> empleados;

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

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    public ContentValues toContentValues() {

        ContentValues values = new ContentValues();
        values.put(DispositivosEmpleados.IMEI, Imei);
        values.put(DispositivosEmpleados.ID_EMPLEADO, IdEmpleado);

        return values;
    }

}
