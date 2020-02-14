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
    List<Places> listPlace;


    public PlacesAdapter(@NonNull Context mcontext, int layoutRes, List<Places> listPlace, DatabaseHelper mDatabase) {
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
        TextView tvdate = view.findViewById(R.id.tv_date);
        final Places list = listPlace.get(position);
        tvname.setText(list.getNameoffavrtplace());
        tvaddress.setText(list.getAddress());
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

}
