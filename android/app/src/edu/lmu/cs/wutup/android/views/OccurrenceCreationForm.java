package edu.lmu.cs.wutup.android.views;

import java.util.concurrent.ExecutionException;

import org.joda.time.DateTimeConstants;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import edu.lmu.cs.wutup.android.autofill.DynamicSearchTrigger;
import edu.lmu.cs.wutup.android.autofill.ManualListAdapter;
import edu.lmu.cs.wutup.android.button.ResponceToPostOccurrenceButton;
import edu.lmu.cs.wutup.android.communication.HttpWutup;
import edu.lmu.cs.wutup.android.communication.PostEvent;
import edu.lmu.cs.wutup.android.communication.PostVenue;
import edu.lmu.cs.wutup.android.manager.LogTags;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Venue;

public class OccurrenceCreationForm extends Activity {
    
/**********************************************************************************************************************
 * Member Variables BEGIN
 **********************************************************************************************************************/

    
    private static final int NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS = 1;
    
    private AutoCompleteTextView eventNameTextField;
    private ManualListAdapter<Event> eventAdapter;
    private EditText eventDescriptionTextField;
    
    private AutoCompleteTextView venueNameTextField;
    private ManualListAdapter<Venue> venueAdapter;
    private EditText venueAddressTextField;
    
    private CalendarView startDate;
    private TimePicker startTime;
    private CalendarView endDate;
    private TimePicker endTime;
    
    
    private Button cancelButton;
    private Button postOccurrenceButton;
    
    private Event selectedEvent = null;
    private Venue selectedVenue = null;

/**********************************************************************************************************************
 * Member Variables END & View Programming Interface BEGIN
 **********************************************************************************************************************/
    
    public int getEventId() throws InterruptedException, ExecutionException {
        
        int selectedEventId;
        
        if (selectedEvent == null) {
            
            String name = eventNameTextField.getText().toString();
            String description = eventDescriptionTextField.getText().toString();
            
            AsyncTask<Object, Integer, Object> postEvent = new PostEvent().execute(name, description);
            selectedEventId = (Integer) postEvent.get();

                        
        } else {
            selectedEventId = selectedEvent.getId();
        }
        
        return selectedEventId;
        
    }

    public int getVenueId() throws InterruptedException, ExecutionException {
        
        int selectedVenueId;
        
        if (selectedVenue == null) {
            
            String name = venueNameTextField.getText().toString();
            String address = venueAddressTextField.getText().toString();
            
            AsyncTask<Object, Integer, Object> postVenue = new PostVenue().execute(name, address);
            selectedVenueId = (Integer) postVenue.get();
            
        } else {
            selectedVenueId = selectedVenue.getId();
        }
        
        return selectedVenueId;
        
    }
    
    public long getStart() {
        
        long start = startDate.getDate();
        
        start += startTime.getCurrentHour() * DateTimeConstants.MILLIS_PER_HOUR;
        start += startTime.getCurrentMinute() * DateTimeConstants.MILLIS_PER_MINUTE;
        
        return start;
        
    }
    
    public long getEnd() {
        
        long end = endDate.getDate();
        
        end += endTime.getCurrentHour() * DateTimeConstants.MILLIS_PER_HOUR;
        end += endTime.getCurrentMinute() * DateTimeConstants.MILLIS_PER_MINUTE;
        
        return end;
        
    }

/**********************************************************************************************************************
 * View Programming Interface END & Android Life Cycle Methods BEGIN
 **********************************************************************************************************************/
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occurrence_creation_form_layout);
        
        initiateEventDynamicSearch();
        initiateVenueDynamicSearch();
        
        initiateDateAndTimeWidgets();
        
        initiatePostOccurrenceButton();
        initiateCancelButtton();
        
    }
    
/**********************************************************************************************************************
 * Android Life Cycle Methods END & Other Public Methods BEGIN
 **********************************************************************************************************************/
    

/**********************************************************************************************************************
 * Public Methods END & Private Methods BEGIN
 **********************************************************************************************************************/

    private void initiateEventDynamicSearch() {
        
        eventNameTextField = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_event_name_text_field);
        eventDescriptionTextField = (EditText) findViewById(R.id.occurrence_creation_form_event_description_text_field);
        eventAdapter = new ManualListAdapter<Event>(this, android.R.layout.simple_dropdown_item_1line);
        
        eventNameTextField.setAdapter(eventAdapter);
        eventNameTextField.setThreshold(NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS);
        eventNameTextField.addTextChangedListener(new DynamicSearchTrigger<Event>(Event.class, 
                                                                              eventAdapter, 
                                                                              HttpWutup.ADDRESS_OF_EVENTS));
        
        initiateEventSelectionListener();
        
    }
    
    private void initiateEventSelectionListener() {
        
        eventNameTextField.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                
                setEvent(eventAdapter.getItem(position));
                
                Log.i(LogTags.OCCURRENCE_CREATION, 
                      "Selected event " + selectedEvent.getId() + ". Event named \"" + selectedEvent.getName() + "\".");
                
            }
            
            private void setEvent(Event event) {
                
                selectedEvent = event;
                
                eventDescriptionTextField.setFocusable(false);
                eventDescriptionTextField.setText(event.getDescription());
                
            }
            
        });
        
    }    
    
    private void initiateVenueDynamicSearch() {
        
        venueNameTextField = (AutoCompleteTextView) findViewById(R.id.occurrence_creation_form_venue_name_text_field);
        venueAddressTextField = (EditText) findViewById(R.id.occurrence_creation_form_venue_address_text_field);
        venueAdapter = new ManualListAdapter<Venue>(this, android.R.layout.simple_dropdown_item_1line);
        
        venueNameTextField.setAdapter(venueAdapter);
        venueNameTextField.setThreshold(NUMBER_OF_CHARACTERS_REQUIRED_BEFORE_OFFERING_SUGGESTIONS);
        venueNameTextField.addTextChangedListener(new DynamicSearchTrigger<Venue>(Venue.class, 
                                                                              venueAdapter, 
                                                                              HttpWutup.ADDRESS_OF_VENUES));
        
        initiateVenueSelectionListener();
        
    }
    
    private void initiateVenueSelectionListener() {
        
        venueNameTextField.setOnItemClickListener(new OnItemClickListener() {
            

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                setVenue(venueAdapter.getItem(position));
                
                Log.i(LogTags.OCCURRENCE_CREATION, 
                      "Selected venue " + selectedVenue.getId() + ". Venue named \"" + selectedVenue.getName() + "\".");
                
            }
            
            private void setVenue(Venue venue) {
                
                selectedVenue = venue;
                
                venueAddressTextField.setFocusable(false);
                venueAddressTextField.setText(venue.getAddress());
                
            }
            
        });
        
    }

    private void initiateDateAndTimeWidgets() {
        startDate = (CalendarView) findViewById(R.id.occurrence_creation_form_date_start);
        startTime = (TimePicker) findViewById(R.id.occurrence_creation_form_time_start);
        endDate = (CalendarView) findViewById(R.id.occurrence_creation_form_date_end);
        endTime = (TimePicker) findViewById(R.id.occurrence_creation_form_time_end);
    }
    
    
    private void initiatePostOccurrenceButton() {
        
        postOccurrenceButton = (Button) findViewById(R.id.occurrence_creation_form_button_post);
        
        postOccurrenceButton.setOnClickListener(new ResponceToPostOccurrenceButton(this));
        
    }
    
    protected Event reretrieveEventInFocus() {
        // TODO Auto-generated method stub
        return null;
    }



    private void initiateCancelButtton() {
        
        cancelButton = (Button) findViewById(R.id.occurence_creation_form_button_cancel);
        
        cancelButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
              Log.i(LogTags.OCCURRENCE_CREATION, 
                    "User opted out of occurence creation. Occurrence creation activity scheduled to close.");    
              
              finish();
              
            }
            
          });
        
    }
    

    

    
/**********************************************************************************************************************
 * Private Methods END
 **********************************************************************************************************************/    
    
}
