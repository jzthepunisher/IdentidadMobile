package com.soloparaapasionados.identidadmobile.fragmentos;

import android.app.Activity;
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
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadoSeleccionadoAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosSugerenciaListaAdaptador;
import com.soloparaapasionados.identidadmobile.helper.DividerItemDecoration;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.observadores.MiObervador;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;

public class EmpleadoListadoFragment extends Fragment
        implements EmpleadosListaAdaptador.OnItemClickListener,
        EmpleadosSugerenciaListaAdaptador.OnItemClickListener,
        EmpleadoSeleccionadoAdaptador.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerViewListadoEmpleado;
    private RecyclerView recyclerViewListadoEmpleadoSugerencia;
    private RecyclerView recyclerViewEmpleadoSeleccionados;

    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager linearLayoutManagerHorizontal;

    private EmpleadosListaAdaptador empleadosListaAdaptador;
    private EmpleadosSugerenciaListaAdaptador empleadosListaAdaptadorsSugerencia;
    private EmpleadoSeleccionadoAdaptador empleadoSeleccionadoAdaptador;

    private SwipeRefreshLayout swipeRefreshLayoutEmpleadoListado;
    private FloatingActionButton floatingActionButtonAdicionar;
    private int offSetInicial=0;

    public static final String EXTRA_FILTRO_BUSQUEDA="extra_filtro_busqueda";
    public static final int REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO = 2;
    public static final String ARGUMENTO_IMEI="argumento_imei";
    public String mImei=null;

    private boolean aptoParaCargar;

    public EmpleadoListadoFragment() {
        // Required empty public constructor
    }

    public static EmpleadoListadoFragment newInstance() {
        return new EmpleadoListadoFragment();
    }

    public static EmpleadoListadoFragment newInstance (String imei) {
        EmpleadoListadoFragment fragment = new EmpleadoListadoFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENTO_IMEI, imei);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mImei = getArguments().getString(ARGUMENTO_IMEI);
        }

        setHasOptionsMenu(true);
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

        //Preparar lista de Empleados de Sugerencias
        recyclerViewListadoEmpleadoSugerencia = (RecyclerView) root.findViewById(R.id.recyclerViewListadoEmpleadoSugerencia);
        recyclerViewListadoEmpleadoSugerencia.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewListadoEmpleadoSugerencia.setLayoutManager(linearLayoutManager);

        empleadosListaAdaptadorsSugerencia = new EmpleadosSugerenciaListaAdaptador(getActivity(), this);
        recyclerViewListadoEmpleadoSugerencia.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewListadoEmpleadoSugerencia.setAdapter(empleadosListaAdaptadorsSugerencia);

        //Preparar lista de Empleados de Seleccionados
        recyclerViewEmpleadoSeleccionados = (RecyclerView) root.findViewById(R.id.recyclerViewEmpleadoSeleccionados);
        recyclerViewEmpleadoSeleccionados.setHasFixedSize(true);

        linearLayoutManagerHorizontal = new LinearLayoutManager(getActivity());
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewEmpleadoSeleccionados.setLayoutManager(linearLayoutManagerHorizontal);

        empleadoSeleccionadoAdaptador = new EmpleadoSeleccionadoAdaptador(getActivity(), this);
        recyclerViewEmpleadoSeleccionados.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerViewEmpleadoSeleccionados.setAdapter(empleadoSeleccionadoAdaptador);



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
    public void onClick(EmpleadosListaAdaptador.ViewHolder holder, String idEmpleado,int position) {
        if (mImei!=null){
            Empleado empleado= empleadosListaAdaptador.obtenerEmpleado(position);
            empleadoSeleccionadoAdaptador.adicionarIem(empleado);
            if (empleadoSeleccionadoAdaptador.getItemCount()>0){
                this.recyclerViewEmpleadoSeleccionados.setVisibility(View.VISIBLE);
                //linearLayoutManagerHorizontal.scrollToPositionWithOffset(empleadoSeleccionadoAdaptador.getItemCount(), 0);
                recyclerViewEmpleadoSeleccionados.scrollToPosition(empleadoSeleccionadoAdaptador.getItemCount()-1);
            }else{
                this.recyclerViewEmpleadoSeleccionados.setVisibility(View.GONE);
            }
        }else{
            muestraPantallaDetalle(idEmpleado);
        }
    }

    @Override
    public void onClick(EmpleadosSugerenciaListaAdaptador.ViewHolder holder, String idEmpleado) {
        if (mImei!=null){

        }else{
            muestraPantallaDetalle(idEmpleado);
        }

    }

    @Override
    public void onClick(EmpleadoSeleccionadoAdaptador.ViewHolder holder, String idEmpleado,int position) {
        if (mImei!=null){

        }else{
            muestraPantallaDetalle(idEmpleado);
        }

    }

    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Empleados.crearUriEmpleadoOffSet(String.valueOf(offSetInicial)), null, null, null, null);
            case 2:
                String filtroBusqueda= args.getString(EXTRA_FILTRO_BUSQUEDA);
                return new  CursorLoader(getActivity(), Empleados.crearUriEmpleadoConFiltroBusqueda(filtroBusqueda), null, null, null, null);
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
            case 2:
                if(data!=null){
                    if (empleadosListaAdaptador != null) {
                        empleadosListaAdaptadorsSugerencia.swapCursor(data);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case EmpleadoAdicionarEditarActivity.REQUEST_ADICIONAR_EMPLEADO:
                    muestraMensajeSalvadoSatisfactorio();

                    break;
                case REQUEST_ACTUALIZAR_ELIMINAR_EMPLEADO:
                    muestraMensajeSalvadoSatisfactorio();
                    break;
            }
        }
    }

    private void muestraMensajeSalvadoSatisfactorio() {
        Toast.makeText(getActivity(),
                "Empleado guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    public void muestraSugerenciaEmpleados(){
        this.recyclerViewListadoEmpleadoSugerencia.setVisibility(View.VISIBLE);
        this.recyclerViewListadoEmpleado.setVisibility(View.GONE);
    }

    public void ocultaSugerenciaEmpleados(){
        this.recyclerViewListadoEmpleadoSugerencia.setVisibility(View.GONE);
        this.recyclerViewListadoEmpleado.setVisibility(View.VISIBLE);
    }

    public void filtrarEmpleados(String consultaBusqueda){
        // Iniciar loader
        Bundle bundle=new Bundle();
        bundle.putString(EXTRA_FILTRO_BUSQUEDA,consultaBusqueda);
        getActivity().getSupportLoaderManager().restartLoader(2,bundle,  this);
    }

    public void limpiarSugerenciaEmpleados(){
        // Iniciar loader
        empleadosListaAdaptador.swapCursor(null);
    }

}
