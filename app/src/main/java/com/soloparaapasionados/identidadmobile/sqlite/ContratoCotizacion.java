package com.soloparaapasionados.identidadmobile.sqlite;

import android.net.Uri;

/**
 * Created by USUARIO on 25/03/2017.
 */

public class ContratoCotizacion {

    interface ColumnasDispositivo {
        String IMEI = "imei";
        String ID_TIPO_DISPOSITIVO = "id_tipo_dispositivo";
        String ID_SIM_CARD = "id_sim_card";
        String NUMERO_CELULAR = "numero_celular";
        String ENVIADO = "enviado";
        String RECIBIDO = "recibido";
        String VALIDADO= "validado";
    }
    // [URIS]
    public static final String AUTORIDAD = "com.soloparaapasionados.identidadmobile";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTORIDAD);

    private static final String RUTA_DISPOSITIVOS="dispositivos";
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

    public static class Dispositivos implements ColumnasDispositivo {

        public static final Uri URI_CONTENIDO = URI_BASE.buildUpon().appendPath(RUTA_DISPOSITIVOS).build();

        public static String obtenerIdDispostivo(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri crearUriDispositivo(String id) {
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
    }

}
