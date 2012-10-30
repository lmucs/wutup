package edu.lmu.cs.wutup.ws.service;

import static edu.lmu.cs.wutup.ws.model.GoogleGateway.extractAddressFromJSON;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.geocodeAddressToLatLong;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.geocodeCoordinatesToAddress;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.parseJSONResponseToLocation;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.lmu.cs.wutup.ws.exception.MalformedCoordinatesException;
import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;

@Service
public class GeocodeServiceImpl implements GeocodeService {
    public LatLong resolveAddressToLatLong(String address) throws NoAddressProvidedException {

        if (address == null || address == "") {
            throw new NoAddressProvidedException();
        }
        
        try {
            JSONObject r = parseJSONResponseToLocation(new JSONObject(
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
}
