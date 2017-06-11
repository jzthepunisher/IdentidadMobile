package com.soloparaapasionados.identidadmobile.web.request;

import com.soloparaapasionados.identidadmobile.modelo.Empleado;

import java.util.List;

/**
 * Created by USUARIO on 04/06/2017.
 */

public class ListaEmpleados {
    // Encapsulamiento de Posts
    private String Estado;
    private String Mensaje;
    private String idEmpleado;
    private List<Empleado> Items;


    public ListaEmpleados(List<Empleado> Items) {
        this.Items = Items;
    }

    public String getEstado() {
        return Estado;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public List<Empleado> getItems() {
        return Items;
    }
}

