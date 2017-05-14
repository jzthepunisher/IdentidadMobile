package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

/**
 * Created by USUARIO on 13/05/2017.
 */

public class CargoServicioLocal  extends IntentService {
    private static final String TAG = CargoServicioLocal.class.getSimpleName();

    public CargoServicioLocal() {
        super("CargoServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
            case Constantes.ACCION_OBTENER_CARGOS:
                obtenerCargos();
            }
        }
    }

    /**
     * Maneja la acción de ejecución del servicio
     */
    private void obtenerCargos() {
        try {
            // Se construye la notificación
           /* NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio en segundo plano")
                    .setContentText("Procesando...");*/

            // Bucle de simulación
            for (int i = 1; i <= 10; i++) {

                Log.d(TAG, i + ""); // Logueo

                // Poner en primer plano
                /*builder.setProgress( 10, i, false);
                startForeground(1, builder.build());*/

                Cursor cursorCargos=null;

                cursorCargos=getContentResolver().query(ContratoCotizacion.Cargos.crearUriCargoLista(), null, null, null, null);

                int count =cursorCargos.getCount();

                /*CursorWindow window = new CursorWindow("MI_CURSOR_WINDOW");
                cursorCargos.
                cursorCargos.fillWindow(0, window);
                Bundle bundle=new Bundle();
                bundle.c*/

                /*Intent localIntent = new Intent(Constantes.ACTION_RUN_ISERVICE)
                        .putExtra(Constantes.EXTRA_CARGOS, cursorCargos);*/

                // Emisión de {@code localIntent}
                //LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

                // Retardo de 1 segundo en la iteración
                Thread.sleep(1000);
            }
            // Quitar de primer plano
            stopForeground(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Servicio destruido...", Toast.LENGTH_SHORT).show();

        // Emisión para avisar que se terminó el servicio
        Intent localIntent = new Intent(Constantes.ACTION_PROGRESS_EXIT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        Log.d(TAG, "Servicio destruido...");
    }

}
