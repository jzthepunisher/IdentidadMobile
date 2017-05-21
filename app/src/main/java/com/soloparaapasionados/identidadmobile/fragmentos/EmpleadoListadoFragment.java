package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoAdicionarEditarActivity;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoDetalleActivity;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoListadoActivity;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.observadores.MiObervador;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;

public class EmpleadoListadoFragment extends Fragment
        implements EmpleadosListaAdaptador.OnItemClickListener,LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerViewListadoEmpleado;
    private LinearLayoutManager linearLayoutManager;
    private EmpleadosListaAdaptador empleadosListaAdaptador;
    private SwipeRefreshLayout swipeRefreshLayoutEmpleadoListado;
    private FloatingActionButton floatingActionButtonAdicionar;
    private int offSetInicial=0;

    public static final int REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO = 2;
    private boolean aptoParaCargar;

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
        recyclerViewListadoEmpleado.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoEmpleado.setAdapter(empleadosListaAdaptador);

        floatingActionButtonAdicionar = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonAdicionar);
        floatingActionButtonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muestraPatallaAdicionarEditar();
            }
        });

        /*swipeRefreshLayoutEmpleadoListado = (SwipeRefreshLayout) root.findViewById(R.id.SwipeRefreshLayoutEmpleadoListado);
        swipeRefreshLayoutEmpleadoListado.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipeRefreshLayoutEmpleadoListado.setRefreshing(false);
                        //int a;
                        //a=0+1;
                        //new HackingBackgroundTask().execute();
                    }
                }
        );*/

        recyclerViewListadoEmpleado.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    /*if (aptoParaCargar) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i("EmpleadoListadoFragment", " Llegamos al final. visibleItemCount + pastVisibleItems" + visibleItemCount + pastVisibleItems );
                            Log.i("EmpleadoListadoFragment", " Llegamos al final. totalItemCount " + totalItemCount );
                            Log.i("EmpleadoListadoFragment", " Llegamos al final. offSetInicial " + offSetInicial );

                            swipeRefreshLayoutEmpleadoListado.setRefreshing(true);
                            aptoParaCargar = false;
                            offSetInicial += 15;

                            obtenerDatos(offSetInicial);
                        }
                    }*/
                }
            }
        });

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);


        return root;
    }

    private void muestraPatallaAdicionarEditar() {
        Intent intent = new Intent(getActivity(), EmpleadoAdicionarEditarActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    private void obtenerDatos(int offSetInicial) {
        getActivity().getSupportLoaderManager().restartLoader(1, null,this);
    }

    @Override
    public void onClick(EmpleadosListaAdaptador.ViewHolder holder, String idEmpleado) {
        muestraPantallaDetalle(idEmpleado);
    }

    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Empleados.crearUriEmpleadoOffSet(String.valueOf(offSetInicial)), null, null, null, null);
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
                        aptoParaCargar=true;
                        //swipeRefreshLayoutEmpleadoListado.setRefreshing(false);
                    }
                }
                break;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void muestraPantallaDetalle(String idEmpleado) {
        Intent intent = new Intent(getActivity(), EmpleadoDetalleActivity.class);
        intent.putExtra(EmpleadoListadoActivity.EXTRA_ID_EMPLEADO, idEmpleado);
        startActivityForResult(intent, REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO);
    }
}
