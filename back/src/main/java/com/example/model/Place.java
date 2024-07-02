package com.example.model;

public class Place {

    public int id;//代号
    public String name;
    public double latitude;
    public double longitude;//
    public String type;//种类

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Place(){}
    public Place(int id,String name,double latitude,double longitude,String type){
        this.id=id;
        this.name=name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.type=type;
    }



}
