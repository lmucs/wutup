package edu.lmu.cs.wutup.ws.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.joda.time.DateTime;

import com.restfb.types.User;

public interface FBAuthService {

    String getAccessToken(String code, String redirectUri) throws IOException;

    Response fetchFBCode(String redirectUri) throws ParseException, ClientProtocolException,
            UnsupportedEncodingException, IOException, URISyntaxException;

    String getUserNameFromFB(com.restfb.types.User u);

    String getUserIdFromFB(com.restfb.types.User u);

    User getFBUser(String accessToken);

    String getUserEvents(String accessToken) throws ParseException, ClientProtocolException, IOException;

    String getVenueFromFbById(String accessToken, String fbResourceId) throws ParseException, ClientProtocolException,
            IOException;

    edu.lmu.cs.wutup.ws.model.User findOrCreateFBUser(String accessToken, String fbId);

    edu.lmu.cs.wutup.ws.model.User syncUser(String accessToken);

    String postUserEvent(String accessToken, String name, DateTime start, DateTime end, String description,
            String location, String FBLocationId, String privacyType);
}
