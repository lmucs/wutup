package edu.lmu.cs.wutup.ws.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONException;

import edu.lmu.cs.wutup.ws.exception.LocationNotFoundByGoogleException;
import edu.lmu.cs.wutup.ws.exception.MalformedCoordinatesException;
import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;
import edu.lmu.cs.wutup.ws.model.Venue;

public interface GeocodeService {

    public LatLong resolveAddressToLatLong(String address)throws NoAddressProvidedException, LocationNotFoundByGoogleException, IOException;

    public String resolveLatLongToAddress(Double lat, Double lng) throws MalformedCoordinatesException;
    
    public Venue resolveVenue(String address, Double lat, Double lng) throws ClientProtocolException, JSONException, IOException;

}
