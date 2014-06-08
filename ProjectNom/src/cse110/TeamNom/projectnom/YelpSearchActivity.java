package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 * Activity class that processes Yelp's JSON files. Outputs
 * Strings that are used for businesses.
 */
public class YelpSearchActivity extends Activity {
	private TextView mSearchResultsText;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        // Finds the restaurant using yelps search services.
        setContentView(R.layout.fragment_yelpsearch);
        setTitle("Finding Restaurants...");
        mSearchResultsText = (TextView)findViewById(R.id.searchResults);

        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, String>() {
        	@Override
        	protected String doInBackground(Void... params) {
                YelpActivity yelp = YelpActivity.getYelp(YelpSearchActivity.this);
                String businesses = yelp.search("tacos", 37.788022, -122.399797);
                try {
                	return processJson(businesses);
                } catch (JSONException e) {
                    return businesses;
                }
        	}

        	@Override
        	protected void onPostExecute(String result) {
        		mSearchResultsText.setText(result);
        		setProgressBarIndeterminateVisibility(false);
        	}
        }.execute();
    }

	/*
	 * Method that process JSON strings from Yelp and populates
	 * a list of businesses.
	 */
	String processJson(String jsonStuff) throws JSONException {
		JSONObject json = new JSONObject(jsonStuff);
		JSONArray businesses = json.getJSONArray("businesses");
		ArrayList<String> businessNames = new ArrayList<String>(businesses.length());
		for (int i = 0; i < businesses.length(); i++) {
			JSONObject business = businesses.getJSONObject(i);
			businessNames.add(business.getString("name"));
		}
		return TextUtils.join("\n", businessNames);
	}
}
