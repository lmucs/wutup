package edu.lmu.cs.wutup.ws.service;

import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireFBCode;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireUserEvents;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.createUserEvent;
import static edu.lmu.cs.wutup.ws.service.GeocodeServiceImpl.resolveVenue;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;

import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.FacebookGateway;

@Service
@Transactional
public class FBAuthServiceImpl implements FBAuthService {
    private static final Pattern accessTokenPattern = Pattern.compile("(?<=access_token=)[A-z0-9_-]+(?=&)");

    @Override
    public String getAccessToken(String code, String redirectUri) throws IOException {
        return extractAccessToken(FacebookGateway.acquireAccessToken(code, redirectUri));
    }

    @Override
    public Response fetchFBCode(String redirectUri) throws ParseException, ClientProtocolException,
            UnsupportedEncodingException, IOException, URISyntaxException {
        return acquireFBCode(redirectUri);
    }

    private String extractAccessToken(String tokenContainer) {
        Matcher m = accessTokenPattern.matcher(tokenContainer);

        if (m.find()) {
            return m.group(0);
        }

        // TODO: We need to protect against this by returning the proper HTTP
        // response code
        return null;
    }

    @Override
    public String getUserNameFromFB(String accessToken) {
        return new DefaultFacebookClient(accessToken).fetchObject("me", User.class).getName();
    }

    @Override
    public String getUserIdFromFB(String accessToken) {
        return new DefaultFacebookClient(accessToken).fetchObject("me", User.class).getId();
    }

    @Override
    public String getUserEvents(String accessToken) throws ParseException, ClientProtocolException, IOException {
        return acquireUserEvents(accessToken);
    }

    @Override
    public edu.lmu.cs.wutup.ws.model.User syncUser(String accessToken, edu.lmu.cs.wutup.ws.model.User u) {
        // TODO: Under construction.

        try {
            JSONArray events = new JSONObject(getUserEvents(accessToken)).getJSONArray("data");

            EventOccurrence e;
            for (int x = 0; x < events.length(); x++) {
                // TODO: Check DB to see if Event
                // exists. If so, use that.
                // If not, construct a new one and put all this stuff into the database.
                JSONObject current = events.getJSONObject(x);
                e = new EventOccurrence(new Event(null, current.getString("name"), current.getString("name"), u),
                        resolveVenue(current.getString("location"), null, null), new DateTime(
                                current.getString("start_time")));
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

    @Override
    public String postUserEvent(String accessToken, String name, DateTime start, DateTime end,
            String description, String location, String FBLocationId, String privacyType) {

        try {
            return createUserEvent(accessToken, getUserIdFromFB(accessToken), name, start, end, description, location,
                    FBLocationId, privacyType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
