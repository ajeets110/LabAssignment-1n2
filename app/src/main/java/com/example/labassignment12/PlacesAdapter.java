package com.example.labassignment12;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class PlacesAdapter extends ArrayAdapter {

    Context mcontext;
    int layoutRes;
    DatabaseHelper mDatabase;
    List<Places> listPlace;
    Context context;


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

        return view;
    }

    private void deletePlace(final Places places) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Are You Sure");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*
                String sql="DELETE FROM employees WHERE id=?";
                mDatabase.execSQL(sql,new Integer [] {employee.getId()});
                loadEmployees();
                 */
                mDatabase.deletePlaces(places.getId());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}
