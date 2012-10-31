package edu.lmu.cs.wutup.ws.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;

public class FacebookGateway extends AbstractGateway {
    public static String acquireAccessToken(String code) throws ClientProtocolException, IOException {
        return stringifyEntity(executeRequest(constructLandingUrl(code)));
    }
    
    private static String constructLandingUrl(String code) throws UnsupportedEncodingException {
        return "https://graph.facebook.com/oauth/access_token?" +
                "client_id=" + System.getenv("WUTUP_FB_APP_ID") +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/wutup/auth/landing", "ISO-8859-1") +
                "&client_secret=" + System.getenv("WUTUP_FB_APP_SECRET") +
                "&code=" + code;
    }
}
