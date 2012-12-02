package edu.lmu.cs.wutup.android.autofill;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.lmu.cs.wutup.android.manager.LogTags;

public class DynamicSearch<T> extends AsyncTask<Object, Integer, Void> {
	
/**********************************************************************************************************************
 * Member Variables BEGIN
 **********************************************************************************************************************/	
	
	public static final int INDEX_OF_CLASS_IN_PARAMETERS = 0;
	public static final int INDEX_OF_ADAPTER_IN_PARAMETERS = 1;
	public static final int INDEX_OF_ADDRESS_IN_PARAMETERS = 2;
	
	private Class<T> c;
	private ManualListAdapter<T> adapter;
	private String address;
	
/**********************************************************************************************************************
 * Member Variables END & Method Overriding BEGIN
 **********************************************************************************************************************/
	
	@Override
	@SuppressWarnings("unchecked")
	protected Void doInBackground(Object... parameters) {
		
		if (parameters[INDEX_OF_CLASS_IN_PARAMETERS] instanceof Class &&
			parameters[INDEX_OF_ADAPTER_IN_PARAMETERS] instanceof ManualListAdapter &&	
			parameters[INDEX_OF_ADDRESS_IN_PARAMETERS] instanceof String)
		{
			c = (Class<T>) parameters[INDEX_OF_CLASS_IN_PARAMETERS];
			adapter = (ManualListAdapter<T>) parameters[INDEX_OF_ADAPTER_IN_PARAMETERS];
			address = (String) parameters[INDEX_OF_ADDRESS_IN_PARAMETERS];
			
			try {
				
				BufferedInputStream serializedObjects = retrieveSerializedObjects(address);				
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
 * Method Overriding END & Private Methods BEGIN
 **********************************************************************************************************************/

	private BufferedInputStream retrieveSerializedObjects(String address) throws IllegalStateException, IOException {

		HttpClient httpClient = new DefaultHttpClient();            
	    HttpGet httpGet = new HttpGet(address);
	    HttpResponse httpResponse = httpClient.execute(httpGet);
	    
	    InputStream inputStream = httpResponse.getEntity().getContent();;
	    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);       
                        
	    return bufferedInputStream;
	    
	}
	
	private MappingIterator<T> deserializeObjects(BufferedInputStream serializedObjects) throws JsonProcessingException, IOException {
     
        ObjectMapper eventObjectMapper = new ObjectMapper();
        ObjectReader eventObjectReader = eventObjectMapper.reader(c); 
        
        MappingIterator<T> deserializeObjects = eventObjectReader.readValues(serializedObjects);
        serializedObjects.close();
              
        return deserializeObjects;
	
	}
	
	private void populateAutoCompleteSuggestions(List<T> autoCompleteSuggestions) {
		
		adapter.clear();		
		adapter.addAll(autoCompleteSuggestions);		
		adapter.notifyDataSetChanged();		
		
		Log.i(LogTags.EVENT_CREATION, "Cleared auto complete suggestions. Repopulated list with " + autoCompleteSuggestions.size() +" new suggestions.");
		
	}

/**********************************************************************************************************************
 * Private Methods END
 **********************************************************************************************************************/	
	
}
