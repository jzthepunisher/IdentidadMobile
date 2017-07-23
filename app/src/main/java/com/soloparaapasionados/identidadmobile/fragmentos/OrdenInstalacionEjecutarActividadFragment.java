package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
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
import com.soloparaapasionados.identidadmobile.adaptadores.OrdenInstalacionEjecucionActividadAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.OrdenInstalacionEjecucionInicioTerminoAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionActividad;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionInicioTerminoActividad;

public class OrdenInstalacionEjecutarActividadFragment extends Fragment
    implements OrdenInstalacionEjecucionInicioTerminoAdaptador.OnItemClickListener,
        OrdenInstalacionEjecucionActividadAdaptador.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    public static final String ARGUMENTO_ID_ORDEN_INSTALACION="argumento_orden_instalacion";
    public String mIdOrdenInstalacion=null;
    String estadoSincronizacion;

    RecyclerView recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad;
    LinearLayoutManager linearLayoutManager;
    OrdenInstalacionEjecucionInicioTerminoAdaptador ordenInstalacionEjecucionInicioTerminoAdaptador;
    TextView textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio;

    RecyclerView recyclerViewListadoOrdenInstalacionEjecucionActividad;
    OrdenInstalacionEjecucionActividadAdaptador ordenInstalacionEjecucionActividadAdaptador;
    TextView textViewListadoOrdenInstalacionEjecucionActividadVacio;


    public OrdenInstalacionEjecutarActividadFragment()
    {
        // Required empty public constructor
    }


    public static OrdenInstalacionEjecutarActividadFragment newInstance (String idOrdenInstalacion)
    {
        OrdenInstalacionEjecutarActividadFragment fragment = new OrdenInstalacionEjecutarActividadFragment();

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
        View root =  inflater.inflate(R.layout.fragment_orden_instalacion_ejecutar_actividad, container, false);

               // Preparar lista
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad = (RecyclerView) root.findViewById(R.id.recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad);
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setLayoutManager(linearLayoutManager);

        ordenInstalacionEjecucionInicioTerminoAdaptador = new OrdenInstalacionEjecucionInicioTerminoAdaptador(getActivity(), this);
        //recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setAdapter(ordenInstalacionEjecucionInicioTerminoAdaptador);

        textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio=(TextView)root.findViewById(R.id.textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio);

        // Preparar lista actividades
        recyclerViewListadoOrdenInstalacionEjecucionActividad = (RecyclerView) root.findViewById(R.id.recyclerViewListadoOrdenInstalacionEjecucionActividad);
        recyclerViewListadoOrdenInstalacionEjecucionActividad.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoOrdenInstalacionEjecucionActividad.setLayoutManager(linearLayoutManager);

        ordenInstalacionEjecucionActividadAdaptador = new OrdenInstalacionEjecucionActividadAdaptador(getActivity(), this);
        //recyclerViewListadoOrdenInstalacionEjecucionActividad.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoOrdenInstalacionEjecucionActividad.setAdapter(ordenInstalacionEjecucionActividadAdaptador);

        textViewListadoOrdenInstalacionEjecucionActividadVacio=(TextView)root.findViewById(R.id.textViewListadoOrdenInstalacionEjecucionActividadVacio);




        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);
        getActivity().getSupportLoaderManager().restartLoader(2, null,  this);

        return root;

    }

    /////////////////////////////////////////////////// LOADERS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 1:
                return new CursorLoader(getActivity(), OrdenesInstalacionEjecucionInicioTerminoActividad.crearUriOrdenesInstalacion_InicioTerminoActividadesListado(mIdOrdenInstalacion,estadoSincronizacion), null, null, null, null);
            case 2:
                return new CursorLoader(getActivity(), OrdenesInstalacionEjecucionActividad.crearUriOrdenesInstalacion_ActividadesListado(mIdOrdenInstalacion,estadoSincronizacion), null, null, null, null);

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
                    if (ordenInstalacionEjecucionInicioTerminoAdaptador != null && data.getCount()>0) {
                        ordenInstalacionEjecucionInicioTerminoAdaptador.swapCursor(data);

                        //swipeRefreshLayoutEmpleadoListado.setRefreshing(false);
                        textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio.setVisibility(View.GONE);
                        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio.setVisibility(View.VISIBLE);
                    recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setVisibility(View.GONE);
                }
                break;
            case 2:
                if(data!=null)
                {
                    if (ordenInstalacionEjecucionActividadAdaptador != null && data.getCount()>0) {
                        ordenInstalacionEjecucionActividadAdaptador.swapCursor(data);

                        textViewListadoOrdenInstalacionEjecucionActividadVacio.setVisibility(View.GONE);
                        recyclerViewListadoOrdenInstalacionEjecucionActividad.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    textViewListadoOrdenInstalacionEjecucionActividadVacio.setVisibility(View.VISIBLE);
                    recyclerViewListadoOrdenInstalacionEjecucionActividad.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /////////////////////////////////////////////////// LOADERS

    @Override
    public void onClick(OrdenInstalacionEjecucionInicioTerminoAdaptador.ViewHolder holder, String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position) {

    }

    @Override
    public void onClick(OrdenInstalacionEjecucionActividadAdaptador.ViewHolder holder, String idOrdenInstalacion, String idActividad, int position) {

    }
}
