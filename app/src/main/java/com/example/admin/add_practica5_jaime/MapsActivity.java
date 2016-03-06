package com.example.admin.add_practica5_jaime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.admin.add_practica5_jaime.db4o.DB4O;
import com.example.admin.add_practica5_jaime.db4o.Posicion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Posicion> posicionList;
    private List<PolylineOptions> polylineOptionsList;
    private DB4O datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startService(new Intent(getBaseContext(), Servicio.class));


    }


    /******************************************************************************/

    private BroadcastReceiver receptor= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receptor, new IntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receptor);
    }

    /******************************************************************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng po=new LatLng(0,0);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(po));


        try {
            datos=new DB4O(this);
            posicionList=datos.getPosicionList();

            PolylineOptions pol=new PolylineOptions();
            pol.width((float) 15);
            pol.color(Color.RED);
            pol.visible(true);

            for (Posicion p : posicionList) {
                pol.add(p.getCoordenada());
            }
            mMap.addPolyline(pol);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(posicionList.get(0).getCoordenada()));

            MarkerOptions mo=new MarkerOptions();
            mo.title("Origen");
            mo.position(posicionList.get(0).getCoordenada());
            mMap.addMarker(mo);

            mo.title("Fin");
            mo.position(posicionList.get(posicionList.size() -1).getCoordenada());
            mMap.addMarker(mo);

            datos.cerrar();
        } catch (IOException e) {
            Log.v("LOGV", "exception");
            e.printStackTrace();
        }
    }
}
