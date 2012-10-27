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
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.NoSuchVenueException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.exception.VenueExistsException;
import edu.lmu.cs.wutup.ws.model.Circle;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.Venue;
import edu.lmu.cs.wutup.ws.service.VenueService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/venues")
public class VenueResource extends AbstractWutupResource {

    private static final String VENUE_NOT_FOUND = "Venue %d does not exist";
    private static final String VENUE_ALREADY_EXISTS = "Venue %d already exists";
    private static final String COMMENT_NOT_FOUND = "Comment %d does not exist for venue %d";
    private static final String COMMENT_ALREADY_EXISTS = "Comment %d already exists for venue %d";

    @Autowired
    VenueService venueService;

    @GET
    @Path("/")
    public List<Venue> findVenues(
            @QueryParam("name") String name,
            @QueryParam("event") String eventIdString,
            @QueryParam("center") String center,
            @QueryParam("radius") String radiusString,
            @QueryParam("page") String pageString,
            @QueryParam("pageSize") String pageSizeString) {

        Integer eventId = eventIdString == null ? null : toInteger("event", eventIdString);
        Circle circle = fromCenterAndRadiusParameters(center, radiusString);
        PaginationData pagination = paginationDataFor(pageString, pageSizeString);

        return venueService.findVenues(name, eventId, circle, pagination);
    }

    @GET
    @Path("/{id}")
    public Venue findVenueById(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);

        try {
            return venueService.findVenueById(id);
        } catch (NoSuchVenueException e) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, id);
        }
    }

    @POST
    @Path("/")
    public Response createVenue(final Venue venue, @Context UriInfo uriInfo) {
        try {
            venueService.createVenue(venue);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(venue.getId() + "").build();
            return Response.created(newLocation).build();
        } catch (VenueExistsException e) {
            throw new ServiceException(CONFLICT, VENUE_ALREADY_EXISTS, venue.getId());
        }
    }

    @PATCH
    @Path("/{id}")
    public Response updateVenue(@PathParam("id") String idString, Venue venue) {
        int id = toInteger("id", idString);
        checkIdAgreement(id, venue.getId());

        try {
            venue.setId(id);
            venueService.updateVenue(venue);
            return Response.noContent().build();
        } catch (NoSuchEventException e) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, id);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEvent(@PathParam("id") String idString) {
        int id = toInteger("id", idString);

        try {
            venueService.deleteVenue(id);
            return Response.noContent().build();
        } catch (NoSuchEventException ex) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, id);
        }
    }

    @GET
    @Path("/{id}/comments")
    public List<Comment> findVenueComments(@PathParam("id") String idString, @QueryParam("page") String pageString,
            @QueryParam("pageSize") String pageSizeString) {
        checkRequiredParameter("id", idString);
        int venueId = toInteger("id", idString);
        PaginationData pagination = paginationDataFor(pageString, pageSizeString);

        return venueService.findComments(venueId, pagination);
    }

    @POST
    @Path("/{id}/comments")
    public void addComment(@PathParam("id") String idString, Comment venueComment) {
        int venueId = toInteger("id", idString);

        try {
            venueService.addComment(venueId, venueComment);
        } catch (CommentExistsException e) {
            throw new ServiceException(CONFLICT, COMMENT_ALREADY_EXISTS, venueComment.getId());
        }
    }

    @PUT
    @Path("/{id}/comments/{commentid}")
    public Response updateComment(@PathParam("id") String venueIdString, @PathParam("commentid") String commentIdString,
            Comment venueComment) {
        int venueId = toInteger("id", commentIdString);
        int commentId = toInteger("commentid", commentIdString);
        checkIdAgreement(commentId, venueComment.getId());

        try {
            venueService.updateComment(venueId, venueComment);
            return Response.noContent().build();
        } catch (NoSuchCommentException e) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, venueId);
        }
    }

    @DELETE
    @Path("/{id}/comments/{commentid}")
    public Response deleteComment(@PathParam("id") String venueIdString, @PathParam("commentid") String commentIdString) {
        int venueId = toInteger("id", commentIdString);
        int commentId = toInteger("commentid", commentIdString);
        try {
            venueService.deleteComment(venueId, commentId);
            return Response.noContent().build();
        } catch (NoSuchVenueException ex) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, venueId);
        }
    }
}
