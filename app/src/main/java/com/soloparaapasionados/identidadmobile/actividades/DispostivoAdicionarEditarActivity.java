package com.soloparaapasionados.identidadmobile.actividades;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.DispositivoAdicionarEditarFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoAdicionarEditarFragment;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Dispositivos;

import java.util.ArrayList;
import java.util.Calendar;

public class DispostivoAdicionarEditarActivity extends AppCompatActivity {

    public static final String EXTRA_IMEI = "extra_imei";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivo_adicionar_editar);

        setupWindowAnimations();

        //Establec el toolbar de la actividad.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Registro de Dispositivo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String imei = getIntent().getStringExtra(DispostivoAdicionarEditarActivity.EXTRA_IMEI);

        DispositivoAdicionarEditarFragment dispositivoAdicionarEditarFragment = (DispositivoAdicionarEditarFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleado_adicionar_editar_container);

        if (dispositivoAdicionarEditarFragment == null) {
            dispositivoAdicionarEditarFragment = DispositivoAdicionarEditarFragment.newInstance(imei);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dispositivo_adicionar_editar_container, dispositivoAdicionarEditarFragment)
                    .commit();
        }
    }
    private void setupWindowAnimations() {
        getWindow().setReenterTransition(new Explode());
        getWindow().setExitTransition(new Explode().setDuration(500));
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }





}
