package com.matthew.ceftrails;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by splatt on 4/18/2016.
 */
public class ExternalDB extends AsyncTask<String, Void, String> {
    private Context ctx;

    ExternalDB(Context context) { ctx = context; }

    @Override
    protected String doInBackground(String... params) {
        String method = params[0];
        String url_home = "url";            //fix the url
        String url_poi = url_home + "";     //fix

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

                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String resultBig) {

    }
}
