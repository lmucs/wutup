package edu.lmu.cs.wutup.ws.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.dao.EventOccurrenceDao;
import edu.lmu.cs.wutup.ws.exception.MalformedDateTimeStringException;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
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
    public List<EventOccurrence> findAllEventOccurrences(PaginationData pagination) {
        return eventOccurrenceDao.findAllEventOccurrences(pagination);
    }

    @Override
    public List<User> findAttendeesByEventOccurrenceId(int id, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findAttendeesByEventOccurrenceId(id, pageNumber, pageSize);
    }

    @Override
    public EventOccurrence findEventOccurrenceById(int id) {
        return eventOccurrenceDao.findEventOccurrenceById(id);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByAttendees(List<User> attendees, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrencesByAttendees(attendees, pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByCategories(List<Category> categories, int pageNumber,
            int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrencesByCategories(categories, pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByCenterAndRadius(double latitude, double longitude,
            double radius, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrencesByCenterAndRadius(latitude, longitude, radius, pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByDateRange(DateTime start, DateTime end, int pageNumber,
            int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrencesByDateRange(start, end, pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByEventId(int eventId, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrencesByEventId(eventId, pageNumber, pageSize);
    }

    @Override
    public List<EventOccurrence> findAllEventOccurrencesByVenues(List<Venue> venues, int pageNumber, int pageSize) {
        return eventOccurrenceDao.findAllEventOccurrencesByVenues(venues, pageNumber, pageSize);
    }

    @Override
    public void registerAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId) {
        eventOccurrenceDao.registerAttendeeForEventOccurrence(eventOccurrenceId, attendeeId);
    }

    @Override
    public void unregisterAttendeeForEventOccurrence(int eventOccurrenceId, int attendeeId) {
        eventOccurrenceDao.unregisterAttendeeForEventOccurrence(eventOccurrenceId, attendeeId);
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
    public List<Comment> findComments(int eventId, PaginationData pagination) {
        return eventOccurrenceDao.findComments(eventId, pagination);
    }

    @Override
    public void deleteComment(int eventId, int commentId) {
        eventOccurrenceDao.deleteComment(eventId, commentId);
    }

    @Override
    public DateTime parseStringToDateTime(String time) throws MalformedDateTimeStringException {
        if (time == null) {
            return null;
        }

        try {
            return new DateTime(time);
        } catch (IllegalArgumentException e) {
            throw new MalformedDateTimeStringException();
        }
    }
}
