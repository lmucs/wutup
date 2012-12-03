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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.lmu.cs.wutup.ws.exception.MalformedCoordinatesException;
import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;
import edu.lmu.cs.wutup.ws.model.Venue;

@Service
@Transactional
public class GeocodeServiceImpl implements GeocodeService {
    public LatLong resolveAddressToLatLong(String address) throws NoAddressProvidedException {

        if (address == null || address == "") {
            throw new NoAddressProvidedException();
        }
        
        try {
            JSONObject r = extractLocationFromJSON(new JSONObject(
                    geocodeAddressToLatLong(address)));

            return new LatLong(r.getDouble("lat"), r.getDouble("lng"));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
        Venue v = new Venue();
        String resolvedName, resolvedAddress;
        Double resolvedLat, resolvedLng;
        LatLong location;
        
        if (lat != null && lng != null) {
            resolvedAddress = resolveLatLongToAddress(lat, lng);
            location = resolveAddressToLatLong(resolvedAddress);
        } else if (address != null) {
            location = resolveAddressToLatLong(address);
            resolvedAddress = resolveLatLongToAddress(location.latitude, location.longitude);
        } else {
            return null;
        }
        
        resolvedLat = location.latitude;
        resolvedLng = location.longitude;
        resolvedName = extractNameFromJSON(new JSONObject(geocodeAddressToLatLong(resolvedAddress)));
        v.setAddress(resolvedAddress);
        v.setLatitude(resolvedLat);
        v.setLongitude(resolvedLng);
        v.setName(resolvedName);
        
        return v;
    }
}
