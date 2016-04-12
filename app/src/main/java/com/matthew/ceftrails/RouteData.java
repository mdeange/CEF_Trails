package com.matthew.ceftrails;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class RouteData {
    private static RouteData instance;
    private static ArrayList<Coordinate> coords;
    private static int routeNum = 0;

    private RouteData() {
        coords = new ArrayList<Coordinate>();
    }

    public static RouteData getInstance() {
        if (instance == null) instance = new RouteData();
        return instance;
    }

    public void addCoords(LatLng latLng) {
        coords.add(new Coordinate(latLng));
    }

    public void startRecording() {
        coords.clear();
    }

    public void stopRecording(Context context) {

        String filename = "route" + routeNum + ".csv";

        String str = "LATITUDE, LONGITUDE, DATETIME";

        for (int i=0; i<coords.size(); i++) {
            str = str.concat(coords.get(i).latLng.latitude + ", " + coords.get(i).latLng.longitude + ", " + coords.get(i).time + "\n");
        }

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(str.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        DBManager.getInstance(context).addRoute(routeNum);

        routeNum++;
    }
}
