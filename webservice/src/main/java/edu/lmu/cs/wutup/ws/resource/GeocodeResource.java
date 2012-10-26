package edu.lmu.cs.wutup.ws.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;
import edu.lmu.cs.wutup.ws.service.GeocodeServiceImpl;

@Component
@Path("/geocode")
public class GeocodeResource {

    @Autowired
    GeocodeServiceImpl geocodeService;
    
    @GET
    @Produces({"application/json"})
    public Response resolveAddressToLatLong(@QueryParam("address") String address) throws NoAddressProvidedException {
        if (address == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        
        // TODO: We need to expect that address has already had spaces replaced with + characters
        LatLong response = geocodeService
                .resolveAddressToLatLong(address);
        
        if (response == null) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
        
        return Response
                .ok(response)
                .build();
    }
    
    
}
