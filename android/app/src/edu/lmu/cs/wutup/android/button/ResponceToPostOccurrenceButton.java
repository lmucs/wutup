package edu.lmu.cs.wutup.android.button;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import android.view.View;
import android.view.View.OnClickListener;
import edu.lmu.cs.wutup.android.communication.PostOccurrences;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Venue;
import edu.lmu.cs.wutup.android.views.OccurrenceCreationForm;

public class ResponceToPostOccurrenceButton implements OnClickListener {
    
    private OccurrenceCreationForm occurrenceCreationForm;
    
    private Event event;
    private Venue venue;
    private DateTime start;
    private DateTime end;
    
    public ResponceToPostOccurrenceButton(OccurrenceCreationForm occurrenceCreationForm) {
        this.occurrenceCreationForm = occurrenceCreationForm;
    }
    
    @Override
    public void onClick(View v) {
        
        event = reretrieveEvent();
        venue = reretrieveVenue();
        
        long startInUnixTime = reretrieveStartInUnixTime();
        long endInUnixTime = reretrieveEndInUnixTime();
        
        start = new DateTime(startInUnixTime);
        end = new DateTime(endInUnixTime);
        
        new PostOccurrences().execute(event.getId(), venue.getId(), start.toString(), end.toString());
        occurrenceCreationForm.finish();
        
    }
    
    private Event reretrieveEvent() {
        
        Event eventInFocus = occurrenceCreationForm.getSelectedEvent();
        
        if (eventInFocus == null) {
            throw new UnsupportedOperationException();
        }
        
        return eventInFocus;
        
    }
    
    private Venue reretrieveVenue() {
        
        Venue venueInFocus = occurrenceCreationForm.getSelectedVenue();
        
        if (venueInFocus == null) {
            throw new UnsupportedOperationException();
        }
            
        return venueInFocus;
        
    }
    
    private long reretrieveStartInUnixTime() {
        
        long startInUnixTime = occurrenceCreationForm.getStartDate();
        
        startInUnixTime += occurrenceCreationForm.getStartHour() * DateTimeConstants.MILLIS_PER_HOUR;
        startInUnixTime += occurrenceCreationForm.getStartHour() * DateTimeConstants.MILLIS_PER_MINUTE;
        
        return startInUnixTime;
        
    }
    
    private long reretrieveEndInUnixTime() {
        
        long endInUnixTime = occurrenceCreationForm.getEndDate();
        
        endInUnixTime += occurrenceCreationForm.getEndHour() * DateTimeConstants.MILLIS_PER_HOUR;
        endInUnixTime += occurrenceCreationForm.getEndHour() * DateTimeConstants.MILLIS_PER_MINUTE;
        
        return endInUnixTime;
        
    }
    
}
