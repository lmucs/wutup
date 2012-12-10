package edu.lmu.cs.wutup.android.views;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.lmu.cs.wutup.android.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.container.Occurrences;
import edu.lmu.cs.wutup.android.manager.EventPlotter;
import edu.lmu.cs.wutup.android.manager.GestureOverlay;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Occurrence;
import edu.lmu.cs.wutup.android.model.Venue;

public class Map extends MapActivity {
    
    public static final int DEFAULT_RADIUS_FOR_RETRIEVING_OCCURRENCES = 100;
    
    private static Drawable dropPin;
    private static MapView mapView; 
    private static List<Overlay> mapOverlays;
    private static EventPlotter occurrenceOverlay;
    
    private static GestureOverlay gestureOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
						
		mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		
		dropPin = this.getResources().getDrawable(R.drawable.androidmarker);
		mapOverlays = mapView.getOverlays();
		occurrenceOverlay = new EventPlotter(dropPin, this);
		gestureOverlay = new GestureOverlay(this);
		
		mapOverlays.add(occurrenceOverlay);
		mapOverlays.add(gestureOverlay);
		
		refreshMap();
	
	}
	
	public static void refreshMap() {
       new GetOccurrences().execute(mapView.getMapCenter(), DEFAULT_RADIUS_FOR_RETRIEVING_OCCURRENCES);
	}
	
	@Override
    protected boolean isRouteDisplayed() {
        return false;
    }
	
	private static OverlayItem makeOverlayItem(Occurrence occurrence){
	    	    
	    Event event = occurrence.getEvent();
	    Venue venue = occurrence.getVenue();
	    
	    GeoPoint position = new GeoPoint(venue.getLatitude(), venue.getLongitude());
	    
	    return new OverlayItem(position, event.getName(), occurrence.getDetails());	    
	}
	
	public static void plotOccurrences() {
	    
	    occurrenceOverlay.clearOverlay();
	    Log.i("overlay", "Cleared occurrence overlay.");
	    
	    for (Occurrence occurrence : Occurrences.getAll()) {
	        occurrenceOverlay.addOverlay(makeOverlayItem(occurrence));
	        Log.i("overlay", "Plotted occurrence " + occurrence.getId() + ".");
	    }
	      
	}	
	
}
     