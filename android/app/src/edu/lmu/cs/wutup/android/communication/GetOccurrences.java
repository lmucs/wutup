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

import edu.lmu.cs.wutup.android.container.EventOccurrences;
import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public class GetOccurrences extends HttpWutup{
    
    private HttpClient client = new DefaultHttpClient();
    
    @Override
    protected Object doInBackground(Object... parameters) {
        
        retrieveOccurences();
        
        return null;

        
    }
    
    private void retrieveOccurences() {
        
        try {
            
            HttpGet requestForOccurences = new HttpGet(ADDRESS_OF_OCCURRENCES);
            HttpResponse responceToRequestForOccurences = client.execute(requestForOccurences);
            
            InputStream occurenceStream = responceToRequestForOccurences.getEntity().getContent();;
            BufferedInputStream occurenceBuffer = new BufferedInputStream(occurenceStream);       
                        
            try {
                
                ObjectMapper occurenceObjectMapper = new ObjectMapper();
                ObjectReader occurenceObjectReader = occurenceObjectMapper.reader(EventOccurrence.class);
                
                MappingIterator<EventOccurrence> occurenceIterator = occurenceObjectReader.readValues(occurenceBuffer);
                fillOccurrences(occurenceIterator);
                
            } finally {
                occurenceStream.close();
                occurenceBuffer.close();
            }
            
        } catch (IOException ioException){
            Log.e("GET", "Failed to retrieve occurrences form web service!", ioException);     
        }
 
    }
    
    private void fillOccurrences(MappingIterator<EventOccurrence> occurenceIterator) {
        
        EventOccurrences.clear();
        
        while (occurenceIterator.hasNext()) {
            
            EventOccurrence occurrence = (EventOccurrence) occurenceIterator.next();
            Log.i("GET", "Retrieved occurence " + occurrence.getId() + ".");
            
            EventOccurrences.add(occurrence);
            
        }
        
    }

}