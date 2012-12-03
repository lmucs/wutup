package edu.lmu.cs.wutup.ws.model;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GoogleGateway extends AbstractGateway {

    public static String geocodeAddressToLatLong(String address) throws ClientProtocolException, IOException {
        return stringifyEntity(executeGetRequest(constructAddressResolutionUrl(address)));
    }
    
    public static String geocodeCoordinatesToAddress(Double lat, Double lng) throws ParseException, ClientProtocolException, IOException {
        return stringifyEntity(executeGetRequest(constructCoordinateResolutionUrl(lat, lng)));
    }
    
    //TODO: Revise method to account for the response Google hands back for reverse geocoding
    public static JSONObject extractLocationFromJSON(JSONObject j) throws JSONException {
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
    
    public static String extractNameFromJSON(JSONObject j) throws JSONException {
        return filterNameByTypes(j, "point_of_interest", "establishment");
    }
    
    private static String filterNameByTypes(JSONObject j, String ... type) throws JSONException {
        JSONArray addressComponents = ((JSONObject) j
                .getJSONArray("results")
                .get(0))
                .getJSONArray("address_components");
        
        for (int w = 0; w < type.length; w++) {
            for (int x = 0; x < addressComponents.length(); x++) {
                JSONArray types = ((JSONObject) addressComponents.get(x)).getJSONArray("types");
                for (int y = 0; y < types.length(); y++) {
                    if(((String) types.get(y)).equals(type[w])) {
                        return (String) ((JSONObject) addressComponents.get(x)).get("long_name");
                    }
                }
            }
        }
        
        return extractAddressFromJSON(j);
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
