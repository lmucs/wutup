package edu.lmu.cs.wutup.android.button;

import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import edu.lmu.cs.wutup.android.communication.PostOccurrences;
import edu.lmu.cs.wutup.android.manager.LogTags;
import edu.lmu.cs.wutup.android.views.OccurrenceCreationForm;

public class ResponceToPostOccurrenceButton implements OnClickListener {
    
    public static final String ERROR_MESSAGE = "Failed to post occurrence!";
    
    private OccurrenceCreationForm occurrenceCreationForm;
    
    private int eventId;
    private int venueId;
    private String start;
    private String end;
    
    public ResponceToPostOccurrenceButton(OccurrenceCreationForm occurrenceCreationForm) {
        this.occurrenceCreationForm = occurrenceCreationForm;
    }
    
    @Override
    public void onClick(View v) {
        
        try {

            eventId = occurrenceCreationForm.getEventId();
            venueId = occurrenceCreationForm.getVenueId();
            
            start = new DateTime(occurrenceCreationForm.getStart()).toString();
            end = new DateTime(occurrenceCreationForm.getEnd()).toString();
            
            new PostOccurrences().execute(eventId, venueId, start, end);
            
        } catch (InterruptedException e) {
            Log.e(LogTags.POST, ERROR_MESSAGE, e);
            
        } catch (ExecutionException e) {
            Log.e(LogTags.POST, ERROR_MESSAGE, e);
        }
        
        occurrenceCreationForm.finish();
        
    }
     
}
