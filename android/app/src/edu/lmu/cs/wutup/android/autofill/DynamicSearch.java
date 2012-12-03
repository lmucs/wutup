package edu.lmu.cs.wutup.android.autofill;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.collections.IteratorUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.lmu.cs.wutup.android.manager.LogTags;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class DynamicSearch<T> extends AsyncTask<Object, Integer, List<T>> {
	
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
 * Member Variables END & Public Methods BEGIN
 **********************************************************************************************************************/
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<T> doInBackground(Object... parameters) {
		
		List<T> autoCompleteSuggestions = new ArrayList<T>();
		
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
				autoCompleteSuggestions = IteratorUtils.toList(deserializedObjects);
				
			} catch (IllegalStateException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException();
			
			Log.e(LogTags.AUTO_COMPLETE, "Invalid dynamic search parameters!", illegalArgumentException);
			throw illegalArgumentException;
			
		}
		
		return autoCompleteSuggestions;
		
	}
	
	@Override
	protected void onPostExecute (List<T> autoCompleteSuggestions) {
		populateAutoCompleteSuggestions(autoCompleteSuggestions);	
	}
	
/**********************************************************************************************************************
 * Public Methods END & Private Methods BEGIN
 **********************************************************************************************************************/

	private BufferedInputStream retrieveSerializedObjects(String address) throws IllegalStateException, IOException {

		HttpClient httpClient = new DefaultHttpClient();            
	    HttpGet httpGet = new HttpGet(address);
	    HttpResponse httpResponse = httpClient.execute(httpGet);
	    
	    InputStream inputStream = httpResponse.getEntity().getContent();;
	    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);       
                    
	    Log.i(LogTags.AUTO_COMPLETE, "Retrieve auto complete serialized suggestions from web service.");
	    return bufferedInputStream;
	    
	}
	
	private MappingIterator<T> deserializeObjects(BufferedInputStream serializedObjects) throws JsonProcessingException, 
	                                                                                            IOException 
    { 
        ObjectMapper eventObjectMapper = new ObjectMapper();
        ObjectReader eventObjectReader = eventObjectMapper.reader(c); 
        
        MappingIterator<T> deserializeObjects = eventObjectReader.readValues(serializedObjects);
        serializedObjects.close();
              
        Log.i(LogTags.AUTO_COMPLETE, "Deserialized auto complete suggestions from web service.");
        return deserializeObjects;
	
	}
	
	private void populateAutoCompleteSuggestions(List<T> autoCompleteSuggestions) {
		adapter.clear();		
		adapter.addAll(autoCompleteSuggestions);		
		adapter.notifyDataSetChanged();			
	}

/**********************************************************************************************************************
 * Private Methods END
 **********************************************************************************************************************/	
	
}
