package edu.lmu.cs.wutup.android.manager;

import java.util.ArrayList;

import android.provider.ContactsContract.CommonDataKinds.Event;

public class Events {
    
    private static ArrayList<Event> events= new ArrayList<Event>();
    
    public static ArrayList<Event> get() {
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
