package com.matthew.ceftrails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class RoutesActivity extends AppCompatActivity {
    private ListView listView;
    private DBManager db;
    private ArrayList<String> namesList = new ArrayList<String>();
    private ArrayAdapter<String> tableArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        listView = (ListView) findViewById(R.id.routesListView);
        db = DBManager.getInstance(getApplicationContext());

        tableArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesList);
        listView.setAdapter(tableArrayAdapter);
        refreshTable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTable();

        // set listener for clicking on a route
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                MapsActivity.routeNum = DBManager.getInstance(getApplicationContext()).getRouteNum(pos);
                startActivity(new Intent(RoutesActivity.this, MapsActivity.class));
            }
        });
    }

    /**
     * Refreshes the table of routes on the main page to contain the full list of all stored routes.
     */
    private void refreshTable() {
        int[] routes = db.getRoutes();
        namesList.clear();
        System.out.println(routes.length + " routes");
        for (int i=0; i<routes.length; i++) namesList.add(new String("route" + i));

        tableArrayAdapter.notifyDataSetChanged();
    }
}
