package edu.lmu.cs.wutup.android.manager;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class EventPlotter extends ItemizedOverlay<OverlayItem> {
    
        private Context mContext;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public EventPlotter(Drawable defaultMarker) {
	    super(boundCenterBottom(defaultMarker));
	    populate();
	}
	
	public EventPlotter(Drawable defaultMarker, Context context) {
	    super(boundCenterBottom(defaultMarker));
	    mContext = context;
	    populate();
	}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public void clearOverlay() {
	    mOverlays.clear();
            populate();
        }

	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
}
