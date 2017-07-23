package com.soloparaapasionados.identidadmobile.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.OrdenInstalacionEjecutarActividadFragment;

public class OrdenInstalacionEjecutarActividadActivity extends AppCompatActivity {

    private String idOrdenInstalacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_instalacion_ejecutar_actividad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setSubtitle("toca para iniciar o terminar la actividad");


        idOrdenInstalacion = getIntent().getStringExtra(OrdenInstalacionListadoActivity.EXTRA_ID_ORDEN_INSTALACION);

        OrdenInstalacionEjecutarActividadFragment ordenInstalacionEjecutarActividadFragment = (OrdenInstalacionEjecutarActividadFragment)
                getSupportFragmentManager().findFragmentById(R.id.ordenes_instalacion_ejecuta_actividad_container);

        if (ordenInstalacionEjecutarActividadFragment == null) {
            ordenInstalacionEjecutarActividadFragment = OrdenInstalacionEjecutarActividadFragment.newInstance(idOrdenInstalacion);
            getSupportFragmentManager().beginTransaction().add(R.id.ordenes_instalacion_ejecuta_actividad_container, ordenInstalacionEjecutarActividadFragment)
                    .commit();
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

}
