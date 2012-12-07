package edu.lmu.cs.wutup.android.container;

import java.util.ArrayList;

import android.util.Log;

import edu.lmu.cs.wutup.ws.model.EventOccurrence;

public class EventOccurrences {
    
    private static ArrayList<EventOccurrence> occurrences= new ArrayList<EventOccurrence>();
    
    public static EventOccurrence get(int index) {
        return occurrences.get(index);
    }
    
    public static ArrayList<EventOccurrence> getAll() {
        return occurrences;
    }
    
    public static void add(EventOccurrence occurrence) {
        occurrences.add(occurrence);
        Log.i("container", "Added occurence " + occurrence.getId() + ".");
    }
    
    public static void addAll(ArrayList<EventOccurrence> occurrences) {
        EventOccurrences.occurrences.addAll(occurrences);        
    }
    
    public static void clear() {
        occurrences.clear();
        Log.i("container", "Cleared occurrences.");

        
    }

}
