package com.matthew.ceftrails;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by splatt on 4/18/2016.
 */
public class ExternalDBGet {
    public static ArrayList<POI> getPOIs() {
        String url_home = "http://people.cs.clemson.edu/~sohamap/ceftrails/scripts/";
        String url_poi = url_home + "getpoi.php";

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

            System.out.println(response);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
