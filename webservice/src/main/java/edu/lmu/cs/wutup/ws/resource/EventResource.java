package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.EventExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.service.EventService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/events")
public class EventResource extends AbstractWutupResource {

    private static final String EVENT_NOT_FOUND = "Event %d does not exist";
    private static final String EVENT_ALREADY_EXISTS = "Event %d already exists";
    private static final String COMMENT_NOT_FOUND = "Comment %d does not exist for event %d";
    private static final String COMMENT_ALREADY_EXISTS = "Comment %d already exists for event %d";

    @Autowired
    EventService eventService;

    @GET
    @Path("/")
    public List<Event> findEventsByName(@QueryParam("name") String name, @QueryParam("page") String pageString,
            @QueryParam("pageSize") String pageSizeString) {
        int page = toInteger("page", pageString);
        int pageSize = toInteger("pageSize", pageSizeString);
        checkPageSizeRange(pageSize);

        return eventService.findEventsByName(name, page, pageSize);
    }

    @GET
    @Path("/{id}")
    public Event findEventById(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);

        try {
            return eventService.findEventById(id);
        } catch (NoSuchEventException e) {
            throw new ServiceException(NOT_FOUND, EVENT_NOT_FOUND, id);
        }
    }

    @POST
    @Path("/")
    public Response createEvent(final Event event, @Context UriInfo uriInfo) {
        try {
            eventService.createEvent(event);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(event.getId() + "").build();
            return Response.created(newLocation).build();
        } catch (EventExistsException e) {
            throw new ServiceException(CONFLICT, EVENT_ALREADY_EXISTS, event.getId());
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateEvent(@PathParam("id") String idString, Event event) {
        int id = toInteger("id", idString);
        checkIdAgreement(id, event.getId());

        try {
            eventService.updateEvent(event);
            return Response.noContent().build();
        } catch (NoSuchEventException e) {
            throw new ServiceException(NOT_FOUND, EVENT_NOT_FOUND, id);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") String idString) {
        int id = toInteger("id", idString);

        try {
            eventService.deleteEvent(id);
            return Response.noContent().build();
        } catch (NoSuchEventException ex) {
            throw new ServiceException(NOT_FOUND, EVENT_NOT_FOUND, id);
        }
    }

    @GET
    @Path("/{id}/comments")
    public List<Comment> findEventComments(@PathParam("id") String idString, @QueryParam("page") String pageString,
            @QueryParam("pageSize") String pageSizeString) {
        checkRequiredParameter("id", idString);
        int eventId = toInteger("id", idString);
        int page = toInteger("page", pageString);
        int pageSize = toInteger("pageSize", pageSizeString);
        checkPageSizeRange(pageSize);

        return eventService.findEventComments(eventId, page, pageSize);
    }

    @POST
    @Path("/{id}/comments")
    public void addComment(@PathParam("id") String idString, Comment eventComment) {
        int eventId = toInteger("id", idString);

        try {
            eventService.addComment(eventId, eventComment);
        } catch (CommentExistsException e) {
            throw new ServiceException(CONFLICT, COMMENT_ALREADY_EXISTS, eventComment.getId());
        }
    }

    @PUT
    @Path("/{id}/comments/{commentid}")
    public Response updateComment(@PathParam("id") String eventIdString, @PathParam("commentid") String commentIdString,
            Comment eventComment) {
        int eventId = toInteger("id", commentIdString);
        int commentId = toInteger("commentid", commentIdString);
        checkIdAgreement(commentId, eventComment.getId());

        try {
            eventService.updateComment(eventId, eventComment);
            return Response.noContent().build();
        } catch (NoSuchCommentException e) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, eventId);
        }
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    public Response deleteComment(@PathParam("id") String eventIdString, @PathParam("commentid") String commentIdString) {
        int eventId = toInteger("id", commentIdString);
        int commentId = toInteger("commentid", commentIdString);
        try {
            eventService.deleteComment(eventId, commentId);
            return Response.noContent().build();
        } catch (NoSuchEventException ex) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, eventId);
        }
    }
}
