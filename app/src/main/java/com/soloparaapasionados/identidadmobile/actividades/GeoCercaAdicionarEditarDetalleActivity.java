package com.soloparaapasionados.identidadmobile.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoAdicionarEditarFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.GeoCercaAdicionarEditarDetalleFragment;

public class GeoCercaAdicionarEditarDetalleActivity extends AppCompatActivity {
    public static final String EXTRA_ID_GEOCERCA = "extra_id_geocerca";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_cerca_adicionar_editar_detalle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String idGeocerca = getIntent().getStringExtra(GeoCercaAdicionarEditarDetalleActivity.EXTRA_ID_GEOCERCA);

        GeoCercaAdicionarEditarDetalleFragment geoCercaAdicionarEditarDetalleFragment = (GeoCercaAdicionarEditarDetalleFragment)
                getSupportFragmentManager().findFragmentById(R.id.geocerca_adicionar_editar_container);

        if (geoCercaAdicionarEditarDetalleFragment == null) {
            geoCercaAdicionarEditarDetalleFragment = GeoCercaAdicionarEditarDetalleFragment.newInstance(idGeocerca);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.geocerca_adicionar_editar_container, geoCercaAdicionarEditarDetalleFragment)
                    .commit();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
