package com.example.admin.add_practica5_jaime;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.add_practica5_jaime.db4o.DB4O;
import com.example.admin.add_practica5_jaime.db4o.Posicion;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Usuario on 17/02/2016.
 */
public class Servicio extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private final int CTEPLAY = 1;
    private Location ultimaLocalizacion;
    private GoogleApiClient cliente;
    private LocationRequest peticionLocalizaciones;
    private Posicion posicion;
    private DB4O datos;

    public Servicio() {
    }

    /***********************************************************************/

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        Intent i=new Intent(Servicio.this, Servicio.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Notification.Builder constructorNotificacion = new
                Notification.Builder(Servicio.this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("ADD Practica 5")
                .setContentText("Obteniendo posicion")
                .setContentIntent(PendingIntent.getActivity(Servicio.this, 0, i, 0));

        NotificationManager gestorNotificacion = (NotificationManager)
                getSystemService(Servicio.this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startForeground(1, constructorNotificacion.build());
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        posicion=null;
        Log.v("LOGV", "localizacion");
        int status= GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status== ConnectionResult.SUCCESS) {
            Log.v("LOGV","success");
            cliente = new GoogleApiClient.Builder(getBaseContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            cliente.connect();
        }else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            GooglePlayServicesUtil.getErrorDialog(status, (Activity) getBaseContext(), CTEPLAY).show();
            Log.v("ASDF", "elseif");
        } else {
            Toast.makeText(this, "No", Toast.LENGTH_LONG).show();
        }

        return START_REDELIVER_INTENT;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /*****************************************************************************/
    /*****************************************************************************/

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("LOGV","connected");
        if (ActivityCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("LOGV","return");
            return;
        }
        ultimaLocalizacion = LocationServices.
                FusedLocationApi.getLastLocation(cliente);
        if (ultimaLocalizacion != null) {

        }
        Log.v("LOGV","peticion");
        peticionLocalizaciones = new LocationRequest();
        peticionLocalizaciones.setInterval(10000);
        peticionLocalizaciones.setFastestInterval(50000);
        peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(cliente, peticionLocalizaciones, this);
        Log.v("LOGV", "fin peticion");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("LOGV", "suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        posicion=new Posicion(location.getAccuracy(),location.getAltitude(),location.getLatitude(),location.getLongitude(),location.getProvider(), Calendar.getInstance().getTime());
        Log.v("LOGV", posicion.getCoordenada() + "");
        try {
            datos=new DB4O(getBaseContext());
            datos.insert(posicion);
            Log.v("LOGV","insertado");
            datos.cerrar();
        } catch (IOException e) {
            Log.v("LOGV","exception");
            datos.cerrar();
            e.printStackTrace();
        }


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("LOGV", "failed");
    }

    /*****************************************************************/
    public Posicion getPosicion() {
        Log.v("LOGV", posicion.getCoordenada() + "");
        return posicion;
    }
}
