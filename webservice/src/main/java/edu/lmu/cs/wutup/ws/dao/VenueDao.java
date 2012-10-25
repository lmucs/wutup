package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface VenueDao extends CommentDao {

    void createVenue(Venue loc);

    List<Venue> findVenues(String name, Integer eventId, Circle circle, int pageNumber, int pageSize);

    Venue findVenueById(int id);

    void updateVenue(Venue loc);

    void deleteVenue(int venueId);

    int findNumberOfVenues();

}
