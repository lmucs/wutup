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

    public void addComment(Integer eventId, Comment comment);

    public void updateComment(Integer eventId, Comment comment);

    Comment findCommentById(int id);
    
    void deleteComment(Comment sampleEventComment);

}
