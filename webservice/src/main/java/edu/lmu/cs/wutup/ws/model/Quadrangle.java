package edu.lmu.cs.wutup.ws.model;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;

/**
 * A quadrangle on a sphere, with measurements in degrees.  Useful for searching.
 */
public class Quadrangle {

    double minLatitude;
    double minLongitude;
    double maxLatitude;
    double maxLongitude;

    public Quadrangle(double lat1, double lon1, double lat2, double lon2) {
        Preconditions.checkArgument(lat1 >= -90 && lat1 <= 90);
        Preconditions.checkArgument(lat2 >= -90 && lat2 <= 90);
        Preconditions.checkArgument(lon1 >= -180 && lon1 <= 180);
        Preconditions.checkArgument(lon2 >= -180 && lon2 <= 180);
        this.minLatitude = Doubles.min(lat1, lat2);
        this.minLongitude = Doubles.min(lon1, lon2);
        this.maxLatitude = Doubles.max(lat1, lat2);
        this.maxLongitude = Doubles.max(lon1, lon2);
    }

    public static Quadrangle fromString(String descriptor) {
        String[] components = descriptor.split("\\s*,\\s*");
        Preconditions.checkArgument(components.length == 4);
        return new Quadrangle(
                Double.parseDouble(components[0]),
                Double.parseDouble(components[1]),
                Double.parseDouble(components[2]),
                Double.parseDouble(components[3]));
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    @Override
    public String toString() {
        return String.format("<%s,%s;%s,%s>",
                latitude(minLatitude), longitude(minLongitude),
                latitude(maxLatitude), longitude(maxLongitude));
    }

    private String latitude(double lat) {
        return Math.abs(lat) + (lat < 0 ? "S" : "N");
    }

    private String longitude(double lon) {
        return Math.abs(lon) + (lon < 0 ? "W" : "E");
    }
}
