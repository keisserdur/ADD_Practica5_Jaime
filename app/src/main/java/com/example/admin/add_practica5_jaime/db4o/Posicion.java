package com.example.admin.add_practica5_jaime.db4o;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

/**
 * Created by Admin on 05/03/2016.
 */
public class Posicion implements Parcelable {
    private double accuracy,altitude,latitude,longitude;
    private String provider;
    private LatLng coordenada;
    private Date date;

    public Posicion() {
    }

    public Posicion(double accuracy, double altitude, double latitude, double longitude, String provider,Date date) {
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.date=date;

        coordenada=new LatLng(latitude,longitude);
    }

    protected Posicion(Parcel in) {
        accuracy = in.readDouble();
        altitude = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
        provider = in.readString();
        coordenada = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<Posicion> CREATOR = new Creator<Posicion>() {
        @Override
        public Posicion createFromParcel(Parcel in) {
            return new Posicion(in);
        }

        @Override
        public Posicion[] newArray(int size) {
            return new Posicion[size];
        }
    };

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public LatLng getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(LatLng coordenada) {
        this.coordenada = coordenada;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Posicion{" +
                "accuracy=" + accuracy +
                ", altitude=" + altitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", provider=" + provider +
                ", coordenada=" + coordenada +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(accuracy);
        dest.writeDouble(altitude);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(provider);
        dest.writeParcelable(coordenada, flags);
    }
}
