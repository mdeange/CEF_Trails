package com.matthew.ceftrails;

import java.util.ArrayList;

/**
 * Created by splatt on 4/18/2016.
 */
public class Singleton {
    private static Singleton singleton = new Singleton();
    private ArrayList<POI> pois;

    private Singleton() {
        pois = new ArrayList<>();
    }

    public static Singleton getInstance() {
        return singleton;
    }

    public ArrayList<POI> getPois() {
        return pois;
    }

    public void setPois(ArrayList<POI> pois) {
        this.pois = pois;
    }
}
