package com.matthew.ceftrails;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
        String url_home = "https://people.cs.clemson.edu/~sohamap/ceftrails/";
        String url_poi = url_home + "scripts/getpoi.php";
        String url_upload_image = url_home + "uploadimage.php";

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

        if(method.equals("upload_image")) {
            String bm = params[1];
            try {
                URL url = new URL(url_upload_image);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream OS = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("image", "UTF-8")+"="+URLEncoder.encode(bm, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.close();
                OS.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine = "";
                String response = "";

                while((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();

                //Log.d("response", response);
                return "image_upload_success";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
