package edu.lmu.cs.wutup.android.communication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

public class PostOccurrences extends HttpWutup {
    
    public static int INDEX_OF_EVENT_ID_IN_PARAMETERS = 0;
    public static int INDEX_OF_VENUE_ID_IN_PARAMETERS = 1;
    public static int INDEX_OF_START_IN_PARAMETERS = 2;
    public static int INDEX_OF_END_IN_PARAMETERS = 3;

	@Override
	protected Object doInBackground(Object... parameters) {
		
        if (parameters[INDEX_OF_EVENT_ID_IN_PARAMETERS] instanceof Integer &&
            parameters[INDEX_OF_VENUE_ID_IN_PARAMETERS] instanceof Integer &&
            parameters[INDEX_OF_START_IN_PARAMETERS] instanceof String &&
            parameters[INDEX_OF_END_IN_PARAMETERS] instanceof String) {
            
            Integer eventId = (Integer) parameters[INDEX_OF_EVENT_ID_IN_PARAMETERS];
            Integer venueId = (Integer) parameters[INDEX_OF_VENUE_ID_IN_PARAMETERS];
            String start = (String) parameters[INDEX_OF_START_IN_PARAMETERS];
            String end = (String) parameters[INDEX_OF_END_IN_PARAMETERS];
            
            try {
                postOccurrence(eventId, venueId, start, end);
                
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	    

		
		return null;
		
	}
	
	private HttpResponse postOccurrence(Integer eventId, Integer venueId, String start, String end) throws ClientProtocolException, IOException {
		
		HttpPost postOccurrence = new HttpPost(ADDRESS_OF_OCCURRENCES);
		StringEntity serializedOccurrence = new StringEntity(generateJson(eventId, venueId, start, end));
		postOccurrence.setEntity(serializedOccurrence);
		postOccurrence.addHeader("Content-type", "application/json");
		
		HttpResponse response = client.execute(postOccurrence);
		
		Log.i("POST", "Posted occurrence " + serializedOccurrence + ".");
		return response;
		
	}
	
	private String generateJson(Integer eventId, Integer venueId, String start, String end) throws JsonProcessingException {
	    
	    String jsonFormat = "{\"event\":{\"id\":%s},\"venue\":{\"id\":%s},\"start\":\"%s\",\"end\":\"%s\"}";
	    String json = String.format(jsonFormat, eventId, venueId, start, end);
	    
	    return json;
	    	    
	}

}
