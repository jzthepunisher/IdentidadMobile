package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.soloparaapasionados.identidadmobile.ServicioLocal.TurnoServiceLocal;
import com.soloparaapasionados.identidadmobile.adaptadores.SeccionesPagerAdaptador;
import com.soloparaapasionados.identidadmobile.modelo.Turno_UnidadReaccionUbicacion;

public class MapaTermicoAgrupacionFragment extends Fragment  {

    TabLayout tabs;
    ViewPager mViewPager;
    public static Toolbar toolbar;

    public static final String ARGUMENTO_ID_TURNO="argumento_id_turno";
    public static final String ARGUMENTO_DESCRIPCION_TURNO="argumento_descripcion_turno";
    public static final String ARGUMENTO_HORARIO_TURNO="argumento_horario_turno";

    public static String idTurno;
    public static String descripcionTurno;
    public static String rangoHorarioTurno;

    public static String idUnidadReaccion;
    public static String descripcionUnidadReaccion;
    public static double latitudUbicacionUnidadReaccion;
    public static double longitudUbicacionUnidadReaccion;
    public static String direccionUbicacionUnidadReaccion;


    SeccionesPagerAdaptador adapter;

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

        FloatingActionButton floatingActionButtonSalvarUbicacionUnidadReaccion = (FloatingActionButton) v.findViewById(R.id.floatingActionButtonSalvarUbicacionUnidadReaccion);
        floatingActionButtonSalvarUbicacionUnidadReaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actualizarUbicacionUnidadReaccionLocalmente( idTurno, idUnidadReaccion,
                        latitudUbicacionUnidadReaccion,  longitudUbicacionUnidadReaccion,
                        direccionUbicacionUnidadReaccion);
            }
        });

        return v;
    }

    private void configuraViewPager(ViewPager viewPager) {
        adapter = new SeccionesPagerAdaptador(getActivity().getSupportFragmentManager());
        adapter.addFragment(MapaTermicoFragment.newInstance("01","02"), getString(R.string.titulo_mapa_termico));
        adapter.addFragment(MapaAgrupacionFragment.newInstance("02","02"), getString(R.string.titulo_mapa_agrupacion));
        //adapter.addFragment(GridFragment.newInstance(3), getString(R.string.title_section3));
        viewPager.setAdapter(adapter);
    }

    private void actualizarUbicacionUnidadReaccionLocalmente(String idTurno,String idUnidadReaccion,double latitud, double longitud,String message){
        Intent intent = new Intent(getActivity(), TurnoServiceLocal.class);
        intent.setAction(TurnoServiceLocal.ACCION_ACTUALIZAR_TURNO_UNIDAD_REACCION_UBICACION_ISERVICE);
        Turno_UnidadReaccionUbicacion turno_unidadReaccionUbicacion=generarEntidadTurno_UnidadReaccionUbicacion(idTurno,idUnidadReaccion, latitud,  longitud, message);
        intent.putExtra(TurnoServiceLocal.EXTRA_MI_TURNO_UNIDAD_REACCION_UBICACION, turno_unidadReaccionUbicacion);
        getActivity().startService(intent);
    }

    private Turno_UnidadReaccionUbicacion generarEntidadTurno_UnidadReaccionUbicacion(
            String idTurno,String idUnidadReaccion,
            double latitud, double longitud,String direccion){

        Turno_UnidadReaccionUbicacion turno_unidadReaccionUbicacion =new Turno_UnidadReaccionUbicacion();

        turno_unidadReaccionUbicacion.setIdTurno(idTurno);
        turno_unidadReaccionUbicacion.setIdUnidadReaccion(idUnidadReaccion);
        turno_unidadReaccionUbicacion.setLatitud(latitud);
        turno_unidadReaccionUbicacion.setLongitud(longitud);
        turno_unidadReaccionUbicacion.setDireccion(direccion);

        return turno_unidadReaccionUbicacion;
    }

    public void setSubTituloTurno(String idTurno,String descripcionTurno, String rangoHorarioTurno, int position){

        this.idTurno =idTurno==null?"":idTurno;
        this.descripcionTurno=descripcionTurno==null?"":descripcionTurno;
        this.rangoHorarioTurno=rangoHorarioTurno==null?"":rangoHorarioTurno;
        setSubTitulo();

        ((MapaTermicoFragment)adapter.getItem(0)).setIdTurno(this.idTurno);
    }

    public void setSubTituloUbicacionUnidadReaccion(String idUnidadReaccion,String descripcionUnidadReaccion, String direccionUbicacionUnidadReaccion,double latiudUnidadReaccion, double longitudUnidadReaccion, int position){
        this.idUnidadReaccion=idUnidadReaccion==null?"":idUnidadReaccion;
        this.descripcionUnidadReaccion=descripcionUnidadReaccion==null?"":descripcionUnidadReaccion;
        this.direccionUbicacionUnidadReaccion=direccionUbicacionUnidadReaccion==null?"":direccionUbicacionUnidadReaccion;
        this.latitudUbicacionUnidadReaccion=latiudUnidadReaccion;
        this.longitudUbicacionUnidadReaccion=longitudUnidadReaccion;

        setSubTitulo();

        ((MapaTermicoFragment)adapter.getItem(0)).setIdUnidadReaccion(this.idUnidadReaccion,this.latitudUbicacionUnidadReaccion,this.longitudUbicacionUnidadReaccion, this.direccionUbicacionUnidadReaccion);
        ((MapaAgrupacionFragment)adapter.getItem(1)).setIdUnidadReaccion(this.idUnidadReaccion,this.latitudUbicacionUnidadReaccion,this.longitudUbicacionUnidadReaccion, this.direccionUbicacionUnidadReaccion);
    }

    public static void setSubTitulo(){

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
