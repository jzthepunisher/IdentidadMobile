package com.soloparaapasionados.identidadmobile.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.MapaTermicoAgrupacionFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.UnidadReaccionListaPorTurnoFragment;

public class UnidadReaccionAsignacionActivity extends AppCompatActivity {

    UnidadReaccionListaPorTurnoFragment unidadReaccionListaPorTurnoFragment;
    boolean patronMasterDetalle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad_reaccion_asignacion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        unidadReaccionListaPorTurnoFragment = (UnidadReaccionListaPorTurnoFragment)
                getSupportFragmentManager().findFragmentById(R.id.unidad_reaccion_listado_por_turno_container);

        if (unidadReaccionListaPorTurnoFragment == null) {
            unidadReaccionListaPorTurnoFragment = UnidadReaccionListaPorTurnoFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.unidad_reaccion_listado_por_turno_container, unidadReaccionListaPorTurnoFragment).commit();
        }

        // Verificación: ¿Existe el detalle en el layout?
        if (findViewById(R.id.mapa_termico_agrupacion_container) != null) {
            // Si es asi, entonces confirmar modo Master-Detail
            patronMasterDetalle = true;

            cargarFragmentoDetalle("0101");
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

    private void cargarFragmentoDetalle(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(MapaTermicoAgrupacionFragment.ARGUMENTO_ID_TURNO, id);

        MapaTermicoAgrupacionFragment mapaTermicoAgrupacionFragment = new MapaTermicoAgrupacionFragment();
        mapaTermicoAgrupacionFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapa_termico_agrupacion_container, mapaTermicoAgrupacionFragment)
                .commit();
    }
}
