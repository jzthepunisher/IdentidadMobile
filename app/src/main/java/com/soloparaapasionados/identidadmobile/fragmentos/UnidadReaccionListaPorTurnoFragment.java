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
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.TurnoListaAdaptador;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;



public class UnidadReaccionListaPorTurnoFragment extends Fragment
        implements TurnoListaAdaptador.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerViewTurnos;
    private LinearLayoutManager linearLayoutManager;
    private TurnoListaAdaptador turnoListaAdaptador;

    private String ARGUMENTO_ID_TURNO="argumento_id_turno";
    String idTurno;
    public UnidadReaccionListaPorTurnoFragment() {
        // Required empty public constructor
    }

    public static UnidadReaccionListaPorTurnoFragment newInstance() {
        return new UnidadReaccionListaPorTurnoFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_unidad_reaccion_lista_por_turno, container, false);

        // Preparar lista
        recyclerViewTurnos = (RecyclerView) root.findViewById(R.id.recyclerViewTurnos);
        recyclerViewTurnos.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTurnos.setLayoutManager(linearLayoutManager);

        turnoListaAdaptador = new TurnoListaAdaptador(getActivity(), this);
        recyclerViewTurnos.setAdapter(turnoListaAdaptador);

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);

        return root;
    }

    @Override
    public void onClick(TurnoListaAdaptador.ViewHolder holder, String idTurno, int position) {
        Toast.makeText(getActivity()," Hola " + idTurno + "position : " + position,Toast.LENGTH_SHORT).show();

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(2, null,  this);
    }
    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Turnos.crearUriTurnoLista(), null, null, null, null);
            case 2:
                idTurno = args.getString(ARGUMENTO_ID_TURNO);

                return new CursorLoader(getActivity(), Turnos.crearUriTurno_UnidadesReaccionUbicacion(idTurno), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 1:
                if(data!=null)
                {
                    if (turnoListaAdaptador != null && data.getCount()>0) {
                        turnoListaAdaptador.swapCursor(data);

                        idTurno = data.getString(data.getColumnIndex(Turnos.ID_TURNO));

                        Bundle bundle= new Bundle();
                        bundle.putString(ARGUMENTO_ID_TURNO,idTurno);
                        // Iniciar loader
                        getActivity().getSupportLoaderManager().restartLoader(2, bundle,  this);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    public interface OnTurnoItemClickListener {
        public void onClick( String idTurno, int position);
    }
}
