package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public interface EventOccurrenceDao extends CommentDao {

    void createEventOccurrence(EventOccurrence e);

    EventOccurrence findEventOccurrenceById(int id);
    List<EventOccurrence> findAllEventOccurrences(int pageNumber, int pageSize);

    void updateEventOccurrence(EventOccurrence e);

    void deleteEventOccurrence(EventOccurrence e);

    int findNumberOfEventOccurrences();
}
