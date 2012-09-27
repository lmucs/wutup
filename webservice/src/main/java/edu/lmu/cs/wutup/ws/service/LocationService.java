package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Location;

public interface LocationService {

    void createLocation(Location loc);

    Location findLocationById(int id);
    List<Location> findLocationsByAddress(String address, int pageNumber, int pageSize);
    List<Location> findLocationsByPropertyMap(String propertyMap, int pageNumber, int pageSize);
    List<Location> findAllLocations(int pageNumber, int pageSize);
    
    void updateLocation(Location loc);

    void deleteLocation(Location loc);

    int findNumberOfLocations();
}