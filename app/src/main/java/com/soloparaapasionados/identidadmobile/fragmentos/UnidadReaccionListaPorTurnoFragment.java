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
import android.widget.Toast;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.actividades.UnidadReaccionAsignacionActivity;
import com.soloparaapasionados.identidadmobile.adaptadores.EmpleadosListaAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.TurnoListaAdaptador;
import com.soloparaapasionados.identidadmobile.adaptadores.UnidadReaccionPorTurnoAdaptador;
import com.soloparaapasionados.identidadmobile.modelo.Turno_UnidadReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos_UnidadesReaccionUbicacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UnidadesReaccion;


public class UnidadReaccionListaPorTurnoFragment extends Fragment
        implements TurnoListaAdaptador.OnItemClickListener,
                    UnidadReaccionPorTurnoAdaptador.OnItemClickListener,
                    LoaderManager.LoaderCallbacks<Cursor>{

    private RecyclerView recyclerViewTurnos;
    private LinearLayoutManager linearLayoutManager;
    private TurnoListaAdaptador turnoListaAdaptador;

    private RecyclerView recyclerViewUbicacionUnidadReaccionPorTurno;
    //private LinearLayoutManager linearLayoutManager;
    private UnidadReaccionPorTurnoAdaptador unidadReaccionPorTurnoAdaptador;
    private TextView textViewCantidadTurnosDisponibles;

    private String ARGUMENTO_ID_TURNO="argumento_id_turno";

    String idTurno;
    String descripcionTurno;
    String rangoHorarioTurno;
    int position;

    String idUnidadReaccion;
    String descripcionUnidadReaccion;
    Double latitudUnidadReaccion;
    Double longitudUnidadReaccion;
    String direccionUbicacionUnidadReaccion;

    String estadoSincronizacion="activado";

    private OnTurnoItemClickFragmentoListener onTurnoItemClickFragmentoListener;
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


        // Preparar lista de Turnos
        recyclerViewTurnos = (RecyclerView) root.findViewById(R.id.recyclerViewTurnos);
        recyclerViewTurnos.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTurnos.setLayoutManager(linearLayoutManager);

        turnoListaAdaptador = new TurnoListaAdaptador(getActivity(), this);
        recyclerViewTurnos.setAdapter(turnoListaAdaptador);

        // Preparar lista las Ubicaciones de Unidades de Reaccion por Turno
        recyclerViewUbicacionUnidadReaccionPorTurno = (RecyclerView) root.findViewById(R.id.recyclerViewUbicacionUnidadReaccionPorTurno);
        recyclerViewUbicacionUnidadReaccionPorTurno.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewUbicacionUnidadReaccionPorTurno.setLayoutManager(linearLayoutManager);

        unidadReaccionPorTurnoAdaptador = new UnidadReaccionPorTurnoAdaptador(getActivity(), this);
        recyclerViewUbicacionUnidadReaccionPorTurno.setAdapter(unidadReaccionPorTurnoAdaptador);
        //
        textViewCantidadTurnosDisponibles=(TextView)root.findViewById(R.id.textViewCantidadTurnosDisponibles);
        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);

        return root;
    }

    @Override
    public void onClick(TurnoListaAdaptador.ViewHolder holder, String idTurno,String descripcionTurno,String rangoHorarioTurno, int position) {
        //Toast.makeText(getActivity()," Hola " + idTurno + "position : " + position,Toast.LENGTH_SHORT).show();

        if (UnidadReaccionAsignacionActivity.patronMasterDetalle==true){
            cargarTurno(idTurno, descripcionTurno, rangoHorarioTurno, position);
        }

        Bundle bundle= new Bundle();
        bundle.putString(ARGUMENTO_ID_TURNO,idTurno);
        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(2, bundle,  this);
    }

    @Override
    public void onClick(UnidadReaccionPorTurnoAdaptador.ViewHolder holder, String idUnidadReaccion,String descripcionUnidadReaccion,Double latitudUnidadReaccion, Double longitudUnidadReaccion, String direccionUbicacionUnidadReaccion, int position) {

        if (UnidadReaccionAsignacionActivity.patronMasterDetalle==true){
            cargarUbicacionUnidadReaccion(idUnidadReaccion, descripcionUnidadReaccion, direccionUbicacionUnidadReaccion,latitudUnidadReaccion==null?0.0:latitudUnidadReaccion,longitudUnidadReaccion==null?0.0:longitudUnidadReaccion, position);
        }

        //Toast.makeText(getActivity()," Hola 2 " + idTurno + " Unidad Reaccion :" + idUnidadReaccion +  "position : " + position,Toast.LENGTH_SHORT).show();
    }
    //Métodos implementados de la interface de comunicación LoaderManager.LoaderCallbacks<Cursor>
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Turnos.crearUriTurnoLista(estadoSincronizacion), null, null, null, null);
            case 2:
                idTurno = args.getString(ARGUMENTO_ID_TURNO);
                return new CursorLoader(getActivity(), Turnos.crearUriTurno_UnidadesReaccionUbicacion(idTurno,estadoSincronizacion), null, null, null, null);

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

                        textViewCantidadTurnosDisponibles.setText(String.valueOf(turnoListaAdaptador.getItemCount()));

                        data.moveToFirst();

                        idTurno = data.getString(data.getColumnIndex(Turnos.ID_TURNO));
                        descripcionTurno = data.getString(data.getColumnIndex(Turnos.DESCRIPCION));
                        rangoHorarioTurno=" Inicio :";
                        rangoHorarioTurno += " " + data.getString(data.getColumnIndex(Turnos.HORA_INICIO));
                        rangoHorarioTurno += " | Fin :";
                        rangoHorarioTurno += " " + data.getString(data.getColumnIndex(Turnos.HORA_FIN));
                        position=0 ;
                        cargarTurno(idTurno, descripcionTurno, rangoHorarioTurno, position);

                        Bundle bundle= new Bundle();
                        bundle.putString(ARGUMENTO_ID_TURNO,idTurno);
                        // Iniciar loader
                        getActivity().getSupportLoaderManager().restartLoader(2, bundle,  this);
                    }
                }
                break;
            case 2:
                if(data!=null)
                {
                    if (unidadReaccionPorTurnoAdaptador != null && data.getCount()>0) {
                        unidadReaccionPorTurnoAdaptador.swapCursor(data);

                        data.moveToFirst();

                        idUnidadReaccion=data.getString(data.getColumnIndex(UnidadesReaccion.ID_UNIDAD_REACCION));
                        descripcionUnidadReaccion=data.getString(data.getColumnIndex(UnidadesReaccion.DESCRIPCION));

                        if(data.getString(data.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LATITUD))!=null){
                            latitudUnidadReaccion=Double.valueOf(data.getString(data.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LATITUD)));
                        }
                        else
                        {
                            latitudUnidadReaccion=null;
                        }

                        if(data.getString(data.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LONGITUD))!=null)
                        {
                            longitudUnidadReaccion=Double.valueOf(data.getString(data.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LONGITUD)));
                        }else{
                            longitudUnidadReaccion=null;
                        }

                        String direccion=data.getString(data.getColumnIndex(Turnos_UnidadesReaccionUbicacion.DIRECCION));

                        /*if (direccion!=null)
                        {*/
                            direccionUbicacionUnidadReaccion=direccion==null||direccion.isEmpty() ?"No existe ubicación asignada":direccion;
                       /* }
                        else
                        {
                            direccionUbicacionUnidadReaccion="No existe ubicación asignada";
                        }*/


                        position=0 ;

                        cargarUbicacionUnidadReaccion(idUnidadReaccion, descripcionUnidadReaccion,  direccionUbicacionUnidadReaccion,latitudUnidadReaccion, longitudUnidadReaccion,position);
                    }
                }
                estadoSincronizacion="desactivado";
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTurnoItemClickFragmentoListener) {
            onTurnoItemClickFragmentoListener = (OnTurnoItemClickFragmentoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " debes implementar EscuchaFragmento");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onTurnoItemClickFragmentoListener = null;
    }

    public void cargarTurno(String idTurno,String descripcionTurno, String rangoHorarioTurno, int position) {
        if (onTurnoItemClickFragmentoListener != null) {
            onTurnoItemClickFragmentoListener.OnTurnoItemFragmentoClick(idTurno,descripcionTurno, rangoHorarioTurno, position);
        }
    }

    public void cargarUbicacionUnidadReaccion(String idUnidadReaccion,String descripcionUnidadReaccion, String direccionUbicacionUnidadReaccion,double latiudUnidadReaccion,double longitudUnidadReaccion, int position) {
        if (onTurnoItemClickFragmentoListener != null) {
            onTurnoItemClickFragmentoListener.OnUbicacionUnidadReaccionItemFragmentoClick(idUnidadReaccion, descripcionUnidadReaccion, direccionUbicacionUnidadReaccion, latiudUnidadReaccion, longitudUnidadReaccion, position);
        }
    }

    public interface OnTurnoItemClickFragmentoListener {
        public void OnTurnoItemFragmentoClick( String idTurno,String descripcionTurno, String rangoHorarioTurno, int position);
        public void OnUbicacionUnidadReaccionItemFragmentoClick( String idUnidadReaccion,String descripcionUnidadReaccion, String direccionUbicacionUnidadReaccion,double latiudUnidadReaccion,double longitudUnidadReaccion, int position);
    }
}
