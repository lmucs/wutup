package edu.lmu.cs.wutup.android.maps;

import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		EventPlotter itemizedoverlay = new EventPlotter(drawable, this);
		
		GeoPoint point = new GeoPoint(33947006,-118212891);
		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
	
		itemizedoverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedoverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
     