package com.soloparaapasionados.identidadmobile.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.soloparaapasionados.identidadmobile.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class MapaTermicoFragment extends Fragment   {

    private GoogleMap googleMap;
    MapView mMapView;

    private HashMap<String, DataSet> mLists = new HashMap<String, DataSet>();
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

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


        try {
            mLists.put(getString(R.string.police_stations), new DataSet(readItems(R.raw.police),
                    getString(R.string.police_stations_url)));
            mLists.put(getString(R.string.medicare), new DataSet(readItems(R.raw.medicare),
                    getString(R.string.medicare_url)));

            llenaMapa();
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }

        // Make the handler deal with the map
        // Input: list of WeightedLatLngs, minimum and maximum zoom levels to calculate custom
        // intensity from, and the map to draw the heatmap on
        // radius, gradient and opacity not specified, so default are used
    }

    private void llenaMapa(){
        // Check if need to instantiate (avoid setData etc twice)
        if (mProvider == null) {
            mProvider = new HeatmapTileProvider.Builder().data(
                    mLists.get(getString(R.string.police_stations)).getData()).build();
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));


        } else {
            mProvider.setData(mLists.get(R.string.police_stations).getData());
            mOverlay.clearTileCache();
        }

    }

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

}
