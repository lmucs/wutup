package edu.lmu.cs.wutup.ws.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.dao.EventDao;
import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;

public class EventServiceTest {

    EventServiceImpl service;
    EventDao dao;

    User sampleUser = new User(4, "hello@gmail.com");
    Event sampleEvent = new Event(1, "Alice", "Alice description", sampleUser);
    Comment sampleComment = new Comment(56, "Mic check 1 2", null, sampleUser);
    List<Comment> sampleComments = new ArrayList<Comment>();
    PaginationData samplePagination = new PaginationData(0, 10);

    @Before
    public void setUp() {
        service = new EventServiceImpl();
        dao = mock(EventDao.class);
        service.eventDao = dao;
    }

    @Test
    public void creationDelegatesToDao() {
        service.createEvent(sampleEvent);
        verify(dao).createEvent(sampleEvent);
    }

    @Test(expected = EventExistsException.class)
    public void creationPropagatesExistExceptions() {
        doThrow(new EventExistsException()).when(dao).createEvent(sampleEvent);
        service.createEvent(sampleEvent);
    }

    @Test
    public void findEventByName() {
        when(dao.findEventByName(sampleEvent.getName())).thenReturn(sampleEvent);
        assertThat(service.findEventByName(sampleEvent.getName()), equalTo(sampleEvent));
    }

    @Test(expected = NoSuchEventException.class)
    public void findNonexistentEventByNameFails() {
        doThrow(new NoSuchEventException()).when(dao).findEventByName(sampleEvent.getName());
        assertThat(service.findEventByName(sampleEvent.getName()), equalTo(sampleEvent));
    }

    @Test
    public void updatesDelegateToDao() {
        service.updateEvent(sampleEvent);
        verify(dao).updateEvent(sampleEvent);
    }

    @Test(expected = NoSuchEventException.class)
    public void updatePropagatesNoSuchExceptions() {
        doThrow(new NoSuchEventException()).when(dao).createEvent(sampleEvent);
        service.createEvent(sampleEvent);
    }

    @Test
    public void findingDelegatesToDao() {
        when(dao.findEventById(7)).thenReturn(sampleEvent);
        assertThat(service.findEventById(7), equalTo(sampleEvent));

        service.findEvents(sampleEvent.getName(), null, samplePagination);
        verify(dao).findEvents(sampleEvent.getName(), null, samplePagination);

        service.findEvents(null, null, samplePagination);
        verify(dao).findEvents(null, null, samplePagination);
    }

    @Test(expected = NoSuchEventException.class)
    public void findingPropagatesNoSuchExceptions() {
        doThrow(new NoSuchEventException()).when(dao).findEventById(8675309);
        service.findEventById(8675309);
    }

    @Test
    public void deletionDelegatesToDao() {
        service.deleteEvent(sampleEvent.getId());
        verify(dao).deleteEvent(sampleEvent.getId());
    }

    @Test(expected = NoSuchEventException.class)
    public void deletionPropagatesNoSuchExceptions() {
        doThrow(new NoSuchEventException()).when(dao).deleteEvent(8675309);
        service.deleteEvent(8675309);
    }

    @Test
    public void findingEventCommentsDelegatesToDao() {
        when(dao.findComments(1, samplePagination)).thenReturn(sampleComments);
        assertThat(service.findComments(1, samplePagination), equalTo(sampleComments));
    }

    @Test
    public void addingCommentDelegatesToDao() {
        service.addComment(2, sampleComment);
        verify(dao).addComment(2, sampleComment);
    }

    @Test
    public void updatingCommentDelegatestoDao() {
        service.updateComment(2, sampleComment);
        verify(dao).updateComment(2, sampleComment);
    }

    @Test
    public void deletingCommentDelegatesToDao() {
        service.deleteComment(1, 2);
        verify(dao).deleteComment(1, 2);
    }
}
