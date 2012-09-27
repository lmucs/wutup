package edu.lmu.cs.wutup.ws.resource;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.ServiceException;
import edu.lmu.cs.wutup.ws.model.User;
import edu.lmu.cs.wutup.ws. service.UserService;

import edu.lmu.cs.wutup.ws.exception.ServiceException;

@Path("/users/")
public class UserResource {
    
    private static final String USER_NOT_FOUND = "User %d does not exist.";
    private static final String USER_ALREADY_EXISTS = "User with id %d already exists.";
    private static final String PARAMETER_REQUIRED = "The parameter %s is required.";
    private static final String PARAMETER_NON_INTEGER = "The paramater %s must be an Integer.";
    private static final String PATH_BODY_CONFLICT = "Conflicting IDs in path and: %d does not match %d.";
    
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
    private static void checkRequiredParameter(String name, String value) {
        if (value == null || value.equals("")) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_REQUIRED, name);
        }
    }
    
    private static int toInteger(String name, String value) {
        checkRequiredParameter(name, value);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ServiceException(BAD_REQUEST, PARAMETER_NON_INTEGER, name);
        }
    }
    
    private static void checkIdAgreement(int id_path, int id_body) {
        if (id_path != id_body) {
            throw new ServiceException(CONFLICT, PATH_BODY_CONFLICT, id_path, id_body);
        }
    }

}
