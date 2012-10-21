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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.EventOccurrenceService;

public class EventOccurrenceResourceTest {

    EventOccurrenceResource resource;
    EventOccurrenceService service;

    EventOccurrence sampleEventOccurrence = new EventOccurrence(1, new Venue());
    Comment sampleEventOccurrenceComment = new Comment(1, "body", new DateTime(), new User());
    List<EventOccurrence> sampleEventOccurrenceList = new ArrayList<EventOccurrence>();
    List<Comment> sampleEventOccurrenceCommentList = new ArrayList<Comment>();
    UriInfo sampleUriInfo;

    @Before
    public void setUp() {
        resource = new EventOccurrenceResource();
        service = mock(EventOccurrenceService.class);
        resource.eventOccurrenceService = service;
        sampleUriInfo = mock(UriInfo.class);
        UriBuilder uriBuilder = UriBuilder.fromUri("http://example.com");
        when(sampleUriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
    }

    @Test
    public void eventOccurrenceCreationCreatesEventOccurrenceWithLocationHeader() {
        Response response = resource.createEventOccurrence(sampleEventOccurrence, sampleUriInfo);
        verify(service).createEventOccurrence(sampleEventOccurrence);
        assertThat(response.getMetadata().getFirst("Location").toString(), is("http://example.com/1"));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void creatingDuplicateEventOccurrenceThrowsException() {
        try {
            doThrow(new EventOccurrenceExistsException()).when(service).createEventOccurrence(sampleEventOccurrence);
            resource.createEventOccurrence(sampleEventOccurrence, sampleUriInfo);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingEventOccurrenceProducesHttp204() {
        Response response = resource.updateEventOccurrence("1", sampleEventOccurrence);
        verify(service).updateEventOccurrence(sampleEventOccurrence);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void updatingEventOccurrenceWithMismatchedIdThrowsException() {
        try {
            resource.updateEventOccurrence("7", sampleEventOccurrence);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(409));
        }
    }

    @Test
    public void updatingNonexistingEventOccurrenceThrowsException() {
        try {
            doThrow(new NoSuchEventOccurrenceException()).when(service).updateEventOccurrence(sampleEventOccurrence);
            resource.updateEventOccurrence("1", sampleEventOccurrence);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void updatingEventOccurrenceWithNonIntegerIdThrowsException() {
        try {
            resource.updateEventOccurrence("zzzzz", sampleEventOccurrence);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void deletingEventOccurrenceProducesHttp204() {
        when(service.findEventOccurrenceById(1)).thenReturn(sampleEventOccurrence);
        Response response = resource.deleteEventOccurrence("1");
        verify(service).deleteEventOccurrence(sampleEventOccurrence.getId());
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void deletingNonexistingEventOccurrenceThrowsException() {
        try {
            when(service.findEventOccurrenceById(1)).thenReturn(sampleEventOccurrence);
            doThrow(new NoSuchEventOccurrenceException()).when(service).deleteEventOccurrence(
                    sampleEventOccurrence.getId());
            resource.deleteEventOccurrence("1");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(404));
        }
    }

    @Test
    public void deletingEventOccurrenceWithNonIntegerIdThrowsException() {
        try {
            resource.deleteEventOccurrence("zzzzz");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingExistingEventOccurrenceByIdProducesHttp200() {
        when(service.findEventOccurrenceById(1)).thenReturn(sampleEventOccurrence);
        resource.findEventOccurrenceById("1");
        verify(service).findEventOccurrenceById(1);
    }

    @Test
    public void findingByIdWithNoIdThrowsException() {
        try {
            resource.findEventOccurrenceById(null);
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void findingByIdWithBadIdThrowsException() {
        try {
            resource.findEventOccurrenceById("zzzzz");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    // TODO: Need to test GET on occurrences... a little scared.

    /* Begin Event Comment Testing */
    @Test
    public void canAddCommentsToEventOccurrence() {
        resource.addComment("1", sampleEventOccurrenceComment);
        verify(service).addComment(1, sampleEventOccurrenceComment);
    }

    @Test
    public void updatingCommentProducesHttp204() {
        Response response = resource.updateComment("1", "1", sampleEventOccurrenceComment);
        verify(service).updateComment(1, sampleEventOccurrenceComment);
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
        when(service.findComments(1, 1, 10)).thenReturn(sampleEventOccurrenceCommentList);
        List<Comment> result = resource.findEventOccurrenceComments("1", "1", "10");
        assertThat(result, is(sampleEventOccurrenceCommentList));
    }

    @Test
    public void findingCommentsWithPageSizeTooHighProducesHttp403() {
        try {
            resource.findEventOccurrenceComments("1", "1", "51");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }

    @Test
    public void findingCommentsWithPageSizeTooLowProducesHttp403() {
        try {
            resource.findEventOccurrenceComments("1", "0", "0");
            fail();
        } catch (ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(403));
        }
    }
}
