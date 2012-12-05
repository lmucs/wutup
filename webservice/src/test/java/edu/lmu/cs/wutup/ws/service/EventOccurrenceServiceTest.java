package edu.lmu.cs.wutup.ws.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;

public class EventOccurrenceServiceTest {

    EventOccurrenceServiceImpl service;
    EventOccurrenceDao dao;

    User sampleUser = new User(4, "hello@gmail.com");
    Event sampleEvent = new Event(1, "Alice", "Party At Alice's", sampleUser);
    Venue sampleVenue = new Venue(4, "Harry", "Sally", 1.0, 2.0, new HashMap<String,String>());
    EventOccurrence sampleEventOccurrence = new EventOccurrence(4, sampleEvent, sampleVenue, new DateTime(), new DateTime());
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
    public void creationDelegatesToDao() {
        service.createEventOccurrence(sampleEventOccurrence);
        verify(dao).createEventOccurrence(sampleEventOccurrence);
    }

    @Test
    public void retrieveEventOccurrenceById() {
        when(dao.findEventOccurrenceById(4)).thenReturn(sampleEventOccurrence);
        dao.createEventOccurrence(sampleEventOccurrence);
        assertThat(dao.findEventOccurrenceById(sampleEventOccurrence.getId()).getId(), equalTo(sampleEventOccurrence.getId()));
    }
}
