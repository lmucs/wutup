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
import com.google.android.maps.GeoPoint;

import edu.lmu.cs.wutup.android.container.Occurrences;
import edu.lmu.cs.wutup.android.manager.LogTags;
import edu.lmu.cs.wutup.android.model.Occurrence;
import edu.lmu.cs.wutup.android.views.Map;

public class GetOccurrences extends HttpWutup{
    
    public static final String DEFAULT_ERROR_MESSAGE = "Failed to plot event occurrences!";
    
    public static final int INDEX_OF_CENTER_IN_PARAMETERS = 0;
    public static final int INDEX_OF_RADIUS_IN_PARAMETERS = 1;   
    
    @Override
    protected Object doInBackground(Object... parameters) {
        
        if (parameters[INDEX_OF_CENTER_IN_PARAMETERS] instanceof GeoPoint &&
            parameters[INDEX_OF_RADIUS_IN_PARAMETERS] instanceof Integer) {
            
            GeoPoint center = (GeoPoint) parameters[INDEX_OF_CENTER_IN_PARAMETERS];
            int radius = (Integer) parameters[INDEX_OF_RADIUS_IN_PARAMETERS];
   
            retrieveOccurences(center, radius);    
                
        } else {
            
            IllegalArgumentException illegalArgumentException  = new IllegalArgumentException();
            
            Log.e(LogTags.HTTP, 
                  "Passed invalid parameters for getting occurrences! Requires a GeoPoint, Integer, and Map.", 
                  illegalArgumentException);
            
            throw illegalArgumentException;
            
        }   

        return null;
        
    }
    
    @Override
    protected void onPostExecute(Object result) {
        Map.plotOccurrences();        
    }   
    
    private void retrieveOccurences(GeoPoint center, int radius) {
        
        try {
            
            String httpCallWithParametersToGetOccurrences = generateHttpCallWithParametersToGetOccurrences(center, radius);
            HttpGet getOccurrences = new HttpGet(httpCallWithParametersToGetOccurrences);
            HttpResponse responceToGettingOccurrences = client.execute(getOccurrences);
            
            Log.i(LogTags.HTTP, "Executed HTTP call to get occurrences with the following query. " + httpCallWithParametersToGetOccurrences);
            
            InputStream occurenceStream = responceToGettingOccurrences.getEntity().getContent();;
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
            Log.e(LogTags.HTTP, "Failed to retrieve occurrences form web service!", ioException);     
        }
 
    }
    
    private void fillOccurrences(MappingIterator<Occurrence> occurenceIterator) {
        
        Occurrences.clear();
        
        while (occurenceIterator.hasNext()) {
            
            Occurrence occurrence = (Occurrence) occurenceIterator.next();
            Log.i(LogTags.HTTP, "Retrieved occurence " + occurrence.getId() + ".");
            
            Occurrences.add(occurrence);
            
        }
        
    }
    
    private String generateHttpCallWithParametersToGetOccurrences(GeoPoint center, int radius) {
        
        String httpCallFormat = ADDRESS_OF_OCCURRENCES + "?center=%s,%s&radius=%s";
        
        double latitude = microDegreesToDegrees(center.getLatitudeE6());
        double longitude = microDegreesToDegrees(center.getLongitudeE6());
        
        String httpCall = String.format(httpCallFormat, latitude, longitude, radius);
        
        return httpCall;
        
    }
    
    private static double microDegreesToDegrees(int microDegrees) {
        
        double degrees =  microDegrees / 1E6;
        
        return degrees;
        
    }

}