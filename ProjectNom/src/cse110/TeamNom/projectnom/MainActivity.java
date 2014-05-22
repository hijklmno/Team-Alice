package cse110.TeamNom.projectnom;

import com.facebook.Session;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import cse110.TeamNom.projectnom.tabsadapter.TabsPagerAdapter;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private EditText mSearchTerm;
	private EditText mSearchLocation;

	// Testing comment
	// Tab titles
	private String[] tabs = { "Camera", "Profile", "Search", "News Feed" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//retrieves the facebook session established in the splash page
		Intent i = getIntent();
		Session session = (Session)i.getSerializableExtra("FacebookSession");
		
		//debugging, test if session is logged in
		if (session != null && session.isOpened()) {
			Log.d("MainActivityFacebookSession", "Logged in");
		}
		else {
			Log.d("MainActivityFacebookSession", "Logged out");
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		GPSFragment gps = new GPSFragment(this);
		double longitude;
		double latitude;
		
		if(gps.canGetLocation())
		{
			longitude = gps.getLongitude();
			latitude = gps.getLatitude();
			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLongitude" + longitude);
			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLatitude" + latitude);

		}
		else
		{
			gps.showSettingsAlert();
			longitude = gps.getLongitude();
			latitude = gps.getLatitude();
			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLongitude" + longitude);
			System.out.println("LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLatitude" + latitude);
		}
		// Parse stuff
		Parse.initialize(this, "k6xrLx1ka30TdyjSmZZRF2XVkyrvEJJq38YtZbKW", "KTchPGVBZhFSaCOetY7XbBWyaQN262o2T04b60RC");

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
            .setTabListener(this));
		}
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		 
		    @Override
		    public void onPageSelected(int position) {
		        // on changing the page
		        // make respected tab selected
		        actionBar.setSelectedNavigationItem(position);
		    }
		 
		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }
		 
		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
		});
	}

	@Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }   
    
	public void search(View v) {
 	    mSearchTerm = (EditText)findViewById(R.id.searchTerm);
 	    mSearchLocation = (EditText)findViewById(R.id.searchLocation);
		String term = mSearchTerm.getText().toString();
		String location = mSearchLocation.getText().toString();
		Intent intent = new Intent(this, YelpSearchListActivity.class);
		intent.setData(new Uri.Builder().appendQueryParameter("term", term).appendQueryParameter("location", location).build());
		startActivity(intent);
	}
}