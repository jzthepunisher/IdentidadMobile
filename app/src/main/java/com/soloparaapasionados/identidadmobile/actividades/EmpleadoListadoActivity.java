package com.soloparaapasionados.identidadmobile.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.MainActivity;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;
import com.soloparaapasionados.identidadmobile.observadores.MiObervador;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

public class EmpleadoListadoActivity extends AppCompatActivity {
    MiObervador miObervador;
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

        miObervador = new MiObervador(new Handler());

        EmpleadoListadoFragment empleadoListadoFragment = (EmpleadoListadoFragment)
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

   /* @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }*/

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

        getContentResolver().registerContentObserver(ContratoCotizacion.Empleados.crearUriEmpleadoOffSet(String.valueOf(0)),true,miObervador);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //getContentResolver().unregisterContentObserver(miObervador);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(miObervador);

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
                            Intent intent2 = new Intent(EmpleadoListadoActivity.this, MainActivity.class);
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
