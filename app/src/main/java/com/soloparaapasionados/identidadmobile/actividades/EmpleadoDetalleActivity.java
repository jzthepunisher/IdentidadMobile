package com.soloparaapasionados.identidadmobile.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoDetalleFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;

public class EmpleadoDetalleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String idEmpleado = getIntent().getStringExtra(EmpleadoListadoActivity.EXTRA_ID_EMPLEADO);

        EmpleadoDetalleFragment empleadoDetalleFragment = (EmpleadoDetalleFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleado_detalle_container);

        if (empleadoDetalleFragment == null) {
            empleadoDetalleFragment = EmpleadoDetalleFragment.newInstance(idEmpleado);
            getSupportFragmentManager().beginTransaction().add(R.id.empleado_detalle_container, empleadoDetalleFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empleado_detalle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
