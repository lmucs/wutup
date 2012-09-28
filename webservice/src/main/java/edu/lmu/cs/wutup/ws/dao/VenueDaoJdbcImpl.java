package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import edu.lmu.cs.wutup.ws.model.Venue;

@Repository
public class VenueDaoJdbcImpl implements VenueDao {

    @Override
    public void createVenue(Venue loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public Venue findVenueById(int id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Venue> findVenuesByAddress(String address, int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Venue> findVenuesByPropertyMap(String propertyMap, int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Venue> findAllVenues(int pageNumber, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateVenue(Venue loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteVenue(Venue loc) {
        // TODO Auto-generated method stub

    }

    @Override
    public int findNumberOfVenues() {
        // TODO Auto-generated method stub
        return 0;
    }
}
