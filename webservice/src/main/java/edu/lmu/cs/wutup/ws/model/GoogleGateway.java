package edu.lmu.cs.wutup.ws.model;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GoogleGateway {

    public static String geocodeAddressToLatLong(String address) throws ClientProtocolException, IOException {
        return stringifyEntity(executeRequest(constructAddressResolutionUrl(address)));
    }
    
    public static JSONObject parseJSONResponseToLocation(JSONObject j) throws JSONException {
        return ((JSONObject) j
                .getJSONArray("results")
                .get(0))
                .getJSONObject("geometry")
                .getJSONObject("location");
    }
    
    private static HttpEntity executeRequest(String address) throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(address);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        
        return entity;
    }
    
    private static String stringifyEntity(HttpEntity entity) throws ParseException, IOException {
        return EntityUtils.toString(entity);
    }
    
    //TODO: Spaces will need to be replaced with + characters client-side
    private static String constructAddressResolutionUrl(String address) {
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + 
                address.replaceAll("\\s+", "+") + "&sensor=true";
    }
}
