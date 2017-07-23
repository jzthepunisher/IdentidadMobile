package com.soloparaapasionados.identidadmobile.actividades;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.OrdenInslacionListadoFragment;

public class OrdenInstalacionListadoActivity extends AppCompatActivity {

    public static final String EXTRA_ID_ORDEN_INSTALACION = "extra_id_orden_instalacion";
    private String mIdOrdenInstalacion=null;

    OrdenInslacionListadoFragment ordenInslacionListadoFragment;
    DrawerLayout navigationDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_instalacion_listado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.navigationDrawerLayout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null)
        {
            setupNavigationDrawerContent(navigationView);
        }

        setupNavigationDrawerContent(navigationView);

        mIdOrdenInstalacion = getIntent().getStringExtra(OrdenInstalacionListadoActivity.EXTRA_ID_ORDEN_INSTALACION);
        setToolbar();
        ordenInslacionListadoFragment = (OrdenInslacionListadoFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleados_listado_container);

        if (ordenInslacionListadoFragment == null)
        {
            ordenInslacionListadoFragment = OrdenInslacionListadoFragment.newInstance(mIdOrdenInstalacion);
            getSupportFragmentManager().beginTransaction().add(R.id.ordenes_instalacion_listado_container, ordenInslacionListadoFragment).commit();
        }

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

        String tituloActividad;
        if (mIdOrdenInstalacion!=null)
        {
            tituloActividad= "Selecciona ordenes de instlacion";
            toolbar.setTitle(tituloActividad);
            toolbar.setSubtitle("toca para info. de Orden Instalación");
            //toolbar.setSubtitle("hola");
        }
        else
        {
            tituloActividad= "Ordenes de Servicio Técnico";
            toolbar.setTitle(tituloActividad);
            toolbar.setSubtitle("toca para info. de Orden Servicio Técnico");
            //toolbar.setSubtitle("hola");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empleado_listado, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            //searchView.setIconifiedByDefault(false);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    String cadenaConsulta=newText.trim();
                    if (cadenaConsulta.equals("") || cadenaConsulta == null){
////////////////////////empleadoListadoFragment.ocultaSugerenciaEmpleados();
////////////////////////empleadoListadoFragment.limpiarSugerenciaEmpleados();
                    }else
                    {
////////////////////////empleadoListadoFragment.filtrarEmpleados(cadenaConsulta);
////////////////////////empleadoListadoFragment.muestraSugerenciaEmpleados();
                    }

                    return false;
                }
            });
        }

        return true;
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

                                Toast.makeText(OrdenInstalacionListadoActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(OrdenInstalacionListadoActivity.this, EmpleadoListadoActivity.class);
                                startActivity(intent);

                                return true;
                            case R.id.item_navigation_telefono_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(OrdenInstalacionListadoActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent2 = new Intent(OrdenInstalacionListadoActivity.this, DispostivoAdicionarEditarActivity.class);
                                startActivity(intent2);

                                return true;
                            case R.id.item_navigation_orden_instalacion_actividades_crud:
                                menuItem.setChecked(true);

                                Toast.makeText(OrdenInstalacionListadoActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent4 = new Intent(OrdenInstalacionListadoActivity.this, OrdenInstalacionListadoActivity.class);
                                startActivity(intent4);

                                return true;
                            case R.id.item_navigation_geocerca:
                                menuItem.setChecked(true);

                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent3 = new Intent(OrdenInstalacionListadoActivity.this, GeoCercaAdicionarEditarDetalleActivity.class);
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
                                Toast.makeText(OrdenInstalacionListadoActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                navigationDrawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }


}
