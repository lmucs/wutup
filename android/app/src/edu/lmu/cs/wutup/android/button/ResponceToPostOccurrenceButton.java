package edu.lmu.cs.wutup.android.button;

import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import edu.lmu.cs.wutup.android.communication.PostOccurrences;
import edu.lmu.cs.wutup.android.manager.LogTags;
import edu.lmu.cs.wutup.android.views.Map;
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
        
        boolean formIsFilledCorrectly = occurrenceCreationForm.formIsCorrectlyFilled();
        
        if (formIsFilledCorrectly) {
            
            try {

                eventId = occurrenceCreationForm.getEventId();
                venueId = occurrenceCreationForm.getVenueId();
                
                start = new DateTime(occurrenceCreationForm.getStart()).toString();
                end = new DateTime(occurrenceCreationForm.getEnd()).toString();
                
                new PostOccurrences().execute(eventId, venueId, start, end);
                Map.refreshMap();
                occurrenceCreationForm.finish();
                
            } catch (InterruptedException interruptedException) {
                Log.e(LogTags.HTTP, ERROR_MESSAGE, interruptedException);
                handleErrorMessage(formIsFilledCorrectly);
                
            } catch (ExecutionException executionException) {
                Log.e(LogTags.HTTP, ERROR_MESSAGE, executionException);
                handleErrorMessage(formIsFilledCorrectly);
            
            } catch (Exception anyException) {
                Log.e(LogTags.HTTP, ERROR_MESSAGE, anyException);
                handleErrorMessage(formIsFilledCorrectly);
            }
            
            
        } else {
            handleErrorMessage(formIsFilledCorrectly);
        }
           
    }
    
    private void handleErrorMessage(boolean formIsCorrectlyFilled) {
        
        AlertDialog alertForIncompleteForm = new AlertDialog.Builder(occurrenceCreationForm).create();
        
        String alertTitle = "Form Filled Incorrectly";
        String alertBody = "";
                
        if (!formIsCorrectlyFilled) {
            
            alertBody = "Please correct the following errors. ";
            
            if (!occurrenceCreationForm.eventFieldsArecomplete()) {
                alertBody += "Event fields are incomplete! You must provide a name and a description. ";
            }
            
            if (!occurrenceCreationForm.venueFieldsArecomplete()) {
                alertBody += "Venue fields are incomplete! You must provide a name and an address. ";
            }
            
            if (!occurrenceCreationForm.startBeforeEnd()) {
                alertBody += "Event ends before it starts! The event must end after it starts. ";
            }
            
        } else {
            alertBody = "Occurrence creation failed for unknown reason! Check address field. ";
        }

        alertForIncompleteForm.setTitle(alertTitle);
        alertForIncompleteForm.setMessage(alertBody);
        alertForIncompleteForm.show();
        
    }
     
}