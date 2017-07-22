package com.soloparaapasionados.identidadmobile.aplicacion;

/**
 * Created by USUARIO on 13/05/2017.
 */

public class Constantes {
    /**
     * Constantes para {@link MemoryService}
     */
    public static final String ACTION_RUN_SERVICE = "com.herprogramacion.memoryout.action.RUN_SERVICE";
    public static final String ACTION_MEMORY_EXIT = "com.herprogramacion.memoryout.action.MEMORY_EXIT";

    public static final String EXTRA_MEMORY = "com.herprogramacion.memoryout.extra.MEMORY";

    /**
     * Constantes para {@link ProgressIntentService}
     */
    public static final String ACTION_RUN_ISERVICE = "com.herprogramacion.memoryout.action.RUN_INTENT_SERVICE";
    public static final String ACTION_PROGRESS_EXIT = "com.herprogramacion.memoryout.action.PROGRESS_EXIT";

    public static final String ACCION_OBTENER_CARGOS = "com.soloparaapasionados.identidadmobile.aplicacion.action.OBTENER_CARGOS";

    public static final String EXTRA_CARGOS = "com.herprogramacion.memoryout.extra.cargos";

    /**
     * URLs del Web Service
     */
    private static final String PUERTO_HOST = "";
    //private static final String PUERTO_HOST = ":9999";
    //private static final String PUERTO_HOST = ":3000";
    //private static final String PUERTO_HOST = "";

    //private static final String IP = "192.168.1.33";
    //private static final String IP = "localhost";
    //private static final String IP = "10.0.2.2";
    private static final String IP = "yodeseoapirest.somee.com";

    public static final String EMPLEADOS_GET = "http://" + IP + PUERTO_HOST + "/api/empleados";
    public static final String EMPLEADOS_POST = "http://" + IP + PUERTO_HOST + "/api/empleados";
    public static final String GET_BY_ID = "http://" + IP + PUERTO_HOST + "/api/metas/5";
    public static final String UPDATE = "http://" + IP + PUERTO_HOST + "/I%20Wish/actualizar_meta.php";
    public static final String DELETE = "http://" + IP + PUERTO_HOST + "/I%20Wish/borrar_meta.php";

    public static final String GET_TURNOS = "http://" + IP + PUERTO_HOST + "/api/turnos";
    public static final String GET_TIPOS_UNIDAD_REACCION = "http://" + IP + PUERTO_HOST + "/api/tiposunidadreaccion";
    public static final String GET_UNIDADES_REACCION = "http://" + IP + PUERTO_HOST + "/api/unidadesreaccion";
    public static final String GET_TURNOS_UNIDADES_REACCION_UBICACION = "http://" + IP + PUERTO_HOST + "/api/turnosunidadesreaccionubicacion";
    public static final String GET_CLIENTES = "http://" + IP + PUERTO_HOST + "/api/clientes";

    public static final String PUT_TURNOS_UNIDADES_REACCION_UBICACION = "http://" + IP + PUERTO_HOST + "/api/turnosunidadesreaccionubicacion";
}
