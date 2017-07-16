package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.soloparaapasionados.identidadmobile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GeoCercaAdicionarEditarDetalleFragment extends Fragment
        implements GoogleMap.OnMapLongClickListener {
    private static final String ARGUMENTO_ID_GEOCERCA = "argumento_id_geocerca";

    private String mIdGeocerca;
    MapView mMapView;
    private GoogleMap googleMap;
    private ArrayList<Marker> listaMarkers = new ArrayList<Marker>();

    FloatingActionButton floatingActionButtonCalcular;
    PolyUtil polyUtil;

    public static final CameraPosition ESTADIO_NACIONAL =
            new CameraPosition.Builder().target(new LatLng(-12.066886, -77.033745))
                    .zoom(17.5f)
                    .bearing(300)
                    .tilt(0)
                    .build();

    public GeoCercaAdicionarEditarDetalleFragment() {
        // Required empty public constructor
    }

    public static GeoCercaAdicionarEditarDetalleFragment newInstance(String idGeocerca) {
        GeoCercaAdicionarEditarDetalleFragment fragment = new GeoCercaAdicionarEditarDetalleFragment();

        Bundle args = new Bundle();
        args.putString(ARGUMENTO_ID_GEOCERCA, idGeocerca);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIdGeocerca = getArguments().getString(ARGUMENTO_ID_GEOCERCA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_geo_cerca_adicionar_editar_detalle, container, false);

        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        floatingActionButtonCalcular = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButtonCalcular);
        // Eventos
        floatingActionButtonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listaMarkers!=null){
                    LatLng estadionacional=new LatLng(-12.066886, -77.033745);
                    Boolean loContiene=PolyUtil.containsLocation(estadionacional, creaPoligono(),false);
                    Toast.makeText(getContext(),"Esta dentro?:" + String.valueOf(loContiene),Toast.LENGTH_LONG).show();
                }

            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                asignaListener();

                mMap.addMarker(new MarkerOptions().position(new LatLng(-12.066886, -77.033745)).title("Marker"));

                changeCamera(CameraUpdateFactory.newCameraPosition(ESTADIO_NACIONAL));
                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                //LatLng sydney = new LatLng(-34, 151);
                //googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {
        //if (mAnimateToggle.isChecked()) {
        //    if (mCustomDurationToggle.isChecked()) {
        //        int duration = mCustomDurationBar.getProgress();
        //        // The duration must be strictly positive so we make it at least 1.
        //        mMap.animateCamera(update, Math.max(duration, 1), callback);
        //    } else {
        //        mMap.animateCamera(update, callback);
        //    }
        //} else {
        googleMap.moveCamera(update);
        //}
    }

    @Override
    public void onMapLongClick(LatLng point) {
        /*// We know the center, let's place the outline at a point 3/4 along the view.
        View view = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getView();
        LatLng radiusLatLng = mMap.getProjection().fromScreenLocation(new Point(
                view.getHeight() * 3 / 4, view.getWidth() * 3 / 4));

        // ok create it
        DraggableCircle circle =
                new DraggableCircle(point, radiusLatLng, mClickabilityCheckbox.isChecked());
        mCircles.add(circle);*/


        Marker marcador =googleMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Hito X")
                .draggable(true));

        listaMarkers.add(marcador);

        for (Marker marcadorlista : listaMarkers) {
            //marcadorlista.setVisible(false);
            //marker.remove(); <-- works too!
            marcadorlista.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }

        if (listaMarkers!=null){
            googleMap.addPolygon(new PolygonOptions()
                    .addAll(creaPoligono())
                    .fillColor(Color.CYAN)
                    .strokeColor(Color.BLUE)
                    .strokeWidth(5));
        }


    }

    private List<LatLng> creaPoligono() {
        List<LatLng> poligono=new ArrayList<>() ;


        if (listaMarkers!=null){
            for (Marker marcadorlista : listaMarkers) {
                //marcadorlista.setVisible(false);
                //marker.remove(); <-- works too!
                poligono.add(marcadorlista.getPosition());
            }
        }

      return poligono;
    }

    private  void asignaListener(){
        googleMap.setOnMapLongClickListener(this);

    }
}
