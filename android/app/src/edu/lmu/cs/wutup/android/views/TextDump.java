package edu.lmu.cs.wutup.android.views;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import edu.lmu.cs.wutup.andoird.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Occurrence;

public class TextDump extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_view);
        
        TextView log = (TextView) this.findViewById(R.id.log_text);       
        log.append("blarkar");
        log.append("narnar");
        
        Log.i("sometag", "yay log printed");
        
        

                
        
        AsyncTask<Void, Integer, ArrayList<Occurrence>> blarkar = new GetOccurrences().execute();
        
        try {
            ArrayList<Occurrence> naspa = blarkar.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
        
    }
    
    
    

}
