package com.agh.dudek;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

/**
 * Created by dudek on 10/18/16.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final int WIDTH_DIVISIONS = 50;
    public static final int HEIGHT_DIVISIONS = 130;
    public static final int MAP_HEIGHT = 20;

    public static final String BUILDINGS_POINTS = "buildingsPoints";
    public static final String WIDTH = "mapWidth";
    public static final String DEPTH = "mapDepth";
    public static final String HEIGHT = "mapHeight";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        CameraUpdate wimiipBuilding = CameraUpdateFactory.newLatLng(new LatLng(50.067034, 19.916931));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        googleMap.moveCamera(wimiipBuilding);
        googleMap.animateCamera(zoom);

        Button button = ((Button) findViewById(R.id.snapshot_button));
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.map_without_roads_style));
                captureMap(googleMap);
            }
        });

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_with_roads_style));

    }

    private void captureMap(final GoogleMap googleMap){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        new MapConverterTask(MapActivity.this, WIDTH_DIVISIONS, HEIGHT_DIVISIONS).execute(bitmap);
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapActivity.this, R.raw.map_with_roads_style));
                    }
                });
            }
        }, 500);
    }
}
