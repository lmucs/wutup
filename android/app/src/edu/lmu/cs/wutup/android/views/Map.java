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
import edu.lmu.cs.wutup.android.manager.HelloItemizedOverlay;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Occurrence;
import edu.lmu.cs.wutup.android.model.Venue;

public class Map extends MapActivity {
    
    private Drawable marker;
    
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
		
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		
		mapOverlays = mapView.getOverlays();
		occurrenceOverlay = new EventPlotter(drawable, this);
		
		GeoPoint point = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");		
		occurrenceOverlay.addOverlay(overlayitem);
		
		GeoPoint point2 = new GeoPoint(35410000, 139460000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!", "I'm in Japan!");		
		occurrenceOverlay.addOverlay(overlayitem2);
		
		try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		Log.i("overlay", "" + Occurrences.get().size());
		occurrenceOverlay.addOverlay(makeOverlayItem(Occurrences.get().get(0)));
		
		addPlotOverlays();
		
		mapOverlays.add(occurrenceOverlay);
		
//		
//		GeoPoint point2 = new GeoPoint(35410000, 139460000);
//		
//                OverlayItem overlayitem2 = new OverlayItem(point2, "Hello, world!", "I'm in Mexico City!");
//		occurrenceOverlay.addOverlay(overlayitem2);
//		
//		mapView.getOverlays().add(occurrenceOverlay);
//		mapView.postInvalidate();
	}
	
	@Override
        protected boolean isRouteDisplayed() {
                return false;
        }
	
	private OverlayItem makeOverlayItem(Occurrence occurrence){
	    	    
	    Log.i("overlay", "blarkar");
	    
	    Event event = occurrence.getEvent();
	    Venue venue = occurrence.getVenue();
	    
	    Log.i("overlay", "" + venue.getLatitude());
	    Log.i("overlay", "" + venue.getLongitude());
	    GeoPoint position = new GeoPoint(venue.getLatitude(), venue.getLongitude());
	    
	    return new OverlayItem(position, event.getName(), event.getDescription());	    
	}
	
	private void addPlotOverlays() {
	    
	    for (Occurrence occurrence : Occurrences.get()) {
	        occurrenceOverlay.addOverlay(makeOverlayItem(occurrence));
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
     