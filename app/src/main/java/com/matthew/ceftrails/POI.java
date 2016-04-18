package com.matthew.ceftrails;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by splatt on 4/11/2016.
 */
public class POI {
    private String name;
    private String description;
    private String filename;
    private String path;
    private LatLng coord;

    public POI(String n, String d, String f, String p, double lat, double lng) {
        name = n;
        description = d;
        filename = f;
        path = p;
        coord = new LatLng(lat, lng);
    }

    public POI(String breakSeparated) {
        String params[] = breakSeparated.split("<BREAK>");
        name = params[0];
        description = params[1];
        filename = params[2];
        path = params[3];
        coord = new LatLng(Double.parseDouble(params[5]), Double.parseDouble(params[4]));
    }

    public String getName() {
        return name;
    }

    public LatLng getCoord() {
        return coord;
    }

    public String getDescription() {
        return description;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }
}
