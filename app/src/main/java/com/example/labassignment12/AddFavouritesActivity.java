package com.example.labassignment12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddFavouritesActivity extends AppCompatActivity implements OnMapReadyCallback {

    // variable declaration
    GoogleMap mMap;

    private final int REQUEST_CODE = 1;

    DatabaseHelper mDatabase;
    Geocoder gcode;
    String address;
    List<Address> addresses;
    Spinner maptype;
    boolean onMarkerClick = false;

    // getting user's location using FusedLocationProviderClient
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    Double  dest_lat,dest_lng;
    final int RADIUS = 1500;
    LatLng customMarker;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initMap();
        getUserLocation();
        setHomeMarker();
        mDatabase = new DatabaseHelper(this);

        if (!checkPermission()) {
            requestPermission();
        }
        else {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
        mDatabase = new DatabaseHelper(this);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.addmap);
        mapFragment.getMapAsync(this);
    }

    private void getUserLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
        setHomeMarker();
    }

    private void setHomeMarker() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    currentLocation = userLocation;
                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(userLocation)
                            .zoom(15)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(userLocation)
                            .title("your location"));
                }
            }
        };
    }

    private boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setHomeMarker();
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddFavouritesActivity.this);
                builder.setMessage("Do you want to add the place in Favourites");
                builder.setCancelable(true);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onMarkerClick = true;
                        addressOfFav(customMarker);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onMarkerClick = false;
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
               Location location = new Location("Your Destination");
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                dest_lat = latLng.latitude;
                dest_lng = latLng.longitude;

                customMarker = latLng;
                setMarker(latLng);
                addressOfFav(customMarker);
            }
        });

    }

    private void addressOfFav(LatLng latLng){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String addDate = simpleDateFormat.format(calendar.getTime());
        gcode = new Geocoder(this, Locale.getDefault());
        try{
            addresses = gcode.getFromLocation(latLng.latitude, latLng.longitude,1);
            if(!addresses.isEmpty()){
                address = addresses.get(0).getLocality() + " " + addresses.get(0).getAddressLine(0);
                System.out.println(addresses.get(0).getAddressLine(0));

                 if (onMarkerClick && mDatabase.addFavouritePlaces(addresses.get(0).getLocality(),addDate,addresses.get(0).getAddressLine(0),latLng.latitude,latLng.longitude)){
                     onMarkerClick = false;
                    Toast.makeText(AddFavouritesActivity.this, "places:"+addresses.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddFavouritesActivity.this, "place NOT FOUND:", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void btnClick(View view) {
        Intent intent = new Intent(this,ListOfFavouritePlaces.class);
        startActivity(intent);
    }

    private void setMarker(LatLng latLng){

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address)
                .snippet("you are going there").draggable(false).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);
    }
}
