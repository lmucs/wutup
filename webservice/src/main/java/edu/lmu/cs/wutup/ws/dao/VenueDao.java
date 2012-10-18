package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface VenueDao extends CommentDao{

    void createVenue(Venue loc);

    Venue findVenueById(int id);

    List<Venue> findVenuesByAddress(String address, int pageNumber, int pageSize);

    List<Venue> findVenuesByPropertyMap(String propertyMap, int pageNumber, int pageSize);

    List<Venue> findAllVenues(int pageNumber, int pageSize);

    void updateVenue(Venue loc);

    void deleteVenue(Venue loc);

    int findNumberOfVenues();

}
