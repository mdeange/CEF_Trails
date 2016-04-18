package com.matthew.ceftrails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * This is the main activity.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExternalDB externalDB = new ExternalDB(this);
        externalDB.execute("poi");
    }

    public void goToMap(View view) {
        MapsActivity.routeNum = -1;
        startActivity(new Intent(this, MapsActivity.class));
    }

    public void goToRoutes(View view) {
        startActivity(new Intent(this, RoutesActivity.class));
    }
}
