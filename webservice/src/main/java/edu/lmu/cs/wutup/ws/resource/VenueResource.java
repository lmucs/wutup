package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.CommentExistsException;
import edu.lmu.cs.wutup.ws.exception.NoSuchCommentException;
import edu.lmu.cs.wutup.ws.exception.NoSuchVenueException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.service.VenueService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
@Path("/venues")
public class VenueResource extends AbstractWutupResource {
    private static final String VENUE_NOT_FOUND = "Venue %d does not exist";
    private static final String VENUE_ALREADY_EXISTS = "Venue %d already exists";
    private static final String COMMENT_NOT_FOUND = "Comment %d does not exist for venue %d";
    private static final String COMMENT_ALREADY_EXISTS = "Comment %d already exists for venue %d";

    @Autowired
    VenueService venueService;
    
    /* Begins the Comment implementation. */
    @GET
    @Path("/{id}/comments")
    public List<Comment> findVenueComments(@PathParam("id") String idString, @QueryParam("page") String pageString,
            @QueryParam("pageSize") String pageSizeString) {
        checkRequiredParameter("id", idString);
        int venueId = toInteger("id", idString);
        int page = toInteger("page", pageString);
        int pageSize = toInteger("pageSize", pageSizeString);
        checkPageSizeRange(pageSize);

        return venueService.findComments(venueId, page, pageSize);
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
