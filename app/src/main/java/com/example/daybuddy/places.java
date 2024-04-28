package com.example.daybuddy;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.RelativeDateTimeFormatter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.daybuddy.databinding.ActivityPlacesBinding;
import com.google.android.datatransport.cct.internal.LogEvent;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

public class places extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "LOCATION_PICKER_TAG";
    private static final int DEFAULT_ZOOM = 19;
    private GoogleMap mMap = null;

    private PlacesClient mPlacesClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation = null;
    public boolean isSearching = false;

    private Double selectedLatitude = null;

    private String selectedTitle = null;
    private Double selectedLongitude = null;
    private String selectedAddress = "";
    private String travelMode = "walking";

    LinearLayout screen;


    private ActivityPlacesBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlacesBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.doneLl.setVisibility(View.GONE);

        Places.initialize(this, getString(R.string.google_api_key));

        mPlacesClient = Places.createClient(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        Place.Field[] placesList = new Place.Field[]{Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG};


        autocompleteSupportFragment.setPlaceFields(Arrays.asList(placesList));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onError(@NonNull Status status) {

                Log.d(TAG, "onError: status" + status);
                Toast.makeText(places.this, "Error", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {

                String id = place.getId();
                String title = place.getName();
                LatLng latLng = place.getLatLng();
                selectedLatitude = latLng.latitude;
                selectedLongitude = latLng.longitude;
                selectedAddress = place.getAddress();
                selectedTitle = place.getName();

                addMarkert(latLng, title, selectedAddress);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));


            }
        });

        binding.toolbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.toolbarGpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGpsEnabled()){
                    requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }else{
                    Toast.makeText(places.this, "Location is not on! Turn it on to show current location... ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("latitude", selectedLatitude);
                intent.putExtra("longitude", selectedLongitude);
                intent.putExtra("address", selectedAddress);
                intent.putExtra("title", selectedAddress);
                intent.putExtra("travelMode", travelMode);


                setResult(RESULT_OK, intent);

                finish();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);


    }


    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: ");
        mMap = googleMap;
        LatLng ll = new LatLng(0,0);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, DEFAULT_ZOOM));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));



        requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


            @Override
            public void onMapClick(@NonNull LatLng latLng) {




                    selectedLatitude = latLng.latitude;
                    selectedLongitude = latLng.longitude;

                    Log.d(TAG, "onMapClick: selectedLatitude: " + selectedLatitude);
                    Log.d(TAG, "onMapClick: selectedLongitude: " + selectedLongitude);


                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));

                    addressFromLatLng(latLng);
                }

        });

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));


            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

    }

    @SuppressLint("MissingPermission")
    private ActivityResultLauncher<String> requestLocationPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {

        @Override
        public void onActivityResult(Boolean isGranted) {
            Log.d(TAG, "onActivityResult: ");

            if (isGranted){

                mMap.setMyLocationEnabled(true);
                pickCurrentPlace();
            }else {
                Toast.makeText(places.this, "Permission Denied... !", Toast.LENGTH_SHORT).show();
            }

        }
    });

    private void addressFromLatLng(LatLng latLng){
        Log.d(TAG, "addressFromLatLng: ");

        Geocoder geocoder = new Geocoder(this);
        
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            Address address = addressList.get(0);
            String addressLine = address.getAddressLine(0);
            String countryName = address.getCountryName();
            String adminArea = address.getAdminArea();
            String subAdminArea = address.getSubAdminArea();
            String locality = address.getLocality();
            String subLocality = address.getSubLocality();
            String postalCode = address.getPostalCode();

            selectedAddress = "" + addressLine;

            addMarkert(latLng, ""+ subLocality, "" + addressLine);
        }catch (Exception e){
            Log.e(TAG, "addressFromLatLng: ", e);
        }
        
    }


    private void pickCurrentPlace(){
        Log.d(TAG, "pickCurrentPlace: ");
        if (mMap == null){
            return;
        }

        detectAndShowDeviceLocationMap();
    }


    @SuppressLint("MissingPermission")
    private void detectAndShowDeviceLocationMap() {


        try {

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                mLastKnownLocation = location;

                                selectedLatitude = location.getLatitude();
                                selectedLongitude = location.getLongitude();
                                Log.d(TAG, "onSuccess: selectedLatitude: " + selectedLatitude);
                                Log.d(TAG, "onSuccess: selectedLongitude: " + selectedLongitude);

                                LatLng latLng = new LatLng(selectedLatitude, selectedLongitude);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));


                                addressFromLatLng(latLng);
                            }else {
                                Log.d(TAG, "onSuccess: Location is Null");
                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.e(TAG, "onFailure: ", e);

                        }
                    });

        } catch (Exception e) {
            Log.e(TAG, "detectAndShowDeviceLocationMap", e);
        }
    }

    private boolean isGpsEnabled() {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {

            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception e) {
            Log.e(TAG, "isGpsEnabled: ", e);
        }

        try {

            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {
            Log.e(TAG, "isGpsEnabled: ", e);
        }

        return !(!gpsEnabled && !networkEnabled);
    }

    private void addMarkert(LatLng latLng, String title, String address) {
        mMap.clear();

        try {

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("" + title);
            markerOptions.snippet("" + address);

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
            binding.doneLl.setVisibility(View.VISIBLE);
            binding.selectedPlaceTv.setText(address);

        } catch (Exception e) {
            Log.e(TAG, "addMarkert: ", e);
        }
    }


    public void Bike(View view) {
        travelMode = "bicycling";
    }

    public void Car(View view) {
        travelMode = "driving";
    }

    public void Walk(View view) {
        travelMode = "walking";
    }
}