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

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;
import edu.lmu.cs.wutup.ws.model.Comment;
import edu.lmu.cs.wutup.ws.model.PaginationData;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.UserService;

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/users")
public class UserResource extends AbstractWutupResource {

    private static final String MISSING_QUERY_PARAM = "Must provide query parameter %s.";
    private static final String USER_NOT_FOUND = "User %d does not exist.";
    private static final String USER_NOT_FOUND_BY_QUERY = "No User found by specified query parameters";
    private static final String USER_ALREADY_EXISTS = "User %d already exists";
    private static final String USER_UNDER_SPECIFIED = "User is not sufficiently specified for creation.";

    @Autowired
    UserService userService;

    @POST
    @Path("/")
    public Response createUser(User u, @Context UriInfo uriInfo) {
        // User id will be automatically generated
        u.setId(null);
        try {
            checkUserHasRequiredFields(u);
            userService.createUser(u);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(u.getId() + "").build();
            return Response.created(newLocation).build();
        } catch (UserExistsException e) {
            throw new ServiceException(CONFLICT, USER_ALREADY_EXISTS, u.getId());
        }
    }

    @GET
    @Path("/")
    public User findUserByFacebookId(@DefaultValue("") @QueryParam("fbId") String idString) {
        if (idString.equals("")) {
            throw new ServiceException(BAD_REQUEST, MISSING_QUERY_PARAM, "fbId");
        }

        try {
            return userService.findUserByFacebookId(idString);
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND_BY_QUERY);
        }
    }

    @PATCH
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String idString, User u) {
        int id = toInteger("id", idString);
        checkIdAgreement(id, u.getId());
        u.setId(Integer.valueOf(id));
        try {
            userService.updateUser(u);
            return Response.noContent().build();
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, id);
        }
    }

    @GET
    @Path("/{id}")
    public User findUserById(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);
        try {
            return userService.findUserById(id);
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, id);
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") String idString) {
        checkRequiredParameter("id", idString);
        int id = toInteger("id", idString);
        try {
            userService.deleteUser(id);
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, id);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/comments")
    public List<Comment> findCommentsByUserId(@PathParam("id") String idString,
            @QueryParam("page") @DefaultValue(DEFAULT_PAGE) String pageString,
            @QueryParam("pageSize") @DefaultValue(DEFAULT_PAGE_SIZE) String pageSizeString) {
        int id = toIntegerRequired("id", idString);
        PaginationData pagination = paginationDataFor(pageString, pageSizeString);
        try {
            User existingUser = userService.findUserById(id);
            return userService.findCommentsByUser(existingUser, pagination);
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, id);
        }
    }

    public void checkUserHasRequiredFields(User u) {
        if (u.getEmail() == null || u.getFirstName() == null || u.getLastName() == null) {
            throw new ServiceException(BAD_REQUEST, USER_UNDER_SPECIFIED);
        }
    }
}
