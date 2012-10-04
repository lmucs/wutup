package edu.lmu.cs.wutup.ws.resource;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Random;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.service.FacebookAuthenticationImpl;

@Component
@Path("/auth")
public class FacebookAuthenticationResource {
    FacebookAuthenticationImpl FacebookAuthentication = new FacebookAuthenticationImpl();
    
    @GET
    @Path("/facebook")
    public Response authenticate() {
        try {
            String redirectURI = "https://www.facebook.com/dialog/oauth?" +
                    "client_id=" + System.getenv("WUTUP_FB_APP_ID") +
                    "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/wutup/auth/landing", "ISO-8859-1") +
                    "&scope=user_events,create_event" +
                    "&state=" + Math.abs(new Random().nextInt());
            
            return Response
                    .seeOther(new URI(redirectURI))
                    .build();
        } catch (Exception e) {
            return Response
                    .serverError()
                    .build();
        }
        
    }
    
    @GET
    @Path("/landing")
    public Response handleFacebookAuthenticationResponse (
            @DefaultValue("") @QueryParam("state") String state,
            @DefaultValue("") @QueryParam("code") String code,
            @DefaultValue("") @QueryParam("access_token") String accessToken,
            @DefaultValue("") @QueryParam("expires") String accessTokenExpiry,
            @DefaultValue("") @QueryParam("error_reason") String errorReason,
            @DefaultValue("") @QueryParam("error") String error,
            @DefaultValue("") @QueryParam("error_description") String errorDescription
            ) {
        
        if (!error.equals("")) {
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        } else if (!code.equals("")) {
            try {
                FacebookAuthentication.getAccessToken(code);
            } catch (Exception e) {
                return Response
                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                        .build();
            }
        } else if (!accessToken.equals("")) {
            // Realistically, we want to store the access token and expiry in the DB here
            return Response
                    .ok(accessToken)
                    .build();
        }
        
        /*
         * We need to send a request to FB in order to exchange our code for an access token.
         * Once we have our access token, we will be golden.
         */
        
        return Response
                .ok(code)
                .build();
    }
}
