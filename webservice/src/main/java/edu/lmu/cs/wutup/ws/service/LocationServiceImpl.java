package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.LocationDao;
import edu.lmu.cs.wutup.ws.model.Location;

@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationDao locationDao;

    @Override
    public void createLocation(Location loc) {
        LocationDao.createLocation(loc);
    }

    @Override
    public void updateLocation(Location loc) {
        LocationDao.updateLocation(loc);
    }

    @Override
    public Location findLocationById(int id) {
        return LocationDao.findLocationById(id);
    }

    @Override
    public List<Location> findLocationByAddress(String address, int pageNumber, int pageSize) {
        return LocationDao.findLocationsByAddress(address, pageNumber, pageSize);
    }

    @Override
    public List<Location> findLocationsByPropertyMap(String propertyMap, int pageNumber, int pageSize) {
        return LocationDao.findLocationsByPropertyMap(propertyMap, pageNumber, pageSize);
    }

    @Override
    public List<Location> findAllLocations(int pageNumber, int pageSize) {
        return LocationDao.findAllLocations(pageNumber, pageSize);
    }

    @Override
    public void deleteLocation(Location loc) {
        LocationDao.deleteLocation(loc);
    }

    @Override
    public int findNumberOfLocations() {
        return LocationDao.findNumberOfLocations();
    }
}
