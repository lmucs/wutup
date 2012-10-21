package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface EventOccurrenceDao extends CommentDao {

    void createEventOccurrence(EventOccurrence e);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(int id);

    List<User> findAttendeesByEventOccurrenceId(int id, int pageNumber, int pageSize);

    EventOccurrence findEventOccurrenceById(int id);

    List<EventOccurrence> findAllEventOccurrencesByAttendees(List<User> attendees, int pageNumber, int pageSize);

    List<EventOccurrence> findAllEventOccurrencesByCategories(List<Category> categories, int pageNumber, int pageSize);

    List<EventOccurrence> findAllEventOccurrencesByCenterAndRadius(double latitude, double longitude, double radius,
            int pageNumber, int pageSize);

    List<EventOccurrence> findAllEventOccurrencesByDateRange(DateTime start, DateTime end, int pageNumber, int pageSize);

    List<EventOccurrence> findAllEventOccurrencesByEventId(int eventId, int pageNumber, int pageSize);

    List<EventOccurrence> findAllEventOccurrencesByVenues(List<Venue> venues, int pageNumber, int pageSize);

    void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

    void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

}
