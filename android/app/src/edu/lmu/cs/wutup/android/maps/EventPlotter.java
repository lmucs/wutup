package edu.lmu.cs.wutup.android.maps;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class EventPlotter extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> events = new ArrayList<OverlayItem>();

	public EventPlotter(Drawable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
