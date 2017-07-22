package com.soloparaapasionados.identidadmobile.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.modelo.OrdenInstalacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposOrdenInstalacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.OrdenesInstalacion;


/**
 * Created by USUARIO on 22/07/2017.
 */

public class OrdenesInstalacionListaAdaptador extends RecyclerView.Adapter<OrdenesInstalacionListaAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;

////private SparseBooleanArray elementosSeleccionados;
    private static int indiceSeleccionadoActual = -1;

    private OnItemClickListener escucha;

    public interface OnItemClickListener {
        public void onClick(ViewHolder holder, String idEmpleado,int position);
    }

    public OrdenesInstalacionListaAdaptador(Context contexto, OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;

////////this.elementosSeleccionados = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_orden_instalacion, parent, false);
        return new ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Referencias UI
        public TextView textViewNombresCliente;
        public TextView textViewFechaEmision;
        public TextView textViewDescripcionTipoOrdenInstalacion;
        public TextView textViewIdOrdenInstalacion;
        public ViewHolder(View v) {
            super(v);
            textViewNombresCliente = (TextView) v.findViewById(R.id.textViewNombresCliente);
            textViewFechaEmision = (TextView) v.findViewById(R.id.textViewFechaEmision);
            textViewDescripcionTipoOrdenInstalacion = (TextView) v.findViewById(R.id.textViewDescripcionTipoOrdenInstalacion);
            textViewIdOrdenInstalacion = (TextView) v.findViewById(R.id.textViewIdOrdenInstalacion);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdOrdenInstalacion(getAdapterPosition()),getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        items.moveToPosition(position);

        String s,nombresCompletos;

        // Asignación UI
        nombresCompletos = items.getString(items.getColumnIndex(Clientes.NOMBRES_CLIENTE));
        nombresCompletos += " " + items.getString(items.getColumnIndex(Clientes.APELLIDO_PATERNO));
        nombresCompletos += " " + items.getString(items.getColumnIndex(Clientes.APELLIDO_MATERNO));

        holder.textViewNombresCliente.setText(nombresCompletos);
        holder.textViewFechaEmision.setText(items.getString(items.getColumnIndex(OrdenesInstalacion.FECHA_EMISION)));
        holder.textViewDescripcionTipoOrdenInstalacion.setText(items.getString(items.getColumnIndex(TiposOrdenInstalacion.DESCRIPCION)));
        holder.textViewIdOrdenInstalacion.setText("Nro: " + items.getString(items.getColumnIndex(OrdenesInstalacion.ID_ORDEN_INSTALACION)));

        // change the row state to activated
////////holder.itemView.setActivated(elementosSeleccionados.get(position, false));
    }

    private String obtenerIdOrdenInstalacion(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                return items.getString(items.getColumnIndex(OrdenesInstalacion.ID_ORDEN_INSTALACION));
            }
        }
        return null;
    }

    public OrdenInstalacion obtenerOrdenInstalacion(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                OrdenInstalacion empleado=new OrdenInstalacion(items);
                return empleado;
            }
        }
        return null;
    }







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

    /*public void cambiaSeleccion(int posicion) {
        indiceSeleccionadoActual = posicion;
        if (elementosSeleccionados.get(posicion, false)) {
            elementosSeleccionados.delete(posicion);
            //animationItemsIndex.delete(posicion);
        } else {
            elementosSeleccionados.put(posicion, true);
            //animationItemsIndex.put(posicion, true);
        }
        notifyItemChanged(posicion);
    }*/

    /*public void buscaIndiceData(String idEmpleado){
        int tamanoData=items.getCount();
        int indiceSeleccionado=-1;
        String idEmpleadoElemento;

        for(int indice=0;indice<tamanoData;indice++) {
            items.moveToPosition(indice);
            idEmpleadoElemento = items.getString(items.getColumnIndex(Empleados.ID_EMPLEADO));
            if (idEmpleadoElemento.equals(idEmpleado)){
                indiceSeleccionado=indice;
                break;
            }
        }

        if (indiceSeleccionado==-1){
            //empleados.add(empleado);
        }else {

            if (elementosSeleccionados.get(indiceSeleccionado, false)) {
                //elementosSeleccionados.delete(indiceSeleccionado);
                elementosSeleccionados.delete(indiceSeleccionado);
            } else {
                elementosSeleccionados.put(indiceSeleccionado, true);
            }

            //notifyDataSetChanged();
            notifyItemChanged(indiceSeleccionado);
        }

    }*/
}
