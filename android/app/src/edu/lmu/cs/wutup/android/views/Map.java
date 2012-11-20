package edu.lmu.cs.wutup.android.views;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import edu.lmu.cs.wutup.android.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.manager.EventPlotter;
import edu.lmu.cs.wutup.android.manager.R;

public class Map extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		new GetOccurrences().execute();
		
//		MapView mapView = (MapView) findViewById(R.id.map);
//		mapView.setBuiltInZoomControls(true);
//		
//		List<Overlay> mapOverlays = mapView.getOverlays();
//		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
//		EventPlotter itemizedoverlay = new EventPlotter(drawable, this);
//		
//		GeoPoint point = new GeoPoint(33947006,-118212891);
//		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
//			
//		itemizedoverlay.addOverlay(overlayitem);
//		mapOverlays.add(itemizedoverlay);
	}
	
	private void plotOccurrences(){
	    
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
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
     