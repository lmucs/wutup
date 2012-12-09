package edu.lmu.cs.wutup.android.communication;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public abstract class HttpWutup extends AsyncTask<Object, Integer, Object> {
	
	public static final String ADDRESS_OF_SERVER = "http://wutup.cs.lmu.edu:8080/";
	
	public static final String ADDRESS_OF_EVENTS = ADDRESS_OF_SERVER + "wutup/events/";
	public static final String ADDRESS_OF_OCCURRENCES = ADDRESS_OF_SERVER + "wutup/occurrences/";
	public static final String ADDRESS_OF_VENUES = ADDRESS_OF_SERVER + "wutup/venues/";
	
	
	
	protected HttpClient client = new DefaultHttpClient();

}
