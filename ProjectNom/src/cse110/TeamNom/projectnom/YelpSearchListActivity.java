package cse110.TeamNom.projectnom;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Main Yelp's search activity that obtains and displays
 * the business names and url of searched terms and location.
 */
public class YelpSearchListActivity extends ListActivity{
	class Business {
		final String name;
		final String url;

		/*
		 * Constructor that contains the business name and url.
		 */
		public Business(String name, String url) {
			this.name = name;
			this.url = url;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setTitle("Finding Location...");
        // Gets the term and location the user input
        Intent intent = getIntent();
        final String searchTerm = intent.getData().getQueryParameter("term");
        final String searchLocation = intent.getData().getQueryParameter("location");
        
        setProgressBarIndeterminateVisibility(true);
        new AsyncTask<Void, Void, List<Business>>() {
        	/*
        	 * Method that outputs a list of businesses after calling Yelps search.
        	 */
        	@Override
        	protected List<Business> doInBackground(Void... params) {
                String businesses = YelpActivity.getYelp(YelpSearchListActivity.this).search(searchTerm, searchLocation);
                try {
                	return processJson(businesses);
                } catch (JSONException e) {
                    return Collections.<Business>emptyList();
                }
        	}

        	@Override
        	protected void onPostExecute(List<Business> businesses) {
        		setTitle("Restaurant Found");
        		setProgressBarIndeterminateVisibility(false);
        		getListView().setAdapter(new ArrayAdapter<Business>(YelpSearchListActivity.this, android.R.layout.simple_list_item_1, businesses));
        	}
        }.execute();
    }
	
	/*
	 * Separates the businesses that was obtained from a list and displays the url
	 * once clicked.
	 */
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		Business biz = (Business)listView.getItemAtPosition(position);
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(biz.url)));
	};

	/*
	 * Method that processes Yelp's JSON files.
	 */
	List<Business> processJson(String jsonStuff) throws JSONException {
		JSONObject json = new JSONObject(jsonStuff);
		JSONArray businesses = json.getJSONArray("businesses");
		ArrayList<Business> businessObjs = new ArrayList<Business>(businesses.length());
		for (int i = 0; i < businesses.length(); i++) {
			JSONObject business = businesses.getJSONObject(i);
			businessObjs.add(new Business(business.optString("name"), business.optString("mobile_url")));
		}
		return businessObjs;
	}
}
