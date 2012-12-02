package edu.lmu.cs.wutup.ws.resource;

import static edu.lmu.cs.wutup.ws.service.FBAuthServiceImpl.getAccessToken;
import static edu.lmu.cs.wutup.ws.service.FBAuthServiceImpl.fetchFBCode;
import static edu.lmu.cs.wutup.ws.service.FBAuthServiceImpl.getUserEvents;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.service.UserService;

@Component
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
@Path("/auth")
public class FBAuthResource {

    @Autowired
    UserService userService;

    @GET
    @Path("/facebook/events")
    public Response getFBEvents(
            @DefaultValue("") @QueryParam("state") String state,
            @DefaultValue("") @QueryParam("code") String code,
            @DefaultValue("") @QueryParam("error_reason") String errorReason,
            @DefaultValue("") @QueryParam("error") String error,
            @DefaultValue("") @QueryParam("error_description") String errorDescription) {

        if (!error.equals("")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();

        } else if (code.equals("")) {
            try {
                return fetchFBCode("http://localhost:8080/wutup/auth/facebook/events");

            } catch (Exception e) {
                e.printStackTrace();
                return Response.serverError().build();
            }
        }

        try {
            return Response
                    .ok(getUserEvents(getAccessToken(code, "http://localhost:8080/wutup/auth/facebook/events")))
                    .build();

        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{sessionId}")
    public Response retrieveUserBySessionId(
            @DefaultValue("") @PathParam("sessionId") String sessionId) {

        try {
            return Response
                    .ok(userService.findUserBySessionId(sessionId))
                    .build();

        } catch (NoSuchUserException e) {
            return Response
                    .ok("{}")
                    .build();
        }
    }
}
