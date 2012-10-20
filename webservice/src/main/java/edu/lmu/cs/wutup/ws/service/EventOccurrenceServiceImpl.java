package edu.lmu.cs.wutup.ws.service;

import java.util.List;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.EventOccurrenceDao;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

@Service
@Transactional
public class EventOccurrenceServiceImpl implements EventOccurrenceService {

    @Autowired
    EventOccurrenceDao eventOccurrenceDao;

    @Override
    public void createEventOccurrence(EventOccurrence e) {
        eventOccurrenceDao.createEventOccurrence(e);
    }

    @Override
    public void updateEventOccurrence(EventOccurrence e) {
        eventOccurrenceDao.updateEventOccurrence(e);
    }

    @Override
    public void deleteEventOccurrence(int id) {
        eventOccurrenceDao.deleteEventOccurrence(id);
    }

    @Override
    public List<User> findAttendeesById(int id, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findAttendeesById(id, pageNumber, pageSize);
    }

    @Override
    public int findNumberOfEventOccurrences() {
        return eventOccurrenceDao.findNumberOfEventOccurrences();
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        return eventOccurrenceDao.findEventOccurrenceById(id);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByAttendees(List<User> attendees, int pageNumber, int pageSize) {
        return new ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByCategories(List<Category> categories, int pageNumber,
            int pageSize) {
        return new ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByCenterAndRadius(double latitude, double longitude,
            double radius, int pageNumber, int pageSize) {
        return new ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByDateRange(DateTime start, DateTime end, int pageNumber,
            int pageSize) {
        return new ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByEventId(int eventId, int pageNumber, int pageSize) {
        return new ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByVenues(List<Venue> venues, int pageNumber, int pageSize) {
        return new ArrayList<EventOccurrence>();
        // TODO
    }

    @Override
    public void registerAttendeeForEventOccurrence(User attendee) {
        // TODO
    }

    @Override
    public void unregisterAttendeeForEventOccurrence(User attendee) {
        // TODO
    }

    @Override
    public void addComment(int eventId, Comment comment) {
        eventOccurrenceDao.addComment(eventId, comment);
    }

    @Override
    public void updateComment(int commentId, Comment comment) {
        eventOccurrenceDao.updateComment(commentId, comment);

    }

    @Override
    public List<Comment> findComments(int eventId, int page, int pageSize) {
        return eventOccurrenceDao.findComments(eventId, page, pageSize);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        eventOccurrenceDao.deleteComment(eventId, commentId);

    }

}
