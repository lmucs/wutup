package edu.lmu.cs.wutup.android.container;

import java.util.ArrayList;

import edu.lmu.cs.wutup.android.model.Event;

public class Events {
    
    private static ArrayList<Event> events= new ArrayList<Event>();
    
    public static Event get(int index) {
        return events.get(index);
    }
    
    public static ArrayList<Event> getAll() {
        return events;
    }
    
    public static void add(Event event) {
        events.add(event);
    }
    
    public static void addAll(ArrayList<Event> events) {
        Events.events.addAll(events);        
    }
    
    public static void clear() {
        events.clear();        
    }

}
