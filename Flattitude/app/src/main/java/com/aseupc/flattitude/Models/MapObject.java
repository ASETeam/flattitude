package com.aseupc.flattitude.Models;

import java.util.Date;

/**
 * Created by Jordi on 04/11/2015.
 */
public class MapObject {

    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;

    public MapObject(){

    }

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
        this.name = new String(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new String(description);
    }

    public void setCoordinates(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return name;
    }

}
