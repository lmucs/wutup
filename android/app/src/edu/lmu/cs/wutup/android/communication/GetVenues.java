package edu.lmu.cs.wutup.android.communication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import android.util.Log;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.lmu.cs.wutup.android.container.Venues;
import edu.lmu.cs.wutup.android.model.Venue;

public class GetVenues extends HttpWutup{
        
    @Override
    protected Object doInBackground(Object... parameters) {
        
        retrieveVenues();
        
        return null;

    }
    
    private void retrieveVenues() {
        
        try {
            
            HttpGet requestForVenues = new HttpGet(ADDRESS_OF_VENUES);
            HttpResponse responceToRequestForVenues = client.execute(requestForVenues);
            
            InputStream venueStream = responceToRequestForVenues.getEntity().getContent();;
            BufferedInputStream venueBuffer = new BufferedInputStream(venueStream);       
                        
            try {
                
                ObjectMapper venueObjectMapper = new ObjectMapper();
                ObjectReader venueObjectReader = venueObjectMapper.reader(Venue.class);
                
                MappingIterator<Venue> venueIterator = venueObjectReader.readValues(venueBuffer);
                fillVenues(venueIterator);
                
            } finally {
                venueStream.close();
                venueBuffer.close();
            }
            
        } catch (IOException ioException){
            Log.e("GET", "Failed to retrieve venues form web service!", ioException);     
        }
 
    }
    
    private void fillVenues(MappingIterator<Venue> venueIterator) {
        
        Venues.clear();
        
        while (venueIterator.hasNext()) {
            
            Venue venue = (Venue) venueIterator.next();
            Log.i("GET", "Retrieved venue " + venue.getId() + ".");
            
            Venues.add(venue);
            
        }
        
    }

}