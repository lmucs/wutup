package edu.lmu.cs.wutup.android.manager;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.view.MotionEvent;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import edu.lmu.cs.wutup.android.views.Map;
import edu.lmu.cs.wutup.android.views.OccurrenceCreationForm;


public class GestureOverlay extends Overlay {
  
    private MapActivity mapActivity;
    
    private Timer timer = new Timer();
    private TimerTask timerTask;
       
    private float touchDownXposition = 0;
    private float touchDownYPosition = 0;
    
    private float distanceMovedAlongXAxisSinceLastRelease = 0;
    private float distanceMovedAlongYAxisSinceLastRelease = 0;
    
    private float distanceGreatingThenShaking = 30;
    
    public GestureOverlay(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
            Map.refreshMap();
        }
        
        handleLongTouch(motionEvent);
  
        return super.onTouchEvent(motionEvent, mapView);
        
    }
    
    public void onLongTouch() {
        mapActivity.startActivity(new Intent(mapActivity, OccurrenceCreationForm.class));
    }
        
    private void handleLongTouch(MotionEvent motionEvent) {
        
        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            
            timerTask = new java.util.TimerTask() {
                
                @Override
                public void run() {
                    onLongTouch();
                }
                
            };
            
            timer.schedule(timerTask, 1000);
            
            touchDownXposition = motionEvent.getX();
            touchDownYPosition = motionEvent.getY();
            
        } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
            
            timerTask.cancel();
            
            distanceMovedAlongXAxisSinceLastRelease = 0;
            distanceMovedAlongYAxisSinceLastRelease = 0;
                    
        } else {
      
            distanceMovedAlongXAxisSinceLastRelease += Math.abs(touchDownXposition - motionEvent.getX());
            distanceMovedAlongYAxisSinceLastRelease += Math.abs(touchDownYPosition - motionEvent.getY());
            
            if (distanceMovedAlongXAxisSinceLastRelease > distanceGreatingThenShaking || distanceMovedAlongYAxisSinceLastRelease > distanceGreatingThenShaking) {
                timerTask.cancel();
            }

        }
        
    }

}
