package com.example.labassignment12;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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

import java.util.Arrays;
import java.util.List;

public class PlacesAdapter extends ArrayAdapter {

    Context mcontext;
    int layoutRes;
    DatabaseHelper mDatabase;
    List<Places> listPlace;


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

        return view;
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
    private void updatePlace(Places places)
    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(mcontext);
    LayoutInflater inflater = LayoutInflater.from(mcontext);
    View v = inflater.inflate(R.layout.dialog_layout_update_employee, null);
        alert.setView(v);
    final android.app.AlertDialog alertDialog = alert.create();
        alertDialog.show();

    final EditText etName = v.findViewById(R.id.editTextName);
    final EditText etSalary = v.findViewById(R.id.editTextSalary);
    final Spinner spinner = v.findViewById(R.id.spinnerDepartment);

    String[] departmentsArray = mContext.getResources().getStringArray(R.array.departments);
    int position = Arrays.asList(departmentsArray).indexOf(employee.getDept());

        etName.setText(employee.getName());
        etSalary.setText(String.valueOf(employee.getSalary()));
        spinner.setSelection(position);

        v.findViewById(R.id.btn_update_employee).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = etName.getText().toString().trim();
            String salary = etSalary.getText().toString().trim();
            String dept = spinner.getSelectedItem().toString();

            if (name.isEmpty()) {
                etName.setError("Name field is mandatory!");
                etName.requestFocus();
                return;
            }

            if (salary.isEmpty()) {
                etSalary.setError("Salary field is mandatory!");
                etSalary.requestFocus();
                return;
            }

                /*
                String sql = "UPDATE employees SET name = ?, department = ?, salary = ? WHERE id = ?";
                mDatabase.execSQL(sql, new String[]{name, dept, salary, String.valueOf(employee.getId())});
                Toast.makeText(mContext,"employee updated", Toast.LENGTH_SHORT).show();

                 */

            if (mDatabase.updateEmployee(employee.getId(), name, dept, Double.parseDouble(salary))) {
                Toast.makeText(mContext,"employee updated", Toast.LENGTH_SHORT).show();
                loadEmployees();
            } else
                Toast.makeText(mContext,"employee not updated", Toast.LENGTH_SHORT).show();

//                loadEmployees();
            alertDialog.dismiss();
        }
    });
}
}
