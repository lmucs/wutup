package edu.lmu.cs.wutup.ws.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.EventOccurrenceDao;
import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchAttendeeOrOccurrenceException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

public class EventOccurrenceServiceTest {

    private final int JENNY = 8675309;

    EventOccurrenceServiceImpl service;
    EventOccurrenceDao dao;

    User sampleUser = new User(4, "hello@gmail.com");
    Event sampleEvent = new Event(1, "Alice", "Party At Alice's", sampleUser);
    Venue sampleVenue = new Venue(4, "Harry", "Sally", 1.0, 2.0, new HashMap<String, String>());

    EventOccurrence sampleEventOccurrence = new EventOccurrence(4, sampleEvent, sampleVenue, new DateTime(),
            new DateTime());
    Comment sampleComment = new Comment(56, "Mic check 1 2", null, sampleUser);
    List<Comment> sampleComments = new ArrayList<Comment>();
    PaginationData samplePagination = new PaginationData(0, 10);

    @Before
    public void setUp() {
        service = new EventOccurrenceServiceImpl();
        dao = mock(EventOccurrenceDao.class);
        service.eventOccurrenceDao = dao;
    }

    @Test
    public void findingEventOccurrenceByIdDelegatesToDao() {
        when(dao.findEventOccurrenceById(4)).thenReturn(sampleEventOccurrence);
        service.createEventOccurrence(sampleEventOccurrence);
        assertThat(dao.findEventOccurrenceById(sampleEventOccurrence.getId()).getId(),
                equalTo(sampleEventOccurrence.getId()));
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void findingEventOccurrenceByIdPropagatesExceptions() {
        int occurrenceId = sampleEventOccurrence.getId();
        doThrow(new NoSuchEventOccurrenceException()).when(dao).findEventOccurrenceById(occurrenceId);
        service.findEventOccurrenceById(occurrenceId);
    }

    @Test
    public void findingEventOccurrenceDelegatesToDao() {
        int venueId = sampleEventOccurrence.getVenue().getId();
        List<Integer> sampleIntegerList = new ArrayList<Integer>();
        service.findEventOccurrences(null, null, null, sampleIntegerList, venueId, samplePagination);
        verify(dao).findEventOccurrences(null, null, null, sampleIntegerList, venueId, samplePagination);
    }

    @Test
    public void creationDelegatesToDao() {
        service.createEventOccurrence(sampleEventOccurrence);
        verify(dao).createEventOccurrence(sampleEventOccurrence);
    }

    @Test(expected = EventOccurrenceExistsException.class)
    public void creationPropagatesExceptions() {
        doThrow(new EventOccurrenceExistsException()).when(dao).createEventOccurrence(sampleEventOccurrence);
        service.createEventOccurrence(sampleEventOccurrence);
    }

    @Test
    public void updatingDelegatesToDao() {
        service.updateEventOccurrence(sampleEventOccurrence);
        verify(dao).updateEventOccurrence(sampleEventOccurrence);
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void updatingPropagatesExceptions() {
        doThrow(new NoSuchEventOccurrenceException()).when(dao).updateEventOccurrence(sampleEventOccurrence);
        service.updateEventOccurrence(sampleEventOccurrence);
    }

    @Test
    public void deletingDelegatesToDao() {
        int occurrenceId = sampleEventOccurrence.getId();
        service.deleteEventOccurrence(occurrenceId);
        verify(dao).deleteEventOccurrence(occurrenceId);
    }

    @Test(expected = NoSuchEventOccurrenceException.class)
    public void deletingPropagatesExceptions() {
        doThrow(new NoSuchEventOccurrenceException()).when(dao).deleteEventOccurrence(JENNY);
        service.deleteEventOccurrence(JENNY);
    }

    @Test
    public void findAttendeesDelegatesToDao() {
        int occurrenceId = sampleEventOccurrence.getId();
        service.findAttendeesByEventOccurrenceId(occurrenceId, samplePagination);
        verify(dao).findAttendeesByEventOccurrenceId(occurrenceId, samplePagination);
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void findAttendeesPropagatesExceptions() {
        doThrow(new NoSuchAttendeeOrOccurrenceException()).when(dao).findAttendeesByEventOccurrenceId(JENNY,
                samplePagination);
        service.findAttendeesByEventOccurrenceId(JENNY, samplePagination);
    }

    @Test
    public void registerAttendeeDelegatesToDao() {
        int userId = sampleUser.getId();
        int occurrenceId = sampleEventOccurrence.getId();
        service.registerAttendeeForEventOccurrence(occurrenceId, userId);
        verify(dao).registerAttendeeForEventOccurrence(occurrenceId, userId);
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void registerAttendeePropagatesExceptions() {
        doThrow(new NoSuchAttendeeOrOccurrenceException()).when(dao).registerAttendeeForEventOccurrence(JENNY, JENNY);
        service.registerAttendeeForEventOccurrence(JENNY, JENNY);
    }

    @Test
    public void unregisterAttendeeDelegatesToDao() {
        int userId = sampleUser.getId();
        int occurrenceId = sampleEventOccurrence.getId();
        service.unregisterAttendeeForEventOccurrence(occurrenceId, userId);
        verify(dao).unregisterAttendeeForEventOccurrence(occurrenceId, userId);
    }

    @Test(expected = NoSuchAttendeeOrOccurrenceException.class)
    public void unregisterAttendeePropagatesExceptions() {
        doThrow(new NoSuchAttendeeOrOccurrenceException()).when(dao).unregisterAttendeeForEventOccurrence(JENNY, JENNY);
        service.unregisterAttendeeForEventOccurrence(JENNY, JENNY);
    }
}
