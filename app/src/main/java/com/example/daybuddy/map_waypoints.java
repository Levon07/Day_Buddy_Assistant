package com.example.daybuddy;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class map_waypoints extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_waypoints);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getDirections();
    }



    private void getDirections() {
        // Make a request to the Directions API
        // You'll need to use your own API key and origin/destination coordinates
        String apiKey = "AIzaSyBa-ttElPoQgDetwieuuMp360EJeXlr5RY";
        String origin = "latitude,longitude"; // e.g., "37.7749,-122.4194"
        String destination = "latitude,longitude"; // e.g., "37.8199,-122.4783"

        String url = "https://maps.googleapis.com/maps/api/directions/json" +
                "origin=" + origin +
                "&destination=" + destination +
                "&key=" + apiKey;

        // Use Volley, Retrofit, or another HTTP library to make the API request
        // Once you have the directions data, parse it and draw the route on the map
        // Here, we'll just demonstrate adding a dummy polyline
        LatLng originLatLng = new LatLng(40.712776, -74.005974);
        LatLng destinationLatLng = new LatLng(37.8199, -122.4783);
        mMap.addPolyline(new PolylineOptions()
                .add(originLatLng, destinationLatLng)
                .width(15)
                .color(Color.RED));
    }



}
