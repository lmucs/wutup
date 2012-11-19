package edu.lmu.cs.wutup.android.views;

import edu.lmu.cs.wutup.android.manager.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Log extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);        
        //TextView log = (TextView) this.findViewById(R.id.log);        
        setContentView(R.layout.log_view);
        
        //log.setText(R.string.content_log);
        
    }

}
