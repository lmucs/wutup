package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

public class Location {
    private String address;
    private double latitude;
    private double longtitude;
    private String propertyMap;

    public Location() {
        // No-arg constructor
    }

    public Location(String address, double latitude, double longtitude, String propertyMap) {
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.propertyMap = propertyMap;
    }

    @XmlElement(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @XmlElement(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @XmlElement(name = "longtitude")
    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    @XmlElement(name = "propertymap")
    public String getPropertyMap() {
        return propertyMap;
    }

    public void setPropertyMap(String propertyMap) {
        this.propertyMap = propertyMap;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof Location) {
            Location other = Location.class.cast(obj);
            result = Objects.equal(address, other.address) &&
                     Objects.equal(latitude, other.latitude) &&
                     Objects.equal(longtitude, other.longtitude) &&
                     Objects.equal(propertyMap, other.propertyMap);
        }

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("address", this.address)
                .add("latitude", this.latitude)
                .add("longtitude", this.longtitude)
                .add("propertyMap", this.propertyMap).toString();
    }

}