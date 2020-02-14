package com.example.labassignment12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PlacesAdapter extends ArrayAdapter {

    Context mcontext;
    int layoutRes;
    DatabaseHelper mDatabase;
    List<ClassOfPlaces> listPlace;


    public PlacesAdapter(@NonNull Context mcontext, int layoutRes, List<ClassOfPlaces> listPlace, DatabaseHelper mDatabase) {
        super(mcontext, layoutRes,listPlace);
        this.mcontext = mcontext;
        this.layoutRes = layoutRes;
        this.listPlace = listPlace;
        this.mDatabase = mDatabase;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View view = inflater.inflate(layoutRes,null);
        TextView tvname = view.findViewById(R.id.tv_name);
        TextView tvaddress = view.findViewById(R.id.tv_address);
        TextView tvlongitude = view.findViewById(R.id.tv_longitude);
        TextView tvlatitude = view.findViewById(R.id.tv_latitude);
        TextView tvdate = view.findViewById(R.id.tv_date);




        final ClassOfPlaces list = listPlace.get(position);
        tvname.setText(list.getNameoffavrtplace());
        tvaddress.setText(list.getAddress());
        //tvlatitude.setText(list.getLatitude());
        //tvlongitude.setText(list.getLongitude());
        tvdate.setText(list.getDate());

        view.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;

    }

//    private void loadPlaces(){
//
//        String sql = "SELECT * FROM places";
//        Cursor c = mDatabase.getAllPlaces();
//        listPlace.clear();
//        if (c.moveToFirst()){
//
//            do{
//
//                listPlace.add(new ClassOfPlaces(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5)));
//            }while (c.moveToNext());
//            c.close();
//
//
//
//        }
//        notifyDataSetChanged();
//
//    }
//


}
