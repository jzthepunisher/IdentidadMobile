package com.soloparaapasionados.identidadmobile.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;

/**
 * Created by USUARIO on 19/05/2017.
 */

public class EmpleadosListaAdaptador extends RecyclerView.Adapter<EmpleadosListaAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;

    private OnItemClickListener escucha;

    public interface OnItemClickListener {
        public void onClick(ViewHolder holder, String idEmpleado);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Referencias UI
        public TextView textViewNombresEmpleado;
        public ImageView imageViewFotoEmpleado;

        public ViewHolder(View v) {
            super(v);
            textViewNombresEmpleado = (TextView) v.findViewById(R.id.textViewNombresEmpleado);
            imageViewFotoEmpleado = (ImageView) v.findViewById(R.id.imageViewFotoEmpleado);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdEmpleado(getAdapterPosition()));
        }
    }

    private String obtenerIdEmpleado(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                return items.getString(items.getColumnIndex(Empleados.ID_EMPLEADO));
            }
        }
        return null;
    }

    public EmpleadosListaAdaptador(Context contexto, OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empleado, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        items.moveToPosition(position);

        String s;

        // Asignación UI
        s = items.getString(items.getColumnIndex(Empleados.NOMBRES));
        holder.textViewNombresEmpleado.setText(s);

        s = items.getString(items.getColumnIndex(Empleados.FOTO));
        Glide.with(contexto).load(s).centerCrop().into(holder.imageViewFotoEmpleado);
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
}
