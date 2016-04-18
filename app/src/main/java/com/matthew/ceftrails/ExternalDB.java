package com.matthew.ceftrails;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by splatt on 4/18/2016.
 */
public class ExternalDB extends AsyncTask<String, Void, String> {
    private Context ctx;

    ExternalDB(Context context) { ctx = context; }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String url_home = "https://people.cs.clemson.edu/~sohamap/ceftrails/scripts/";
        String url_poi = url_home + "getpoi.php";

        if(method.equals("poi")) {
            try {
                URL url = new URL(url_poi);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine = "";
                String response = "";

                while((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();

                ArrayList<POI> pois = new ArrayList<>();
                String arr[] = response.split("BREAK");
                for(int i = 0; i < arr.length/3; i++) {
                    pois.add(new POI(arr[3*i + 1], Double.parseDouble(arr[3*i + 3]), Double.parseDouble(arr[3*i + 2])));
                }

                Singleton.getInstance().setPois(pois);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
