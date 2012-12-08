package edu.lmu.cs.wutup.android.container;

import java.util.ArrayList;

import android.util.Log;

import edu.lmu.cs.wutup.android.model.Venue;

public class Venues {
    
    private static ArrayList<Venue> venues= new ArrayList<Venue>();
    
    public static Venue get(int index) {
        return venues.get(index);
    }
    
    public static ArrayList<Venue> getAll() {
        return venues;
    }
    
    public static void add(Venue venue) {
        venues.add(venue);
        Log.i("container", "Added venue " + venue.getId() + ".");
    }
    
    public static void addAll(ArrayList<Venue> venues) {
        Venues.venues.addAll(venues);        
    }
    
    public static void clear() {
        venues.clear();
        Log.i("container", "Cleared venues.");
    }

}
