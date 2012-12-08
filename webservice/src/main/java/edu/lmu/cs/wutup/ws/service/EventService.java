package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;

public interface EventService extends CommentService {

    int createEvent(Event e);

    void updateEvent(Event e);

    Event findEventById(int id);
    
    Event findEventByName(String name);

    List<Event> findEvents(String name, List<Integer> owners, PaginationData pagination);

    void deleteEvent(int id);
}
