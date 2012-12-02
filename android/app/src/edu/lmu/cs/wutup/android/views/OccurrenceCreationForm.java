package edu.lmu.cs.wutup.android.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import edu.lmu.cs.wutup.android.autofill.DynamicSearchTrigger;
import edu.lmu.cs.wutup.android.autofill.ManualListAdapter;
import edu.lmu.cs.wutup.android.communication.HttpWutup;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Venue;


public class OccurrenceCreationForm extends Activity {
	
/**********************************************************************************************************************
 * Member Variables BEGIN
 **********************************************************************************************************************/

	private static final int NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS = 1;
	
	AutoCompleteTextView eventTextField;
	ManualListAdapter<Event> eventAdapter;
	
	AutoCompleteTextView venueTextField;
	ManualListAdapter<Venue> venueAdapter;

/**********************************************************************************************************************
 * Member Variables END & Method Overriding BEGIN
 **********************************************************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occurrence_creation_form_v3);
		
		eventTextField = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_event_text_field);
		eventAdapter = new ManualListAdapter<Event>(this, android.R.layout.simple_dropdown_item_1line);
		eventTextField.setAdapter(eventAdapter);
		eventTextField.setThreshold(NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS);
		eventTextField.addTextChangedListener(new DynamicSearchTrigger<Event>(Event.class, eventAdapter, HttpWutup.ADDRESS_OF_EVENTS));
		
//		venueTextField = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_venue_text_field);
//		venueAdapter = new AdapterAutoComplete<Venue>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<Venue>());
//		venueTextField.setAdapter(venueAdapter);
//		venueTextField.addTextChangedListener(new DynamicSearchTrigger<Venue>(venueAdapter, Venue.class, HttpWutup.ADDRESS_OF_VENUES));

	}
	
/**********************************************************************************************************************
 * Method Overriding END & Public Methods BEGIN
 **********************************************************************************************************************/
	
	
	
/**********************************************************************************************************************
 * Public Methods END & Private Methods BEGIN
 **********************************************************************************************************************/

	
	
/**********************************************************************************************************************
 * Private Methods END
 **********************************************************************************************************************/	
	
}
