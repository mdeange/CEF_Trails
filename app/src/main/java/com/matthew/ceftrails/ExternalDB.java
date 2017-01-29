package com.matthew.ceftrails;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
        String url_poi_link = url_home + "scripts/geturl.php";
        String url_hazards = url_home + "scripts/gethazard.php";
        String url_upload_image = url_home + "uploadimage.php";
        String url_report_hazard = url_home + "reportHazard.php";

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

                URL url2 = new URL(url_hazards);
                con = (HttpURLConnection)url2.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in2 = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine2 = "";
                String response2 = "";

                while((inputLine2 = in2.readLine()) != null) {
                    response2 += inputLine2;
                }


                in.close();

                ArrayList<POI> pois = new ArrayList<>();
                String arr[] = response.split("BREAK");
                for(int i = 0; i < arr.length/3; i++) {
                    pois.add(new POI(arr[3*i + 1], Double.parseDouble(arr[3*i + 3]), Double.parseDouble(arr[3*i + 2])));
                }

                ArrayList<POI> hazards = new ArrayList<>();
                String arr2[] = response2.split("BREAK");
                for(int i = 0; i < arr2.length/3; i++) {
                    hazards.add(new POI(arr2[3*i + 1], Double.parseDouble(arr2[3*i + 3]), Double.parseDouble(arr2[3*i + 2])));
                }

                Singleton.getInstance().setPois(pois);
                Singleton.getInstance().setHazards(hazards);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(method.equals("poi_url")) {
            try {
                Log.i("Executing", "poi_url");
                URL url = new URL(url_poi_link);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine = "";
                String response = "";

                while((inputLine = in.readLine()) != null) {
                    response += inputLine;
                    Log.i("Executing 1", "poi_url");
                }


                in.close();

                ArrayList<UrlPOI> pois = new ArrayList<>();
                String arr[] = response.split("BREAK");
                for(int i = 0; i < arr.length/3; i++) {
                    pois.add(new UrlPOI(arr[3*i + 1], arr[3*i + 3]+"", arr[3*i + 2]+""));
                    Log.i("Executing 2", "poi_url");
                }

                Log.i("Executing 3", "poi_url");
                UrlSingleton.getInstance().setNameDes(pois);
               // for(int i =0; i< pois.size(); ++i){
               // Log.i("Executing", "poi_url"+" element "+pois.get(i).getUrl());
               // }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.i("Exception caught 1", "poi_url");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Exception caught 2", "poi_url");
            }
        }

        if(method.equals("reportHazard")) {
            String bm = params[1];
            String lat = params[2];
            String lng = params[3];
            String uploader = params[4];
            String description = params[5];
            String phone = params[6];
            String email = params[7];
            Log.i("params", params[4]+params[5]+params[6]+params[7]);
            try {
                URL url = new URL(url_report_hazard);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream OS = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("image", "UTF-8")+"="+URLEncoder.encode(bm, "UTF-8") + "&" +
                        URLEncoder.encode("lat", "UTF-8")+"="+URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("lng", "UTF-8")+"="+URLEncoder.encode(lng, "UTF-8") + "&" +
                        URLEncoder.encode("uploader", "UTF-8")+"="+URLEncoder.encode(uploader, "UTF-8") + "&" +
                        URLEncoder.encode("description", "UTF-8")+"="+URLEncoder.encode(description, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("phone", "UTF-8")+"="+URLEncoder.encode(phone, "UTF-8");
                Log.i("data: ", data);
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
                return "reportHazard_success";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(method.equals("upload_image")) {
            String bm = params[1];
            String lat = params[2];
            String lng = params[3];
            try {
                URL url = new URL(url_upload_image);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStream OS = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("image", "UTF-8")+"="+URLEncoder.encode(bm, "UTF-8") + "&" +
                        URLEncoder.encode("lat", "UTF-8")+"="+URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("lng", "UTF-8")+"="+URLEncoder.encode(lng, "UTF-8");
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
