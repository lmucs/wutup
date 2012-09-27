package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public interface EventOccurrenceDao {

    void createEventOccurrence(EventOccurrence e);

    EventOccurrence findEventOccurrenceById(int id);
    List<EventOccurrence> findEventOccurrencesByLocation(String location, int pageNumber, int pageSize);
    List<EventOccurrence> findEventOccurrencesByStartTime(DateTime start, int pageNumber, int pageSize);
    List<EventOccurrence> findAllEventOccurrences(int pageNumber, int pageSize);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(EventOccurrence e);

    int findNumberOfEventOccurrences();
}
