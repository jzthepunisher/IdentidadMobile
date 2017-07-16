package com.soloparaapasionados.identidadmobile.fragmentos;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.helper.MyItemReader;
import com.soloparaapasionados.identidadmobile.modelo.MyItem;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;


public class MapaAgrupacionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap googleMap;
    MapView mMapView;
    private ClusterManager<MyItem> mClusterManager;

    public MapaAgrupacionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapaAgrupacionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapaAgrupacionFragment newInstance(String param1, String param2) {
        MapaAgrupacionFragment fragment = new MapaAgrupacionFragment();
        Bundle args = new Bundle();

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
        View view= inflater.inflate(R.layout.fragment_mapa_agrupacion, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                //asignaListener();

                startDemo();
                //mMap.addMarker(new MarkerOptions().position(new LatLng(-12.066886, -77.033745)).title("Marker"));

                // changeCamera(CameraUpdateFactory.newCameraPosition(ESTADIO_NACIONAL));



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

        return view;
    }


    protected void startDemo() {
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        mClusterManager = new ClusterManager<MyItem>(getActivity(), googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);

        readItems();

    }

    private void readItems()  {
        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);
    }
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 1:
                return new CursorLoader(getActivity(), Clientes.crearUriClienteListadoConFiltroMonitoreoActivo(true), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 1:
                if(data!=null)
                {
                    if ( data.getCount()>0) {

                        //data.moveToFirst();
                        while(data.moveToNext()){
                            List<MyItem> items = new MyItemReader().leeClientes(data);
                            mClusterManager.addItems(items);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
/////////////////////////////////////////////////////////////////////////////////////////
}
