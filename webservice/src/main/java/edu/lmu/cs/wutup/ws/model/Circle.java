package edu.lmu.cs.wutup.ws.model;

import com.google.common.base.Preconditions;

/**
 * A container for search circle data.
 */
public class Circle {

    private static double MAX_RADIUS = 100;

    private static final String BAD_LATITUDE = "Latitude out of range: %s";
    private static final String BAD_LONGITUDE = "Longitude out of range: %s";
    private static final String BAD_RADIUS = "Radius out of range: %s";

    public double centerLatitude;
    public double centerLongitude;
    public double radius;

    public Circle(double centerLatitude, double centerLongitude, double radius) {

        Preconditions.checkArgument(centerLatitude >= -90 && centerLatitude <= 90, BAD_LATITUDE, centerLatitude);
        Preconditions.checkArgument(centerLongitude >= -180 && centerLongitude <= 180, BAD_LONGITUDE, centerLongitude);
        Preconditions.checkArgument(radius >= 0 && radius <= MAX_RADIUS, BAD_RADIUS, radius);

        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.radius = radius;
    }
}
