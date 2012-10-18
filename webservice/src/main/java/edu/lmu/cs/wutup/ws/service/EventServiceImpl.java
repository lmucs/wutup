package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.EventDao;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    EventDao eventDao;

    @Override
    public void createEvent(Event e) {
        eventDao.createEvent(e);
    }

    @Override
    public void updateEvent(Event e) {
        eventDao.updateEvent(e);
    }

    @Override
    public Event findEventById(int id) {
        return eventDao.findEventById(id);
    }

    @Override
    public List<Event> findEventsByName(String name, int pageNumber, int pageSize) {
        return name == null ? eventDao.findAllEvents(pageNumber, pageSize) : eventDao.findEventsByName(name,
                pageNumber, pageSize);
    }

    @Override
    public void deleteEvent(int id) {
        eventDao.deleteEvent(id);
    }

    @Override
    public void addComment(int eventId, Comment comment) {
        eventDao.addComment(eventId, comment);

    }

    @Override
    public void updateComment(int commentId, Comment comment) {
        eventDao.updateComment(commentId, comment);

    }

    @Override
    public List<Comment> findEventComments(int eventId, int page, int pageSize) {
        return eventDao.findComments(eventId, page, pageSize);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        eventDao.deleteComment(eventId, commentId);

    }
}
