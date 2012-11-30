package edu.lmu.cs.wutup.ws.resource;

import static edu.lmu.cs.wutup.ws.service.FBAuthServiceImpl.getAccessToken;
import static edu.lmu.cs.wutup.ws.service.FBAuthServiceImpl.getUserNameFromFB;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Random;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

@Component
@Path("/auth")
public class FBAuthResource {

    @GET
    @Path("/facebook")
    public Response authenticate(@DefaultValue("bonkers") @QueryParam("sessionId") String sessionId) {
        if (sessionId.equals("bonkers")) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        try {
            String redirectURI = "https://www.facebook.com/dialog/oauth?"
                    + "client_id=" + System.getenv("WUTUP_FB_APP_ID")
                    + "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/wutup/auth/" + sessionId + "/landing", "ISO-8859-1")
                    + "&scope=user_events,create_event" + "&state=" + Math.abs(new Random().nextInt());

            return Response.seeOther(new URI(redirectURI)).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }

    }

    @GET
    @Path("/{sessionId}/landing")
    public Response handleFacebookAuthenticationResponse(
            @DefaultValue("") @QueryParam("state") String state,
            @DefaultValue("") @QueryParam("code") String code,
            @DefaultValue("") @QueryParam("error_reason") String errorReason,
            @DefaultValue("") @QueryParam("error") String error,
            @DefaultValue("") @QueryParam("error_description") String errorDescription,
            @DefaultValue("") @PathParam("sessionId") String sessionId) {

        if (!error.equals("")) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        try {
            return Response
                    .seeOther(new URI("http://localhost:8080/wutup/auth/test?code=" + code
                            +"&sessionId=" + sessionId))
                    .build();
//            return Response
//                    .ok(sessionId)
//                    .build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Response
                    .serverError()
                    .build();

        }
    }
    
    @GET
    @Path("/test")
    public Response testingResource(
            @QueryParam("code") String fbCode,
            @QueryParam("sessionId") String sessionId) {
        
        try {
//            return Response
//                    .ok(postUserEvent(getAccessToken(fbCode), "Brous house", new DateTime("2012-11-02"), null, null, null, null, null))
//                    .build();
            
            return Response
                    .ok(sessionId + "\n\n" + getUserNameFromFB(getAccessToken(fbCode, sessionId)))
                    .build();

//            return Response
//                    .ok(getUserEvents(getAccessToken(fbCode, sessionId)))
//                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
        
    }
}
