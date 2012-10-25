package edu.lmu.cs.wutup.ws.model;

import com.google.common.base.Preconditions;

public class Circle {

    public double centerLatitude;
    public double centerLongitude;
    public double radius;

    public Circle(double centerLatitude, double centerLongitude, double radius) {
        Preconditions.checkArgument(centerLatitude >= -90 && centerLatitude <= 90);
        Preconditions.checkArgument(centerLongitude >= -180 && centerLongitude <= 180);
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radius = radius;
    }
}
