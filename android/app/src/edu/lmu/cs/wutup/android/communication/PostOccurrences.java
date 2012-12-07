package edu.lmu.cs.wutup.android.communication;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public class PostOccurrences extends HttpWutup {

	@Override
	protected Object doInBackground(Object... parameters) {
		
		try {
			postOccurrence((EventOccurrence) parameters[0]);
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	private HttpResponse postOccurrence(EventOccurrence occurrence) throws ClientProtocolException, IOException {
		
		HttpPost postOccurrence = new HttpPost(ADDRESS_OF_OCCURRENCES);
		StringEntity serializedOccurrence = new StringEntity(serializeOccurrence(occurrence));
		postOccurrence.setEntity(serializedOccurrence);
		
		HttpResponse response = client.execute(postOccurrence);
		
		Log.i("POST", "Posted occurrence " + occurrence.getId() + ".");
		return response;
		
	}
	
	private String serializeOccurrence(EventOccurrence occurrence) throws JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String serializedOccurrence = objectMapper.writeValueAsString(occurrence);
		
		Log.i("POST", "Serialized occurrence " + occurrence.getId() + ".");
		return serializedOccurrence;
	}

}
