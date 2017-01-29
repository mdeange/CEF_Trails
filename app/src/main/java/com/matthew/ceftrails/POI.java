package com.matthew.ceftrails;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by splatt on 4/11/2016.
 */
public class POI {
    private String name;
    private String desc;
    private String[] temp;
    //private String filename;
    //private String path;
    private LatLng coord;


    public POI(String n, double lat, double lng) {
        //String name_des[] = n.split("DES");
        //name = name_des[0];
        //description = name_des[1];

        name = n;
        temp = name.split("DES");
               name = temp[0];
        Log.i("Name", name);
        //Log.i("Description", temp[1]);
        //Log.i("Description: ", description );
        //description = d;
        //filename = f;
        //path = p;
        coord = new LatLng(lat, lng);
        try{
            desc = temp[1];
        }
        catch (Exception e){
            desc = "No description available";
            Log.i("Exception at", name );
        }
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    public LatLng getCoord() {
        return coord;
    }

    /*public String getDescription() {
        return description;
    }

    public String getFilename() {
        return filename;
    }

    public String getPath() {
        return path;
    }*/
}
