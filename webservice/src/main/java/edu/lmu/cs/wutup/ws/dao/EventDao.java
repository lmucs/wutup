package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Event;

public interface EventDao extends CommentDao {

    void createEvent(Event e);

    Event findEventById(int id);

    List<Event> findEventsByName(String name, int pageNumber, int pageSize);

    List<Event> findAllEvents(int pageNumber, int pageSize);

    void updateEvent(Event e);

    void deleteEvent(int id);

    int findNumberOfEvents();

}
