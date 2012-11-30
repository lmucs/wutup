package edu.lmu.cs.wutup.android.autofill;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class DynamicSearch<T> extends AsyncTask<Object, Integer, Void> {
	
/**********************************************************************************************************************
 * Member Variables BEGINS
 **********************************************************************************************************************/	
	
	public static final int INDEX_OF_ADAPTER_IN_PARAMETERS = 0;
	public static final int INDEX_OF_CLASS_IN_PARAMETERS = 1;
	public static final int INDEX_OF_SEARCH_TERM_IN_PARAMETERS = 2;
	public static final int INDEX_OF_ADDRESS_IN_PARAMETERS = 3;
	
	private ArrayAdapter<T> arrayAdapter;
	private Class<T> typeOfSearch;
	private URI uri;
	
/**********************************************************************************************************************
 * Member Variables ENDS & Method Overriding BEGINS
 **********************************************************************************************************************/
	
	@Override
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Object... parameters) {
		
		if (parameters[INDEX_OF_ADAPTER_IN_PARAMETERS] instanceof ArrayAdapter &&
			parameters[INDEX_OF_CLASS_IN_PARAMETERS] instanceof Class &&	
			parameters[INDEX_OF_SEARCH_TERM_IN_PARAMETERS] instanceof String &&
			parameters[INDEX_OF_ADDRESS_IN_PARAMETERS] instanceof URI)
		{
			arrayAdapter = (ArrayAdapter<T>) parameters[INDEX_OF_ADAPTER_IN_PARAMETERS];
			typeOfSearch = (Class<T>) parameters[INDEX_OF_CLASS_IN_PARAMETERS];
			uri = (URI) parameters[INDEX_OF_ADDRESS_IN_PARAMETERS];
			
			try {
				
				BufferedInputStream serializedObjects = retrieveSerializedObjects(uri);				
				MappingIterator<T> deserializedObjects = deserializeObjects(serializedObjects);
				List<T> autoCompleteSuggestions = IteratorUtils.toList(deserializedObjects);
				populateAutoCompleteSuggestions(autoCompleteSuggestions);
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			throw new IllegalArgumentException();
		}
		
		return null;
		
	}
	
/**********************************************************************************************************************
 * Method Overriding ENDS & Private Methods BEGINS
 **********************************************************************************************************************/

	private BufferedInputStream retrieveSerializedObjects(URI address) throws IllegalStateException, IOException {

		HttpClient httpClient = new DefaultHttpClient();            
	    HttpGet httpGet = new HttpGet(address);
	    HttpResponse httpResponse = httpClient.execute(httpGet);
	    
	    InputStream inputStream = httpResponse.getEntity().getContent();;
	    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);       
                        
	    return bufferedInputStream;
	    
	}
	
	private MappingIterator<T> deserializeObjects(BufferedInputStream serializedObjects) throws JsonProcessingException, IOException {
     
        ObjectMapper eventObjectMapper = new ObjectMapper();
        ObjectReader eventObjectReader = eventObjectMapper.reader(typeOfSearch); 
        
        MappingIterator<T> deserializeObjects = eventObjectReader.readValues(serializedObjects);
        serializedObjects.close();
              
        return deserializeObjects;
	
	}
	
	private void populateAutoCompleteSuggestions(List<T> autoCompleteSuggestions) {
		
	}

}
