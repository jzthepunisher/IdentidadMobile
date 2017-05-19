package com.soloparaapasionados.identidadmobile.ServicioLocal;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.aplicacion.Constantes;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;

/**
 * Created by USUARIO on 13/05/2017.
 */

public class EmpleadoServicioLocal extends IntentService {
    private static final String TAG = EmpleadoServicioLocal.class.getSimpleName();

    public static final String ACCION_INSERTAR_EMPLEADO_ISERVICE = "com.soloparaapasionados.identidadmobile.ServicioLocal.action.ACCION_INSERTAR_EMPLEADO_ISERVICE";
    public static final String EXTRA_MI_EMPLEADO="EXTRA_MI_EMPLEADO";

    public EmpleadoServicioLocal() {
        super("EmpleadoServicioLocal");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (EmpleadoServicioLocal.ACCION_INSERTAR_EMPLEADO_ISERVICE.equals(action)) {

                Empleado empleado=(Empleado)intent.getSerializableExtra(EmpleadoServicioLocal.EXTRA_MI_EMPLEADO);

                Toast.makeText(this,empleado.getNombres(), Toast.LENGTH_LONG).show();
                Toast.makeText(this,empleado.getApellidoMaterno(),Toast.LENGTH_LONG).show();

                insertarEmpleadoLocal(empleado);
            }
        }
    }

    private void insertarEmpleadoLocal(Empleado empleado){
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio Local en segundo plano")
                    .setContentText("Procesando inserción de empleado...");

            // Bucle de simulación
            for (int i = 1; i <= 10; i++) {

                Log.d(TAG, i + ""); // Logueo

                // Poner en primer plano
                builder.setProgress( 10, i, false);
                startForeground(1, builder.build());

                // Intent localIntent = new Intent(Constantes.ACTION_RUN_ISERVICE)
                //         .putExtra(Constantes.EXTRA_PROGRESS, i);

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
    /**
     * Maneja la acción de ejecución del servicio
     */
    private void handleActionRun() {
        try {
            // Se construye la notificación
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle("Servicio en segundo plano")
                    .setContentText("Procesando...");

            // Bucle de simulación
            for (int i = 1; i <= 10; i++) {

                Log.d(TAG, i + ""); // Logueo

                // Poner en primer plano
                builder.setProgress( 10, i, false);
                startForeground(1, builder.build());

               // Intent localIntent = new Intent(Constantes.ACTION_RUN_ISERVICE)
               //         .putExtra(Constantes.EXTRA_PROGRESS, i);

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
