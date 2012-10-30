package edu.lmu.cs.wutup.ws.model;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement
public class Venue implements Commentable {

    private Integer id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private ArrayList<Comment> comments;

    private Map<String, String> propertyMap;

    public Venue() {
        // No-arg constructor, needed for annotations
    }

    public Venue(Integer id, String name, String address) {
        this(id, name, address, 0.0, 0.0, null);
    }

    public Venue(Integer id, String name, String address, double latitude, double longitude,
            Map<String, String> propertyMap) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.propertyMap = propertyMap;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlElement
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @XmlElement
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(Map<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public String getProperty(String key) {
        return propertyMap.get(key);
    }

    public void setProperty(String key, String value) {
        propertyMap.put(key, value);
    }

    @Override
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Override
    public void removeComment(int commentIndex) {
        this.comments.remove(commentIndex);
    }

    @Override
    public void updateComment(int commentIndex, Comment comment) {
        this.comments.set(commentIndex, comment);
    }

    @Override
    public ArrayList<Comment> getComments() {
        return this.comments;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Venue && Objects.equal(id, Venue.class.cast(obj).id);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", this.id)
                .add("address", this.address)
                .add("latitude", this.latitude)
                .add("longitude", this.longitude)
                .add("propertyMap", this.propertyMap)
                .toString();
    }
}
