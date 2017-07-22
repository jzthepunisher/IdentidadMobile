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
import com.soloparaapasionados.identidadmobile.modelo.UnidadReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.UnidadesReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.TiposUnidadReaccion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos_UnidadesReaccionUbicacion;
/**
 * Created by USUARIO on 15/07/2017.
 */

public class UnidadReaccionPorTurnoAdaptador extends RecyclerView.Adapter<UnidadReaccionPorTurnoAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;

    private SparseBooleanArray elementosSeleccionados;
    private static int indiceSeleccionadoActual = -1;

    String idUnidadReaccion;
    String descripcionUnidadReaccion;
    Double latitudUnidadReaccion;
    Double longitudUnidadReaccion;
    String direccionUbicacionUnidadReaccion;
    int position;

    private UnidadReaccionPorTurnoAdaptador.OnItemClickListener escucha;

    public interface OnItemClickListener {
        //public void onClick(UnidadReaccionPorTurnoAdaptador.ViewHolder holder, String idTurno,String descripcionTurno,String rangoHorarioTurno, int position);
        public void onClick(UnidadReaccionPorTurnoAdaptador.ViewHolder holder, String idUnidadReaccion,String descripcionUnidadReaccion,Double latitudUnidadReaccion,Double longitudUnidadReaccion, String direccionUbicacionUnidadReaccion,int position);
    }

    private void obtenerUbicacionUnidadReaccion(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                this.idUnidadReaccion= items.getString(items.getColumnIndex(UnidadesReaccion.ID_UNIDAD_REACCION));
                this.descripcionUnidadReaccion=items.getString(items.getColumnIndex(UnidadesReaccion.DESCRIPCION));

                if(items.getString(items.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LATITUD))!=null)
                {
                    this.latitudUnidadReaccion=Double.valueOf(items.getString(items.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LATITUD)));
                }
                else
                {
                    this.latitudUnidadReaccion=null;
                }

                if(items.getString(items.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LONGITUD))!=null){
                    this.longitudUnidadReaccion=Double.valueOf(items.getString(items.getColumnIndex(Turnos_UnidadesReaccionUbicacion.LONGITUD)));
                }
                else
                {
                    this.longitudUnidadReaccion=null;
                }


                String direccion=items.getString(items.getColumnIndex(Turnos_UnidadesReaccionUbicacion.DIRECCION));
                //direccion+=direccion==null?"":direccion;
                this.direccionUbicacionUnidadReaccion=direccion==null|| direccion.isEmpty()?"No existe ubicación asignada":direccion;

                position=posicion;
            }
        }
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

    public UnidadReaccionPorTurnoAdaptador(Context contexto, UnidadReaccionPorTurnoAdaptador.OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;

        this.elementosSeleccionados = new SparseBooleanArray();
    }

    @Override
    public UnidadReaccionPorTurnoAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_unidad_reaccion_ubicacion, parent, false);
        return new UnidadReaccionPorTurnoAdaptador.ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Referencias UI
        public ImageView imageViewFotoTipoUnidadReaccion;
        public TextView textViewDescripcionUnidadReaccion;
        public TextView textViewPlaca;
        public TextView textViewDireccionUbicacion;

        public ViewHolder(View v) {
            super(v);
            imageViewFotoTipoUnidadReaccion = (ImageView) v.findViewById(R.id.imageViewFotoTipoUnidadReaccion);
            textViewDescripcionUnidadReaccion = (TextView) v.findViewById(R.id.textViewDescripcionUnidadReaccion);
            textViewPlaca= (TextView) v.findViewById(R.id.textViewPlaca);
            textViewDireccionUbicacion= (TextView) v.findViewById(R.id.textViewDireccionUbicacion);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            obtenerUbicacionUnidadReaccion(getAdapterPosition());

            escucha.onClick(this, idUnidadReaccion, descripcionUnidadReaccion,latitudUnidadReaccion,longitudUnidadReaccion, direccionUbicacionUnidadReaccion,getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(UnidadReaccionPorTurnoAdaptador.ViewHolder holder, int position) {
        items.moveToPosition(position);

        holder.textViewDescripcionUnidadReaccion.setText(items.getString(items.getColumnIndex(UnidadesReaccion.DESCRIPCION)));
        String placa=items.getString(items.getColumnIndex(UnidadesReaccion.PLACA));
        holder.textViewPlaca.setText(placa);
        String direccion=items.getString(items.getColumnIndex(Turnos_UnidadesReaccionUbicacion.DIRECCION));

        if (direccion!=null)
        {
            if (direccion.isEmpty()){
                holder.textViewDireccionUbicacion.setText("No existe ubicación asignada");
            }
            else
            {
                holder.textViewDireccionUbicacion.setText(direccion);
            }
        }
        else
        {
            holder.textViewDireccionUbicacion.setText("No existe ubicación asignada");
        }


        String fotoTipoUnidadReaccion = items.getString(items.getColumnIndex(TiposUnidadReaccion.FOTO));
        final ImageView imageViewFotoTipoUnidadReaccion = holder.imageViewFotoTipoUnidadReaccion;
        //Glide.with(contexto).load(s).error(R.drawable.ic_account_circle_black_24dp).centerCrop().into(holder.imageViewFotoEmpleado);

        Glide.with(contexto)
                .load(Uri.parse("file:///android_asset/" + fotoTipoUnidadReaccion))
                .asBitmap()
                .error(R.drawable.ic_account_circle_black_24dp)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.imageViewFotoTipoUnidadReaccion) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(contexto.getResources(), resource);
                        drawable.setCircular(true);
                        imageViewFotoTipoUnidadReaccion.setImageDrawable(drawable);
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
}
