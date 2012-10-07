package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws.service.UserService;

@Path("/users/")
public class UserResource extends AbstractWutupResource {

    private static final String USER_NOT_FOUND = "User %d does not exist.";

    @Autowired
    UserService userService;

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/")
    public Response createUser(User u) {
        //TODO need to automate user ids
        return null;
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

    @DELETE
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") String idString) {
        int id = toInteger("id", idString);

        try {
            User u = userService.findUserById(id);
            userService.deleteUser(u);
        } catch (NoSuchUserException e) {
            throw new ServiceException(NOT_FOUND, USER_NOT_FOUND, id);
        }
        return Response.noContent().build();
    }
}
