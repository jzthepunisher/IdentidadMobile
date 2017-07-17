package com.soloparaapasionados.identidadmobile.web.request;

import com.soloparaapasionados.identidadmobile.modelo.Turno;

import java.util.List;

/**
 * Created by USUARIO on 16/07/2017.
 */

public class ListaTurnos {
    // Encapsulamiento de Posts
    private String Estado;
    private String Mensaje;
    private String idTurno;
    private List<Turno> Items;


    public ListaTurnos(List<Turno> Items) {
        this.Items = Items;
    }

    public String getEstado() {
        return Estado;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idEmpleado) {
        this.idTurno = idTurno;
    }

    public List<Turno> getItems() {
        return Items;
    }
}


