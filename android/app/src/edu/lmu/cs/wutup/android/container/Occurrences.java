package edu.lmu.cs.wutup.android.container;

import java.util.ArrayList;

import android.util.Log;

import edu.lmu.cs.wutup.android.model.Occurrence;

public class Occurrences {
    
    private static ArrayList<Occurrence> occurrences= new ArrayList<Occurrence>();
    
    public static Occurrence get(int index) {
        return occurrences.get(index);
    }
    
    public static ArrayList<Occurrence> getAll() {
        return occurrences;
    }
    
    public static void add(Occurrence occurrence) {
        occurrences.add(occurrence);
        Log.i("container", "Added occurence " + occurrence.getId() + ".");
    }
    
    public static void addAll(ArrayList<Occurrence> occurrences) {
        Occurrences.occurrences.addAll(occurrences);        
    }
    
    public static void clear() {
        occurrences.clear();        
    }

}
