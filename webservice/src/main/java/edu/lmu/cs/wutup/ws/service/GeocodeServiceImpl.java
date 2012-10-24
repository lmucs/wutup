package edu.lmu.cs.wutup.ws.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.lmu.cs.wutup.ws.exception.NoAddressProvidedException;
import edu.lmu.cs.wutup.ws.model.LatLong;

@Service
public class GeocodeServiceImpl implements GeocodeService {
    public LatLong resolveAddressToLatLong(String address) throws NoAddressProvidedException {
        
        if (address.equals("") || address == null) {
            throw new NoAddressProvidedException();
        }
        
        // TODO: Spaces will need to be replaced with + characters client-side
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + 
                address.replaceAll("\\s+", "+") + "&sensor=true";
        
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            
            JSONObject r = new JSONObject(EntityUtils.toString(entity));
            
            return new LatLong(
                    ((JSONObject) (r.getJSONArray("results")
                        .get(0)))
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                        .getDouble("lat"),
                        
                    ((JSONObject) (r.getJSONArray("results")
                        .get(0)))
                        .getJSONObject("geometry")
                        .getJSONObject("location")
                        .getDouble("lng"));

        } catch (Exception e) {
            // TODO: Properly handle individual exceptions
            e.printStackTrace();
            return null;
        }
        
    }
}
