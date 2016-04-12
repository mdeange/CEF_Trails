package com.matthew.ceftrails;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by splatt on 4/11/2016.
 */
public class POI {
    private String name;
    private LatLng coord;

    public POI(String n, double lat, double lng) {
        name = n;
        coord = new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public LatLng getCoord() {
        return coord;
    }
}
