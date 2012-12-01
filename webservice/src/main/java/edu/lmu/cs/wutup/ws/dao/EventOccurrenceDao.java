package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import org.joda.time.Interval;

import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface EventOccurrenceDao extends CommentDao {

    int createEventOccurrence(EventOccurrence e);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(int id);

    List<User> findAttendeesByEventOccurrenceId(int id, PaginationData pagination);

    EventOccurrence findEventOccurrenceById(int id);

    List<EventOccurrence> findEventOccurrences(List<Category> categories, Circle circle,
            Interval interval, Integer eventId, Integer venueId, PaginationData pagination);

    int findNumberOfEventOccurrences();

    void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

    void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

}
