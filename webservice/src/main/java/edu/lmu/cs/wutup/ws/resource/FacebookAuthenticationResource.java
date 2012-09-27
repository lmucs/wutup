package edu.lmu.cs.wutup.ws.resource;

import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component
@Path("/auth")
public class FacebookAuthenticationResource {
    @GET
    @Path("/facebook")
    public Response authenticate() throws URISyntaxException {
        return Response
                .temporaryRedirect(new java.net.URI("http://www.facebook.com/"))
                .build();
    }
}
