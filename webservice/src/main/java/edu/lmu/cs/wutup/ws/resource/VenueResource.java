package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.net.URI;
import java.util.List;
import java.util.Map;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.NoSuchPropertyException;
import edu.lmu.cs.wutup.ws.exception.NoSuchResourceException;
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
    private static final String VENUE_ALREADY_EXISTS = "Vensue %d already exists";
    private static final String COMMENT_NOT_FOUND = "Comment %d does not exist for venue %d";
    private static final String PROPERTY_VENUE_ID_CONFLICT = "Property does not exist for Venue %d, cannot be deleted";
    private static final String INVALID_NUM_OF_PROPERTIES = "Only one property key and value pair must be specified";

    @Autowired
    VenueService venueService;

    @GET
    @Path("/")
    public List<Venue> findVenues(@QueryParam("name") String name, @QueryParam("event") String eventIdString,
            @QueryParam("center") String center, @QueryParam("radius") String radiusString,
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE) String pageString,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) String pageSizeString) {

        Integer eventId = toInteger("event", eventIdString);
        Circle circle = fromCenterAndRadiusParameters(center, radiusString);
        PaginationData pagination = paginationDataFor(pageString, pageSizeString);

        return venueService.findVenues(name, eventId, circle, pagination);
    }

    @GET
    @Path("/{id}")
    public Venue findVenueById(@PathParam("id") String idString) {
        int id = toIntegerRequired("id", idString);
        try {
            return venueService.findVenueById(id);
        } catch (NoSuchVenueException e) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, id);
        }
    }

    @POST
    @Path("/")
    public Response createVenue(Venue venue, @Context UriInfo uriInfo) {
        logger.debug("Creating venue");
        venue.setId(null);
        preprocessVenue(venue);
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

        int id = toIntegerRequired("id", idString);
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
    public Response deleteVenue(@PathParam("id") String idString) {

        int id = toIntegerRequired("id", idString);
        try {
            venueService.deleteVenue(id);
            return Response.noContent().build();
        } catch (NoSuchEventException ex) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, id);
        }
    }

    @GET
    @Path("/{id}/comments")
    public List<Comment> findVenueComments(@PathParam("id") String idString,
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE) String pageString,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) String pageSizeString) {

        int venueId = toIntegerRequired("id", idString);
        PaginationData pagination = paginationDataFor(pageString, pageSizeString);

        return venueService.findComments(venueId, pagination);
    }

    @POST
    @Path("/{id}/comments")
    public void addComment(@PathParam("id") String idString, Comment venueComment) {

        int venueId = toIntegerRequired("id", idString);
        try {
            venueService.addComment(venueId, venueComment);
        } catch (NoSuchResourceException e) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, venueId);
        }
    }

    @PUT
    @Path("/{id}/comments/{commentid}")
    public Response updateComment(@PathParam("id") String venueIdString,
            @PathParam("commentid") String commentIdString, Comment venueComment) {

        int venueId = toIntegerRequired("id", commentIdString);
        int commentId = toIntegerRequired("commentid", commentIdString);
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

        int venueId = toIntegerRequired("id", venueIdString);
        int commentId = toIntegerRequired("commentid", commentIdString);
        try {
            venueService.deleteComment(venueId, commentId);
            return Response.noContent().build();
        } catch (NoSuchCommentException ex) {
            throw new ServiceException(NOT_FOUND, COMMENT_NOT_FOUND, commentId, venueId);
        }
    }

    @GET
    @Path("/{id}/properties")
    public Map<String, String> getProperties(@PathParam("id") String venueIdString) {
        int venueId = toIntegerRequired("id", venueIdString);
        try {
            Venue existingVenue = venueService.findVenueById(venueId);
            return venueService.findProperties(existingVenue.getId());
        } catch (NoSuchVenueException e) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, venueId);
        }
    }

    @POST
    @Path("/{id}/properties")
    public Response updateProperty(@PathParam("id") String venueIdString, Map<String, String> keyValuePair) {
        validateMapSize(keyValuePair, 1);
        int venueId = toIntegerRequired("id", venueIdString);
        try {
            Venue v = venueService.findVenueById(venueId);
            venueService.updateOrAddProperty(v.getId(), keyValuePair);
            return Response.noContent().build();
        } catch (NoSuchPropertyException e) {
            throw new ServiceException(CONFLICT, PROPERTY_VENUE_ID_CONFLICT, venueId);
        } catch (NoSuchVenueException e) {
            throw new ServiceException(NOT_FOUND, VENUE_NOT_FOUND, venueId);
        }
    }

    public void validateMapSize(Map<String, String> map, int requiredSize) {
        if (map.isEmpty() || map.size() > 1) {
            throw new ServiceException(BAD_REQUEST, INVALID_NUM_OF_PROPERTIES);
        }
    }
}
