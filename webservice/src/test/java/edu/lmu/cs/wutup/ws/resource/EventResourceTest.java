package edu.lmu.cs.wutup.ws.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.service.EventService;

public class EventResourceTest {

    EventResource resource;
    EventService service;

    Event sampleEvent = new Event(1, "Alice");
    List<Event> sampleEventList = new ArrayList<Event>();
    UriInfo sampleUriInfo;

    @Before
    public void setUp() {
        resource = new EventResource();
        service = mock(EventService.class);
        resource.eventService = service;
        sampleUriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(sampleUriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    }

    @Test
    public void EventCreationCreatesEventWithLocationHeader() {
        Response response = resource.createEvent(sampleEvent, sampleUriInfo);
        verify(service).createEvent(sampleEvent);
        assertThat(response.getMetadata().getFirst("Location").toString(), is("http://example.com/1"));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void creatingDuplicateEventThrowsException() {
        try {
            doThrow(new EventExistsException()).when(service).createEvent(sampleEvent);
            resource.createEvent(sampleEvent, sampleUriInfo);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingEventProducesHttp204() {
        Response response = resource.updateEvent("1", sampleEvent);
        verify(service).updateEvent(sampleEvent);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void updatingEventWithMismatchedIdThrowsException() {
        try {
            resource.updateEvent("7", sampleEvent);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingNonexistingEventThrowsException() {
        try {
            doThrow(new NoSuchEventException()).when(service).updateEvent(sampleEvent);
            resource.updateEvent("1", sampleEvent);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void updatingEventWithNonIntegerIdThrowsException() {
        try {
            resource.updateEvent("zzzzz", sampleEvent);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void deletingEventProducesHttp204() {
        when(service.findEventById(1)).thenReturn(sampleEvent);
        Response response = resource.deleteEvent("1");
        verify(service).deleteEvent(sampleEvent);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingNonexistingEventThrowsException() {
        try {
            when(service.findEventById(1)).thenReturn(sampleEvent);
            doThrow(new NoSuchEventException()).when(service).deleteEvent(sampleEvent);
            resource.deleteEvent("1");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void deletingEventWithNonIntegerIdThrowsException() {
        try {
            resource.deleteEvent("zzzzz");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingExistingEventByIdProducesHttp200() {
        when(service.findEventById(1)).thenReturn(sampleEvent);
        resource.findEventById("1");
        verify(service).findEventById(1);
    }

    @Test
    public void findingByIdWithNoIdThrowsException() {
        try {
            resource.findEventById(null);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingByIdWithBadIdThrowsException() {
        try {
            resource.findEventById("zzzzz");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingByNameReturnsList() {
        when(service.findEvents("Alice", 1, 10)).thenReturn(sampleEventList);
        List<Event> result = resource.findEvents("Alice", "1", "10");
        assertThat(result, is(sampleEventList));
    }

    @Test
    public void findingByNameWithPageSizeTooHighProducesHttp403() {
        try {
            resource.findEvents("Alice", "1", "51");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    @Test
    public void findingByNameWithPageSizeTooLowProducesHttp403() {
        try {
            resource.findEvents("Alice", "0", "0");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }
}
