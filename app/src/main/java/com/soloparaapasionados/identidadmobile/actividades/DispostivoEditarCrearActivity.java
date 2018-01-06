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
import com.soloparaapasionados.identidadmobile.fragmentos.DispositivoEditarCrearFragment;

public class DispostivoEditarCrearActivity extends AppCompatActivity
{
    public static final String EXTRA_IMEI = "extra_imei";

    DrawerLayout navigationDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispostivo_editar_crear);

        //Establece el toolbar de la actividad.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.navigationDrawerLayout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null)
        {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);
        setToolbar();
        String imei = getIntent().getStringExtra(DispostivoEditarCrearActivity.EXTRA_IMEI);


        DispositivoEditarCrearFragment dispositivoEditarCrearFragment = (DispositivoEditarCrearFragment)
                getSupportFragmentManager().findFragmentById(R.id.dispositivo_editar_crear_container);

        if (dispositivoEditarCrearFragment == null)
        {
            dispositivoEditarCrearFragment = DispositivoEditarCrearFragment.newInstance(imei);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.dispositivo_editar_crear_container, dispositivoEditarCrearFragment)
                    .commit();
        }

    }

    private void setupNavigationDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        //textView = (TextView) findViewById(R.id.textView);
                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_empleado_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(DispostivoEditarCrearActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(DispostivoEditarCrearActivity.this, EmpleadoListadoActivity.class);
                                startActivity(intent);

                                return true;
                            case R.id.item_navigation_telefono_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(DispostivoEditarCrearActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent2 = new Intent(DispostivoEditarCrearActivity.this, DispostivoEditarCrearActivity.class);
                                startActivity(intent2);

                                return true;
                            case R.id.item_navigation_orden_instalacion_actividades_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(DispostivoEditarCrearActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent4 = new Intent(DispostivoEditarCrearActivity.this, OrdenInstalacionListadoActivity.class);
                                startActivity(intent4);

                                return true;
                            case R.id.item_navigation_drawer_asignacion_unidades_reaccion:
                                menuItem.setChecked(true);

                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent5 = new Intent(DispostivoEditarCrearActivity.this, UnidadReaccionAsignacionActivity.class);
                                startActivity(intent5);

                                return true;
                            case R.id.item_navigation_geocerca:
                                menuItem.setChecked(true);

                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent3 = new Intent(DispostivoEditarCrearActivity.this, GeoCercaAdicionarEditarDetalleActivity.class);
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
                                Toast.makeText(DispostivoEditarCrearActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }

    private void setToolbar()
    {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        {
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        /*String tituloActividad;
        if (mIdOrdenInstalacion!=null)
        {
            tituloActividad= "Selecciona ordenes de instlacion";
            toolbar.setTitle(tituloActividad);
            toolbar.setSubtitle("toca para info. de Orden Instalación");
        }
        else
        {
            tituloActividad= "Ordenes de Servicio Técnico";
            toolbar.setTitle(tituloActividad);
            toolbar.setSubtitle("toca para info. de Orden Servicio Técnico");
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                navigationDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                //onSearchRequested();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
