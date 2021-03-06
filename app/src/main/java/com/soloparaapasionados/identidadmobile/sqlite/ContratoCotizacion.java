package com.soloparaapasionados.identidadmobile.sqlite;

import android.net.Uri;

/**
 * Created by USUARIO on 25/03/2017.
 */

public class ContratoCotizacion
{
    interface ColumnasCargo
    {
        String ID_CARGO    = "id_cargo";
        String DESCRIPCION = "descripcion";
    }

    interface ColumnasDispositivoEmpleado
    {
        String IMEI             = "imei";
        String ID_EMPLEADO      = "id_empleado";
    }

    interface ColumnasDispositivo
    {
        String IMEI                  = "imei";
        String ID_SIM_CARD           = "id_sim_card";
        String DESCRIPCION           = "descripcion";
        String NUMERO_CELULAR        = "numero_celular";
        String ENVIADO               = "enviado";
        String RECIBIDO              = "recibido";
        String VALIDADO              = "validado";
        String ESTADO_SINCRONIZACION = "estado_sincronizacion";
    }

    interface ColumnasEmpleado
    {
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
        String ESTADO           = "estado";
    }

    interface ColumnasTurno
    {
        String ID_TURNO    = "id_turno";
        String DESCRIPCION = "descripcion";
        String HORA_INICIO = "hora_inicio";
        String HORA_FIN    = "hora_fin";
    }

    interface ColumnasTipoUnidadReaccion
    {
        String ID_TIPO_UNIDAD_REACCION = "id_tipo_unidad_reaccion";
        String DESCRIPCION = "descripcion";
        String FOTO        = "foto";
    }

    interface ColumnasUnidadReaccion
    {
        String ID_UNIDAD_REACCION      = "id_unidad_reaccion";
        String ID_TIPO_UNIDAD_REACCION = "id_tipo_unidad_reaccion";
        String DESCRIPCION             = "descripcion";
        String PLACA                   = "placa";
        String MARCA                   = "marca";
        String MODELO                  = "modelo";
        String COLOR                   = "color";
    }

    interface ColumnasTurno_UnidadReaccionUbicacion
    {
        String ID_TURNO           = "id_turno";
        String ID_UNIDAD_REACCION = "id_unidad_reaccion";
        String LATITUD            = "latitud";
        String LONGITUD           = "longitud";
        String DIRECCION          = "direccion";

        String PENDIENTE_PETICION    = "pendiente_peticion";
        String ESTADO_SINCRONIZACION = "estado_sincronizacion";

    }

    interface ColumnasCliente
    {
        String ID_CLIENTE           = "id_cliente";
        String NOMBRES_CLIENTE      = "nombres_cliente";
        String APELLIDO_PATERNO     = "apellido_paterno";
        String APELLIDO_MATERNO     = "apellido_materno";
        String RAZON_SOCIAL_CLIENTE = "razon_social_cliente";
        String RUC_CLIENTE          = "ruc_cliente";
        String DIRECCION_CLIENTE    = "direccion_cliente";
        String LATITUD_CLIENTE      = "latitud_cliente";
        String LONGITUD_CLIENTE     = "longitud_cliente";
        String MONITOREO_ACTIVO     = "monitoreo_activo";
    }

    interface ColumnasOrdenInstalacion
    {
        String ID_ORDEN_INSTALACION      = "id_empleado";
        String FECHA_EMISION             = "fecha_emision";
        String ID_CLIENTE                = "id_cliente";
        String ID_TIPO_ORDEN_INSTALACION = "id_tipo_orden_instalacion";

        String PENDIENTE_PETICION    = "pendiente_peticion";
        String ESTADO_SINCRONIZACION = "estado_sincronizacion";
    }

    interface ColumnasTipoOrdenInstalacion
    {
        String ID_TIPO_ORDEN_INSTALACION   = "id_tipo_orden_instalacion";
        String DESCRIPCION = "descripcion";
    }

    interface ColumnasOrdenInstalacionEjecucionInicioTerminoActividad
    {
        String FECHA_INICIO_TERMINADO_EJECUCION = "fecha_inicio_terminado_ejecucion";
        String ID_ORDEN_INSTALACION             = "id_orden_instalacion";
        String ID_ACTIVIDAD                     = "id_actividad";
        String INICIADO                         = "iniciado";
        String FECHA_HORA_INICIO                = "fecha_hora_inicio";
        String LATITUD_INICIO                   = "latitud_inicio";
        String LONGITUD_INICIO                  = "longitud_inicio";
        String DIRECCION_INICIO                 = "direccion_inicio";
        String TERMINADO                        = "terminado";
        String FECHA_HORA_TERMINO               = "fecha_hora_termino";
        String LATITUD_TERMINO                  = "latitud_termino";
        String LONGITUD_TERMINO                 = "longitud_Termino";
        String DIRECCION_TERMINO                = "direccion_termino";

        String PENDIENTE_PETICION    = "pendiente_peticion";
        String ESTADO_SINCRONIZACION = "estado_sincronizacion";
    }

    interface ColumnasActividad
    {
        String ID_ACTIVIDAD   = "id_actividad";
        String DESCRIPCION = "descripcion";
    }

    interface ColumnasOrdenInstalacionEjecucionActividad
    {
        String ID_ORDEN_INSTALACION             = "id_orden_instalacion";
        String ID_ACTIVIDAD                     = "id_actividad";
        String INICIADO                         = "iniciado";
        String FECHA_HORA_INICIO                = "fecha_hora_inicio";
        String LATITUD_INICIO                   = "latitud_inicio";
        String LONGITUD_INICIO                  = "longitud_inicio";
        String DIRECCION_INICIO                 = "direccion_inicio";
        String TERMINADO                        = "terminado";
        String FECHA_HORA_TERMINO               = "fecha_hora_termino";
        String LATITUD_TERMINO                  = "latitud_termino";
        String LONGITUD_TERMINO                 = "longitud_Termino";
        String DIRECCION_TERMINO                = "direccion_termino";

        String PENDIENTE_PETICION    = "pendiente_peticion";
        String ESTADO_SINCRONIZACION = "estado_sincronizacion";
    }

    interface ColumnasUbicacionDispositivoGps
    {
        String ID_UBICACION             = "id_ubicacion";
        String DIRECCION_UBICACION      = "direccion_ubicacion";
        String LATITUD                  = "latitud";
        String LONGITUD                 = "longitud";
        String FECHA_HORA_UBICACION     = "fecha_hora_ubicacion";
        String BATERIA                  = "bateria";
        String FECHA_HORA_CREACION      = "fecha_hora_creacion";
        String ESTADO_SINCRONIZACION    = "estado_sincronizacion";
    }

    interface ColumnasCorrelativoTabla
    {
        String TABLA        = "tabla";
        String CORRELATIVO  = "correlativo";
    }

    interface ColumnasProgramacionRastregoGps
    {
        String ID_PROGRAMACION_RASTREO_GPS = "id_programacion_rastreo_gps";
        String DESCRIPCION  = "descripcion";
    }

    interface ColumnasProgramacionRastregoGpsDetalle
    {
        String ID_PROGRAMACION_RASTREO_GPS = "id_programacion_rastreo_gps";
        String DIA="dia";
        String RASTREO_GPS="rastreo_gps";
        String RANGO_HORA_INICIO="rango_hora_inicio";
        String RANGO_HORA_FINAL="rango_hora_final";
        String INTERVALO_HORA_CANTIDAD="intervalo_hora_cantidad";
        String INTERVALO_MINUTO_CANTIDAD="intervalo_minuto_cantidad";
        String INTERVALO_SEGUNDO_CANTIDAD="intervalo_segundo_cantidad";
        String ESTADO_SINCRONIZACION ="estado_sincronizacion";
    }

    interface ColumnasGrupo
    {
        String ID_GRUPO = "id_grupo";
        String DESCRIPCION="descripcion";
        String RASTREO_GPS="rastreo_gps";
        String VER_EN_MAPA="ver_en_mapa";
        String FECHA_HORA_ULTIMA_UBICACION="fecha_hora_ultima_ubicacion";
        String DIRECCION_ULTIMA_UBICACION="direccion_ultima_ubicacion";
        String ID_PROGRAMACION_RASTREO_GPS="id_programacion_rastreo_gps";
        String FECHA_HORA_CREACION="fecha_hora_creacion";
        String ESTADO_SINCRONIZACION ="estado_sincronizacion";
    }

    public interface EstadoRegistro
    {
        String REGISTRADO_LOCALMENTE= "registrado.localmente";
        String REGISTRANDO_REMOTAMENTE= "registrando.remotamente";
        String REGISTRADO_REMOTAMENTE= "registrado.remotamente";

        String ACTUALIZADO_LOCALMENTE= "actualizado.localmente";
        String ACTUALIZANDO_REMOTAMENTE= "actualizando.remotamente";
        String ACTUALIZADO_REMOTAMENTE= "actualizado.remotamente";

        String ELIMINADO_LOCALMENTE= "eliminado.localmente";
        String ELIMINANDO_REMOTAMENTE= "eliminando.remotamente";
        String ELIMINADO_REMOTAMENTE= "eliminado.remotamente";

        String ESTADO_OK= "estado.ok";
        String ESTADO_SINCRONIZANDO= "estado.sincronizando";

    }
    // [URIS]
    public static final String AUTORIDAD = "com.soloparaapasionados.identidadmobile";
    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD);

    private static final String RUTA_DISPOSITIVOS="dispositivos";
    private static final String RUTA_EMPLEADOS="empleados";
    private static final String RUTA_CARGOS="cargos";
    private static final String RUTA_DISPOSITIVOS_EMPLEADOS="dispositivos_empleados";
    private static final String RUTA_DISPOSITIVOS_EMPLEADOS_TEMPORAL="dispositivos_empleados_temporal";
    private static final String RUTA_TURNOS="turnos";
    private static final String RUTA_TIPOS_UNIDAD_REACCION="tipos_unidad_reaccion";
    private static final String RUTA_UNIDADES_REACCION="unidades_reaccion";
    private static final String RUTA_CLIENTES="clientes";
    private static final String RUTA_UBICACIONES="ubicaciones";
    private static final String RUTA_TURNOS_UNIDADES_REACCION_UBICACION="turnos_unidades_reaccion_ubicacion";
    private static final String RUTA_ORDENES_INSTALACION="ordenes_instalacion";
    private static final String RUTA_ORDENES_INSTALACION_EJECUCION_INICIO_TERMINO_ACTIVIDAD="ordenes_instalacion_ejecucion_inicio_termino_actividad";
    private static final String RUTA_ACTIVIDADES="actividades";
    private static final String RUTA_ORDENES_INSTALACION_EJECUCION_ACTIVIDAD="ordenes_instalacion_ejecucion_actividad";
    private static final String RUTA_UBICACIONES_DISPOSITIVO_GPS="ubicaciones_dispositivo_gps";
    private static final String RUTA_CORRELATIVOS_TABLA="correlativos_tabla";
    private static final String RUTA_PROGRAMACION_RASTREO_GPS_TABLA="programacion_rastreo_gps_tabla";
    private static final String RUTA_PROGRAMACION_RASTREO_GPS_DETALLE_TABLA="programacion_rastreo_gps_detalle_tabla";
    private static final String RUTA_GRUPO_TABLA="grupo_tabla";

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
    public static class Dispositivos implements ColumnasDispositivo
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_DISPOSITIVOS).build();

        public static final String PARAMETRO_ESTADO_REGISTRO = "estado_registro";
        public static final String PARAMETRO_ENVIADO_REGISTRO = "enviado_registro";
        public static final String PARAMETRO_RECIBIDO_REGISTRO = "recibido_registro";
        public static final String PARAMETRO_VALIDADO_REGISTRO = "validado_registro";

        public static String obtenerIdDispostivo(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriDispositivo(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriDispositivoConEstado(String idDispositivo,String estadoDispositivo)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idDispositivo).appendQueryParameter(PARAMETRO_ESTADO_REGISTRO,estadoDispositivo).build();
        }

        public static boolean tieneEstadoRegistro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_ESTADO_REGISTRO) != null;
        }

        public static Uri crearUriDispositivoEnviadoConEstado(String idDispositivo,String enviado)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idDispositivo).appendQueryParameter(PARAMETRO_ENVIADO_REGISTRO,enviado).build();
        }

        public static boolean tieneEnviadoRegistro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_ENVIADO_REGISTRO) != null;
        }

        public static Uri crearUriDispositivoRecibidoConEstado(String idDispositivo,String recibido)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idDispositivo).appendQueryParameter(PARAMETRO_RECIBIDO_REGISTRO,recibido).build();
        }

        public static boolean tieneRecibidoRegistro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_RECIBIDO_REGISTRO) != null;
        }

        public static Uri crearUriDispositivoValidadoConEstado(String idDispositivo,String validado)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idDispositivo).appendQueryParameter(PARAMETRO_VALIDADO_REGISTRO,validado).build();
        }

        public static boolean tieneValidadoRegistro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_VALIDADO_REGISTRO) != null;
        }
    }

    //Clase contrato de Empleado
    public static class Empleados implements ColumnasEmpleado
    {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_EMPLEADOS).build();

        public static final String PARAMETRO_CONSULTA = "off_set";
        public static final String PARAMETRO_FILTRO_BUSQUEDA = "filtro_busqueda";
        public static final String PARAMETRO_ESTADO_REGISTRO = "estado_registro";
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

        public static Uri crearUriEmpleadoConEstado(String idEmpleado,String estadoEmpleado) {
            return URI_CONTENIDO.buildUpon().appendPath(idEmpleado).appendQueryParameter(PARAMETRO_ESTADO_REGISTRO,estadoEmpleado).build();
        }

        public static boolean tieneOffSet(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_CONSULTA) != null;
        }

        public static boolean tieneFiltroBusqueda(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO_BUSQUEDA) != null;
        }

        public static boolean tieneEstadoRegistro(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_ESTADO_REGISTRO) != null;
        }
    }

    //Clase contrato de Cargo
    public static class Cargos implements ColumnasCargo
    {

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

        public static String[] obtenerIdDispositivoEmpleado(Uri uri)
        {
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

    //Clase contrato de Turno
    public static class Turnos implements ColumnasTurno
    {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_TURNOS).build();
        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static String obtenerIdTurno(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String obtenerIdUnidadReaccion(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        public static Uri crearUriTurno(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriTurno_UnidadesReaccionUbicacion(String idTurno,String estadoSincronizacion) {
            return URI_CONTENIDO.buildUpon().appendPath(idTurno).appendPath(RUTA_UNIDADES_REACCION).appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static Uri crearUriTurno_UnidadesReaccionUbicacion_Ubicacion(String idTurno,String idUnidadReaccion) {
            return URI_CONTENIDO.buildUpon().appendPath(idTurno).appendPath(RUTA_UNIDADES_REACCION).appendPath(idUnidadReaccion).appendPath(RUTA_UBICACIONES).build();
        }

        public static Uri crearUriTurnoLista(String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static boolean tieneEstadoSincronizaion(Uri uri)
        {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }
    }

    //Clase contrato de Turno
    public static class TiposUnidadReaccion implements ColumnasTipoUnidadReaccion
    {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_TIPOS_UNIDAD_REACCION).build();
        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static String obtenerIdTipoUnidadReaccion(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriTipoUnidadReaccion(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriTipoUnidadReaccionLista(String estadoSincronizacion) {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static boolean tieneEstadoSincronizaion(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }
    }

    //Clase contrato de Turno
    public static class UnidadesReaccion implements ColumnasUnidadReaccion
    {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_UNIDADES_REACCION).build();
        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static Uri crearUriUnidadReaccionLista(String estadoSincronizacion) {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static boolean tieneEstadoSincronizaion(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }

        public static Uri crearUriUnidadReaccion(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

    //Clase contrato de TurnoUnidadReaccionUbicacion
    public static class Turnos_UnidadesReaccionUbicacion implements ColumnasTurno_UnidadReaccionUbicacion
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_TURNOS_UNIDADES_REACCION_UBICACION).build();
        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static Uri crearUriTurnoUnidadReaccionUbicacionLista(String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static boolean tieneEstadoSincronizaion(Uri uri)
        {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }

        public static Uri crearUriTurnoUnidadReaccionUbicacion(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

    //Clase contrato de Turno
    public static class Clientes implements ColumnasCliente
    {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_CLIENTES).build();
        public static final String PARAMETRO_FILTRO_MONITOREO_ACTIVO = "filtro_monitoreo_activo";
        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static String obtenerIdTipoUnidadReaccion(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriCliente(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriClienteListadoConFiltroMonitoreoActivo(Boolean filtroActivo) {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_FILTRO_MONITOREO_ACTIVO,String.valueOf(filtroActivo)).build();
        }

        public static boolean tieneFiltroMonitoreoActivo(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO_MONITOREO_ACTIVO) != null;
        }

        public static Uri crearUriClienteListado(String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static boolean tieneEstadoSincronizaion(Uri uri)
        {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }
    }

    //Clase contrato de Empleado
    public static class OrdenesInstalacion implements ColumnasOrdenInstalacion
    {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_ORDENES_INSTALACION).build();

        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";
        public static final String PARAMETRO_FILTRO_BUSQUEDA = "filtro_busqueda";

        public static String obtenerIdOrdenInstalacion(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String obtenerFechaInicioTerminadoEjecucion(Uri uri) {
            return uri.getPathSegments().get(3);
        }

        public static Uri crearUriOrdenInstalacion(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriOrdenInstalacionListado(String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static boolean tieneEstadoSincronizaion(Uri uri)
        {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }

        public static Uri crearUriOrdenInstalacionConFiltroBusqueda(String filtro) {
            return URI_CONTENIDO.buildUpon().appendQueryParameter(PARAMETRO_FILTRO_BUSQUEDA,filtro).build();
        }

        public static boolean tieneFiltroBusqueda(Uri uri) {
            return uri != null && uri.getQueryParameter(PARAMETRO_FILTRO_BUSQUEDA) != null;
        }

        public static Uri crearUriOrdenesInstalacion_InicioTerminoActividades(String idOrdenInstalacion,String fechaInicioTerminadoEjecucion,String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idOrdenInstalacion).appendPath(RUTA_ACTIVIDADES+"it").appendPath(fechaInicioTerminadoEjecucion).appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

    }

    //Clase contrato de Turno
    public static class TiposOrdenInstalacion implements ColumnasTipoOrdenInstalacion
    {

    }

    //Clase contrato de Turno
    public static class Actividades implements ColumnasActividad
    {

    }

    //Clase contrato de Turno
    public static class OrdenesInstalacionEjecucionInicioTerminoActividad implements ColumnasOrdenInstalacionEjecucionInicioTerminoActividad
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_ORDENES_INSTALACION_EJECUCION_INICIO_TERMINO_ACTIVIDAD).build();

        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static Uri crearUriOrdenesInstalacion_InicioTerminoActividadesListado(String idOrdenInstalacion,String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idOrdenInstalacion).appendPath(RUTA_ACTIVIDADES).appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static String obtenerIdOrdenInstalacion(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static boolean tieneEstadoSincronizaion(Uri uri)
        {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }


    }

    public static class OrdenesInstalacionEjecucionActividad implements ColumnasOrdenInstalacionEjecucionActividad
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_ORDENES_INSTALACION_EJECUCION_ACTIVIDAD).build();

        public static final String PARAMETRO_SINCRONIZACION = "sincronizacion";

        public static Uri crearUriOrdenesInstalacion_ActividadesListado(String idOrdenInstalacion,String estadoSincronizacion)
        {
            return URI_CONTENIDO.buildUpon().appendPath(idOrdenInstalacion).appendPath(RUTA_ACTIVIDADES).appendQueryParameter(PARAMETRO_SINCRONIZACION,estadoSincronizacion).build();
        }

        public static String obtenerIdOrdenInstalacion(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static boolean tieneEstadoSincronizaion(Uri uri)
        {
            return uri != null && uri.getQueryParameter(PARAMETRO_SINCRONIZACION) != null;
        }
    }

    //Clase contrato de Ubicacion Dispositivo Gps
    public static class UbicacionesDispositvoGps implements ColumnasUbicacionDispositivoGps
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_UBICACIONES_DISPOSITIVO_GPS).build();

        public static String obtenerIdUbicacion(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriUbicacionDispositivoGps(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriUbicacionDispositivoGpsLista()
        {
            return URI_CONTENIDO;
        }
    }

    //Clase contrato de Correlativo Tabla
    public static class CorrelativosTabla implements ColumnasCorrelativoTabla
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_CORRELATIVOS_TABLA).build();

        public static String obtenerIdTabla(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriCorrelativoTabla(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriCorrelativoTablaLista()
        {
            return URI_CONTENIDO;
        }
    }

    //Clase contrato de Programacion Rastreo Gps Tabla
    public static class ProgramacionesRastregoGpsTabla implements ColumnasProgramacionRastregoGps
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_PROGRAMACION_RASTREO_GPS_TABLA).build();

        public static String obtenerIdTabla(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriProgramacionRastreoGpsTabla(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriProgramacionRastreoGpsTablaLista()
        {
            return URI_CONTENIDO;
        }
    }

    //Clase contrato de Programacion Rastreo Gps Detalle Tabla
    public static class ProgramacionesRastregoGpsDetalleTabla implements ColumnasProgramacionRastregoGpsDetalle
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_PROGRAMACION_RASTREO_GPS_DETALLE_TABLA).build();
        public static final String CODIGO_DETALLE = "codigo_detalle";

        public static String obtenerIdTabla(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static String obtenerIdDosTabla(Uri uri)
        {
            return uri.getPathSegments().get(3);
        }

        public static Uri crearUriProgramacionRastreoGpsDetalleTabla(String id,String dia)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).appendPath(CODIGO_DETALLE).appendPath(dia).build();
        }

        public static Uri crearUriProgramacionRastreoGpsDetalleTablaActivadoDesactivado(String id,String dia,String activar_desactivar)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).appendPath(CODIGO_DETALLE).appendPath(dia).appendPath(activar_desactivar).build();
        }

        public static Uri crearUriProgramacionRastreoGpsDetalleTablaLista()
        {
            return URI_CONTENIDO;
        }
    }

    //Clase contrato de Programacion Rastreo Gps Detalle Tabla
    public static class GruposTabla implements ColumnasGrupo
    {
        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_GRUPO_TABLA).build();

        public static String obtenerIdGrupo(Uri uri)
        {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriGrupoTabla(String id)
        {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static Uri crearUriGrupoTablaLista()
        {
            return URI_CONTENIDO;
        }
    }
}
