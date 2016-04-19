package com.matthew.ceftrails;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class RouteData {
    private static RouteData instance;
    private static ArrayList<Coordinate> coords;
    private static int routeNum = 0;
    private static String routeName;

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

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Route finished!");

        // Set up the input
        final EditText input = new EditText(context);
        input.setHint("Enter Route Name");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Save route", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RouteData.routeName = input.getText().toString() + ".csv";
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.create().show();

        String str = "LATITUDE, LONGITUDE, DATETIME";

        for (int i=0; i<coords.size(); i++) {
            str = str.concat(coords.get(i).latLng.latitude + ", " + coords.get(i).latLng.longitude + ", " + coords.get(i).time + "\n");
        }

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(routeName, Context.MODE_PRIVATE);
            outputStream.write(str.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        InternalDB.getInstance(context).addRoute(routeNum, routeName);

        routeNum++;
    }
}
