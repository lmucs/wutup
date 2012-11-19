package edu.lmu.cs.wutup.android.views;

import edu.lmu.cs.wutup.android.manager.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Log extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_view);
        
        TextView log = (TextView) this.findViewById(R.id.log_text);       
        log.append("blarkar");
        log.append("narnar");
        
    }

}
