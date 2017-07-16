package com.soloparaapasionados.identidadmobile.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.MapaTermicoAgrupacionFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.UnidadReaccionListaPorTurnoFragment;

public class UnidadReaccionAsignacionActivity extends AppCompatActivity
        implements UnidadReaccionListaPorTurnoFragment.OnTurnoItemClickFragmentoListener{

    UnidadReaccionListaPorTurnoFragment unidadReaccionListaPorTurnoFragment;
    MapaTermicoAgrupacionFragment mapaTermicoAgrupacionFragment;

    public static boolean patronMasterDetalle;
    DrawerLayout navigationDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unidad_reaccion_asignacion);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setSubtitle("toca para un turno y unidad de reacción");
        //String tituloActividad= "toca para un turno y unidad de reacción";
        //toolbar.setSubtitle(tituloActividad);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.navigationDrawerLayout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }

        setToolbar();

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
            cargarFragmentoDetalle("","","",-1);
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

    private void cargarFragmentoDetalle(String idTurno,String descripcionTurno, String rangoHorarioTurno,int position) {
        Bundle bundle = new Bundle();
        bundle.putString(MapaTermicoAgrupacionFragment.ARGUMENTO_ID_TURNO, idTurno);
        bundle.putString(MapaTermicoAgrupacionFragment.ARGUMENTO_DESCRIPCION_TURNO, descripcionTurno);
        bundle.putString(MapaTermicoAgrupacionFragment.ARGUMENTO_HORARIO_TURNO, rangoHorarioTurno);

        MapaTermicoAgrupacionFragment mapaTermicoAgrupacionFragment = new MapaTermicoAgrupacionFragment();
        mapaTermicoAgrupacionFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapa_termico_agrupacion_container, mapaTermicoAgrupacionFragment)
                .commit();
    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        String tituloActividad= "toca para un turno y unidad de reacción";
        toolbar.setSubtitle(tituloActividad);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //textView = (TextView) findViewById(R.id.textView);
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_empleado_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(UnidadReaccionAsignacionActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(UnidadReaccionAsignacionActivity.this, EmpleadoListadoActivity.class);
                                startActivity(intent);

                                return true;
                            case R.id.item_navigation_telefono_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(UnidadReaccionAsignacionActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent2 = new Intent(UnidadReaccionAsignacionActivity.this, DispostivoAdicionarEditarActivity.class);
                                startActivity(intent2);

                                return true;
                            case R.id.item_navigation_drawer_asignacion_unidades_reaccion:
                                menuItem.setChecked(true);

                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent4 = new Intent(UnidadReaccionAsignacionActivity.this, UnidadReaccionAsignacionActivity.class);
                                startActivity(intent4);

                                return true;

                            case R.id.item_navigation_geocerca:
                                menuItem.setChecked(true);

                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent3 = new Intent(UnidadReaccionAsignacionActivity.this, GeoCercaAdicionarEditarDetalleActivity.class);
                                startActivity(intent3);

                                return true;
                            case R.id.item_navigation_drawer_drafts:
                                menuItem.setChecked(true);
                                //textView.setText(menuItem.getTitle());
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);
                                //textView.setText(menuItem.getTitle());

                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                Toast.makeText(UnidadReaccionAsignacionActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnTurnoItemFragmentoClick(String idTurno, String descripcionTurno, String rangoHorarioTurno, int position) {
        // Verificación: ¿Existe el detalle en el layout?
        if (patronMasterDetalle== true) {

            mapaTermicoAgrupacionFragment = (MapaTermicoAgrupacionFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mapa_termico_agrupacion_container);

            if(mapaTermicoAgrupacionFragment!=null){
                mapaTermicoAgrupacionFragment.setSubTituloTurno(idTurno,  descripcionTurno,  rangoHorarioTurno,  position);
            }

        }
    }

    @Override
    public void OnUbicacionUnidadReaccionItemFragmentoClick(String idTurno, String descripcionTurno, String rangoHorarioTurno, int position) {
        // Verificación: ¿Existe el detalle en el layout?
        if (patronMasterDetalle== true) {

            mapaTermicoAgrupacionFragment = (MapaTermicoAgrupacionFragment)
                    getSupportFragmentManager().findFragmentById(R.id.mapa_termico_agrupacion_container);

            if(mapaTermicoAgrupacionFragment!=null){
                mapaTermicoAgrupacionFragment.setSubTituloUbicacionUnidadReaccion(idTurno,  descripcionTurno,  rangoHorarioTurno,  position);
            }

        }
    }
}
