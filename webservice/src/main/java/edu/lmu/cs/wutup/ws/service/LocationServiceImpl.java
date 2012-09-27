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
        locationDao.createLocation(loc);
    }

    @Override
    public void updateLocation(Location loc) {
        locationDao.updateLocation(loc);
    }

    @Override
    public Location findLocationById(int id) {
        return locationDao.findLocationById(id);
    }

    @Override
    public List<Location> findLocationsByAddress(String address, int pageNumber, int pageSize) {
        return locationDao.findLocationsByAddress(address, pageNumber, pageSize);
    }

    @Override
    public List<Location> findLocationsByPropertyMap(String propertyMap, int pageNumber, int pageSize) {
        return locationDao.findLocationsByPropertyMap(propertyMap, pageNumber, pageSize);
    }

    @Override
    public List<Location> findAllLocations(int pageNumber, int pageSize) {
        return locationDao.findAllLocations(pageNumber, pageSize);
    }

    @Override
    public void deleteLocation(Location loc) {
        locationDao.deleteLocation(loc);
    }

    @Override
    public int findNumberOfLocations() {
        return locationDao.findNumberOfLocations();
    }
}
