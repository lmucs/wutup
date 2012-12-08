package edu.lmu.cs.wutup.android.button;

import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.util.Log;
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
        
//        eventId = occurrenceCreationForm.getEventId();
//        venueId = occurrenceCreationForm.getVenueId();
                
//        start = new DateTime(occurrenceCreationForm.getStart());
//        end = new DateTime(occurrenceCreationForm.getEnd());
//        
//        new PostOccurrences().execute(eventId, venueId, start.toString(), end.toString());
        try {
            int blarkar = occurrenceCreationForm.getVenueId();
            Log.d("POST", "" + blarkar);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        occurrenceCreationForm.finish();
        
    }
     
}
