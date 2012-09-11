package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Event;

public interface EventService {

    void createEvent(Event e);
    void updateEvent(Event e);
    Event findEventById(int id);
    List<Event> findEvents(String name, int pageNumber, int pageSize);
    void deleteEvent(Event e);
}
