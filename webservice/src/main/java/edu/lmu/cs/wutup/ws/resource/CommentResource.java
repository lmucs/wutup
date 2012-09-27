package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
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
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.service.CommentService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Path("/Comments")
public class CommentResource {

    private static final String Comment_NOT_FOUND = "Comment %d does not exist";
    private static final String Comment_ALREADY_EXISTS = "Comment %d already exists";
    private static final String PARAMETER_REQUIRED = "The parameter %s is required";
    private static final String PARAMETER_NON_INTEGER = "The parameter %s should be an integer";
    private static final String PATH_BODY_CONFLICT = "Id %d in path differs from id %d in body";


    @Autowired
    CommentService CommentService;

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/")
    public Response createComment(final Comment Comment, @Context UriInfo uriInfo) {
        try {
            CommentService.createComment(Comment);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(Comment.getId() + "").build();
            return Response.created(newLocation).build();
        } catch (CommentExistsException e) {
            throw new ServiceException(CONFLICT, Comment_ALREADY_EXISTS, Comment.getId());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response updateComment(@PathParam("id") String idString, Comment Comment) {
        int id = toInteger("id", idString);
        checkIdAgreement(id, Comment.getId());

        try {
            CommentService.updateComment(Comment);
            return Response.noContent().build();
        } catch (NoSuchCommentException e) {
            throw new ServiceException(NOT_FOUND, Comment_NOT_FOUND, id);
        }
    }

    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Comment findCommentById(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);

        try {
            return CommentService.findCommentById(id);
        } catch (NoSuchCommentException e) {
            throw new ServiceException(NOT_FOUND, Comment_NOT_FOUND, id);
        }
    }
    
    @GET
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public List<Comment> findCommentsByUserId(@PathParam("id") String userIdString) {
        checkRequiredParameter("id", userIdString);
        int user = toInteger("id", userIdString);

        try {
            return CommentService.findCommentsByUserId(user);
        } catch (NoSuchCommentException e) {
            throw new ServiceException(NOT_FOUND, Comment_NOT_FOUND, user);
        }
    }

    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response deleteComment(@PathParam("id") String idString) {
        int id = toInteger("id", idString);

        try {
            Comment e = CommentService.findCommentById(id);
            CommentService.deleteComment(e);
            return Response.noContent().build();
        } catch (NoSuchCommentException ex) {
            throw new ServiceException(NOT_FOUND, Comment_NOT_FOUND, id);
        }
    }

    private void checkRequiredParameter(String name, String value) {
        if (value == null) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_REQUIRED, name);
        }

    }

    private int toInteger(String name, String value) {
        checkRequiredParameter(name, value);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_NON_INTEGER, name);
        }

    }

    private void checkIdAgreement(int idInPath, int idInBody) {
        if (idInPath != idInBody) {
            throw new ServiceException(CONFLICT, PATH_BODY_CONFLICT, idInPath, idInBody);
        }
    }
}
