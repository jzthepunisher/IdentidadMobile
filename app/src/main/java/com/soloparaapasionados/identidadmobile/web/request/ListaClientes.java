package com.soloparaapasionados.identidadmobile.web.request;

import com.soloparaapasionados.identidadmobile.modelo.Cliente;

import java.util.List;

/**
 * Created by USUARIO on 21/07/2017.
 */

public class ListaClientes {
    // Encapsulamiento de Posts
    private String Estado;
    private String Mensaje;
    private String idCliente;
    private List<Cliente> Items;

    public ListaClientes(List<Cliente> Items) {
        this.Items = Items;
    }

    public String getEstado() {
        return Estado;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public String getIdTurno() {
        return idCliente;
    }

    public void setIdTurno(String idTurno) {
        this.idCliente = idCliente;
    }

    public List<Cliente> getItems() {
        return Items;
    }
}


