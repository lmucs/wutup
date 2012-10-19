package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public interface EventOccurrenceService extends CommentService {

    void createEventOccurrence(EventOccurrence e);

    EventOccurrence findEventOccurrenceById(int id);

    List<EventOccurrence> findAllEventOccurrences(int pageNumber, int pageSize);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(EventOccurrence e);

    int findNumberOfEventOccurrences();
}
