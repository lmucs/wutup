package edu.lmu.cs.wutup.android.communication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.lmu.cs.wutup.android.container.Events;
import edu.lmu.cs.wutup.android.model.Event;

public class GetEvents extends HttpWutup{
    
    private HttpClient client = new DefaultHttpClient();
    
    @Override
    protected Object doInBackground(Object... parameters) {
        
        retrieveOccurences();
        
        return null;

        
    }
    
    private void retrieveOccurences() {
        
        try {
            
            HttpGet requestForEvents = new HttpGet(ADDRESS_OF_EVENTS);
            HttpResponse responceToRequestForEvents = client.execute(requestForEvents);
            
            InputStream eventsStream = responceToRequestForEvents.getEntity().getContent();;
            BufferedInputStream eventsBuffer = new BufferedInputStream(eventsStream);       
                        
            try {
                
                ObjectMapper eventObjectMapper = new ObjectMapper();
                ObjectReader eventObjectReader = eventObjectMapper.reader(Event.class);
                
                MappingIterator<Event> eventIterator = eventObjectReader.readValues(eventsBuffer);
                fillEvents(eventIterator);
                
            } finally {
                eventsStream.close();
                eventsBuffer.close();
            }
            
        } catch (IOException ioException){
            Log.e("GET", "Failed to retrieve events form web service!", ioException);     
        }
 
    }
    
    private void fillEvents(MappingIterator<Event> eventIterator) {
        
        Events.clear();
        
        while (eventIterator.hasNext()) {
            
            Event event = (Event) eventIterator.next();
            
            Events.add(event);
            Log.i("GET", "Retrieved event " + event.getId() + ".");
            
        }
        
    }

}