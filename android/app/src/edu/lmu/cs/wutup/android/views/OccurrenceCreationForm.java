package edu.lmu.cs.wutup.android.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.TimePicker;

import org.joda.time.DateTimeConstants;

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
	
	CalendarView startDate;
	TimePicker startTime;
	CalendarView endDate;
	TimePicker endTime;
	
	long startInUnixTime;
	long endInUnixTime;

/**********************************************************************************************************************
 * Member Variables END & Android Life Cycle Methods BEGIN
 **********************************************************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occurrence_creation_form_layout);
		
		initiateEventDynamicSearch();
		initiateVenueDynamicSearch();
		
		startDate = (CalendarView) findViewById(R.id.occurrence_creation_form_date_start);
		startTime = (TimePicker) findViewById(R.id.occurrence_creation_form_time_start);
		endDate = (CalendarView) findViewById(R.id.occurrence_creation_form_date_end);
		endTime = (TimePicker) findViewById(R.id.occurrence_creation_form_time_end);
		
	}
	
/**********************************************************************************************************************
 * Android Life Cycle Methods END & Other Public Methods BEGIN
 **********************************************************************************************************************/
	
	
	
/**********************************************************************************************************************
 * Public Methods END & Private Methods BEGIN
 **********************************************************************************************************************/

	private void initiateEventDynamicSearch() {
		
		eventTextField = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_event_text_field);
		eventAdapter = new ManualListAdapter<Event>(this, android.R.layout.simple_dropdown_item_1line);
		
		eventTextField.setAdapter(eventAdapter);
		eventTextField.setThreshold(NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS);
		eventTextField.addTextChangedListener(new DynamicSearchTrigger<Event>(Event.class, 
																		      eventAdapter, 
																		      HttpWutup.ADDRESS_OF_EVENTS));
		
	}
	
	private void initiateVenueDynamicSearch() {
		
		venueTextField = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_venue_text_field);
		venueAdapter = new ManualListAdapter<Venue>(this, android.R.layout.simple_dropdown_item_1line);
		
		venueTextField.setAdapter(venueAdapter);
		venueTextField.setThreshold(NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS);
		venueTextField.addTextChangedListener(new DynamicSearchTrigger<Venue>(Venue.class, 
				                                                              venueAdapter, 
				                                                              HttpWutup.ADDRESS_OF_VENUES));
		
	}
	
	private void calculateAndSetStartInUnixTime() {
		
		long unixTime = startDate.getDate();
		
		unixTime += startTime.getCurrentHour() * DateTimeConstants.MILLIS_PER_HOUR;
		unixTime += startTime.getCurrentMinute() * DateTimeConstants.MILLIS_PER_MINUTE;
		
		startInUnixTime = unixTime;
		
	}
	
	private void calculateAndSetEndInUnixTime() {
		
		long unixTime = endDate.getDate();
		
		unixTime += endTime.getCurrentHour() * DateTimeConstants.MILLIS_PER_HOUR;
		unixTime += endTime.getCurrentMinute() * DateTimeConstants.MILLIS_PER_MINUTE;
		
		endInUnixTime = unixTime;
		
	}
	
/**********************************************************************************************************************
 * Private Methods END
 **********************************************************************************************************************/	
	
}
