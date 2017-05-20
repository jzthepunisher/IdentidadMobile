package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;


public class EmpleadoListadoFragment extends Fragment
        implements EmpleadosListaAdaptador.OnItemClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerViewListadoEmpleado;
    private LinearLayoutManager linearLayoutManager;
    private EmpleadosListaAdaptador empleadosListaAdaptador;

    public EmpleadoListadoFragment() {
        // Required empty public constructor
    }

    public static EmpleadoListadoFragment newInstance() {
        return new EmpleadoListadoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_empleado_listado, container, false);


        // Preparar lista
        recyclerViewListadoEmpleado = (RecyclerView) root.findViewById(R.id.recyclerViewListadoEmpleado);
        recyclerViewListadoEmpleado.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoEmpleado.setLayoutManager(linearLayoutManager);

        empleadosListaAdaptador = new EmpleadosListaAdaptador(getActivity(), this);
        recyclerViewListadoEmpleado.setAdapter(empleadosListaAdaptador);

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onClick(EmpleadosListaAdaptador.ViewHolder holder, String idEmpleado) {
        Toast.makeText(getActivity(),":id = " + idEmpleado,Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Empleados.URI_CONTENIDO, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Creando Adaptador para CargoSpinner


        switch (loader.getId()){
            case 1:
                if(data!=null){
                    if (empleadosListaAdaptador != null) {
                        empleadosListaAdaptador.swapCursor(data);
                    }
                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
