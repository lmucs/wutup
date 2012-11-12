package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.EventOccurrenceExistsException;
import edu.lmu.cs.wutup.ws.exception.MalformedDateTimeStringException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Category;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.service.EventOccurrenceService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/occurrences")
public class EventOccurrenceResource extends AbstractWutupResource {

    private static final String EVENT_OCCURRENCE_NOT_FOUND = "Event occurrence %d does not exist";
    private static final String EVENT_OCCURRENCE_ALREADY_EXISTS = "Event occurrence %d already exists";
    private static final String EVENT_OCCURRENCE_QUERY_PARAMETERS_BAD = "Event occurrence query parameters improperly formed";
    private static final String CONDITIONAL_PARAMETER_MISSING = "The %s parameter must be present if the %s parameter is supplied";
    private static final String COMMENT_NOT_FOUND = "Comment %d does not exist for event %d";
    private static final String COMMENT_ALREADY_EXISTS = "Comment %d already exists for event %d";
    private static final String USER_NOT_FOUND = "User %d does not exist";

    @Autowired
    EventOccurrenceService eventOccurrenceService;

    @GET
    @Path("/")
    public List<EventOccurrence> getEventOccurrences(@QueryParam("category") List<Category> categories,
            @QueryParam("center") String center, @QueryParam("radius") String radiusString,
            @QueryParam("start") String start, @QueryParam("end") String end, @QueryParam("eventId") Integer eventId,
            @QueryParam("venue") List<Venue> venues,
            @QueryParam("pageNumber") @DefaultValue(DEFAULT_PAGE) String pageNumberString,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) String pageSizeString) {

        PaginationData pagination = paginationDataFor(pageNumberString, pageSizeString);
        Circle circle = fromCenterAndRadiusParameters(center, radiusString);
        Interval interval = makeIntervalFromStartAndEndTime(start, end);

        validateQuery(categories, circle, interval, eventId, venues);

        return eventOccurrenceService.findEventOccurrencesByQuery(categories, circle, interval, eventId, venues,
                pagination);
    }

    @POST
    @Path("/")
    public Response createEventOccurrence(EventOccurrence eventOccurrence, @Context UriInfo uriInfo) {
        try {
            int newId = eventOccurrenceService.createEventOccurrence(eventOccurrence);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(newId + "").build();
            return Response.created(newLocation).build();
        } catch (EventOccurrenceExistsException e) {
            throw new ServiceException(CONFLICT, EVENT_OCCURRENCE_ALREADY_EXISTS, eventOccurrence.getId());
        }
    }

    @PATCH
    @Path("/{id}")
    public Response updateEventOccurrence(@PathParam("id") String idString, EventOccurrence eventOccurrence) {
        int id = toIntegerRequired("id", idString);
        checkIdAgreement(id, eventOccurrence.getId());

        try {
            eventOccurrence.setId(id);
            eventOccurrenceService.updateEventOccurrence(eventOccurrence);
            return Response.noContent().build();
        } catch (NoSuchEventOccurrenceException e) {
            throw new ServiceException(NOT_FOUND, EVENT_OCCURRENCE_NOT_FOUND, id);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEventOccurrence(@PathParam("id") String idString) {
        int id = toInteger("id", idString);

        try {
            eventOccurrenceService.deleteEventOccurrence(id);
            return Response.noContent().build();
        } catch (NoSuchEventOccurrenceException ex) {
            throw new ServiceException(NOT_FOUND, EVENT_OCCURRENCE_NOT_FOUND, id);
        }
    }

    @GET
    @Path("/{id}/attendees")
    public List<User> findAttendeesById(@PathParam("id") String idString,
            @QueryParam("pageNumber") @DefaultValue(DEFAULT_PAGE) String pageNumberString,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) String pageSizeString) {

        PaginationData pagination = paginationDataFor(pageNumberString, pageSizeString);
        int id = toInteger("id", idString);

        return eventOccurrenceService.findAttendeesByEventOccurrenceId(id, pagination);
    }

    @POST
    @Path("/{id}/attendees")
    public Response registerAttendeeForEventOccurrence(@PathParam("id") String idString,
            @QueryParam("userId") String userIdString) {
        int eventOccurrenceId = toInteger("id", idString);
        int attendeeId = toInteger("userId", userIdString);

        try {
            eventOccurrenceService.registerAttendeeForEventOccurrence(eventOccurrenceId, attendeeId);
            return Response.noContent().build();
        } catch (NoSuchEventOccurrenceException ex) {
            throw new ServiceException(NOT_FOUND, EVENT_OCCURRENCE_NOT_FOUND, eventOccurrenceId);
        } catch (NoSuchUserException ex) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, attendeeId);
        }
    }

    @DELETE
    @Path("/{id}/attendees/{userId}")
    public Response unregisterAttendeeForEventOccurrence(@PathParam("id") String idString,
            @PathParam("userId") String userIdString) {
        int eventOccurrenceId = toInteger("id", idString);
        int attendeeId = toInteger("userId", userIdString);

        try {
            eventOccurrenceService.unregisterAttendeeForEventOccurrence(eventOccurrenceId, attendeeId);
            return Response.noContent().build();
        } catch (NoSuchEventOccurrenceException ex) {
            throw new ServiceException(NOT_FOUND, EVENT_OCCURRENCE_NOT_FOUND, eventOccurrenceId);
        } catch (NoSuchUserException ex) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, attendeeId);
        }
    }

    @GET
    @Path("/{id}")
    public EventOccurrence findEventOccurrenceById(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);

        try {
            return eventOccurrenceService.findEventOccurrenceById(id);
        } catch (NoSuchEventOccurrenceException ex) {
            throw new ServiceException(NOT_FOUND, EVENT_OCCURRENCE_NOT_FOUND, id);
        }
    }

    /* Begins the Comment implementation. */
    @GET
    @Path("/{id}/comments")
    public List<Comment> findEventOccurrenceComments(@PathParam("id") String idString,
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE_SIZE) String pageString,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) String pageSizeString) {
        checkRequiredParameter("id", idString);
        int eventOccurrenceId = toInteger("id", idString);
        PaginationData pagination = paginationDataFor(pageString, pageSizeString);

        return eventOccurrenceService.findComments(eventOccurrenceId, pagination);
    }

    @POST
    @Path("/{id}/comments")
    public void addComment(@PathParam("id") String idString, Comment eventOccurrenceComment) {
        int eventOccurrenceId = toInteger("id", idString);

        try {
            eventOccurrenceService.addComment(eventOccurrenceId, eventOccurrenceComment);
        } catch (CommentExistsException e) {
            throw new ServiceException(CONFLICT, COMMENT_ALREADY_EXISTS, eventOccurrenceComment.getId());
        }
    }

    @PUT
    @Path("/{id}/comments/{commentid}")
    public Response updateComment(@PathParam("id") String eventOccurrenceIdString,
            @PathParam("commentid") String commentIdString, Comment eventOccurrenceComment) {
        int eventOccurrenceId = toInteger("id", eventOccurrenceIdString);
        int commentId = toInteger("commentid", commentIdString);
        checkIdAgreement(commentId, eventOccurrenceComment.getId());

        try {
            eventOccurrenceService.updateComment(eventOccurrenceId, eventOccurrenceComment);
            return Response.noContent().build();
        } catch (NoSuchCommentException e) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, eventOccurrenceId);
        }
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    public Response deleteComment(@PathParam("id") String eventOccurrenceIdString,
            @PathParam("commentid") String commentIdString) {
        int eventOccurrenceId = toInteger("id", eventOccurrenceIdString);
        int commentId = toInteger("commentid", commentIdString);
        try {
            eventOccurrenceService.deleteComment(eventOccurrenceId, commentId);
            return Response.noContent().build();
        } catch (NoSuchEventOccurrenceException ex) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, eventOccurrenceId);
        }
    }

    private Interval makeIntervalFromStartAndEndTime(String start, String end) {
        if (start == null && end == null) {
            return null;
        }
        if (end == null) {
            throw new ServiceException(BAD_REQUEST, CONDITIONAL_PARAMETER_MISSING, "end time", "start time");
        }
        if (start == null) {
            throw new ServiceException(BAD_REQUEST, CONDITIONAL_PARAMETER_MISSING, "start time", "end time");
        }

        try {
            DateTime startTime = eventOccurrenceService.parseStringToDateTime(start);
            DateTime endTime = eventOccurrenceService.parseStringToDateTime(end);
            return new Interval(startTime, endTime);
        } catch (MalformedDateTimeStringException e) {
            throw new ServiceException(BAD_REQUEST, EVENT_OCCURRENCE_QUERY_PARAMETERS_BAD);
        }
    }

    private void validateQuery(List<Category> categories, Circle circle, Interval interval, Integer eventId,
            List<Venue> venues) {
        if (!(categories != null || circle != null || interval != null || eventId != null || venues != null)) {
            throw new ServiceException(BAD_REQUEST, EVENT_OCCURRENCE_QUERY_PARAMETERS_BAD);
        }
    }
}
