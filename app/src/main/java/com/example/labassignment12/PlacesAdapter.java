package com.example.labassignment12;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class PlacesAdapter extends ArrayAdapter {

    Context mcontext;
    int layoutRes;
    DatabaseHelper mDatabase;
    List<Places> listPlace;

    GoogleMap mMap;
    Places dest;

    // get user location
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    final int RADIUS = 1500;
    LatLng customMarker;
    LatLng currentLocation;


    public PlacesAdapter(@NonNull Context mcontext, int layoutRes, List<Places> listPlace, DatabaseHelper mDatabase) {
        super(mcontext, layoutRes, listPlace);
        this.mcontext = mcontext;
        this.layoutRes = layoutRes;
        this.listPlace = listPlace;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(layoutRes, null);
        TextView tvname = view.findViewById(R.id.tv_name);
        TextView tvaddress = view.findViewById(R.id.tv_address);
        TextView tvdate = view.findViewById(R.id.tv_date);
        final Places places = listPlace.get(position);
        tvname.setText(places.getNameoffavrtplace());
        tvaddress.setText(places.getAddress());
        tvdate.setText(places.getDate());

        view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deletePlace(places);

            }

        });

        view.findViewById(R.id.layoutid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dest = places;

            }

        });



        return view;


    }



    private void navigate(final Places destination){

        GoogleMap mMap = null;

        Object[] dataTransfer;
        String url;
        dataTransfer = new Object[4];
        url = getDirectionUrl();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = destination;
        dataTransfer[3] = new LatLng(currentLocation.latitude,currentLocation.longitude);
        FetchDirections getDirectionsData = new FetchDirections();
        getDirectionsData.execute(dataTransfer);


    }

    private String getDirectionUrl() {
        StringBuilder googleDirectionUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionUrl.append("origin="+currentLocation.latitude+","+currentLocation.longitude);
        googleDirectionUrl.append("&destination="+dest.latitude+","+dest.longitude);
        googleDirectionUrl.append("&key="+ mcontext.getString(R.string.api_key_places));
        Log.d("", "getDirectionUrl: "+googleDirectionUrl);
        return googleDirectionUrl.toString();
    }


    private void deletePlace(final Places places) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Are You Sure");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(mDatabase.deletePlaces(places.getId()))
                    loadPlaces();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadPlaces() {

        Cursor cursor = mDatabase.getAllPlaces();
        listPlace.clear();
        if (cursor.moveToFirst()) {

            do {
                listPlace.add(new Places(cursor.getInt(0), cursor.getString(0), cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3), cursor.getDouble(4)
                ));


            } while (cursor.moveToNext());

            cursor.close();
        }

        notifyDataSetChanged();
    }

    private void getUserLocation() {
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
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(userLocation)
                            .title("You are here"));

                }
            }
        };
    }

}
