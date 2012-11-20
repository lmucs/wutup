package edu.lmu.cs.wutup.android.views;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import edu.lmu.cs.wutup.andoird.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.manager.R;

public class TextDump extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_view);
        
        TextView log = (TextView) this.findViewById(R.id.log_text);       
        log.append("blarkar");
        log.append("narnar");
        
        Log.i("sometag", "yay log printed");
        
        

                
        
            new GetOccurrences().execute();
        
        
        
        
    }
    
    
    

}
