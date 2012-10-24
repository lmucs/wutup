package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LatLong {

    public Double latitude;
    public Double longitude;
    
    public LatLong() {
        // Intentionally left empty for JAXB
    }
    
    public LatLong(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
}
