package com.soloparaapasionados.identidadmobile.actividades;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.fragmentos.DatePickerFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoAdicionarEditarFragment;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;

public class EmpleadoAdicionarEditarActivity extends AppCompatActivity
        implements DatePickerFragment.OnDateSelectedListener{

    public static final String EXTRA_ID_EMPLEADO = "extra_id_empleado";
    public static final int REQUEST_ADICIONAR_EMPLEADO = 1;

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

        collapsingToolbarLayout.setTitle(idEmpleado == null ? "Registro de Empleado" : "Edición de Empleado");


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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_empleado_adicionar_editar, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public void onDateSelected(int idViewSeleccionadora,int year, int month, int day) {
        EmpleadoAdicionarEditarFragment empleadoAdicionarEditarFragment = (EmpleadoAdicionarEditarFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleado_adicionar_editar_container);

        if (empleadoAdicionarEditarFragment != null) {
            empleadoAdicionarEditarFragment.actualizarFecha(idViewSeleccionadora,year, month, day);
        }
    }


}



