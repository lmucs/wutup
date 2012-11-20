package edu.lmu.cs.wutup.android.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Venue {
    
    private Integer id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
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
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
