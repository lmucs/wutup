package edu.lmu.cs.wutup.android.manager;

public class Converters {
    
    public static int convertToMicrodegrees(double degrees) {    
        
        int microDegrees = (int) (degrees * Math.pow(10, 6));
        
        return microDegrees;
        
    }

}
