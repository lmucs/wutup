package edu.lmu.cs.wutup.ws.service;

import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireFBCode;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireResource;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.acquireUserEvents;
import static edu.lmu.cs.wutup.ws.model.FacebookGateway.createUserEvent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Response;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restfb.DefaultFacebookClient;
import com.restfb.types.User;

import edu.lmu.cs.wutup.ws.exception.FBUserSynchronizationException;
import edu.lmu.cs.wutup.ws.exception.InvalidFBAccessTokenException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventException;
import edu.lmu.cs.wutup.ws.exception.NoSuchEventOccurrenceException;
import edu.lmu.cs.wutup.ws.exception.NoSuchUserException;
import edu.lmu.cs.wutup.ws.exception.NoSuchVenueException;
import edu.lmu.cs.wutup.ws.model.Event;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;
import edu.lmu.cs.wutup.ws.model.FacebookGateway;
import edu.lmu.cs.wutup.ws.model.Venue;

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

    @Autowired
    VenueService venueService;

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
    public String getVenueFromFbById(String accessToken, String fbResourceId) throws ParseException, ClientProtocolException, IOException {
        return acquireResource(accessToken, fbResourceId);
    }

    @Override
    public edu.lmu.cs.wutup.ws.model.User findOrCreateFBUser(String accessToken, String fbId) {
        edu.lmu.cs.wutup.ws.model.User u;
        try {
            u = userService.findUserByFacebookId(fbId);
        } catch (NoSuchUserException e) {
            User fbUser = getFBUser(accessToken);
            u = new edu.lmu.cs.wutup.ws.model.User(null, fbUser.getFirstName(), fbUser.getLastName(),
                    fbUser.getEmail(), fbUser.getName(), null, fbUser.getId());
            userService.createUser(u);
            u = userService.findUserByFacebookId(fbUser.getId());
        }

        return u;
    }

    @Override
    public edu.lmu.cs.wutup.ws.model.User syncUser(String accessToken) {
        JSONArray events;
        try {
            events = new JSONObject(getUserEvents(accessToken)).getJSONArray("data");
        } catch (JSONException e) {
            throw new FBUserSynchronizationException();
        } catch (IOException e) {
            throw new FBUserSynchronizationException();
        }

        Event event;
        EventOccurrence e;
        JSONObject current;
        Venue v;
        String currentName, currentLocation, currentStartTime, currentEndTime, currentVenueFBResourceId;
        edu.lmu.cs.wutup.ws.model.User u = findOrCreateFBUser(accessToken, getUserIdFromFB(getFBUser(accessToken)));

        for (int x = 0; x < events.length(); x++) {
            try {
                current = events.getJSONObject(x);
                currentName = current.getString("name");
                currentStartTime = current.getString("start_time");
                currentLocation = current.getString("location");
                
                // TODO: Not used.
                //currentVenueFBResourceId = new JSONObject(current.getString("venue")).getString("id");
            } catch (JSONException exception) {
                exception.printStackTrace();
                break;
            }

            try {
                currentEndTime = current.getString("end_time");
            } catch (JSONException exception) {
                currentEndTime = null;
            }

            try {
                event = eventService.findEventByName(currentName);
            } catch (NoSuchEventException exception) {
                event = new Event(null, currentName, currentName, u);
                event.setId(Integer.class.cast(eventService.createEvent(event)));
            }

            System.out.println("\n\n" + event);
            
            
            try {
                v = geocodeService.resolveVenue(currentLocation, null, null);
            } catch (Exception exception) {
                exception.printStackTrace();
                break;
            }

            try {
                v = venueService.findVenueByName(v.getName());
            } catch (NoSuchVenueException exception) {
                venueService.createVenue(v);
            }
            
            System.out.println(v + "\n\n");

            DateTime start;
            DateTime end;
            try {
                start = new DateTime(currentStartTime);
                end = (currentEndTime != null ? new DateTime(currentEndTime) : start.plusDays(1));
                
                e = new EventOccurrence(event, v, start, end);
            } catch (Exception exception) {
                exception.printStackTrace();
                break;
            }

            try {
                occurrenceService.findEventOccurrenceByProperties(
                        event.getId(),
                        v.getId(),
                        start != null ? new Timestamp(start.getMillis()) : null,
                        end != null ? new Timestamp(end.getMillis()) : null);
            } catch (NoSuchEventOccurrenceException exception) {
                exception.printStackTrace();
                occurrenceService.createEventOccurrence(e);
            }
        }

        return u;
    }

    @Override
    public String postUserEvent(String accessToken, String name, DateTime start, DateTime end, String description,
            String location, String FBLocationId, String privacyType) {

        try {
            return createUserEvent(accessToken, getUserIdFromFB(getFBUser(accessToken)), name, start, end, description,
                    location, FBLocationId, privacyType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
