package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.exception.MalformedDateTimeStringException;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public interface EventOccurrenceService extends CommentService {

    int createEventOccurrence(EventOccurrence e);

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

    DateTime parseStringToDateTime(String time) throws MalformedDateTimeStringException;
}
