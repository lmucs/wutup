package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Quadrangle;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface VenueDao extends CommentDao {

    void createVenue(Venue loc);

    List<Venue> findVenues(String name, Integer eventId, Quadrangle searchBox, int pageNumber, int pageSize);

    Venue findVenueById(int id);

    void updateVenue(Venue loc);

    void deleteVenue(int venueId);

    int findNumberOfVenues();

}
