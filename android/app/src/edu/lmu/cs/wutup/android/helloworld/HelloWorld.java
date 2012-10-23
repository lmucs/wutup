package edu.lmu.cs.wutup.android.helloworld;

import android.app.Activity;
import android.os.Bundle;
import com.example.R;

public class HelloWorld extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
