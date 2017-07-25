package com.soloparaapasionados.identidadmobile.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Actividades;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacionEjecucionInicioTerminoActividad;

/**
 * Created by USUARIO on 22/07/2017.
 */

public class OrdenInstalacionEjecucionInicioTerminoAdaptador extends RecyclerView.Adapter<OrdenInstalacionEjecucionInicioTerminoAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;

    private String fechaInicioTerminadoEjecucion;
    private String idOrdenInstalacion;
    private String idActividad;

    private OrdenInstalacionEjecucionInicioTerminoAdaptadorListener listener;


////private SparseBooleanArray elementosSeleccionados;
////private static int indiceSeleccionadoActual = -1;



    public OrdenInstalacionEjecucionInicioTerminoAdaptador(Context contexto, OnItemClickListener escucha,OrdenInstalacionEjecucionInicioTerminoAdaptadorListener listener) {
        this.contexto = contexto;
        this.escucha = escucha;

        this.listener = listener;
////////this.elementosSeleccionados = new SparseBooleanArray();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_orden_instalacion_ejecucion_inicio_termino_actividad, parent, false);
        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Referencias UI
        public TextView textViewFechaInicioTerminadoEjecucion;
        public TextView textViewDescripcionActividad;
        public TextView textViewFechaHoraInicio;
        public TextView textViewDireccionIniciado;
        public TextView textViewFechaHoraTermino;
        public TextView textViewDireccionTerminado;

        public Button buttonActividadIniciada,buttonActividadTerminada;

        public ViewHolder(View v) {
            super(v);

            textViewFechaInicioTerminadoEjecucion= (TextView) v.findViewById(R.id.textViewFechaInicioTerminadoEjecucion);
            textViewDescripcionActividad= (TextView) v.findViewById(R.id.textViewDescripcionActividad);
            textViewFechaHoraInicio= (TextView) v.findViewById(R.id.textViewFechaHoraInicio);
            textViewDireccionIniciado= (TextView) v.findViewById(R.id.textViewDireccionIniciado);
            textViewFechaHoraTermino= (TextView) v.findViewById(R.id.textViewFechaHoraTermino);
            textViewDireccionTerminado= (TextView) v.findViewById(R.id.textViewDireccionTerminado);

            buttonActividadIniciada=(Button)v.findViewById(R.id.buttonActividadIniciada);
            buttonActividadTerminada=(Button)v.findViewById(R.id.buttonActividadTerminada);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            obtenerFechaInicioTerminadoEjecucion(getAdapterPosition());

            escucha.onClick(this, fechaInicioTerminadoEjecucion,idOrdenInstalacion,idActividad,getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        items.moveToPosition(position);

        String s,nombresCompletos;

        // Asignación UI
        holder.textViewFechaInicioTerminadoEjecucion.setText(items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_INICIO_TERMINADO_EJECUCION)));
        holder.textViewDescripcionActividad.setText(items.getString(items.getColumnIndex(Actividades.DESCRIPCION)));
        holder.textViewFechaHoraInicio.setText(items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_INICIO)));
        holder.textViewDireccionIniciado.setText(items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_INICIO)));
        holder.textViewFechaHoraTermino.setText(items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_HORA_TERMINO)));
        holder.textViewDireccionTerminado.setText(items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.DIRECCION_TERMINO)));

        int actividadIniciada=0;
        actividadIniciada= items.getInt(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.INICIADO));
        holder.buttonActividadIniciada.setEnabled(actividadIniciada==1?false:true);

        int actividadTerminada=0;
        actividadTerminada= items.getInt(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.TERMINADO));

        if (actividadIniciada==0){
            actividadTerminada=1;
        }

        holder.buttonActividadTerminada.setEnabled(actividadTerminada==1?false:true);

        // apply click events
        applyClickEvents(holder, position);

    }

    private void applyClickEvents(ViewHolder holder, final int position) {

        holder.buttonActividadIniciada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFechaInicioTerminadoEjecucion(position);

                listener.onButtonActividadIniciada_IT_Clicked(fechaInicioTerminadoEjecucion, idOrdenInstalacion, idActividad, position);
            }
        });

        holder.buttonActividadTerminada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerFechaInicioTerminadoEjecucion(position);

                listener.onButtonActividadTerminada_IT_Clicked(fechaInicioTerminadoEjecucion, idOrdenInstalacion, idActividad, position);
            }
        });
    }

    private Void obtenerFechaInicioTerminadoEjecucion(int posicion)
    {
        if (items != null)
        {
            if (items.moveToPosition(posicion))
            {

                this.fechaInicioTerminadoEjecucion=items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.FECHA_INICIO_TERMINADO_EJECUCION));
                this.idOrdenInstalacion=items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.ID_ORDEN_INSTALACION));
                this.idActividad=items.getString(items.getColumnIndex(OrdenesInstalacionEjecucionInicioTerminoActividad.ID_ACTIVIDAD));
            }
        }
        return null;
    }

    /*public Empleado obtenerEmpleado(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                Empleado empleado=new Empleado(items);
                return empleado;
            }
        }
        return null;
    }*/

    @Override
    public int getItemCount() {
        if (items != null)
            return items.getCount();
        return 0;
    }

    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            items = nuevoCursor;
            notifyDataSetChanged();
        }
    }

    public Cursor getCursor() {
        return items;
    }

    private OnItemClickListener escucha;

    public interface OnItemClickListener
    {
        public void onClick(ViewHolder holder,String fechaInicioTerminadoEjecucion,
                            String idOrdenInstalacion,String idActividad,int position);
    }


    public interface OrdenInstalacionEjecucionInicioTerminoAdaptadorListener {
        void onButtonActividadIniciada_IT_Clicked( String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position);
        void onButtonActividadTerminada_IT_Clicked(String fechaInicioTerminadoEjecucion, String idOrdenInstalacion, String idActividad, int position);
    }
}
