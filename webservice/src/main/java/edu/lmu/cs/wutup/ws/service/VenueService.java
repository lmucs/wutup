package edu.lmu.cs.wutup.ws.service;

import java.util.List;
import java.util.Map;

import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface VenueService extends CommentService {

    void createVenue(Venue loc);

    Venue findVenueById(int id);

    List<Venue> findVenues(String name, Integer eventId, Circle circle, PaginationData pagination);

    void updateVenue(Venue loc);

    void deleteVenue(int venueId);

    int findNumberOfVenues();

    Map<String, String> findProperties(int venueId);

    void addProperty(int venueId, String propertyName, String value);

    void updateOrAddProperty(int venueId, Map<String, String> keyValuePair);

    void deleteProperty(int venueId, String propertyName);

}
