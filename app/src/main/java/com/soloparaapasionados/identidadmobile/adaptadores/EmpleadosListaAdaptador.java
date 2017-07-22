package com.soloparaapasionados.identidadmobile.adaptadores;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.modelo.Empleado;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Empleados;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Cargos;

/**
 * Created by USUARIO on 19/05/2017.
 */

public class EmpleadosListaAdaptador extends RecyclerView.Adapter<EmpleadosListaAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;

    private SparseBooleanArray elementosSeleccionados;
    private static int indiceSeleccionadoActual = -1;

    private OnItemClickListener escucha;

    public interface OnItemClickListener {
        public void onClick(ViewHolder holder, String idOrdenInstacion,int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Referencias UI
        public TextView textViewNombresEmpleado;
        public TextView textViewDescripcionCargoEmpleado;
        public ImageView imageViewFotoEmpleado;

        public ViewHolder(View v) {
            super(v);
            textViewNombresEmpleado = (TextView) v.findViewById(R.id.textViewNombresEmpleado);
            imageViewFotoEmpleado = (ImageView) v.findViewById(R.id.imageViewFotoEmpleado);
            textViewDescripcionCargoEmpleado = (TextView) v.findViewById(R.id.textViewDescripcionCargoEmpleado);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdEmpleado(getAdapterPosition()),getAdapterPosition());

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

    public Empleado obtenerEmpleado(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                Empleado empleado=new Empleado(items);
                return empleado;
            }
        }
        return null;
    }

    public EmpleadosListaAdaptador(Context contexto, OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;

        this.elementosSeleccionados = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empleado, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        items.moveToPosition(position);

        String s,nombresCompletos;

        // Asignación UI
        nombresCompletos = items.getString(items.getColumnIndex(Empleados.NOMBRES));
        nombresCompletos += " " + items.getString(items.getColumnIndex(Empleados.APELLIDO_PATERNO));
        nombresCompletos += " " + items.getString(items.getColumnIndex(Empleados.APELLIDO_MAERNO));

        holder.textViewNombresEmpleado.setText(nombresCompletos);

        holder.textViewDescripcionCargoEmpleado.setText(items.getString(items.getColumnIndex(Cargos.DESCRIPCION)));

        // change the row state to activated
        holder.itemView.setActivated(elementosSeleccionados.get(position, false));

        s = items.getString(items.getColumnIndex(Empleados.FOTO));

        final ImageView imageViewFotoEmpleado = holder.imageViewFotoEmpleado;
        //Glide.with(contexto).load(s).error(R.drawable.ic_account_circle_black_24dp).centerCrop().into(holder.imageViewFotoEmpleado);

        Glide.with(contexto)
            .load(Uri.parse("file:///android_asset/" + s))
            .asBitmap()
            .error(R.drawable.ic_account_circle_black_24dp)
            .centerCrop()
            .into(new BitmapImageViewTarget(holder.imageViewFotoEmpleado) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(contexto.getResources(), resource);
                    drawable.setCircular(true);
                    imageViewFotoEmpleado.setImageDrawable(drawable);
                }
            });

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

    public void cambiaSeleccion(int posicion) {
        indiceSeleccionadoActual = posicion;
        if (elementosSeleccionados.get(posicion, false)) {
            elementosSeleccionados.delete(posicion);
            //animationItemsIndex.delete(posicion);
        } else {
            elementosSeleccionados.put(posicion, true);
            //animationItemsIndex.put(posicion, true);
        }
        notifyItemChanged(posicion);
    }

    public void buscaIndiceData(String idEmpleado){
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

    }
}
