package edu.lmu.cs.wutup.android.communication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.lmu.cs.wutup.android.manager.LogTags;
import edu.lmu.cs.wutup.android.model.Occurrence;

public class PostVenue extends HttpWutup {
    
    public static int INDEX_OF_NAME_IN_PARAMETERS = 0;
    public static int INDEX_OF_ADDRESS_IN_PARAMETERS = 0;
    
    @Override
    protected Object doInBackground(Object... parameters) {
        
        if (parameters[INDEX_OF_NAME_IN_PARAMETERS] instanceof String &&
            parameters[INDEX_OF_ADDRESS_IN_PARAMETERS] instanceof String) {
            
            try {
                
                String name = (String) parameters[INDEX_OF_NAME_IN_PARAMETERS];
                String address = (String) parameters[INDEX_OF_ADDRESS_IN_PARAMETERS];
                
                postVenue(name, address);
                
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        
       
        
        return null;
        
    }
    
    private HttpResponse postVenue(String name, String address) throws ClientProtocolException, IOException {
        
        HttpPost postOccurrence = new HttpPost(ADDRESS_OF_VENUES);
        StringEntity jsonForPostingOccurrence = new StringEntity(generateJsonForPostingOccurrecne(name, address));
        postOccurrence.setEntity(jsonForPostingOccurrence);
        postOccurrence.addHeader("Content-type", "application/json");
        
        HttpResponse response = client.execute(postOccurrence);
        
        Log.i("POST", "Executed post with JSON " + jsonForPostingOccurrence + ".");
        return response;
        
    }
    
    private String generateJsonForPostingOccurrecne(String name, String address) throws JsonProcessingException {
        
        String jsonFormat = "{\"name\":\"%s\",\"address\":\"%s\"}";
        String json = String.format(jsonFormat, name, address);
        
        Log.i("POST", "Generated the following JSON for posting an occurrence. " + json);
        return json;
                
    }

}
