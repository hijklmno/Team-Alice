package cse110.TeamNom.projectnom;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
// testing committ
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        
        Parse.initialize(this, "d3r42N8HSeVbFU3RicMpjmjfgiRNHEKTOiSEcofn", "hsnuWjlI3k6gh19PrySFajFZwTvpuWwjbUZkFEo8");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void sendMessage(View view) {
    	ParseObject testObject = new ParseObject("TestObject");
    	testObject.put("foo", "bar");
    	testObject.saveInBackground();
    }
    
    public void getMessage(View view) {
    	ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
    	query.whereEqualTo("foo", "bar");
//    	query.findInBackground(new FindCallback<ParseObject>() {
//    	    public void done(List<ParseObject> scoreList, ParseException e) {
//    	        if (e == null) {
//    	            Log.d("score", "Retrieved " + scoreList.size() + " scores");
//    	        } else {
//    	            Log.d("score", "Error: " + e.getMessage());
//    	        }
//    	    }
//    	});
    }
}
