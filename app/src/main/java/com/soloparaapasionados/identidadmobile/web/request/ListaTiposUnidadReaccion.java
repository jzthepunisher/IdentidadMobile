package com.soloparaapasionados.identidadmobile.web.request;

import com.soloparaapasionados.identidadmobile.modelo.TipoUnidadReaccion;

import java.util.List;

/**
 * Created by USUARIO on 19/07/2017.
 */

public class ListaTiposUnidadReaccion {
    // Encapsulamiento de Posts
    private String Estado;
    private String Mensaje;
    private String idTurno;
    private List<TipoUnidadReaccion> Items;

    public ListaTiposUnidadReaccion(List<TipoUnidadReaccion> Items) {
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

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public List<TipoUnidadReaccion> getItems() {
        return Items;
    }
}


