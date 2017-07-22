package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soloparaapasionados.identidadmobile.R;


public class OrdenInslacionListadoFragment extends Fragment {

    public static final String ARGUMENTO_ID_ORDEN_INSTALACION="argumento_orden_instalacion";

    public OrdenInslacionListadoFragment() {
        // Required empty public constructor
    }

    public static OrdenInslacionListadoFragment newInstance (String idOrdenInstalacion) {
        OrdenInslacionListadoFragment fragment = new OrdenInslacionListadoFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_ID_ORDEN_INSTALACION, idOrdenInstalacion);
        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orden_inslacion_listado, container, false);
    }

}
