package com.soloparaapasionados.identidadmobile.actividades;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;

public class EmpleadoListadoActivity extends AppCompatActivity {

    public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";
    EmpleadoListadoFragment empleadoListadoFragment;
    DrawerLayout navigationDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_listado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationDrawerLayout = (DrawerLayout) findViewById(R.id.navigationDrawerLayout);
        setToolbar();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);
        }
        setupNavigationDrawerContent(navigationView);

        empleadoListadoFragment = (EmpleadoListadoFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleados_listado_container);

        if (empleadoListadoFragment == null) {
            empleadoListadoFragment = EmpleadoListadoFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.empleados_listado_container, empleadoListadoFragment).commit();
        }

    }

    private void setToolbar() {
        // Añadir la Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Listado de Empleados B");

        if (getSupportActionBar() != null){
            // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
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
                        empleadoListadoFragment.ocultaSugerenciaEmpleados();
                        empleadoListadoFragment.limpiarSugerenciaEmpleados();
                    }else
                    {
                        empleadoListadoFragment.filtrarEmpleados(cadenaConsulta);
                        empleadoListadoFragment.muestraSugerenciaEmpleados();
                    }

                    return false;
                }
            });
        }

        return true;
    }

   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                //onSearchRequested();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

   /* @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    //textView = (TextView) findViewById(R.id.textView);
                    switch (menuItem.getItemId()) {
                        case R.id.item_navigation_drawer_inbox:
                            menuItem.setChecked(true);

                            Toast.makeText(EmpleadoListadoActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            navigationDrawerLayout.closeDrawer(GravityCompat.START);
                            Intent intent = new Intent(EmpleadoListadoActivity.this, EmpleadoListadoActivity.class);
                            startActivity(intent);

                            return true;
                        case R.id.item_navigation_drawer_starred:
                            menuItem.setChecked(true);

                            Toast.makeText(EmpleadoListadoActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            navigationDrawerLayout.closeDrawer(GravityCompat.START);
                            Intent intent2 = new Intent(EmpleadoListadoActivity.this, DispostivoAdicionarEditarActivity.class);
                            startActivity(intent2);

                            return true;
                        case R.id.item_navigation_drawer_sent_mail:
                            menuItem.setChecked(true);
                            //textView.setText(menuItem.getTitle());
                            navigationDrawerLayout.closeDrawer(GravityCompat.START);
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
                            Toast.makeText(EmpleadoListadoActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            navigationDrawerLayout.closeDrawer(GravityCompat.START);
                            return true;
                    }
                    return true;
                }
            });
    }

}
