package com.soloparaapasionados.identidadmobile.fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.adaptadores.OrdenInstalacionEjecucionActividadAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.OrdenInstalacionEjecucionInicioTerminoAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionActividad;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionInicioTerminoActividad;

public class OrdenInstalacionEjecutarActividadFragment extends Fragment
        implements OrdenInstalacionEjecucionInicioTerminoAdaptador.OnItemClickListener,
        OrdenInstalacionEjecucionActividadAdaptador.OnItemClickListener,
        OrdenInstalacionEjecucionInicioTerminoAdaptador.OrdenInstalacionEjecucionInicioTerminoAdaptadorListener,
        OrdenInstalacionEjecucionActividadAdaptador.OrdenInstalacionEjecucionActividadListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = OrdenInstalacionEjecutarActividadFragment.class.getSimpleName();

    public static final String ARGUMENTO_ID_ORDEN_INSTALACION = "argumento_orden_instalacion";
    public String mIdOrdenInstalacion = null;
    String estadoSincronizacion;

    RecyclerView recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad;
    LinearLayoutManager linearLayoutManager;
    OrdenInstalacionEjecucionInicioTerminoAdaptador ordenInstalacionEjecucionInicioTerminoAdaptador;
    TextView textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio;

    RecyclerView recyclerViewListadoOrdenInstalacionEjecucionActividad;
    OrdenInstalacionEjecucionActividadAdaptador ordenInstalacionEjecucionActividadAdaptador;
    TextView textViewListadoOrdenInstalacionEjecucionActividadVacio;

    private OrdenInstalacionEjecutarActividadFragmentListener listener;

    public OrdenInstalacionEjecutarActividadFragment() {
        // Required empty public constructor
    }


    public static OrdenInstalacionEjecutarActividadFragment newInstance(String idOrdenInstalacion) {
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
        estadoSincronizacion = "activado";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_orden_instalacion_ejecutar_actividad, container, false);

        // Preparar lista
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad = (RecyclerView) root.findViewById(R.id.recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad);
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setLayoutManager(linearLayoutManager);

        ordenInstalacionEjecucionInicioTerminoAdaptador = new OrdenInstalacionEjecucionInicioTerminoAdaptador(getActivity(), this, this);
        //recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setAdapter(ordenInstalacionEjecucionInicioTerminoAdaptador);

        textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio = (TextView) root.findViewById(R.id.textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio);

        // Preparar lista actividades
        recyclerViewListadoOrdenInstalacionEjecucionActividad = (RecyclerView) root.findViewById(R.id.recyclerViewListadoOrdenInstalacionEjecucionActividad);
        recyclerViewListadoOrdenInstalacionEjecucionActividad.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoOrdenInstalacionEjecucionActividad.setLayoutManager(linearLayoutManager);

        ordenInstalacionEjecucionActividadAdaptador = new OrdenInstalacionEjecucionActividadAdaptador(getActivity(), this, this);
        //recyclerViewListadoOrdenInstalacionEjecucionActividad.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoOrdenInstalacionEjecucionActividad.setAdapter(ordenInstalacionEjecucionActividadAdaptador);

        textViewListadoOrdenInstalacionEjecucionActividadVacio = (TextView) root.findViewById(R.id.textViewListadoOrdenInstalacionEjecucionActividadVacio);


        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null, this);
        getActivity().getSupportLoaderManager().restartLoader(2, null, this);



        return root;

    }

    /////////////////////////////////////////////////// LOADERS
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 1:
                return new CursorLoader(getActivity(), OrdenesInstalacionEjecucionInicioTerminoActividad.crearUriOrdenesInstalacion_InicioTerminoActividadesListado(mIdOrdenInstalacion, estadoSincronizacion), null, null, null, null);
            case 2:
                return new CursorLoader(getActivity(), OrdenesInstalacionEjecucionActividad.crearUriOrdenesInstalacion_ActividadesListado(mIdOrdenInstalacion, estadoSincronizacion), null, null, null, null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Creando Adaptador para CargoSpinner

        switch (loader.getId()) {
            case 1:
                estadoSincronizacion = "desactivado";

                if (data != null) {
                    if (ordenInstalacionEjecucionInicioTerminoAdaptador != null && data.getCount() > 0) {
                        ordenInstalacionEjecucionInicioTerminoAdaptador.swapCursor(data);

                        //swipeRefreshLayoutEmpleadoListado.setRefreshing(false);
                        textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio.setVisibility(View.GONE);
                        recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setVisibility(View.VISIBLE);
                    }
                } else {
                    textViewListadoOrdenesInstacionEjecucionInicioTerminoActividadVacio.setVisibility(View.VISIBLE);
                    recyclerViewListadoOrdenInstalacionEjecucionInicioTerminoActividad.setVisibility(View.GONE);
                }
                break;
            case 2:
                if (data != null) {
                    if (ordenInstalacionEjecucionActividadAdaptador != null && data.getCount() > 0) {
                        ordenInstalacionEjecucionActividadAdaptador.swapCursor(data);

                        textViewListadoOrdenInstalacionEjecucionActividadVacio.setVisibility(View.GONE);
                        recyclerViewListadoOrdenInstalacionEjecucionActividad.setVisibility(View.VISIBLE);
                    }
                } else {
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

    @Override
    public void onButtonActividadIniciadaClicked(int position) {
        Toast.makeText(getActivity(), "onButtonActividadIniciadaClicked : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonActividadTerminadaClicked(int position) {
        Toast.makeText(getActivity(), "onButtonActividadTerminadaClicked : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonActividadIniciada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position) {
        //Toast.makeText(getActivity(), "IT onButtonActividadIniciadaClicked : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
        listener.onButtonActividadIniciada_IT_Clicked(fechaInicioTerminadoEjecucion, idOrdenInstalacion,  idActividad,  position);
    }

    @Override
    public void onButtonActividadTerminada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position) {
        //Toast.makeText(getActivity(), "IT onButtonActividadTerminadaClicked : " + String.valueOf(position), Toast.LENGTH_SHORT).show();
        listener.onButtonActividadTerminada_IT_Clicked( fechaInicioTerminadoEjecucion,  idOrdenInstalacion,  idActividad,  position);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OrdenInstalacionEjecutarActividadFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " debe implementar OrdenInstalacionEjecutarActividadFragmentListener");
        }
    }


    public interface OrdenInstalacionEjecutarActividadFragmentListener {
        void onButtonActividadIniciada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position);
        void onButtonActividadTerminada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position);

    }
}
