package edu.lmu.cs.wutup.android.manager;

import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.views.Log;
import edu.lmu.cs.wutup.android.views.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class Manager extends Activity {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager);
        
        startActivity(new Intent(this, Map.class));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_view, menu);
        return true;
    }
    
}
