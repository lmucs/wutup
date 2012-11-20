package edu.lmu.cs.wutup.android.container;

import java.util.ArrayList;

import edu.lmu.cs.wutup.android.model.Occurrence;

public class Occurrences {
    
    private static ArrayList<Occurrence> occurrences= new ArrayList<Occurrence>();
    
    public static ArrayList<Occurrence> get() {
        return occurrences;
    }
    
    public static void add(Occurrence occurrence) {
        occurrences.add(occurrence);
    }
    
    public static void addAll(ArrayList<Occurrence> occurrences) {
        Occurrences.occurrences.addAll(occurrences);        
    }
    
    public static void clear() {
        occurrences.clear();        
    }

}
