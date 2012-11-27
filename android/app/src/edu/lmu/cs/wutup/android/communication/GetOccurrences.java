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

import edu.lmu.cs.wutup.android.container.Occurrences;
import edu.lmu.cs.wutup.android.model.Occurrence;

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
                ObjectReader occurenceObjectReader = occurenceObjectMapper.reader(Occurrence.class);
                
                MappingIterator<Occurrence> occurenceIterator = occurenceObjectReader.readValues(occurenceBuffer);
                fillOccurrences(occurenceIterator);
                
            } finally {
                occurenceStream.close();
                occurenceBuffer.close();
            }
            
        } catch (IOException ioException){
            Log.e("GET", "Failed to retrieve occurrences form web service!", ioException);     
        }
 
    }
    
    private void fillOccurrences(MappingIterator<Occurrence> occurenceIterator) {
        
        Occurrences.clear();
        Log.i("GET", "Cleared occurences.");
        
        while (occurenceIterator.hasNext()) {
            
            Occurrence occurrence = (Occurrence) occurenceIterator.next();
            Log.i("GET", "Retrieved occurence " + occurrence.getId() + ".");
            
            Occurrences.add(occurrence);
            
        }
        
    }

}