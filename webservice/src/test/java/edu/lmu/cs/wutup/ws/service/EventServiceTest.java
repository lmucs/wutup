package edu.lmu.cs.wutup.ws.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.EventDao;
import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.model.Event;

public class EventServiceTest {

    EventServiceImpl service;
    EventDao dao;

    Event sampleEvent = new Event(1, "Alice");

    @Before
    public void setUp() {
        service = new EventServiceImpl();
        dao = mock(EventDao.class);
        service.EventDao = dao;
    }

    @Test
    public void creationDelegatesToDao() {
        service.createEvent(sampleEvent);
        verify(dao).createEvent(sampleEvent);
    }

    @Test(expected=EventExistsException.class)
    public void creationPropagatesExistExceptions() {
        doThrow(new EventExistsException()).when(dao).createEvent(sampleEvent);
        service.createEvent(sampleEvent);
    }

    @Test
    public void updatesDelegateToDao() {
        service.updateEvent(sampleEvent);
        verify(dao).updateEvent(sampleEvent);
    }

    @Test
    public void findingDelgatesToDao() {
        when(dao.findEventById(7)).thenReturn(sampleEvent);
        assertThat(service.findEventById(7), equalTo(sampleEvent));

        service.findEvents("Alice", 1, 5);
        verify(dao).findEventsByName("Alice", 1, 5);

        service.findEvents(null, 10, 50);
        verify(dao).findAllEvents(10, 50);
    }

    @Test
    public void deletionDelegatesToDao() {
        service.deleteEvent(sampleEvent);
        verify(dao).deleteEvent(sampleEvent);
    }
}
