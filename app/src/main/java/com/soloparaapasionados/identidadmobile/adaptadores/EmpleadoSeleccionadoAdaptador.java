package com.soloparaapasionados.identidadmobile.adaptadores;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by USUARIO on 27/05/2017.
 */

public class EmpleadoSeleccionadoAdaptador extends RecyclerView.Adapter<EmpleadoSeleccionadoAdaptador.ViewHolder> {
    private final Context contexto;
    private List<Empleado> empleados = new ArrayList<>();

    HashMap<String,Integer> elementosSeleccionadosListado ;
    HashMap<String,Integer> elementosSeleccionados ;

    private EmpleadoSeleccionadoAdaptador.OnItemClickListener escucha;

    public interface OnItemClickListener {
        public void onClick(EmpleadoSeleccionadoAdaptador.ViewHolder holder, String idEmpleado,int position);
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
        if (empleados != null) {
            Empleado empleado =empleados.get(posicion);
            if (empleado!=null){
                return empleado.getIdEmpleado();
            }
        }
        return null;
    }

    public EmpleadoSeleccionadoAdaptador(Context contexto, EmpleadoSeleccionadoAdaptador.OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;

        this.elementosSeleccionadosListado = new HashMap<String,Integer>();
        this.elementosSeleccionados= new HashMap<String,Integer>();
    }

    @Override
    public EmpleadoSeleccionadoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_empleado_seleccionado, parent, false);
        return new EmpleadoSeleccionadoAdaptador.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EmpleadoSeleccionadoAdaptador.ViewHolder holder, int position) {
        Empleado empleado= empleados.get(position);

        /*if (!elementosSeleccionados.containsKey(empleado.getIdEmpleado())) {
            elementosSeleccionados.put(empleado.getIdEmpleado(), position);
        }*/

        String s,nombresCompletos;

        // Asignación UI
        nombresCompletos = String.valueOf(position) + ' ' + empleado.getApellidoPaterno();
        nombresCompletos += " " + empleado.getApellidoMaterno();
        nombresCompletos += " " +  empleado.getNombres();

        holder.textViewNombresEmpleado.setText(nombresCompletos);

        s = empleado.getFoto();

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
        if (empleados != null)
            return empleados.size();
        return 0;
    }

    public void swapData(List<Empleado> nuevoEmpleados) {
        if (nuevoEmpleados != null) {
            empleados = nuevoEmpleados;
            notifyDataSetChanged();
        }
    }

    public List<Empleado> getData() {
        return empleados;
    }

    public void adicionarIem( Empleado empleado,int posicionListaEmpleado) {

        buscaIndiceData(empleado);

        notifyDataSetChanged();
    }

    private void buscaIndiceData(Empleado empleado){
        int tamanoData=empleados.size();
        int indiceSeleccionado=-1;

        for(int indice=0;indice<tamanoData;indice++) {
            Empleado empleadoElemento = empleados.get(indice);
            if (empleadoElemento.getIdEmpleado().equals(empleado.getIdEmpleado())){
                indiceSeleccionado=indice;
               break;
            }
        }

        if (indiceSeleccionado==-1){
            empleados.add(empleado);
        }else {
            empleados.remove(indiceSeleccionado);
        }


    }

}
