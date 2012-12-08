package edu.lmu.cs.wutup.android.communication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.lmu.cs.wutup.android.manager.LogTags;

public class PostVenue extends HttpWutup {
    
    public static final String DEFAULT_ERROR_MESSAGE = "Failed to post occurrence!";

    
    public static final int INDEX_OF_NAME_IN_PARAMETERS = 0;
    public static final int INDEX_OF_ADDRESS_IN_PARAMETERS = 1;
    
    @Override
    protected Object doInBackground(Object... parameters) {
        
        int idOfPostedVenue = -1;
        
        if (parameters[INDEX_OF_NAME_IN_PARAMETERS] instanceof String &&
            parameters[INDEX_OF_ADDRESS_IN_PARAMETERS] instanceof String) {
            
            String name = (String) parameters[INDEX_OF_NAME_IN_PARAMETERS];
            String address = (String) parameters[INDEX_OF_ADDRESS_IN_PARAMETERS];
            
            try {  
                idOfPostedVenue = postVenue(name, address);
                
            } catch (ClientProtocolException clientProtocolException) {
                Log.e(LogTags.POST, DEFAULT_ERROR_MESSAGE, clientProtocolException);
                
            } catch (IOException ioException) {
                Log.e(LogTags.POST, DEFAULT_ERROR_MESSAGE, ioException);
            }
            
        } else {
            throw new IllegalArgumentException();
        }
        
        return idOfPostedVenue;
        
    }
    
    private int postVenue(String name, String address) throws ClientProtocolException, IOException {
        
        HttpPost postOccurrence = new HttpPost(ADDRESS_OF_VENUES);
        String jsonForPostingVenue = generateJsonForPostingOccurrecne(name, address);
        StringEntity entityForPostingVenue = new StringEntity(jsonForPostingVenue);
        
        postOccurrence.setEntity(entityForPostingVenue);
        postOccurrence.addHeader("Content-type", "application/json");
        
        HttpResponse responceToPostingVenue = client.execute(postOccurrence);
        int idOfPostedVenue = extractVenueId(responceToPostingVenue);
                
        Log.i(LogTags.POST, "Executed HTTP call to post venue with the following JSON. " + jsonForPostingVenue + 
                      " Posted venue assigned ID " + idOfPostedVenue + ".");
        
        return idOfPostedVenue;
        
    }
    
    private String generateJsonForPostingOccurrecne(String name, String address) throws JsonProcessingException {
        
        String jsonFormat = "{\"name\":\"%s\",\"address\":\"%s\"}";
        String json = String.format(jsonFormat, name, address);
        
        return json;
                
    }
    
    private int extractVenueId(HttpResponse responceToPostingVenue) {
        
        String locationOfPostedVenue = responceToPostingVenue.getFirstHeader("Location").getValue();        
        int indexOfForwardSlashPrecedingId = -1;
        
        for (int index = 0; index < locationOfPostedVenue.length(); index++) {
            
            if (locationOfPostedVenue.charAt(index) == '/') {
                indexOfForwardSlashPrecedingId = index;
            }
            
        }
        
        int venueId = Integer.parseInt(locationOfPostedVenue.substring(indexOfForwardSlashPrecedingId + 1));
        
        return venueId;
        
    }

}
