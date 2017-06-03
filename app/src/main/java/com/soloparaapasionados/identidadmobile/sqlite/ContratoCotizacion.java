package com.soloparaapasionados.identidadmobile.sqlite;

import android.net.Uri;

/**
 * Created by USUARIO on 25/03/2017.
 */

public class ContratoCotizacion {

    interface ColumnasDispositivo {
        String IMEI                = "imei";
        String ID_TIPO_DISPOSITIVO = "id_tipo_dispositivo";
        String ID_SIM_CARD         = "id_sim_card";
        String NUMERO_CELULAR      = "numero_celular";
        String ENVIADO             = "enviado";
        String RECIBIDO            = "recibido";
        String VALIDADO            = "validado";
    }

    interface ColumnasEmpleado{
        String ID_EMPLEADO      = "id_empleado";
        String NOMBRES          = "nombres";
        String APELLIDO_PATERNO = "apellido_paterno";
        String APELLIDO_MAERNO  = "apellido_materno";
        String DIRECCION        = "direccion";
        String DNI              = "dni";
        String CELULAR          = "celular";
        String EMAIL            = "email";
        String FECHA_NACIMIENTO = "fecha_nacimiento";
        String ID_CARGO         = "id_cargo";
        String FECHA_INGRESO    = "fecha_ingreso";
        String FECHA_BAJA       = "fecha_baja";
        String FECHA_CREACION   = "fecha_creacion";
        String FOTO             = "foto";
        String ELIMINADO        = "eliminado";
    }

    interface ColumnasCargo{
        String ID_CARGO    = "id_cargo";
        String DESCRIPCION = "descripcion";
    }

    interface ColumnasDispositivoEmpleado{
        String IMEI                = "imei";
        String ID_EMPLEADO      = "id_empleado";
    }
    // [URIS]
    public static final String AUTORIDAD = "com.soloparaapasionados.identidadmobile";
    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD);
    private static final String RUTA_DISPOSITIVOS="dispositivos";
    private static final String RUTA_EMPLEADOS="empleados";
    private static final String RUTA_CARGOS="cargos";
    private static final String RUTA_DISPOSITIVOS_EMPLEADOS="dispositivos_empleados";
    private static final String RUTA_DISPOSITIVOS_EMPLEADOS_TEMPORAL="dispositivos_empleados_temporal";
    // [/URIS]

    // [TIPOS_MIME]
    public static final String BASE_CONTENIDOS = "cotizaciones.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;

    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }
    // [/TIPOS_MIME]

    public int OFF_SET=10;
    //Clase contrato de Dispositivo
    public static class Dispositivos implements ColumnasDispositivo {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_DISPOSITIVOS).build();

        public static String obtenerIdDispostivo(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriDispositivo(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

    //Clase contrato de Empleado
    public static class Empleados implements ColumnasEmpleado {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_EMPLEADOS).build();

        public static final String PARAMETRO_CONSULTA = "off_set";
        public static final String PARAMETRO_FILTRO_BUSQUEDA = "filtro_busqueda";
        //public static final String FILTRO_CLIENTE = "nombres_completos";

        public static String obtenerIdEmpleado(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriEmpleado(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriEmpleadoOffSet(String offSet) {
            return URI_CONTENIDO.buildUpon().appendQueryParameter("off_set",offSet).build();
        }

        public static Uri crearUriEmpleadoConFiltroBusqueda(String filtro) {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_FILTRO_BUSQUEDA,filtro).build();
        }

        public static boolean tieneOffSet(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_CONSULTA) != null;
        }

        public static boolean tieneFiltroBusqueda(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO_BUSQUEDA) != null;
        }
    }

    //Clase contrato de Cargo
    public static class Cargos implements ColumnasCargo {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_CARGOS).build();

        public static String obtenerIdCargo(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriCargo(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriCargoLista() {
            return URI_CONTENIDO;
        }
    }

    //Clase contrato de Dispositivo_Empleado
    public static class DispositivosEmpleados implements ColumnasDispositivoEmpleado{
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_DISPOSITIVOS_EMPLEADOS).build();

        public static String obtenerIdDispostivoEmpleado(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriDispositivoEmpleado(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriDispositivoEmpleado(String imei,String idEmpleado) {
            return URI_CONTENIDO.buildUpon().appendPath(imei + "%s" + idEmpleado).build();
        }

        public static String[] obtenerIdDispositivoEmpleado(Uri uri) {
            return uri.getLastPathSegment().split("%s");
        }
    }

    //Clase contrato de Dispositivo_Empleado Temporal
    public static class DispositivosEmpleadosTemporal implements ColumnasDispositivoEmpleado{
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_DISPOSITIVOS_EMPLEADOS_TEMPORAL).build();

        public static String obtenerIdDispostivoEmpleadoTemporal(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriDispositivoEmpleadoTemporal(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

}
