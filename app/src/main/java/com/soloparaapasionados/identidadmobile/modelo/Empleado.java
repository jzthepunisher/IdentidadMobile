package com.soloparaapasionados.identidadmobile.modelo;

import android.content.ContentValues;
import android.database.Cursor;

import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;

import java.io.Serializable;

/**
 * Created by USUARIO on 13/05/2017.
 */

public class Empleado implements Serializable {
    private static final long serialVersionUID=1L;
    private String IdEmpleado;
    private String Nombres;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Direccion;
    private String DNI;
    private String Celular;
    private String Email;
    private String FechaNacimiento;
    private String IdCargo;
    private String FechaIngreso;
    private String FechaBaja;
    private String FechaCreacion;
    private String Foto;

    public Empleado(){
    }
    public String getIdEmpleado() {
        return IdEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        IdEmpleado = idEmpleado;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public String getIdCargo() {
        return IdCargo;
    }

    public void setIdCargo(String idCargo) {
        IdCargo = idCargo;
    }

    public String getFechaIngreso() {
        return FechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        FechaIngreso = fechaIngreso;
    }

    public String getFechaBaja() {
        return FechaBaja;
    }

    public void setFechaBaja(String fechaBaja) {
        FechaBaja = fechaBaja;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public ContentValues toContentValues() {

        ContentValues values = new ContentValues();
        values.put(Empleados.ID_EMPLEADO, IdEmpleado);
        values.put(Empleados.NOMBRES, Nombres);
        values.put(Empleados.APELLIDO_PATERNO, ApellidoPaterno);
        values.put(Empleados.APELLIDO_MAERNO, ApellidoMaterno);
        values.put(Empleados.DIRECCION, Direccion);
        values.put(Empleados.DNI, DNI);
        values.put(Empleados.CELULAR, Celular);
        values.put(Empleados.EMAIL, Email);
        values.put(Empleados.FECHA_NACIMIENTO, FechaNacimiento);
        values.put(Empleados.ID_CARGO, IdCargo);
        values.put(Empleados.FECHA_INGRESO, FechaIngreso);
        values.put(Empleados.FECHA_BAJA, FechaBaja);
        values.put(Empleados.FECHA_CREACION, FechaCreacion);
        values.put(Empleados.FOTO, Foto);

        return values;
    }

}
