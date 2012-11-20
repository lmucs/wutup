package edu.lmu.cs.wutup.android.views;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import edu.lmu.cs.wutup.andoird.communication.GetOccurrences;
import edu.lmu.cs.wutup.android.manager.Client;
import edu.lmu.cs.wutup.android.manager.R;
import edu.lmu.cs.wutup.android.model.Event;
import edu.lmu.cs.wutup.android.model.Occurrence;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
