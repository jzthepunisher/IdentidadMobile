package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.Constants;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.FetchAddressIntentService;
import com.soloparaapasionados.identidadmobile.helper.MyItemReader;
import com.soloparaapasionados.identidadmobile.modelo.MyItem;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;

import org.json.JSONException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MapaAgrupacionFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> ,
                    GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerDragListener {

    private GoogleMap googleMap;
    MapView mMapView;
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<Marker> listaMarkers = new ArrayList<Marker>();
    private Location mLastLocation;
    private boolean mAddressRequested;
    private String mAddressOutput;


    String idTurno;
    String idUnidadReaccion;
    Double latitudUnidadReaccion;
    Double longitudUnidadReaccion;
    String direccionUnidadReaccionUbicacion;

    private AddressResultReceiver mResultReceiver;


    public MapaAgrupacionFragment() {
        // Required empty public constructor
    }


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
                asignaListener();

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
        mAddressRequested = false;
        mResultReceiver = new AddressResultReceiver(new Handler());

        return view;
    }


    protected void startDemo() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.074459,  -77.027633), 12));

        mClusterManager = new ClusterManager<MyItem>(getActivity(), googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);

        readItems();

    }

    private  void asignaListener(){
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);
    }

    private void readItems()  {
        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(4, null,  this);
    }
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 4:
                return new CursorLoader(getActivity(), Clientes.crearUriClienteListadoConFiltroMonitoreoActivo(true), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 4:
                if(data!=null)
                {
                    if ( data.getCount()>0) {
                        List<MyItem> items = new MyItemReader().leeClientes(data);
                        mClusterManager.addItems(items);
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        if (listaMarkers.size()<=0){
            Marker marcador =googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Hito X")
                    .draggable(true));

            listaMarkers.add(marcador);

            for (Marker marcadorlista : listaMarkers) {
                //marcadorlista.setVisible(false);
                //marker.remove(); <-- works too!
                marcadorlista.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                fetchAddressButtonHandler();
            }


        }
    }

    public void fetchAddressButtonHandler() {
        for (Marker marcadorlista : listaMarkers) {
            if (marcadorlista.getPosition() != null) {
                mLastLocation=new Location("");
                mLastLocation.setLatitude(marcadorlista.getPosition().latitude);
                mLastLocation.setLongitude(marcadorlista.getPosition().longitude);
                startIntentService();
                return;
            }
        }
        // If we have not yet retrieved the user location, we process the user's request by setting
        // mAddressRequested to true. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
        //updateUIWidgets();
    }

    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        intent.putExtra(FetchAddressIntentService.EXTRA_ID_TURNO,"");
        intent.putExtra(FetchAddressIntentService.EXTRA_ID_UNIDAD_REACCION,"");

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onMarkerMoved(marker);
        fetchAddressButtonHandler();
    }

    private void onMarkerMoved(Marker marker) {
        for (Marker marcadorlista : listaMarkers) {
            if (marker.equals(marcadorlista)) {
                boolean existe=true;
                if (existe==true){
                    marcadorlista=marker;
                }

            }
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                //showToast(getString(R.string.address_found));
                MapaTermicoAgrupacionFragment.direccionUbicacionUnidadReaccion=mAddressOutput;
                MapaTermicoAgrupacionFragment.latitudUbicacionUnidadReaccion=mLastLocation.getLatitude();
                MapaTermicoAgrupacionFragment.longitudUbicacionUnidadReaccion=mLastLocation.getLongitude();
                MapaTermicoAgrupacionFragment.setSubTitulo();
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            //updateUIWidgets();
        }
    }

    private void displayAddressOutput() {
        //mLocationAddressTextView.setText(mAddressOutput);
        showToast(mAddressOutput);
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public void setIdUnidadReaccion(String idUnidadReaccion,Double latitudUnidadReaccion,Double longitudUnidadReaccion,String direccionUnidadReaccionUbicacion){
        this.idUnidadReaccion=idUnidadReaccion==null?"":idUnidadReaccion;
        this.latitudUnidadReaccion=latitudUnidadReaccion;
        this.longitudUnidadReaccion=longitudUnidadReaccion;
        this.direccionUnidadReaccionUbicacion=direccionUnidadReaccionUbicacion;

        if (this.latitudUnidadReaccion!=0.0 && longitudUnidadReaccion !=0.0 ){
            LatLng latLng= new LatLng( this.latitudUnidadReaccion,this.longitudUnidadReaccion);
            if (googleMap!=null){
                AsignarUbicacionUnidadReaccion(latLng);
            }
        }else{
            for (Marker marcadorlista : listaMarkers) {
                marcadorlista.remove();
            }
            listaMarkers.clear();
        }
    }

    private void AsignarUbicacionUnidadReaccion(LatLng latLng){

        for (Marker marcadorlista : listaMarkers) {
            marcadorlista.remove();
        }
        listaMarkers.clear();

        if (listaMarkers.size()<=0){
            Marker marcador =googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Hito X")
                    .draggable(true));

            listaMarkers.add(marcador);

            for (Marker marcadorlista : listaMarkers) {
                //marcadorlista.setVisible(false);
                //marker.remove(); <-- works too!
                marcadorlista.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                //fetchAddressButtonHandler();
            }
        }
    }
}
