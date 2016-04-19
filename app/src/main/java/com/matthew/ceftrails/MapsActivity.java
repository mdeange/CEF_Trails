package com.matthew.ceftrails;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int ROUTEZOOM = 19;
    private static final int MAPZOOM = 13;
    private static final int MSPERSEC = 1000;
    private static final int MINUPDATETIME = 5;
    private static final int MINUPDATEDISTANCE = 5;

    public static int routeNum = -1;

    private GoogleMap mMap;
    private LocationManager lm;
    private double lat, lng;
    private ArrayList<POI> pois;

    private static Button recordButton;
    private static Button routesButton;

    private final LocationListener locListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            lng = location.getLongitude();
            lat = location.getLatitude();
            LatLng myPos = new LatLng(lat, lng);
            RouteData.getInstance().addCoords(myPos);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, ROUTEZOOM));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // get the LocationManager
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        super.onCreate(savedInstanceState);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recordButton = (Button) findViewById(R.id.recordButton);
        routesButton = (Button) findViewById(R.id.routesButton);

        //routeNum = -1;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Enable Google Map and blue dot
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        System.out.println("RouteNum = " + routeNum);

        drawMap();
        if (routeNum == -1) { // In general view
            if (!isLocationEnabled()) showAlert();
        } else { // Showing a specific recorded route
            ((Button) findViewById(R.id.recordButton)).setVisibility(View.INVISIBLE);

            drawRouteOnMap();
        }
    }

    /**
     * Used to draw the map with POIs and CEF Trails. User's routes may optionally be drawn on afterward
     * with drawRouteOnMap().
     */
    public void drawMap() {
        pois = Singleton.getInstance().getPois();

        for(POI p : pois) {
            mMap.addMarker(new MarkerOptions().position(p.getCoord()).title(p.getName()));
        }

        try {
            KmlLayer southLayer = new KmlLayer(mMap, R.raw.south, getApplicationContext());
            southLayer.addLayerToMap();

            KmlLayer northLayer = new KmlLayer(mMap, R.raw.north, getApplication());
            northLayer.addLayerToMap();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // move to Clemson's campus
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.6834, -82.8374), MAPZOOM));
    }

    /**
     * Draws a user's route on the map. POIs and CEF Trails will still be shown.
     */
    public void drawRouteOnMap() {
        ArrayList<LatLng> coords = readCsv();
        System.out.println("Coords Size: " + coords.size());

        PolylineOptions line = new PolylineOptions().width(10).color(Color.RED);
        line.addAll(coords);

        mMap.addPolyline(line);
        if (coords.size() > 0) mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coords.get(0), ROUTEZOOM));
    }

    /* Code taken from http://www.androidauthority.com/get-use-location-data-android-app-625012/
     */
    private boolean isLocationEnabled() {
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your GPS Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void startUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // get updates from the GPS with minimum update time and invterval
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINUPDATETIME * MSPERSEC, MINUPDATEDISTANCE, locListener);
    }

    public void stopUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // stop updates from the GPS
        lm.removeUpdates(locListener);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.matthew.ceftrails/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.matthew.ceftrails/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public void startRecording() {
        routesButton.setVisibility(View.INVISIBLE);
        RouteData.getInstance().startRecording();
        startUpdates();
    }

    public void stopRecording() {
        routesButton.setVisibility(View.VISIBLE);
        RouteData.getInstance().stopRecording(this);
        stopUpdates();
    }

    public void buttonPress(View view) {
        if (recordButton.getText() == "STOP RECORDING") {
            stopRecording();
            recordButton.setText("RECORD NEW ROUTE");
        }
        else {
            startRecording();
            recordButton.setText("STOP RECORDING");
        }
    }

    private ArrayList<LatLng> readCsv() {
        ArrayList<LatLng> coords = new ArrayList<LatLng>();
        String filename = "route" + routeNum + ".csv";

        try {
            FileInputStream fileStream = getApplicationContext().openFileInput(filename);
            DataInputStream inputStream = new DataInputStream(fileStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine(); // throw out the first line which is just column headers

            while ((line = reader.readLine()) != null) {
                List<String> lineList = Arrays.asList(line.split(","));
                coords.add(new LatLng(Double.parseDouble(lineList.get(0)), Double.parseDouble(lineList.get(1))));
            }

            System.out.println("Number of coordinates: " + coords.size());

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return coords;
    }

    public void goToRoutes(View view) {
        startActivity(new Intent(this, RoutesActivity.class));
    }

    public void goToCamera(View view) {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bp = (Bitmap)data.getExtras().get("data");
        ExternalDB externalDB = new ExternalDB(this);
        externalDB.execute("upload_image", getBase64String(bp));
    }

    public static String getBase64String(Bitmap bp) {
        String encodedImageData = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            encodedImageData = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedImageData;
    }
}