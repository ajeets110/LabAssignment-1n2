package com.example.labassignment12;

public class Places {

 int id ;
 String address,nameoffavrtplace,date;
 Double latitude,longitude;

    public Places(String favt_name, String date, String address, Double longitude, Double latitude) {
        this.nameoffavrtplace = favt_name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getNameoffavrtplace() {
        return nameoffavrtplace;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getDate() {
        return date;
    }
}
