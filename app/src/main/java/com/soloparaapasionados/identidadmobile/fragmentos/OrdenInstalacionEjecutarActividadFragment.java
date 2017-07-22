package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soloparaapasionados.identidadmobile.R;

public class OrdenInstalacionEjecutarActividadFragment extends Fragment {

    public static final String ARGUMENTO_ID_ORDEN_INSTALACION="argumento_orden_instalacion";
    public String mIdOrdenInstalacion=null;
    String estadoSincronizacion;

    public OrdenInstalacionEjecutarActividadFragment()
    {
        // Required empty public constructor
    }


    public static OrdenInstalacionEjecutarActividadFragment newInstance (String idOrdenInstalacion)
    {
        OrdenInstalacionEjecutarActividadFragment fragment = new OrdenInstalacionEjecutarActividadFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_ID_ORDEN_INSTALACION, idOrdenInstalacion);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdOrdenInstalacion = getArguments().getString(ARGUMENTO_ID_ORDEN_INSTALACION);
        }

        setHasOptionsMenu(true);
        estadoSincronizacion="activado";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orden_instalacion_ejecutar_actividad, container, false);
    }

}
