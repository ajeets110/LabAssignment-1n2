package com.example.labassignment12;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListOfFavouritePlaces extends AppCompatActivity {

    DatabaseHelper mDatabase;
    List<Places> listPlace;
    ListView listView;
    Context context;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_places_list);
        listView = findViewById(R.id.favrt_places);
        delete = findViewById(R.id.btn_delete);
        listPlace = new ArrayList<>();
        mDatabase = new DatabaseHelper(this);
        loadPlaces();



        PlacesAdapter placesAdaptor = new PlacesAdapter(this,R.layout.list_layout_favrtplaces,listPlace,mDatabase);
        listView.setAdapter(placesAdaptor);


    }



    private void loadPlaces(){

        Cursor cursor = mDatabase.getAllPlaces();
        if(cursor.moveToFirst()){

            do{


                listPlace.add(new Places(cursor.getString(0),cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),cursor.getDouble(4)
                ));


            }while (cursor.moveToNext());

            cursor.close();
        }


        // Custom Adaptor
//        PlacesAdapter placesAdaptor = new PlacesAdapter(this,R.layout.list_layout_favrtplaces,listPlace,mDatabase);
//        listView.setAdapter(placesAdaptor);

    }


}
