package edu.lmu.cs.wutup.ws.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

@XmlRootElement(name = "location")
public class Venue {

    private Integer id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    // private Map propertyMap;

    public Venue() {
        // No-arg constructor
    }

    public Venue(Integer id, String name, String address) {
        this(id, name, address, 0.0, 0.0);
        // this(id, name, address, 0.0, 0.0, null);
    }

    public Venue(Integer id, String name, String address, double latitude,
            double longitude) {
            // double longitude, Map propertyMap) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        // this.propertyMap = propertyMap;
    }

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return longitude;
    }

    public void setLongtitude(double longtitude) {
        this.longitude = longtitude;
    }

    //@XmlElement(name = "propertymap")
    //public Map getPropertyMap() {
        //return propertyMap;
    //}

    //public void setPropertyMap(Map propertyMap) {
    //    this.propertyMap = propertyMap;
    //}

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof Venue) {
            Venue other = Venue.class.cast(obj);
            result = Objects.equal(id, other.id)
                    && Objects.equal(address, other.address);
                    // && Objects.equal(other.propertyMap, this.propertyMap);
        }

        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", this.id)
                .add("address", this.address).add("latitude", this.latitude)
                .add("longtitude", this.longitude).toString();
                // .add("propertyMap", this.propertyMap).toString();
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}