package com.matthew.ceftrails;

import java.util.ArrayList;

/**
 * Created by splatt on 4/18/2016.
 */
public class Singleton {
    private static Singleton singleton = new Singleton();
    private ArrayList<POI> pois;
    private ArrayList<POI> hazards;


    private Singleton() {

        pois = new ArrayList<>();
        hazards = new ArrayList<>();
    }

    public static Singleton getInstance() {
        return singleton;
    }

    public ArrayList<POI> getPois() {
        return pois;
    }

    public ArrayList<POI> getHazards() {
        return hazards;
    }

    public void setPois(ArrayList<POI> pois) {
        this.pois = pois;
    }

    public void setHazards(ArrayList<POI> hazards) {
        this.hazards = hazards;
    }
}
