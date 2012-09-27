package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Location;

public interface LocationDao {

    void createLocation(Location loc);

    Location findLocationById(int id);
    List<Location> findLocationByAddress(String address, int pageNumber, int pageSize);
    List<Location> findLocationByPropertyMap(String propertyMap, int pageNumber, int pageSize);
    List<Location> findAllLocations(int pageNumber, int pageSize);
    
    void updateLocation(Location loc);

    void deleteLocation(Location loc);

    int findNumberOfLocations();
}