package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.adaptadores.SeccionesPagerAdaptador;

public class MapaTermicoAgrupacionFragment extends Fragment  {

    TabLayout tabs;
    ViewPager mViewPager;
    Toolbar toolbar;

    public static final String ARGUMENTO_ID_TURNO="argumento_id_turno";
    public static final String ARGUMENTO_DESCRIPCION_TURNO="argumento_descripcion_turno";
    public static final String ARGUMENTO_HORARIO_TURNO="argumento_horario_turno";

    String idTurno;
    String descripcionTurno;
    String rangoHorarioTurno;

    String idUnidadReaccion;
    String descripcionUnidadReaccion;
    String direccionUbicacionUnidadReaccion;

    public MapaTermicoAgrupacionFragment() {
        // Required empty public constructor
    }

    public static MapaTermicoAgrupacionFragment newInstance(String param1, String param2) {
        MapaTermicoAgrupacionFragment fragment = new MapaTermicoAgrupacionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARGUMENTO_ID_TURNO)) {
            // Cargar modelo según el identificador
            idTurno = getArguments().getString(ARGUMENTO_ID_TURNO);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mapa_termico_agrupacion, container, false);

        idTurno =getArguments().getString(MapaTermicoAgrupacionFragment.ARGUMENTO_ID_TURNO);
        descripcionTurno=getArguments().getString(MapaTermicoAgrupacionFragment.ARGUMENTO_DESCRIPCION_TURNO);
        rangoHorarioTurno=getArguments().getString(MapaTermicoAgrupacionFragment.ARGUMENTO_HORARIO_TURNO);

        String subtitulo=" Turno : " + descripcionTurno + " " + rangoHorarioTurno;
        // Toolbar en master-detail
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.inflateMenu(R.menu.menu_detalle_articulo);
            toolbar.setTitle("Clientes Monitoreados Activos");
            //toolbar.setSubtitle("Unidad Reacción: H51 | Turno : Turno Noche ");
            toolbar.setSubtitle(subtitulo);
        }

        // Setear adaptador al viewpager.
        mViewPager = (ViewPager) v.findViewById(R.id.viewPager);
        configuraViewPager(mViewPager);

        tabs = (TabLayout) v.findViewById(R.id.tabLayout);
        tabs.addTab(tabs.newTab().setText("Mapa Termico"));
        tabs.addTab(tabs.newTab().setText("Mapa Agrupacion"));

        // Preparar las pestañas
        TabLayout tabs = (TabLayout) v.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(mViewPager);

       /* ((TextView) v.findViewById(R.id.titulo)).setText(itemDetallado.titulo);
        ((TextView) v.findViewById(R.id.fecha)).setText(itemDetallado.fecha);
        ((TextView) v.findViewById(R.id.contenido)).setText(getText(R.string.lorem));
        Glide.with(this)
                .load(itemDetallado.urlMiniatura)
                .into((ImageView) v.findViewById(R.id.imagen));*/
        return v;
    }

    private void configuraViewPager(ViewPager viewPager) {
        SeccionesPagerAdaptador adapter = new SeccionesPagerAdaptador(getActivity().getSupportFragmentManager());
        adapter.addFragment(MapaTermicoFragment.newInstance("01","02"), getString(R.string.titulo_mapa_termico));
        adapter.addFragment(MapaAgrupacionFragment.newInstance("02","02"), getString(R.string.titulo_mapa_agrupacion));
        //adapter.addFragment(GridFragment.newInstance(3), getString(R.string.title_section3));
        viewPager.setAdapter(adapter);
    }



    public void setSubTituloTurno(String idTurno,String descripcionTurno, String rangoHorarioTurno, int position){

        this.idTurno =idTurno==null?"":idTurno;
        this.descripcionTurno=descripcionTurno==null?"":descripcionTurno;
        this.rangoHorarioTurno=rangoHorarioTurno==null?"":rangoHorarioTurno;

        setSubTitulo();
    }

    public void setSubTituloUbicacionUnidadReaccion(String idUnidadReaccion,String descripcionUnidadReaccion, String direccionUbicacionUnidadReaccion, int position){
        this.idUnidadReaccion=idUnidadReaccion==null?"":idUnidadReaccion;
        this.descripcionUnidadReaccion=descripcionUnidadReaccion==null?"":descripcionUnidadReaccion;
        this.direccionUbicacionUnidadReaccion=direccionUbicacionUnidadReaccion==null?"":direccionUbicacionUnidadReaccion;

        setSubTitulo();
    }



    public void setSubTitulo(){

        /*String subtitulo=" Turno : " + descripcionTurno + " " + rangoHorarioTurno;
        subtitulo+="   " + " Unidad Reacción : " + descripcionUnidadReaccion + " " + direccionUbicacionUnidadReaccion;*/

        String subtitulo= descripcionTurno + " " + rangoHorarioTurno;
        subtitulo+="   " + descripcionUnidadReaccion + " " + direccionUbicacionUnidadReaccion;
        // Toolbar en master-detail
        if (toolbar != null){
            toolbar.inflateMenu(R.menu.menu_detalle_articulo);
            toolbar.setSubtitle(subtitulo);
        }
    }
}
