package com.example.daybuddy;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class map_waypoints extends AppCompatActivity implements OnMapReadyCallback, RV_Interface_Tasks {

    int Count = 0;

    private GoogleMap mMap;
    private EditText editTextOrigin;
    private EditText editTextDestination;
    String id;

    private boolean locationsEntered = false;

    ArrayList<Task_Model> Task_Model = new ArrayList<>();
    TT_RV_Adapter tasks_adapter = new TT_RV_Adapter(this, Task_Model, this);
    ArrayList<LatLng> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_waypoints);
        Count = 0;


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore.getInstance().collection("daysModel").document(id).collection("taskModels").whereEqualTo("userId", user.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                int STM = queryDocumentSnapshot.get("ST_time_M", Integer.class);
                                int ETM = queryDocumentSnapshot.get("ET_time_M", Integer.class);
                                Task_Model.add(new Task_Model(queryDocumentSnapshot.getString("DocID"), queryDocumentSnapshot.get("Color", int.class), queryDocumentSnapshot.get("Visibility", int.class), queryDocumentSnapshot.getString("Task_text"), queryDocumentSnapshot.getString("address"),
                                        queryDocumentSnapshot.getString("ST_Time"), queryDocumentSnapshot.getString("ET_Time"), STM,
                                        ETM, queryDocumentSnapshot.getDouble("latitude"), queryDocumentSnapshot.getDouble("longitude")));
                            }


                            if(!Task_Model.isEmpty()) {
                                Collections.sort(Task_Model, new Comparator<Task_Model>() {
                                    @Override
                                    public int compare(com.example.daybuddy.Task_Model o1, com.example.daybuddy.Task_Model o2) {
                                        return o1.time_start.compareTo(o2.time_start);
                                    }
                                });
                                tasks_adapter.notifyDataSetChanged();
                                for (Task_Model task : Task_Model) {
                                    locations.add(new LatLng(task.latitude, task.longitude));

                                }
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                                for (int i = 0; i < locations.size(); i++) {
                                    builder.include(locations.get(i));
                                }


                                LatLngBounds bounds = builder.build();
                                int padding = 100;
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                                mMap.animateCamera(cu);

                                if (locations.size() > 2) {
                                    for (int i = 0; i < locations.size() - 1; i++) {
                                        getDirections(locations.get(i), locations.get(i + 1), i + 1);
                                    }
                                }
                            }


                        }
                    });
        }






    }



    private void addMarkersAndZoom(LatLng a, int index) {
        LatLng origin = a;

        if (origin != null) {

            float[] colors = {
                    BitmapDescriptorFactory.HUE_RED,
                    BitmapDescriptorFactory.HUE_ORANGE,
                    BitmapDescriptorFactory.HUE_YELLOW,
                    BitmapDescriptorFactory.HUE_CYAN,
                    BitmapDescriptorFactory.HUE_AZURE,
                    BitmapDescriptorFactory.HUE_VIOLET,
                    BitmapDescriptorFactory.HUE_MAGENTA,
            };

// Generate a random index within the array bounds
            int randomIndex = new Random().nextInt(colors.length);

// Get the random color from the array
            float randomColor = colors[randomIndex];
            Log.d("COLOR", "addMarkersAndZoom: "+ randomColor);


            mMap.addMarker(new MarkerOptions().position(origin).title("Task Point" ).icon(BitmapDescriptorFactory.defaultMarker(randomColor)));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style_dark));


            if (!success) {
                Log.e("MAP", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MAP", "Can't find style. Error: ", e);
        }


    }
    private void getDirections(LatLng a, LatLng b, int index) {

        LatLng origin = a;
        LatLng destination = b;

        if (origin == null || destination == null) {
            return;
        }

        mMap.clear();

        locationsEntered = true;

        for (int i = 0; i < locations.size(); i++) {
            addMarkersAndZoom(locations.get(i), index);
        }

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getString(R.string.google_api_key))
                .build();

        com.google.maps.model.LatLng originLatLng = new com.google.maps.model.LatLng(origin.latitude, origin.longitude);
        com.google.maps.model.LatLng destinationLatLng = new com.google.maps.model.LatLng(destination.latitude, destination.longitude);

        DirectionsApi.newRequest(context)
                .origin(originLatLng)
                .destination(destinationLatLng)
                .mode(TravelMode.DRIVING)
                .setCallback(new PendingResult.Callback<DirectionsResult>() {
                    @Override
                    public void onResult(DirectionsResult result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.routes != null && result.routes.length > 0) {
                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    polylineOptions.color(Color.BLACK); // Задаем синий цвет для линии маршрута
                                    for (com.google.maps.model.LatLng point : result.routes[0].overviewPolyline.decodePath()) {
                                        polylineOptions.add(new LatLng(point.lat, point.lng));
                                    }
                                    mMap.addPolyline(polylineOptions);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e("DirectionsError", "Error getting directions: " + e.getMessage());
                    }
                });
    }

    private LatLng convertToLatLng(String input) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(input, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onItemClickedTasks(int position) {

    }

    @Override
    public void onItemLongClickTasks(int position) {

    }

    public void Back(View view) {
        Intent intent = new Intent(map_waypoints.this, calendar_activity.class);
        startActivity(intent);
        finish();
    }
}