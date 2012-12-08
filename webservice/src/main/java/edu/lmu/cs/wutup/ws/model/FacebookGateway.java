package edu.lmu.cs.wutup.ws.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Random;

import javax.ws.rs.core.Response;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;

import edu.lmu.cs.wutup.ws.exception.FBAccessTokenMissingException;
import edu.lmu.cs.wutup.ws.exception.MissingUserFBIdException;
import edu.lmu.cs.wutup.ws.exception.RequiredFBNameOrStartTimeMissingException;

public class FacebookGateway extends AbstractGateway {
    public static String acquireAccessToken(String code, String redirectUri) throws ClientProtocolException, IOException {
        return stringifyEntity(executeGetRequest(constructAccessTokenLandingUrl(code, redirectUri)));
    }

    public static String acquireUserEvents(String accessToken) throws ParseException, ClientProtocolException,
            IOException {
        return stringifyEntity(executeGetRequest(constructGetEventsUrl(accessToken)));
    }

    public static String acquireResource(String accessToken, String fbResourceId) throws ParseException, ClientProtocolException, IOException {
        return stringifyEntity(executeGetRequest(constructGetResourceByIdUrl(accessToken, fbResourceId)));
    }
    
    public static String createUserEvent(String accessToken, String userFBId, String name, DateTime start,
            DateTime end, String description, String location, String FBLocationId, String privacyType)
            throws RequiredFBNameOrStartTimeMissingException, FBAccessTokenMissingException, ParseException,
            ClientProtocolException, IOException {

        if (accessToken == null) {
            throw new FBAccessTokenMissingException();
        }
        
        if (userFBId == null) {
            throw new MissingUserFBIdException();
        }

        if (name == null || start == null) {
            throw new RequiredFBNameOrStartTimeMissingException();
        }

        return stringifyEntity(executePostRequest(constructPostEventUrl(accessToken, userFBId, name, start, end, description,
                location, FBLocationId, privacyType)));
    }

    private static String constructAccessTokenLandingUrl(String code, String redirectUri) throws UnsupportedEncodingException {
        return "https://graph.facebook.com/oauth/access_token?" + "client_id=" + System.getenv("WUTUP_FB_APP_ID")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, "ISO-8859-1")
                + "&client_secret=" + System.getenv("WUTUP_FB_APP_SECRET") + "&code=" + code;
    }
    
    public static Response acquireFBCode(String redirectUri) throws ParseException, ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException {
        return Response.seeOther(new URI(constructAuthDialogUrl(redirectUri))).build();
    }
    
    private static String constructAuthDialogUrl(String redirectUri) throws UnsupportedEncodingException {
        return "https://www.facebook.com/dialog/oauth?"
                + "client_id=" + System.getenv("WUTUP_FB_APP_ID")
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, "ISO-8859-1")
                + "&scope=user_events,create_event,email" + "&state=" + Math.abs(new Random().nextInt());
    }

    private static String constructGetEventsUrl(String accessToken) {
        return "https://graph.facebook.com/me/events?access_token=" + accessToken;
    }
    
    private static String constructGetResourceByIdUrl(String accessToken, String fbResourceId) {
        return "https://graph.facebook.com/" + fbResourceId + "?access_token=" + accessToken;
    }

    private static String constructPostEventUrl(String accessToken, String userFBId, String name, DateTime start,
            DateTime end, String description, String location, String FBLocationId, String privacyType) throws UnsupportedEncodingException {

        return "https://graph.facebook.com/" + userFBId + "/events?"
                + "access_token=" + accessToken
                + "&name=" + URLEncoder.encode(name, "ISO-8859-1")
                + "&start_time=" + URLEncoder.encode(start.toString(), "ISO-8859-1")
                + ((end != null) ? "&end_time=" + URLEncoder.encode(end.toString(), "ISO-8859-1")  : "")
                + ((description != null) ? "&description=" + URLEncoder.encode(description, "ISO-8859-1") : "")
                + ((location != null) ? "&location=" + URLEncoder.encode(location, "ISO-8859-1") : "")
                + ((FBLocationId != null) ? "&location_id=" + URLEncoder.encode(FBLocationId, "ISO-8859-1") : "")
                + ((privacyType != null) ? "&privacy_type=" + URLEncoder.encode(privacyType, "ISO-8859-1") : "");
    }
}
