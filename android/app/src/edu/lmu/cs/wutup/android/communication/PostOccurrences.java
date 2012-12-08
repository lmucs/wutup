package edu.lmu.cs.wutup.android.communication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.lmu.cs.wutup.android.model.Occurrence;

public class PostOccurrences extends HttpWutup {

	@Override
	protected Object doInBackground(Object... parameters) {
		
		try {
			postOccurrence((Occurrence) parameters[0]);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private HttpResponse postOccurrence(Occurrence occurrence) throws ClientProtocolException, IOException {
		
		HttpPost postOccurrence = new HttpPost(ADDRESS_OF_OCCURRENCES);
		StringEntity serializedOccurrence = new StringEntity(serializeOccurrence(occurrence));
		postOccurrence.setEntity(serializedOccurrence);
		postOccurrence.addHeader("Content-type", "application/json");
		
		HttpResponse response = client.execute(postOccurrence);
		
		Log.i("POST", "Posted occurrence " + occurrence.getId() + ", at lat: " + occurrence.getVenue().getLatitude() + 
		        " long: " + occurrence.getVenue().getLatitude() + " name: " + occurrence.getVenue().getName());
		return response;
		
	}
	
	private String serializeOccurrence(Occurrence occurrence) throws JsonProcessingException {
	    
	    String jsonFormat = "{\"event\":{\"id\":%s},\"venue\":{\"id\":%s},\"start\":\"%s\",\"end\":\"%s\"}";
	    String json = String.format(jsonFormat, 
	                                occurrence.getEvent().getId(), 
	                                occurrence.getVenue().getId(), 
	                                occurrence.getStart().toString(), 
	                                occurrence.getEnd().toString());
	    
	    Log.i("POST", "Serialized occurrence " + occurrence.getId() + ". JSON --> " + json);
	    return json;
	    	    
	}

}
