package com.soloparaapasionados.identidadmobile.actividades;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoAdicionarEditarFragment;

public class EmpleadoAdicionarEditarActivity extends AppCompatActivity {

    public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_adicionar_editar);

        //Establec el toolbar de la actividad.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapser);

        String idEmpleado = getIntent().getStringExtra(EmpleadoAdicionarEditarActivity.EXTRA_ID_EMPLEADO);

        collapsingToolbarLayout.setTitle(idEmpleado == null ? "Añadir Empleado" : "Editar Empleado");


        EmpleadoAdicionarEditarFragment empleadoAdicionarEditarFragment = (EmpleadoAdicionarEditarFragment)
            getSupportFragmentManager().findFragmentById(R.id.empleado_adicionar_editar_container);

        if (empleadoAdicionarEditarFragment == null) {
            empleadoAdicionarEditarFragment = EmpleadoAdicionarEditarFragment.newInstance(idEmpleado);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.empleado_adicionar_editar_container, empleadoAdicionarEditarFragment)
                    .commit();
        }


        FloatingActionButton floatingActionButtonGuardar = (FloatingActionButton) findViewById(R.id.floatingActionButtonGuardar);
        floatingActionButtonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validar los datos

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empleado_adicionar_editar, menu);
        return super.onCreateOptionsMenu(menu);
    }



}



