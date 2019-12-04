package com.example.onlineparking;

import androidx.annotation.Keep;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

@Keep
public class MarkerModel implements Serializable {
    private String ParkingArea;
    private GeoPoint geo;

    public MarkerModel(){}

    public String getParkingArea() {
        return ParkingArea;
    }

    public void setParkingArea(String parkingArea) {
        ParkingArea = parkingArea;
    }

    public GeoPoint getGeo() {
        return geo;
    }

    public void setGeo(GeoPoint geo) {
        this.geo = geo;
    }

    public MarkerModel(String parkingArea, GeoPoint geo) {
        ParkingArea = parkingArea;
        this.geo = geo;
    }
}
