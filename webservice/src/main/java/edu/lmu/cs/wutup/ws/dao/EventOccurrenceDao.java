package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface EventOccurrenceDao extends CommentDao {

    void createEventOccurrence(EventOccurrence e);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(int id);

    List<User> findAttendeesByEventOccurrenceId(int id, PaginationData pagination);

    EventOccurrence findEventOccurrenceById(int id);

    List<EventOccurrence> findAllEventOccurrences(PaginationData pagination);

    List<EventOccurrence> findEventOccurrencesByQuery(List<User> attendees, List<Category> categories, Double latitude,
            Double longitude, Double radius, DateTime start, DateTime end, Integer eventId, String venues,
            PaginationData pagination);

    void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

    void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

}
