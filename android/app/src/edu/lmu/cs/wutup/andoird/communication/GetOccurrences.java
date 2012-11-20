package edu.lmu.cs.wutup.andoird.communication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Occurrence;

import android.os.AsyncTask;
import android.util.Log;

public class GetOccurrences extends AsyncTask<Void, Void, ArrayList<Occurrence>>{
    
    private HttpClient client = new DefaultHttpClient();
    
    private ArrayList<Occurrence> occurrences = new ArrayList<Occurrence>();

    @Override
    protected ArrayList<Occurrence> doInBackground(Void... params) {
        
        refreshOccurences();
        
        
        return occurrences;
    }
    
    private void refreshOccurences() {
        
        try {
            
            URI addressOfOccurrences;            
            HttpGet requestForOccurences;
            HttpResponse responceToRequestForOccurences;
            
            InputStream occurenceStream = null;
            BufferedInputStream occurenceBuffer = null;
            
            ObjectMapper occurenceObjectMapper;
            ObjectReader occurenceObjectReader;
                        
            try {
                
                addressOfOccurrences = new URI("http://wutup.cs.lmu.edu:8080/wutup/occurrences"); // could be outside try finally
                requestForOccurences = new HttpGet(addressOfOccurrences);
                responceToRequestForOccurences = client.execute(requestForOccurences);
                
                occurenceStream = responceToRequestForOccurences.getEntity().getContent();
                occurenceBuffer = new BufferedInputStream(occurenceStream);
                
                occurenceObjectMapper = new ObjectMapper();
                occurenceObjectReader = occurenceObjectMapper.reader(Occurrence.class);
                
                MappingIterator<Occurrence> occurenceIterator = occurenceObjectReader.readValues(occurenceBuffer);
                fillOccurrences(occurenceIterator);
                
            } finally {
                
                if (occurenceStream != null) {
                    occurenceStream.close();
                }
                
                if (occurenceBuffer != null) {
                    occurenceBuffer.close();
                }
                
            }
            
        } catch (URISyntaxException uriSyntaxException) {
            Log.wtf("URI", "Malformed URI for occurrences!", uriSyntaxException);            
            
        } catch (IOException ioException){
            Log.e("GET", "Failed to retrieve occurrences form web service!", ioException);     
        }
 
    }
    
    private void fillOccurrences(MappingIterator<Occurrence> occurenceIterator) {
        
        occurrences.clear();
        Log.i("occurence", "Cleared occurences.");
        
        while (occurenceIterator.hasNext()) {
            
            Occurrence occurrence = (Occurrence) occurenceIterator.next();
            
            occurrences.add(occurrence);
            Log.i("occurence", "Added occurence " + occurrence.getId() + ".");
            
        }
        
    }

}
