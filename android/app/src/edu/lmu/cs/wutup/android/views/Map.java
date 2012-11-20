package edu.lmu.cs.wutup.android.views;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.lmu.cs.wutup.android.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.container.Occurrences;
import edu.lmu.cs.wutup.android.manager.EventPlotter;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Occurrence;
import edu.lmu.cs.wutup.android.model.Venue;

public class Map extends MapActivity {
    
    private Drawable dropPin;
    
    private MapView mapView; 
    private List<Overlay> mapOverlays;
    private EventPlotter occurrenceOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		new GetOccurrences().execute();
		
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		
		dropPin = this.getResources().getDrawable(R.drawable.androidmarker);
		
		mapOverlays = mapView.getOverlays();
		occurrenceOverlay = new EventPlotter(dropPin, this);	
		mapOverlays.add(occurrenceOverlay);
		
		try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }       
		plotOccurrences();		
		
	}
	
	@Override
        protected boolean isRouteDisplayed() {
                return false;
        }
	
	private OverlayItem makeOverlayItem(Occurrence occurrence){
	    	    
	    Event event = occurrence.getEvent();
	    Venue venue = occurrence.getVenue();
	    
	    GeoPoint position = new GeoPoint(venue.getLatitude(), venue.getLongitude());
	    
	    return new OverlayItem(position, event.getName(), event.getDescription());	    
	}
	
	private void plotOccurrences() {
	    
	    occurrenceOverlay.clearOverlay();
	    Log.i("overlay", "Cleared occurrence overlay.");
	    
	    for (Occurrence occurrence : Occurrences.get()) {
	        occurrenceOverlay.addOverlay(makeOverlayItem(occurrence));
	        Log.i("overlay", "Plotted occurrence " + occurrence.getId() + ".");
	    }
	      
	}	
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_map_view, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    //respond to menu item selection
	    
	    switch (item.getItemId()) {
	    
	        case R.id.log:    startActivity(new Intent(this, TextDump.class));
        	                  return true;
        	                      
        	case R.id.list:   return true;
        	                 
        	case R.id.search: return true;                 
        	                     
        	default:          return true;
	    
	    }
	}

}
     