package edu.lmu.cs.wutup.ws.service;

import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireFBCode;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireUserEvents;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.createUserEvent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;

import edu.lmu.cs.wutup.ws.model.FacebookGateway;

@Service
@Transactional
public class FBAuthServiceImpl {
    private static final Pattern accessTokenPattern = Pattern.compile("(?<=access_token=)[A-z0-9_-]+(?=&)");
    
    public static String getAccessToken(String code, String redirectUri) throws IOException {
        return extractAccessToken(FacebookGateway.acquireAccessToken(code, redirectUri));
    }
    
    public static Response fetchFBCode(String redirectUri) throws ParseException, ClientProtocolException, UnsupportedEncodingException, IOException, URISyntaxException {
        return acquireFBCode(redirectUri);
    }
    
    private static String extractAccessToken(String tokenContainer) {
        Matcher m = accessTokenPattern.matcher(tokenContainer);

        if (m.find()) {
            return m.group(0);
        }
        
        //TODO: We need to protect against this by returning the proper HTTP response code
        return null;
    }
    
    public static String getUserNameFromFB(String accessToken) {
        return new DefaultFacebookClient(accessToken)
                .fetchObject("me", User.class)
                .getName();
    }
    
    public static String getUserIdFromFB(String accessToken) {
        return new DefaultFacebookClient(accessToken)
                .fetchObject("me", User.class)
                .getId();
    }
    
    public static String getUserEvents(String accessToken) throws ParseException, ClientProtocolException, IOException {
        return acquireUserEvents(accessToken);
    }
    
    public static edu.lmu.cs.wutup.ws.model.User syncUser(String accessToken, edu.lmu.cs.wutup.ws.model.User u) {
        if (!getUserNameFromFB(accessToken).equals(u.getFirstName() + " " + u.getLastName())) {
            return u;
        }
        
        try {
            JSONArray events = new JSONObject(getUserEvents(accessToken)).getJSONArray("data");
            
            for (int x = 0; x < events.length(); x++) {
                System.out.println(events.getJSONObject(x).getString("name"));
            }
            
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return u;
    }
    
    public static String postUserEvent(String accessToken, String name, DateTime start,
            DateTime end, String description, String location, String FBLocationId, String privacyType) {
        
        try {
            return createUserEvent(accessToken, getUserIdFromFB(accessToken), name, start, end, description, location, FBLocationId, privacyType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
