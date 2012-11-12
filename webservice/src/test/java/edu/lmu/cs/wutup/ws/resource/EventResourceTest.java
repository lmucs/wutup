package edu.lmu.cs.wutup.ws.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.EventService;

public class EventResourceTest {

    EventResource resource;
    EventService service;

    Event sampleEvent = new Event(1, "Alice");
    Comment sampleEventComment = new Comment(1, "body", new DateTime(), new User());
    List<Event> sampleEventList = new ArrayList<Event>();
    List<Comment> sampleEventCommentList = new ArrayList<Comment>();
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
    public void eventCreationCreatesEventWithLocationHeader() {
        Response response = resource.createEvent(sampleEvent, sampleUriInfo);
        verify(service).createEvent(sampleEvent);
        assertThat(response.getMetadata().getFirst("Location").toString(), startsWith("http://example.com/"));
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
        // 204 = server received but no response to send back
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
        verify(service).deleteEvent(sampleEvent.getId());
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingNonexistingEventThrowsException() {
        try {
            doThrow(new NoSuchEventException()).when(service).deleteEvent(sampleEvent.getId());
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
    public void findingAllEventsWithPageSizeTooHighProducesHttp403() {
        try {
            resource.findEvents("1", "51");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    @Test
    public void findingAllEventsWithPageSizeTooLowProducesHttp403() {
        try {
            resource.findEvents("0", "0");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    /* Begin Event Comment Testing */
    @Test
    public void canAddCommentsToEvent() {
        Response response = resource.addComment("1", sampleEventComment, sampleUriInfo);
        verify(service).addComment(1, sampleEventComment);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void updatingCommentProducesHttp204() {
        Response response = resource.updateComment("1", "1", sampleEventComment);
        verify(service).updateComment(1, sampleEventComment);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingCommentProducesHttp204() {
        Response response = resource.deleteComment("1", "1");
        verify(service).deleteComment(1, 1);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void findingCommentsByEventIdReturnsList() {
        when(service.findComments(1, new PaginationData(1, 10))).thenReturn(sampleEventCommentList);
        List<Comment> result = resource.findEventComments("1", "1", "10");
        assertThat(result, is(sampleEventCommentList));
    }

    @Test
    public void findingCommentsWithPageSizeTooHighProducesHttp403() {
        try {
            resource.findEventComments("1", "1", "51");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    @Test
    public void findingCommentsWithPageSizeTooLowProducesHttp403() {
        try {
            resource.findEventComments("1", "0", "0");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }
}
