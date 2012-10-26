package edu.lmu.cs.wutup.ws.service;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.GoogleGateway;
import edu.lmu.cs.wutup.ws.model.LatLong;

@Service
public class GeocodeServiceImpl implements GeocodeService {
    public LatLong resolveAddressToLatLong(String address) {
        
        if (address == null || address.equals("")) {
            throw new NoAddressProvidedException();
        }
        
        try {
            JSONObject r = GoogleGateway.parseJSONResponseToLocation(
                    new JSONObject(
                        GoogleGateway.geocodeAddressToLatLong(address)
                    )
                );
            
            return new LatLong(r.getDouble("lat"), r.getDouble("lng"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
