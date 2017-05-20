package com.soloparaapasionados.identidadmobile.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;

public class EmpleadoListadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_listado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EmpleadoListadoFragment empleadoListadoFragment = (EmpleadoListadoFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleados_listado_container);

        if (empleadoListadoFragment == null) {
            empleadoListadoFragment = EmpleadoListadoFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.empleados_listado_container, empleadoListadoFragment).commit();
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
