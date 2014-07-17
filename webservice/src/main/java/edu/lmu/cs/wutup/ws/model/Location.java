package edu.lmu.cs.wutup.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@XmlRootElement
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    public Double latitude;
    public Double longitude;

}
