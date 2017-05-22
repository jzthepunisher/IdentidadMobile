package com.soloparaapasionados.identidadmobile.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioLocal.EmpleadoServicioLocal;
import com.soloparaapasionados.identidadmobile.dialogos.SimpleDialog;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoDetalleFragment;
import com.soloparaapasionados.identidadmobile.fragmentos.EmpleadoListadoFragment;

public class EmpleadoDetalleActivity extends AppCompatActivity
implements SimpleDialog.OnSimpleDialogListener {
    private String idEmpleado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empleado_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idEmpleado = getIntent().getStringExtra(EmpleadoListadoActivity.EXTRA_ID_EMPLEADO);

        EmpleadoDetalleFragment empleadoDetalleFragment = (EmpleadoDetalleFragment)
                getSupportFragmentManager().findFragmentById(R.id.empleado_detalle_container);

        if (empleadoDetalleFragment == null) {
            empleadoDetalleFragment = EmpleadoDetalleFragment.newInstance(idEmpleado);
            getSupportFragmentManager().beginTransaction().add(R.id.empleado_detalle_container, empleadoDetalleFragment)
                    .commit();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                // Obtención del manejador de fragmentos
                FragmentManager fragmentManager = getSupportFragmentManager();
                new SimpleDialog().show(fragmentManager, "SimpleDialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void eliminarEmpleadoLocalmente(){
        Intent intent = new Intent(this, EmpleadoServicioLocal.class);
        intent.setAction(EmpleadoServicioLocal.ACCION_ELIMINAR_EMPLEADO_ISERVICE);
        intent.putExtra(EmpleadoServicioLocal.EXTRA_ID_EMPLEADO, idEmpleado);
        startService(intent);
    }


    @Override
    public void onPossitiveButtonClick() {
        eliminarEmpleadoLocalmente();
        muestraPantallaListadoEmpleado();
    }

    @Override
    public void onNegativeButtonClick() {
        Toast.makeText(
                this,
                "Botón Negativo Pulsado",
                Toast.LENGTH_LONG)
                .show();
    }


    private void muestraPantallaListadoEmpleado () {
        setResult(Activity.RESULT_OK );
        finish();
    }
}
