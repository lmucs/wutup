package edu.lmu.cs.wutup.android.views;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import edu.lmu.cs.wutup.android.container.Occurrences;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Occurrence;


public class OccurrenceCreationForm extends Activity {
	
	Spinner eventSpinner;
	Spinner venueSpinner;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.occurrence_creation_form_v2);
		
		populateEventSpinner();
	
	}
	
	public void populateEventSpinner() {
	
		eventSpinner = (Spinner) findViewById(R.id.spinner1);
		venueSpinner = (Spinner) findViewById(R.id.spinner2);
			 
		ArrayAdapter<Occurrence> occurrenceAdapter = new ArrayAdapter<Occurrence>(this, 
				                                                                  android.R.layout.simple_spinner_item, 
				                                                                  Occurrences.getAll());
		
		occurrenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventSpinner.setAdapter(occurrenceAdapter);
		
	  }

}
