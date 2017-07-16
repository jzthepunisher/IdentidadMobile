package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.soloparaapasionados.identidadmobile.R;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.Constants;
import com.soloparaapasionados.identidadmobile.ServicioRemoto.FetchAddressIntentService;
import com.soloparaapasionados.identidadmobile.helper.MyItemReader;
import com.soloparaapasionados.identidadmobile.modelo.MyItem;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion;
//import com.soloparaapasionados.identidadmobile.ServicioRemoto.Cons;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class MapaTermicoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
    GoogleMap.OnMapLongClickListener,GoogleMap.OnMarkerDragListener {
    private GoogleMap googleMap;
    MapView mMapView;

    private HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    private ArrayList<Marker> listaMarkers = new ArrayList<Marker>();



    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;
    /**
     * Represents a geographical location.
     */
    private Location mLastLocation;
    /**
     * The formatted location address.
     */
    private String mAddressOutput;



    public static final CameraPosition ESTADIO_NACIONAL =
            new CameraPosition.Builder().target(new LatLng(-12.066886, -77.033745))
                    .zoom(17.5f)
                    .bearing(300)
                    .tilt(0)
                    .build();
    public MapaTermicoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapaTermicoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapaTermicoFragment newInstance(String param1, String param2) {
        MapaTermicoFragment fragment = new MapaTermicoFragment();

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
        View view= inflater.inflate(R.layout.fragment_mapa_termico, container, false);

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

        return  view;
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

    protected void startDemo() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-25, 143), 4));



            /*mLists.put(getString(R.string.police_stations), new DataSet(readItems(R.raw.police), getString(R.string.police_stations_url)));
            mLists.put(getString(R.string.medicare), new DataSet(readItems(R.raw.medicare),
                    getString(R.string.medicare_url)));*/

            llenaMapa();


        // Make the handler deal with the map
        // Input: list of WeightedLatLngs, minimum and maximum zoom levels to calculate custom
        // intensity from, and the map to draw the heatmap on
        // radius, gradient and opacity not specified, so default are used
    }

    private void llenaMapa(){

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.074459,  -77.027633), 12));

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(3, null,  this);

        // Check if need to instantiate (avoid setData etc twice)
        /*if (mProvider == null) {
            mProvider = new HeatmapTileProvider.Builder().data(
                    mLists.get(getString(R.string.police_stations)).getData()).build();
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


        } else {
            mProvider.setData(mLists.get(R.string.police_stations).getData());
            mOverlay.clearTileCache();
        }*/

    }
////////////////////////////////////////////////////////////////////////
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case 3:
                return new CursorLoader(getActivity(), ContratoCotizacion.Clientes.crearUriClienteListadoConFiltroMonitoreoActivo(true), null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()){
            case 3:
                if(data!=null)
                {
                    if ( data.getCount()>0) {

                        // Check if need to instantiate (avoid setData etc twice)
                        if (mProvider == null) {

                            ArrayList<LatLng> mDataset = new MyItemReader().leeClientesMapaTermico(data);

                            mProvider = new HeatmapTileProvider.Builder().data(mDataset).build();
                            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


                        } else {
                            mProvider.setData(mLists.get(R.string.police_stations).getData());
                            mOverlay.clearTileCache();
                        }


                        //data.moveToFirst();
                        /*while(data.moveToNext()){
                            List<MyItem> items = new MyItemReader().leeClientes(data);
                            mClusterManager.addItems(items);
                        }*/
                    }
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    //////////////////////////////////////////////////////////////////////////////////

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
            }
        }

    }
//////////////////////////////DRAG AND DROP/////////////////////////////
    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
       // onMarkerMoved(marker);
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
                if (existe==false){
                    String holas="dddddddddrrrd";
                    holas+=holas +"ddaa";
                    //Toast.makeText(getActivity(),"Latitud : " + String.valueOf(marker.getPosition().latitude) + " Longitud : " + String.valueOf(marker.getPosition().longitude),Toast.LENGTH_LONG ).show();

                }
                else
                {

                    String hola="ddddd";
                    hola+=hola +"ddaa";
                    marcadorlista=marker;
                    Toast.makeText(getActivity(),"Latitud : " + String.valueOf(marker.getPosition().latitude) + " Longitud : " + String.valueOf(marker.getPosition().longitude),Toast.LENGTH_LONG ).show();
                }
                //return true;
            }
        }
    }
 //////////////////////////////DRAG AND DROP/////////////////////////////

    private class DataSet {
        private ArrayList<LatLng> mDataset;
        private String mUrl;

        public DataSet(ArrayList<LatLng> dataSet, String url) {
            this.mDataset = dataSet;
            this.mUrl = url;
        }

        public ArrayList<LatLng> getData() {
            return mDataset;
        }

        public String getUrl() {
            return mUrl;
        }
    }

    // Datasets from http://data.gov.au
    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        // Iniciar loader
        getActivity().getSupportLoaderManager().restartLoader(1, null,  this);

        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    private  void asignaListener(){
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);
    }

    /**
     * Runs when user clicks the Fetch Address button.
     */
    @SuppressWarnings("unused")
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

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
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
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            //updateUIWidgets();
        }
    }

    /**
     * Updates the address in the UI.
     */
    private void displayAddressOutput() {
        //mLocationAddressTextView.setText(mAddressOutput);
        showToast(mAddressOutput);
    }

    /**
     * Shows a toast with the given text.
     */
    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

}
