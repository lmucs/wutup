package edu.lmu.cs.wutup.ws.dao;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;

public interface EventDao {

    void createEvent(Event e);

    Event findEventById(int id);
    List<Event> findEventsByName(String name, int pageNumber, int pageSize);
    List<Event> findAllEvents(int pageNumber, int pageSize);

    void updateEvent(Event e);

    void deleteEvent(Event e);

    int findNumberOfEvents();

    void addComment(Integer eventId, Comment comment);
    void updateComment(Integer commentId, Comment comment);
    List<Comment> findEventComments(int eventId, int page, int pageSize);
    void deleteComment(int eventId, int commentId);
}
