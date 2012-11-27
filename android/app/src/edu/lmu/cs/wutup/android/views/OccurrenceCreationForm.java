package edu.lmu.cs.wutup.android.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import edu.lmu.cs.wutup.android.container.Events;
import edu.lmu.cs.wutup.android.container.Venues;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Venue;


public class OccurrenceCreationForm extends Activity {
	
	Spinner eventSpinner;
	Spinner venueSpinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occurrence_creation_form_v3);
		
		String[] stuff = {"adfasdf", "adsfdf", "3fsdf", "dsf43"};
		AutoCompleteTextView event = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_event);
		ArrayAdapter<String> adp=new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,stuff);
		event.setThreshold(1);
		event.setAdapter(adp);
		
		
		
//		populateEventSpinner();
//		populateVenueSpinner();
	
	}
	
	private void populateEventSpinner() {
	
		eventSpinner = (Spinner) findViewById(R.id.event_spinner);
			 
		ArrayAdapter<Event> eventAdapter = new ArrayAdapter<Event>(this, 
												 				   android.R.layout.simple_spinner_item, 
				                                                   Events.getAll());
		
		eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventSpinner.setAdapter(eventAdapter);
		
	  }
	
	private void populateVenueSpinner() {
		
		venueSpinner = (Spinner) findViewById(R.id.venue_spinner);
			 
		ArrayAdapter<Venue> venueAdapter = new ArrayAdapter<Venue>(this, 
												 				   android.R.layout.simple_spinner_item, 
				                                                   Venues.getAll());
		
		venueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		venueSpinner.setAdapter(venueAdapter);
		
	  }

}
