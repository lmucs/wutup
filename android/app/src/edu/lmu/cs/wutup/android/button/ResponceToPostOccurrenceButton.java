package edu.lmu.cs.wutup.android.button;

import org.joda.time.DateTime;

import android.view.View;
import android.view.View.OnClickListener;
import edu.lmu.cs.wutup.android.communication.PostOccurrences;
import edu.lmu.cs.wutup.android.views.OccurrenceCreationForm;

public class ResponceToPostOccurrenceButton implements OnClickListener {
    
    private OccurrenceCreationForm occurrenceCreationForm;
    
    private int eventId;
    private int venueId;
    private DateTime start;
    private DateTime end;
    
    public ResponceToPostOccurrenceButton(OccurrenceCreationForm occurrenceCreationForm) {
        this.occurrenceCreationForm = occurrenceCreationForm;
    }
    
    @Override
    public void onClick(View v) {
        
        eventId = occurrenceCreationForm.getEventId();
        venueId = occurrenceCreationForm.getVenueId();
                
        start = new DateTime(occurrenceCreationForm.getStart());
        end = new DateTime(occurrenceCreationForm.getEnd());
        
        new PostOccurrences().execute(eventId, venueId, start.toString(), end.toString());
        occurrenceCreationForm.finish();
        
    }
     
}
