package com.matthew.ceftrails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * This is the main activity.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToMap(View view) {
        MapsActivity.routeNum = -1;
        startActivity(new Intent(this, MapsActivity.class));
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
