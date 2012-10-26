package edu.lmu.cs.wutup.ws.service;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import static edu.lmu.cs.wutup.ws.model.GoogleGateway.parseJSONResponseToLocation;

public class GeocodeServiceTest {

    GeocodeServiceImpl service = new GeocodeServiceImpl();

    @Test(expected=NoAddressProvidedException.class)
    public void geocodeBlankAddressThrowsException() {
        service.resolveAddressToLatLong("");
    }
    
    @Test(expected=NoAddressProvidedException.class)
    public void geocodeNullAddressThrowsException() {
        service.resolveAddressToLatLong(null);
    }

    @Test
    public void googleGatewayProperlyParsesGoogleJSONResponse() throws JSONException {
        JSONObject j = parseJSONResponseToLocation(new JSONObject("{\"results\" : [{\"geometry\" :" +
                "{\"location\" : {\"lat\" : 33.9682680,\"lng\" : -118.4219410}}" + 
                "}],\"status\" : \"OK\"}"));
        
        assertThat(j.getDouble("lat"), is(33.9682680));
        assertThat(j.getDouble("lng"), is(-118.4219410));
    }
    
}
