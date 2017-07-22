package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.actividades.EmpleadoListadoActivity;
import com.soloparaapasionados.identidadmobile.actividades.OrdenInstalacionEjecutarActividadActivity;
import com.soloparaapasionados.identidadmobile.actividades.OrdenInstalacionListadoActivity;
import com.soloparaapasionados.identidadmobile.adaptadores.OrdenesInstalacionListaAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacion;


public class OrdenInslacionListadoFragment extends Fragment
        implements OrdenesInstalacionListaAdaptador.OnItemClickListener,
                LoaderManager.LoaderCallbacks<Cursor>{

    public static final String ARGUMENTO_ID_ORDEN_INSTALACION="argumento_orden_instalacion";
    public static final String EXTRA_FILTRO_BUSQUEDA="extra_filtro_busqueda";
    public static final int REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO = 2;

    public String mIdOrdenInstalacion=null;

    private RecyclerView recyclerViewListadoOrdenInstalacion;
    private LinearLayoutManager linearLayoutManager;
    private OrdenesInstalacionListaAdaptador ordenesInstalacionListaAdaptador;

    TextView textViewListadoOrdenesInstacionVacio;
    String estadoSincronizacion;

    public OrdenInslacionListadoFragment() {
        // Required empty public constructor
    }

    public static OrdenInslacionListadoFragment newInstance (String idOrdenInstalacion) {
        OrdenInslacionListadoFragment fragment = new OrdenInslacionListadoFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_ID_ORDEN_INSTALACION, idOrdenInstalacion);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIdOrdenInstalacion = getArguments().getString(ARGUMENTO_ID_ORDEN_INSTALACION);
        }

        setHasOptionsMenu(true);
        estadoSincronizacion="activado";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_orden_inslacion_listado, container, false);

        // Preparar lista
        recyclerViewListadoOrdenInstalacion = (RecyclerView) root.findViewById(R.id.recyclerViewListadoOrdenInstalacion);
        recyclerViewListadoOrdenInstalacion.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoOrdenInstalacion.setLayoutManager(linearLayoutManager);

        ordenesInstalacionListaAdaptador = new OrdenesInstalacionListaAdaptador(getActivity(), this);
        recyclerViewListadoOrdenInstalacion.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoOrdenInstalacion.setAdapter(ordenesInstalacionListaAdaptador);

        //Preparar lista de Empleados de Sugerencias
        /*recyclerViewListadoEmpleadoSugerencia = (RecyclerView) root.findViewById(R.id.recyclerViewListadoEmpleadoSugerencia);
        recyclerViewListadoEmpleadoSugerencia.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoEmpleadoSugerencia.setLayoutManager(linearLayoutManager);

        empleadosSugerenciaListaAdaptador = new EmpleadosSugerenciaListaAdaptador(getActivity(), this);
        recyclerViewListadoEmpleadoSugerencia.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoEmpleadoSugerencia.setAdapter(empleadosSugerenciaListaAdaptador);*/

        //Preparar lista de Empleados de Seleccionados
        /*recyclerViewEmpleadoSeleccionados = (RecyclerView) root.findViewById(R.id.recyclerViewEmpleadoSeleccionados);
        recyclerViewEmpleadoSeleccionados.setHasFixedSize(true);

        linearLayoutManagerHorizontal = new LinearLayoutManager(getActivity());
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewEmpleadoSeleccionados.setLayoutManager(linearLayoutManagerHorizontal);

        empleadoSeleccionadoAdaptador = new EmpleadoSeleccionadoAdaptador(getActivity(), this);
        recyclerViewEmpleadoSeleccionados.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewEmpleadoSeleccionados.setItemAnimator(new DefaultItemAnimator());
        recyclerViewEmpleadoSeleccionados.setAdapter(empleadoSeleccionadoAdaptador);*/

        textViewListadoOrdenesInstacionVacio=(TextView)root.findViewById(R.id.textViewListadoOrdenesInstacionVacio);

        /*floatingActionButtonAdicionar = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonAdicionar);
        floatingActionButtonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImei!=null){
                    grabarDispositivoEmpleado(empleadoSeleccionadoAdaptador.getData());
                    cerrarActivity();
                }else{
                    muestraPatallaAdicionarEditar();
                }

            }
        });*/


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


        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);

        return root;

    }

    @Override
    public void onClick(OrdenesInstalacionListaAdaptador.ViewHolder holder, String idOrdenInstacion, int position) {
        muestraPantallaDetalle(idOrdenInstacion);
    }

    private void muestraPantallaDetalle(String idOrdenInstacion) {
        Intent intent = new Intent(getActivity(), OrdenInstalacionEjecutarActividadActivity.class);
        intent.putExtra(OrdenInstalacionListadoActivity.EXTRA_ID_ORDEN_INSTALACION, idOrdenInstacion);
        startActivityForResult(intent, REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO);
    }
    /////////////////////////////////////////////////// LOADERS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 1:
                return new CursorLoader(getActivity(), OrdenesInstalacion.crearUriOrdenInstalacionListado(estadoSincronizacion), null, null, null, null);
            case 2:
                String filtroBusqueda= args.getString(EXTRA_FILTRO_BUSQUEDA);
                return new  CursorLoader(getActivity(), OrdenesInstalacion.crearUriOrdenInstalacionConFiltroBusqueda(filtroBusqueda), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Creando Adaptador para CargoSpinner

        switch (loader.getId()){
            case 1:
                estadoSincronizacion="desactivado";

                if(data!=null)
                {
                    if (ordenesInstalacionListaAdaptador != null && data.getCount()>0) {
                        ordenesInstalacionListaAdaptador.swapCursor(data);

                        //swipeRefreshLayoutEmpleadoListado.setRefreshing(false);
                        textViewListadoOrdenesInstacionVacio.setVisibility(View.GONE);
                        recyclerViewListadoOrdenInstalacion.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    textViewListadoOrdenesInstacionVacio.setVisibility(View.VISIBLE);
                    recyclerViewListadoOrdenInstalacion.setVisibility(View.GONE);
                }
                break;
            case 2:
                if(data!=null){
                    if (ordenesInstalacionListaAdaptador != null) {
/////////////////////////empleadosSugerenciaListaAdaptador.swapCursor(data);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /////////////////////////////////////////////////// LOADERS
}
