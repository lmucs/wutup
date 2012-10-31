package edu.lmu.cs.wutup.ws.model;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GoogleGateway extends AbstractGateway {

    public static String geocodeAddressToLatLong(String address) throws ClientProtocolException, IOException {
        return stringifyEntity(executeRequest(constructAddressResolutionUrl(address)));
    }
    
    public static String geocodeCoordinatesToAddress(Double lat, Double lng) throws ParseException, ClientProtocolException, IOException {
        return stringifyEntity(executeRequest(constructCoordinateResolutionUrl(lat, lng)));
    }
    
    //TODO: Revise method to account for the response Google hands back for reverse geocoding
    public static JSONObject parseJSONResponseToLocation(JSONObject j) throws JSONException {
        return ((JSONObject) j
                .getJSONArray("results")
                .get(0))
                .getJSONObject("geometry")
                .getJSONObject("location");
    }
    
    public static String extractAddressFromJSON(JSONObject j) throws JSONException {
        return (String) ((JSONObject) j
                .getJSONArray("results")
                .get(0))
                .get("formatted_address");
    }
    
    //TODO: Spaces will need to be replaced with + characters client-side
    private static String constructAddressResolutionUrl(String address) {
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + 
                address.replaceAll("\\s+", "+") + "&sensor=true";
    }
    
    private static String constructCoordinateResolutionUrl(Double lat, Double lng) {
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + lat + "," + lng + "&sensor=true";
    }
}
