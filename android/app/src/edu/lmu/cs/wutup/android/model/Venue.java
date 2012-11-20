package edu.lmu.cs.wutup.android.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Venue {
    
    private Integer id;
    private String name;
    private String address;
    private int latitude;
    private int longitude;
    private ArrayList<Comment> comments;
    private HashMap<String, String> propertyMap;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public int getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = (int) Math.round(latitude * Math.pow(10, 6));
    }
    public int getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = (int) Math.round(longitude * Math.pow(10, 6));
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
    public HashMap<String, String> getPropertyMap() {
        return propertyMap;
    }
    public void setPropertyMap(HashMap<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

}
