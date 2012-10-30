package edu.lmu.cs.wutup.ws.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.lmu.cs.wutup.ws.exception.MalformedCoordinatesException;
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
    public Response resolveAddressToLatLong(
            @QueryParam("address") String address,
            @QueryParam("lat") Double lat,
            @QueryParam("lng") Double lng) {
        
        if (address != null && !address.equals("")) {
            return resolveAddress(address);
        } else {
            return resolveLatLong(lat, lng);
        }
    }
    
    private Response resolveAddress(String address) {
        LatLong response;
        
        try {
            // TODO: We need to expect that address has already had spaces replaced with + characters
            response = geocodeService
                    .resolveAddressToLatLong(address);
        } catch (NoAddressProvidedException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        
        if (response == null) {
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
        
        return Response
                .ok(response)
                .build();
    }
    
    private Response resolveLatLong(Double lat, Double lng) {
        String response;
        
        try {
            response = geocodeService
                    .resolveLatLongToAddress(lat, lng);
        } catch (MalformedCoordinatesException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .build();
        }
        
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
