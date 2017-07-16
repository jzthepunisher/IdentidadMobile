package com.soloparaapasionados.identidadmobile.helper;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.soloparaapasionados.identidadmobile.modelo.MyItem;
import com.soloparaapasionados.identidadmobile.sqlite.ContratoCotizacion.Clientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by USUARIO on 15/07/2017.
 */

public class MyItemReader {

    /*
     * This matches only once in whole input,
     * so Scanner.next returns whole InputStream as a String.
     * http://stackoverflow.com/a/5445161/2183804
     */
    private static final String REGEX_INPUT_BOUNDARY_BEGINNING = "\\A";

    public List<MyItem> read(InputStream inputStream) throws JSONException {
        List<MyItem> items = new ArrayList<MyItem>();
        String json = new Scanner(inputStream).useDelimiter(REGEX_INPUT_BOUNDARY_BEGINNING).next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            String title = null;
            String snippet = null;
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            if (!object.isNull("title")) {
                title = object.getString("title");
            }
            if (!object.isNull("snippet")) {
                snippet = object.getString("snippet");
            }
            items.add(new MyItem(lat, lng, title, snippet));
        }
        return items;
    }

    public List<MyItem> leeClientes(Cursor cursorClientes)  {
        List<MyItem> items = new ArrayList<MyItem>();

        //data.moveToFirst();
        while(cursorClientes.moveToNext()){
            double lat = Double.valueOf(cursorClientes.getString(cursorClientes.getColumnIndex(Clientes.LATITUD_CLIENTE)));
            double lng = Double.valueOf(cursorClientes.getString(cursorClientes.getColumnIndex(Clientes.LONGITUD_CLIENTE)));
            String title = cursorClientes.getString(cursorClientes.getColumnIndex(Clientes.ID_CLIENTE));
            String snippet = cursorClientes.getString(cursorClientes.getColumnIndex(Clientes.NOMBRES_CLIENTE));

            items.add(new MyItem(lat, lng, title, snippet));
        }


        return items;
    }

    public ArrayList<LatLng> leeClientesMapaTermico(Cursor cursorClientes)  {
        ArrayList<LatLng> items = new ArrayList<LatLng>();

        while(cursorClientes.moveToNext()){
            double lat = Double.valueOf(cursorClientes.getString(cursorClientes.getColumnIndex(Clientes.LATITUD_CLIENTE)));
            double lng = Double.valueOf(cursorClientes.getString(cursorClientes.getColumnIndex(Clientes.LONGITUD_CLIENTE)));

            items.add(new LatLng(lat, lng));
        }

        return items;
    }

}
