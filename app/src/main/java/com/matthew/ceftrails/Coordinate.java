package com.matthew.ceftrails;

import com.google.android.gms.maps.model.LatLng;

public class Coordinate {
    public LatLng latLng;
    public long  time;

    public Coordinate(LatLng latLng) {
        this.latLng = latLng;
        time = System.currentTimeMillis();
    }
}
