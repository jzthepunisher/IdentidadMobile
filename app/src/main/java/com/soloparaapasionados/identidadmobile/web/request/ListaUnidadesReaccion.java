package com.soloparaapasionados.identidadmobile.web.request;

import com.soloparaapasionados.identidadmobile.modelo.UnidadReaccion;

import java.util.List;

/**
 * Created by USUARIO on 19/07/2017.
 */

public class ListaUnidadesReaccion{
    // Encapsulamiento de Posts
    private String Estado;
    private String Mensaje;
    private String idUnidadReaccion;
    private List<UnidadReaccion> Items;

    public ListaUnidadesReaccion(List<UnidadReaccion> Items) {
        this.Items = Items;
    }

    public String getEstado() {
        return Estado;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public String getIdTurno() {
        return idUnidadReaccion;
    }

    public void setIdUnidadReaccion(String idUnidadReaccion) {
        this.idUnidadReaccion = idUnidadReaccion;
    }

    public List<UnidadReaccion> getItems() {
        return Items;
    }
}

