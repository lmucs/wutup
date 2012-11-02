package edu.lmu.cs.wutup.ws.dao.util;

import java.lang.Math;

public class Haversine {
    
    public static Double radiusMiles = 3961.0;
    public static Double radiusKm = 6373.0;
    
    public static Double getDistanceInMiles(Double latA, Double latB, Double longA, Double longB) {
        return computeHaversine(latA, latB, longA, longB) * radiusMiles;
    }
    
    public static Double getDistanceInKilometers(Double latA, Double latB, Double longA, Double longB) {
        return computeHaversine(latA, latB, longA, longB) * radiusKm;
    }
    
    public static Double computeHaversine(Double latA, Double latB, Double longA, Double longB) {
        double latDiff = Math.toRadians(latB - latA);
        double longDiff = Math.toRadians(longB - longA);
        double latOrigin = Math.toRadians(latA);
        double latDest = Math.toRadians(latB);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + 
                Math.sin(longDiff / 2) * Math.sin(longDiff / 2) * Math.cos(latOrigin) * Math.cos(latDest);
        return 2 * Math.asin(Math.sqrt(a));
    }
}
