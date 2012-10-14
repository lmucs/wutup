package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;

public interface EventService {

    void createEvent(Event e);

    void updateEvent(Event e);

    Event findEventById(int id);

    List<Event> findEventsByName(String name, int pageNumber, int pageSize);

    void deleteEvent(Event e);

    public void addComment(int eventId, Comment comment);

    public void updateComment(int eventId, Comment comment);

    List<Comment> findEventComments(int eventId, int page, int pageSize);

    void deleteComment(int eventId, int commentId);

}
