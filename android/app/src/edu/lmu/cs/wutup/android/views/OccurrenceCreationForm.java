package edu.lmu.cs.wutup.android.views;

import android.app.Activity;
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import edu.lmu.cs.wutup.android.autofill.DynamicSearchTrigger;
import edu.lmu.cs.wutup.android.autofill.ManualListAdapter;
import edu.lmu.cs.wutup.android.communication.HttpWutup;
import edu.lmu.cs.wutup.android.manager.LogTags;
import edu.lmu.cs.wutup.android.manager.R;
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
    
    private long startInUnixTime;
    private long endInUnixTime;
    
    private Button cancelButton;
    private Button postOccurrenceButton;
    
    private Event selectedEvent = null;
    private Venue selectedVenue = null;

    
    public long getStartDate() {
        return startDate.getDate();
    }

    public long getStartHour() {
        return startTime.getCurrentHour();
    }
    
    public long getStartMinute() {
        return startTime.getCurrentHour();
    }

    public long getEndDate() {
        return endDate.getDate();
    }

    public long getEndHour() {
        return endTime.getCurrentHour();
    }
    
    public long getEndMinute() {
        return endTime.getCurrentHour();
    }


    public Event getSelectedEvent() {
        return selectedEvent;
    }


    public TimePicker getEndTime() {
        return endTime;
    }

    public Venue getSelectedVenue() {
        return selectedVenue;
    }

/**********************************************************************************************************************
 * Member Variables END & Android Life Cycle Methods BEGIN
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
    
    public void aaa(){
        
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
        
        postOccurrenceButton.setOnClickListener(new OnClickListener() {
            
    
            @Override
            public void onClick(View v) {
                
                Event eventInFocus = reretrieveEventInFocus();
                Venue venueInFocus = reretrieveVenueInFocus();
                
                long startInUnixTime = calculateAndSetStartInUnixTime();
                long endInUnixTime = calculateAndSetEndInUnixTime();
                
                
                
                
            }
            
            private Event reretrieveEventInFocus() {
                
                Event eventInFocus;
                
                if (selectedEvent == null) {
                    // TODO post newly created and set as focus
                    eventInFocus = null;
                    
                } else {
                    eventInFocus = selectedEvent;
                }
                
                return eventInFocus;
                
            }
            
            private Venue reretrieveVenueInFocus() {
                
                Venue venueInFocus;
                
                if (selectedVenue == null) {
                    // TODO post newly created and set as focus
                    venueInFocus = null;
                    
                } else {
                    venueInFocus = selectedVenue;
                }
                    
                return venueInFocus;
                
            }
            
            private long calculateAndSetStartInUnixTime() {
                
                long startInUnixTime = startDate.getDate();
                
                startInUnixTime += startTime.getCurrentHour() * DateTimeConstants.MILLIS_PER_HOUR;
                startInUnixTime += startTime.getCurrentMinute() * DateTimeConstants.MILLIS_PER_MINUTE;
                
                return startInUnixTime;
                
            }
            
            private long calculateAndSetEndInUnixTime() {
                
                long endInUnixTime = endDate.getDate();
                
                endInUnixTime += endTime.getCurrentHour() * DateTimeConstants.MILLIS_PER_HOUR;
                endInUnixTime += endTime.getCurrentMinute() * DateTimeConstants.MILLIS_PER_MINUTE;
                
                return endInUnixTime;
                
            }
            
        });
        
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
