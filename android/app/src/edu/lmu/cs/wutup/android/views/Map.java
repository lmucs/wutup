package edu.lmu.cs.wutup.android.views;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import edu.lmu.cs.wutup.android.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.container.Occurrences;
import edu.lmu.cs.wutup.android.manager.EventPlotter;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Occurrence;
import edu.lmu.cs.wutup.android.model.Venue;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {
    
    private static Drawable dropPin;
    
    private static MapView mapView; 
    private static List<Overlay> mapOverlays;
    private static EventPlotter occurrenceOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
						
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		
		dropPin = this.getResources().getDrawable(R.drawable.androidmarker);
		mapOverlays = mapView.getOverlays();
		occurrenceOverlay = new EventPlotter(dropPin, this);	
		mapOverlays.add(occurrenceOverlay);
		
		refreshMap();
		
	}
	
	public static void refreshMap() {
       new GetOccurrences().execute(mapView.getMapCenter(), 100);
	}
	
	@Override
        protected boolean isRouteDisplayed() {
                return false;
        }
	
	private static OverlayItem makeOverlayItem(Occurrence occurrence){
	    	    
	    Event event = occurrence.getEvent();
	    Venue venue = occurrence.getVenue();
	    
	    GeoPoint position = new GeoPoint(venue.getLatitude(), venue.getLongitude());
	    
	    return new OverlayItem(position, event.getName(), event.getDescription());	    
	}
	
	public static void plotOccurrences() {
	    
	    occurrenceOverlay.clearOverlay();
	    Log.i("overlay", "Cleared occurrence overlay.");
	    
	    for (Occurrence occurrence : Occurrences.getAll()) {
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
	    
	        case R.id.create_occurrence: startActivity(new Intent(this, OccurrenceCreationForm.class));  
	        							 break;
        	                      
        	case R.id.list:              break;
        	                 
        	case R.id.search:            break;                 
        	                     
        	default:                     break;
	    
	    }
	    
	    return true;
	}

}
     