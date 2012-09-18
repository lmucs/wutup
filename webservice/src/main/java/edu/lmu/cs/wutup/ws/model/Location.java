package edu.lmu.cs.wutup.ws.model;

public class Location {
    private String address;
    private double latitude;
    private double longtitude;
    private double accuracy;
    
    public Location() {

    }

    public Location(String address, double latitude, double longtitude, double accuracy) {
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.accuracy = accuracy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

}