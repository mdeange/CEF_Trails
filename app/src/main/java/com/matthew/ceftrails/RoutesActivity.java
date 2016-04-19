package com.matthew.ceftrails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {
    private ListView listView;
    private InternalDB db;
    private ArrayList<String> namesList = new ArrayList<String>();
    private ArrayAdapter<String> tableArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        listView = (ListView) findViewById(R.id.routesListView);
        db = InternalDB.getInstance(getApplicationContext());

        tableArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesList);
        listView.setAdapter(tableArrayAdapter);
        refreshTable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTable();

        final Context context = this;

        // set listener for clicking on a route
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                MapsActivity.routeNum = InternalDB.getInstance(context).getRouteNum(pos);

                new AlertDialog.Builder(context)
                        .setTitle("View route")
                        .setPositiveButton("View route", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(RoutesActivity.this, MapsActivity.class));
                            }
                        })
                        .setNegativeButton("Delete route", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                InternalDB.getInstance(context).removeRoute(MapsActivity.routeNum);
                                MapsActivity.routeNum = -1;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .create().show();
            }
        });
    }

    /**
     * Refreshes the table of routes on the main page to contain the full list of all stored routes.
     */
    private void refreshTable() {
        String[] routes = db.getRoutes();
        namesList.clear();
        for (int i=0; i<routes.length; i++) namesList.add(new String(routes[i]));

        tableArrayAdapter.notifyDataSetChanged();
    }
}
