package edu.lmu.cs.wutup.ws.service;

import static edu.lmu.cs.wutup.ws.model.GoogleGateway.extractAddressFromJSON;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.extractLocationFromJSON;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.extractNameFromJSON;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.geocodeAddressToLatLong;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.geocodeCoordinatesToAddress;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.exception.LocationNotFoundByGoogleException;
import edu.lmu.cs.wutup.ws.exception.MalformedCoordinatesException;
import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;
import edu.lmu.cs.wutup.ws.model.Venue;

@Service
@Transactional
public class GeocodeServiceImpl implements GeocodeService {
    @Autowired
    VenueService venueService;
    
    public LatLong resolveAddressToLatLong(String address) throws NoAddressProvidedException, LocationNotFoundByGoogleException, IOException {

        if (address == null || address == "") {
            throw new NoAddressProvidedException();
        }
        
        try {
            JSONObject r = extractLocationFromJSON(new JSONObject(
                    geocodeAddressToLatLong(address)));

            return new LatLong(r.getDouble("lat"), r.getDouble("lng"));

        } catch (JSONException e) {
            throw new LocationNotFoundByGoogleException();
        }
    }

    public String resolveLatLongToAddress(Double lat, Double lng) throws MalformedCoordinatesException {

        if (lat == null || lng == null || lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new MalformedCoordinatesException();
        }
        
        try {
            return extractAddressFromJSON(new JSONObject(geocodeCoordinatesToAddress(lat, lng)));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public Venue resolveVenue(String address, Double lat, Double lng) throws ClientProtocolException, JSONException, IOException {
        if (address == null && (lat == null || lng == null)) {
            throw new NoAddressProvidedException();
        }
        
        Venue v = new Venue();
        String resolvedName, resolvedAddress;
        Double resolvedLat, resolvedLng;
        LatLong location;
        
        try {
            if (lat != null && lng != null) {
                // TODO: Handle resolveLatLongToAddress failure with exception
                resolvedAddress = resolveLatLongToAddress(lat, lng);
                location = resolveAddressToLatLong(resolvedAddress);
            } else if (address != null) {
                location = resolveAddressToLatLong(address);
                resolvedAddress = resolveLatLongToAddress(location.latitude, location.longitude);
            } else {
                return null;
            }
        } catch (LocationNotFoundByGoogleException e) {
            v.setAddress(address);
            v.setLatitude(null);
            v.setLongitude(null);
            v.setName(null);
            
            return v;
        }
        
        resolvedLat = location.latitude;
        resolvedLng = location.longitude;
        resolvedName = extractNameFromJSON(new JSONObject(geocodeAddressToLatLong(resolvedAddress)));
        v.setAddress(resolvedAddress);
        v.setLatitude(resolvedLat);
        v.setLongitude(resolvedLng);
        v.setName(resolvedName);

//        venueService.createVenue(v);

        return v;
    }
}
