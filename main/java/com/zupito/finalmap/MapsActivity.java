package com.zupito.finalmap;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    private ArrayList<LatLng> points;
    Polyline line;
    private static final String TAG = "MainActivity";
    private static final long INTERVAL = 1000 * 60 * 1; //1 minute
    private static final long FASTEST_INTERVAL = 1000 * 60 * 1; // 1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        points = new ArrayList<LatLng>();

    }



    private void redrawLine(){

        //mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
       // addMarker(); //add Marker in current position
        line = mMap.addPolyline(options); //add Polyline
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);


        // Add a marker in Sydney and move the camera
       // LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider,400,1,this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
       // mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Marker"));
       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),17));
        LatLng latLng = new LatLng(lat, lng); //you already have this
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        points.add(latLng); //added

        redrawLine(); //added

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

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
}
