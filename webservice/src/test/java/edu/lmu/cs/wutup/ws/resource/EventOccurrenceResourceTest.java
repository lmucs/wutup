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
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.service.EventOccurrenceService;

public class EventOccurrenceResourceTest {

    EventOccurrenceResource resource;
    EventOccurrenceService service;

    EventOccurrence sampleEventOccurrence = new EventOccurrence(1, new Event(300, "People Party"), new Venue(300, "Party Place", "1 LMU Dr."));
    Comment sampleEventOccurrenceComment = new Comment(1, "body", new DateTime(), new User());
    List<EventOccurrence> sampleEventOccurrenceList = new ArrayList<EventOccurrence>();
    List<User> sampleUserList = new ArrayList<User>();
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
    public void findingExistingEventOccurrenceByIdWorks() {
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

    @Test
    public void findingEventOccurrencesReturnsOccurrencesAsList() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(new Integer(1));
        when(
                service.findEventOccurrences(null, new Circle(1.0, 1.0, 1.0),
                        new Interval(1L, 1L), a, null, new PaginationData(0, 5))).thenReturn(
                sampleEventOccurrenceList);
        List<EventOccurrence> result = resource.findEventOccurrences(null, "1.0,1.0", "1.0", "1",
                "1", "1", null, "0", "5");
        assertThat(result, is(sampleEventOccurrenceList));
    }

    @Test
    public void findingEventOccurrencesByTimeIntervalReturnsAsList() {
        when(
                service.findEventOccurrences(null, null, new Interval(1326196800L, 1328356800L), null, null,
                        new PaginationData(0, 10))).thenReturn(sampleEventOccurrenceList);
        List<EventOccurrence> result = resource.findEventOccurrences(null, null, null, "1326196800", "1328356800",
                null, null, "0", "10");
        assertThat(result, is(sampleEventOccurrenceList));
    }

    @Test
    public void findingEventOccurrencesByCircleReturnsAsList() {
        when(
                service.findEventOccurrences(null, new Circle(20.0, 30.0, 100.0), null, null, null, new PaginationData(
                        0, 10))).thenReturn(sampleEventOccurrenceList);
        List<EventOccurrence> result = resource.findEventOccurrences(null, "20.0,30.0", "100.0", null, null, null,
                null, "0", "10");
        assertThat(result, is(sampleEventOccurrenceList));
    }

    @Test
    public void findingEventOccurrencesByVenueIdReturnsAsList() {
        Integer venueId = new Integer(2);
        when(service.findEventOccurrences(null, null, null, null, venueId, new PaginationData(0, 10))).thenReturn(
                sampleEventOccurrenceList);
        List<EventOccurrence> result = resource.findEventOccurrences(null, null, null, null, null, null, 2, "0", "10");
        assertThat(result, is(sampleEventOccurrenceList));
    }

    @Test
    public void findingEventOccurrencesByEventIdReturnsAsList() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(new Integer(2));
        when(service.findEventOccurrences(null, null, null, a, null, new PaginationData(0, 10))).thenReturn(
                sampleEventOccurrenceList);
        List<EventOccurrence> result = resource.findEventOccurrences(null, null, null, null, null, "2", null, "0", "10");
        assertThat(result, is(sampleEventOccurrenceList));
    }

    @Test
    public void findingEventOccurrencesByMultipleEventIdsReturnsAsList() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(new Integer(2));
        a.add(new Integer(3));
        when(service.findEventOccurrences(null, null, null, a, null, new PaginationData(0, 10))).thenReturn(
                sampleEventOccurrenceList);
        List<EventOccurrence> result = resource.findEventOccurrences(null, null, null, null, null, "2,3", null, "0", "10");
        assertThat(result, is(sampleEventOccurrenceList));
    }

    @Test
    public void findingEventOccurrencesWithoutQueryThrowsException() {
        try {
            resource.findEventOccurrences(null, null, null, null, null, null, null, "0", "10");
        } catch(ServiceException e) {
            assertThat(e.getResponse().getStatus(), is(400));
        }
    }

    @Test
    public void creatingEventOccurrenceReturns201WithLocationHeader() {
        Response response = resource.createEventOccurrence(sampleEventOccurrence, sampleUriInfo);
        verify(service).createEventOccurrence(sampleEventOccurrence);
        assertThat(response.getMetadata().getFirst("Location").toString(), is("http://example.com/0"));
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void creatingDuplicateEventOccurrenceIgnoresProvidedId() {
        resource.createEventOccurrence(sampleEventOccurrence, sampleUriInfo);
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
    public void findingAttendeesByEventOccurrencesReturnsAsList() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(new Integer(2));
        when(service.findAttendeesByEventOccurrenceId(2, new PaginationData(0, 10))).thenReturn(
                sampleUserList);
        List<User> attendees = resource.findAttendeesById("2", "0", "10");
        assertThat(attendees, is(sampleUserList));
    }

    @Test
    public void registeringAttendeeToEventOccurrenceReturns204() {
        Response response = resource.registerAttendeeForEventOccurrence("2", 2);
        verify(service).registerAttendeeForEventOccurrence(2, 2);
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void unregisteringAttendeeToEventOccurrenceReturns204() {
        Response response = resource.unregisterAttendeeForEventOccurrence("2", 2);
        verify(service).unregisterAttendeeForEventOccurrence(2, 2);
        assertThat(response.getStatus(), is(204));
    }

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
        when(service.findComments(1, new PaginationData(1, 10))).thenReturn(sampleEventOccurrenceCommentList);
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
