package com.soloparaapasionados.identidadmobile.web.request;

import com.soloparaapasionados.identidadmobile.modelo.Turno_UnidadReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

import java.util.List;

/**
 * Created by USUARIO on 20/07/2017.
 */

public class ListaTurnosUnidadesReaccionUbicacion {

    // Encapsulamiento de Posts
    private String Estado;
    private String Mensaje;
    private String idTurno;
    private String idUnidadReaccion;
    private List<Turno_UnidadReaccionUbicacion> Items;

    public ListaTurnosUnidadesReaccionUbicacion(List<Turno_UnidadReaccionUbicacion> Items) {
        this.Items = Items;
    }

    public List<Turno_UnidadReaccionUbicacion> getItems() {
        return Items;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getIdUnidadReaccion() {
        return idUnidadReaccion;
    }

    public void setIdUnidadReaccion(String idUnidadReaccion) {
        this.idUnidadReaccion = idUnidadReaccion;
    }

}

