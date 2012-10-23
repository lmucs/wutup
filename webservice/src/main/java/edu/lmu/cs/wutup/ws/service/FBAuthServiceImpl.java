package edu.lmu.cs.wutup.ws.service;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class FBAuthServiceImpl {
    public String getAccessToken(String code) throws IOException {
        
        String landingURL = "https://graph.facebook.com/oauth/access_token?" +
                "client_id=" + System.getenv("WUTUP_FB_APP_ID") +
                "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/wutup/auth/landing", "ISO-8859-1") +
                "&client_secret=" + System.getenv("WUTUP_FB_APP_SECRET") +
                "&code=" + code;
        
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(landingURL);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        
        return EntityUtils.toString(entity);
    }
}
