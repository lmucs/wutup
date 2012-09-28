package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.VenueDao;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.service.VenueService;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    @Autowired
    VenueDao VenueDao;

    @Override
    public void createVenue(Venue loc) {
        VenueDao.createVenue(loc);
    }

    @Override
    public void updateVenue(Venue loc) {
        VenueDao.updateVenue(loc);
    }

    @Override
    public Venue findVenueById(int id) {
        return VenueDao.findVenueById(id);
    }

    @Override
    public List<Venue> findVenuesByAddress(String address, int pageNumber, int pageSize) {
        return VenueDao.findVenuesByAddress(address, pageNumber, pageSize);
    }

    @Override
    public List<Venue> findVenuesByPropertyMap(String propertyMap, int pageNumber, int pageSize) {
        return VenueDao.findVenuesByPropertyMap(propertyMap, pageNumber, pageSize);
    }

    @Override
    public List<Venue> findAllVenues(int pageNumber, int pageSize) {
        return VenueDao.findAllVenues(pageNumber, pageSize);
    }

    @Override
    public void deleteVenue(Venue loc) {
        VenueDao.deleteVenue(loc);
    }

    @Override
    public int findNumberOfVenues() {
        return VenueDao.findNumberOfVenues();
    }
}
