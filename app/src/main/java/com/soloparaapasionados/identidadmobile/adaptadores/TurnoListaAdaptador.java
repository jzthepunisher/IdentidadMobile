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
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Turnos;

/**
 * Created by USUARIO on 13/07/2017.
 */

public class TurnoListaAdaptador extends RecyclerView.Adapter<TurnoListaAdaptador.ViewHolder> {
    private final Context contexto;
    private Cursor items;

    private SparseBooleanArray elementosSeleccionados;
    private static int indiceSeleccionadoActual = -1;

    private TurnoListaAdaptador.OnItemClickListener escucha;

    public interface OnItemClickListener {
        public void onClick(TurnoListaAdaptador.ViewHolder holder, String idTurno, int position);
    }

    private String obtenerIdTurno(int posicion) {
        if (items != null) {
            if (items.moveToPosition(posicion)) {
                return items.getString(items.getColumnIndex(Turnos.ID_TURNO));
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

    public TurnoListaAdaptador(Context contexto, TurnoListaAdaptador.OnItemClickListener escucha) {
        this.contexto = contexto;
        this.escucha = escucha;

        this.elementosSeleccionados = new SparseBooleanArray();
    }

    @Override
    public TurnoListaAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_turno, parent, false);
        return new TurnoListaAdaptador.ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Referencias UI
        public TextView textViewDescripcionTurno;
        public TextView textViewInicioFinTurno;

        public ViewHolder(View v) {
            super(v);
            textViewDescripcionTurno = (TextView) v.findViewById(R.id.textViewDescripcionTurno);
            textViewInicioFinTurno = (TextView) v.findViewById(R.id.textViewInicioFinTurno);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            escucha.onClick(this, obtenerIdTurno(getAdapterPosition()),getAdapterPosition());

        }
    }

    @Override
    public void onBindViewHolder(TurnoListaAdaptador.ViewHolder holder, int position) {
        items.moveToPosition(position);

        String rangoHorarioTurno,descripcionTurno;

        // Asignación UI
        descripcionTurno = items.getString(items.getColumnIndex(Turnos.DESCRIPCION));

        rangoHorarioTurno=" Inicio :";
        rangoHorarioTurno += " " + items.getString(items.getColumnIndex(Turnos.HORA_INICIO));
        rangoHorarioTurno += " | Fin :";
        rangoHorarioTurno += " " + items.getString(items.getColumnIndex(Turnos.HORA_FIN));

        holder.textViewDescripcionTurno.setText(descripcionTurno);
        holder.textViewInicioFinTurno.setText(rangoHorarioTurno);

        // change the row state to activated
        //holder.itemView.setActivated(elementosSeleccionados.get(position, false));
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
