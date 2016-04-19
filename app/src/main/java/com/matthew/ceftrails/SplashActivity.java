package com.matthew.ceftrails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExternalDB externalDB = new ExternalDB(this);
        externalDB.execute("poi");

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}