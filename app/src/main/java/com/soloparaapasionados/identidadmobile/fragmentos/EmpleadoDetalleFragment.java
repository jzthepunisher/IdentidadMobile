package com.soloparaapasionados.identidadmobile.fragmentos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoAdicionarEditarActivity;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;

public class EmpleadoDetalleFragment extends Fragment
implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String ARGUMENTO_ID_EMPLEADO = "argumento_id_empleado";

    private String idEmpleado;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imageViewFotoEmpleado;

    private TextView textViewIdEmpleadoValor;
    private TextView textViewNombresEmpleadoValor;
    private TextView textViewApellidoPaternoEmpleadoValor;
    private TextView textViewApellidoMaternoEmpleadoValor;
    private TextView textViewDireccionEmpleadoValor;
    private TextView textViewDniEmpleadoValor;
    private TextView textViewCelularEmpleadoValor;
    private TextView textViewEmailEmpleadoValor;
    private TextView textViewFechaNacimientoEmpleadoValor;
    private TextView textViewDescripionCargoEmpleadoValor;
    private TextView textViewFechaIngresoEmpleadoValor;
    private TextView textViewFechaBajaEmpleadoValor;

   // private LawyersDbHelper mLawyersDbHelper;


    public EmpleadoDetalleFragment() {
        // Required empty public constructor
    }

    public static EmpleadoDetalleFragment newInstance(String idEmpleado) {
        EmpleadoDetalleFragment fragment = new EmpleadoDetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENTO_ID_EMPLEADO, idEmpleado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idEmpleado = getArguments().getString(ARGUMENTO_ID_EMPLEADO);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_empleado_detalle, container, false);

        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsingToolbarLayout);
        imageViewFotoEmpleado = (ImageView) getActivity().findViewById(R.id.imageViewFotoEmpleado);

        textViewIdEmpleadoValor = (TextView) root.findViewById(R.id.textViewIdEmpleadoValor);
        textViewNombresEmpleadoValor = (TextView) root.findViewById(R.id.textViewNombresEmpleadoValor);
        textViewApellidoPaternoEmpleadoValor = (TextView) root.findViewById(R.id.textViewApellidoPaternoEmpleadoValor);
        textViewApellidoMaternoEmpleadoValor = (TextView) root.findViewById(R.id.textViewApellidoMaternoEmpleadoValor);
        textViewDireccionEmpleadoValor = (TextView) root.findViewById(R.id.textViewDireccionEmpleadoValor);
        textViewDniEmpleadoValor = (TextView) root.findViewById(R.id.textViewDniEmpleadoValor);
        textViewCelularEmpleadoValor = (TextView) root.findViewById(R.id.textViewCelularEmpleadoValor);
        textViewEmailEmpleadoValor = (TextView) root.findViewById(R.id.textViewEmailEmpleadoValor);
        textViewFechaNacimientoEmpleadoValor = (TextView) root.findViewById(R.id.textViewFechaNacimientoEmpleadoValor);
        textViewDescripionCargoEmpleadoValor = (TextView) root.findViewById(R.id.textViewDescripionCargoEmpleadoValor);
        textViewFechaIngresoEmpleadoValor = (TextView) root.findViewById(R.id.textViewFechaIngresoEmpleadoValor);
        textViewFechaBajaEmpleadoValor = (TextView) root.findViewById(R.id.textViewFechaBajaEmpleadoValor);

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                muestraPantallaEdicion();
                break;
            case R.id.action_delete:
                //new DeleteLawyerTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void muestraPantallaEdicion() {
        Intent intent = new Intent(getActivity(), EmpleadoAdicionarEditarActivity.class);
        intent.putExtra(EmpleadoAdicionarEditarActivity.EXTRA_ID_EMPLEADO, idEmpleado);
        startActivityForResult(intent, EmpleadoListadoFragment.REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO);
    }
/*




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LawyersFragment.REQUEST_UPDATE_DELETE_LAWYER) {
            if (resultCode == Activity.RESULT_OK) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        }
    }





    private void showLawyersScreen(boolean requery) {
        if (!requery) {
            showDeleteError();
        }
        getActivity().setResult(requery ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private class DeleteLawyerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return mLawyersDbHelper.deleteLawyer(mLawyerId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showLawyersScreen(integer > 0);
        }

    }
*/

    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Empleados.crearUriEmpleado(idEmpleado), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()){
            case 1:
                if (data != null && data.moveToLast()) {
                    muestraEmpleado(data);
                } else {
                    muestraErrorCarga();
                }

                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void muestraEmpleado(Cursor cursorEmpleado){

        String nombresCompletos="";
        nombresCompletos+=cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.NOMBRES));
        nombresCompletos+=" " + cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_PATERNO));
        nombresCompletos+=" " + cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_MAERNO));

        collapsingToolbarLayout.setTitle(nombresCompletos);
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/" + cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FOTO))))
                .centerCrop()
                .into(imageViewFotoEmpleado);

        textViewIdEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.ID_EMPLEADO)));
        textViewNombresEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.NOMBRES)));
        textViewApellidoPaternoEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_PATERNO)));
        textViewApellidoMaternoEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.APELLIDO_MAERNO)));
        textViewDireccionEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.DIRECCION)));
        textViewDniEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.DNI)));
        textViewCelularEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.CELULAR)));
        textViewEmailEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.EMAIL)));
        textViewFechaNacimientoEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FECHA_NACIMIENTO)));
        textViewDescripionCargoEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Cargos.DESCRIPCION)));
        textViewFechaIngresoEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FECHA_INGRESO)));
        textViewFechaBajaEmpleadoValor.setText(cursorEmpleado.getString(cursorEmpleado.getColumnIndex(Empleados.FECHA_BAJA)));

    }

    private void muestraErrorCarga() {
        Toast.makeText(getActivity(),
                "No se ha cargado información", Toast.LENGTH_SHORT).show();
    }

}
