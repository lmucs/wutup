package edu.lmu.cs.wutup.ws.service;

import java.sql.Timestamp;
import java.util.List;

import org.joda.time.Interval;

import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface EventOccurrenceService extends CommentService {

    int createEventOccurrence(EventOccurrence e);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(int id);

    List<User> findAttendeesByEventOccurrenceId(int id, PaginationData pagination);

    EventOccurrence findEventOccurrenceByProperties(Integer parentEventId, Integer venueId, Timestamp start,
            Timestamp end);

    EventOccurrence findEventOccurrenceById(int id);

    List<EventOccurrence> findEventOccurrences(Integer attendee, Circle circle, Interval interval,
            List<Integer> eventId, Integer venueId, PaginationData pagination);

    void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);

    void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId);
}
