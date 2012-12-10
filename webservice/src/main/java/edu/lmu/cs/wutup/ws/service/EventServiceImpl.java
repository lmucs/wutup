package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.EventDao;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    @Autowired
    EventDao eventDao;

    @Override
    public int createEvent(Event e) {
        return eventDao.createEvent(e);
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
    public Event findEventByName(String name) {
        return eventDao.findEventByName(name);
    }

    @Override
    public List<Event> findEvents(String name, List<Integer> owners, PaginationData pagination) {
        return eventDao.findEvents(name, owners, pagination);
    }

    @Override
    public void deleteEvent(int id) {
        eventDao.deleteEvent(id);
    }

    @Override
    public Integer addComment(int eventId, Comment comment) {
        return eventDao.addComment(eventId, comment);

    }

    @Override
    public void updateComment(int commentId, Comment comment) {
        eventDao.updateComment(commentId, comment);

    }

    @Override
    public List<Comment> findComments(int eventId, PaginationData pagination) {
        return eventDao.findComments(eventId, pagination);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        eventDao.deleteComment(eventId, commentId);

    }
}
