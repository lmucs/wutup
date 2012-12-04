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
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;

import edu.lmu.cs.wutup.ws.exception.FBUserSynchronizationException;
import edu.lmu.cs.wutup.ws.exception.InvalidFBAccessTokenException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.FacebookGateway;

@Service
public class FBAuthServiceImpl implements FBAuthService {
    private static final Pattern accessTokenPattern = Pattern.compile("(?<=access_token=)[A-z0-9_-]+(?=&)");

    @Autowired
    GeocodeService geocodeService;

    @Autowired
    EventService eventService;

    @Autowired
    EventOccurrenceService occurrenceService;

    @Autowired
    UserService userService;

    @Override
    public String getAccessToken(String code, String redirectUri) throws IOException, InvalidFBAccessTokenException {
        return extractAccessToken(FacebookGateway.acquireAccessToken(code, redirectUri));
    }

    @Override
    public Response fetchFBCode(String redirectUri) throws ParseException, ClientProtocolException,
            UnsupportedEncodingException, IOException, URISyntaxException {
        return acquireFBCode(redirectUri);
    }

    private String extractAccessToken(String tokenContainer) throws InvalidFBAccessTokenException {
        Matcher m = accessTokenPattern.matcher(tokenContainer);

        if (m.find()) {
            return m.group(0);
        } else {
            throw new InvalidFBAccessTokenException();
        }
    }

    @Override
    public String getUserNameFromFB(User u) {
        return u.getName();
    }

    @Override
    public String getUserIdFromFB(User u) {
        return u.getId();
    }

    @Override
    public User getFBUser(String accessToken) {
        return new DefaultFacebookClient(accessToken).fetchObject("me", User.class);
    }

    @Override
    public String getUserEvents(String accessToken) throws ParseException, ClientProtocolException, IOException {
        return acquireUserEvents(accessToken);
    }

    @Override
    public edu.lmu.cs.wutup.ws.model.User findOrCreateFBUser(String accessToken, String fbId) {
        edu.lmu.cs.wutup.ws.model.User u;
        try {
            u = userService.findUserByFacebookId(fbId);
        } catch (NoSuchUserException e) {
            User fbUser = getFBUser(accessToken);
            u = new edu.lmu.cs.wutup.ws.model.User(
                    null,
                    fbUser.getFirstName(),
                    fbUser.getLastName(),
                    fbUser.getEmail(),
                    fbUser.getName(),
                    null,
                    fbUser.getId()
                );
            userService.createUser(u);
            u = userService.findUserByFacebookId(fbUser.getId());
        }

        return u;
    }

    @Override
    public edu.lmu.cs.wutup.ws.model.User syncUser(String accessToken) {
        try {
            JSONArray events = new JSONObject(getUserEvents(accessToken)).getJSONArray("data");

            EventOccurrence e;
            edu.lmu.cs.wutup.ws.model.User u = findOrCreateFBUser(accessToken, getUserIdFromFB(getFBUser(accessToken)));;

            for (int x = 0; x < events.length(); x++) {
                JSONObject current = events.getJSONObject(x);

                Event event;
                try {
                    event = eventService.findEventByName(current.getString("name"));
                } catch (NoSuchEventException exception) {
                    event = new Event(null, current.getString("name"), current.getString("name"), u);
                    eventService.createEvent(event);
                }

                // TODO: Check for this in the database first.
                e = new EventOccurrence(
                        event,
                        geocodeService.resolveVenue(current.getString("location"), null, null),
                        new DateTime(current.getString("start_time"))
                    );

                occurrenceService.createEventOccurrence(e);
            }

            return u;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FBUserSynchronizationException();
        }
    }

    @Override
    public String postUserEvent(String accessToken, String name, DateTime start, DateTime end,
            String description, String location, String FBLocationId, String privacyType) {

        try {
            return createUserEvent(accessToken, getUserIdFromFB(getFBUser(accessToken)), name, start, end, description, location,
                    FBLocationId, privacyType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
