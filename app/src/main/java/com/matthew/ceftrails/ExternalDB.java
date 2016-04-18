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
        String url_home = "http://people.cs.clemson.edu/~sohamap/ceftrails/scripts/";

        return null;
    }
}
