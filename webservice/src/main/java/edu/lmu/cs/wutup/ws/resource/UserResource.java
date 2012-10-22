package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.UserExistsException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.UserService;

@Path("/users/")
public class UserResource extends AbstractWutupResource {

    private static final String USER_NOT_FOUND = "User %d does not exist.";
    private static final String USER_ALREADY_EXISTS = "User %d already exists";

    @Autowired
    UserService userService;

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/")
    public Response createUser(User u, @Context UriInfo uriInfo) {
        // User id will be automatically generated
        u.nullOutId();
        try {
            userService.createUser(u);
            URI newLocation = uriInfo.getAbsolutePathBuilder().path(u.getId() + "").build();
            return Response.created(newLocation).build();
        } catch (UserExistsException e) {
            throw new ServiceException(CONFLICT, USER_ALREADY_EXISTS, u.getId());
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response updateUser(@PathParam("id") String idString, User u) {
        int id = toInteger("id", idString);
        checkRequiredParameter("email", u.getEmail());
        checkIdAgreement(id,u.getId());
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
        int id = toInteger("id", idString);
        try {
            return userService.findUserById(id);
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, id);
        }
    }

    @DELETE
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
}
