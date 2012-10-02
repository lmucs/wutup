package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public interface EventOccurrenceService {

    void createEventOccurrence(EventOccurrence e);

    EventOccurrence findEventOccurrenceById(int id);
    List<EventOccurrence> findEventOccurrencesByVenue(String venue, int pageNumber, int pageSize);
    List<EventOccurrence> findEventOccurrencesByStartTime(DateTime start, int pageNumber, int pageSize);
    List<EventOccurrence> findAllEventOccurrences(int pageNumber, int pageSize);

    void updateEventOccurrence(EventOccurrence e);
    void deleteEventOccurrence(EventOccurrence e);

    int findNumberOfEventOccurrences();
}
