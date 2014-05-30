package cse110.TeamNom.projectnom;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import cse110.TeamNom.projectnom.tabsadapter.TabsPagerAdapter;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private EditText mSearchTerm;
	private EditText mSearchLocation;

	// Testing comment
	// Tab titles
	private String[] tabs = { "Camera", "Search", "News Feed", "Profile" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// retrieves the facebook session established in the splash page
		Intent i = getIntent();
		Session session = (Session) i.getSerializableExtra("FacebookSession");

		// debugging, test if session is logged in
		if (session != null && session.isOpened()) {
			Log.d("MainActivityFacebookSession", "Logged in");
		} else {
			Log.d("MainActivityFacebookSession", "Logged out");
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Facebook stuff
		AppFacebookAccess.setActiveSession();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Intent i = getIntent();
					
					Session session = (Session) i.getSerializableExtra("FacebookSession");

					AppFacebookAccess.getNameAndID();
					System.out.println("outside nameandid");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Parse stuff
		Context context = this.getApplicationContext();
		AppParseAccess.initialize(this, context.getString(R.string.ParseAppID), context.getString(R.string.ParseClientKey));
		AppParseAccess.loadOrAddNewUser(AppFacebookAccess.getFacebookId(), AppFacebookAccess.getFacebookName());

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		// viewPager.setOffscreenPageLimit(4);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTag("stringName").setTabListener(this));
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
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int position) {
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
		mSearchTerm = (EditText) findViewById(R.id.searchTerm);
		mSearchLocation = (EditText) findViewById(R.id.searchLocation);
		String term = mSearchTerm.getText().toString();
		String location = mSearchLocation.getText().toString();
		Intent intent = new Intent(this, YelpSearchListActivity.class);
		intent.setData(new Uri.Builder().appendQueryParameter("term", term)
				.appendQueryParameter("location", location).build());
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			final CharSequence teamnom[] = new CharSequence[] {
					"William Huang", "Ryan Fu", "Tiffany Wang", "Alice Chen",
					"David Ung", "Watson Lim", "Trent Stevens", "Rex Tong",
					"Raymond Tran", "Jean Park", "Exit" };
			// TODO
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(false);
			builder.setTitle("Team NOM");
			builder.setItems(teamnom, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Add your own life captions or quote
					switch (which) {
					case 0:
						Toast toast = Toast
								.makeText(
										MainActivity.this,
										teamnom[which]
												+ "- If there's a will there's a William!",
										Toast.LENGTH_LONG);
						toast.show();
						break;
					case 1:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 2:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 3:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 4:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 5:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 6:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 7:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 8:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					case 9:
						toast = Toast.makeText(MainActivity.this,
								teamnom[which] + " is the best",
								Toast.LENGTH_LONG);
						toast.show();
						break;
					default:
						dialog.cancel();
						break;
					}
				}
			});
			builder.show();
		}
		return (super.onOptionsItemSelected(menuItem));
	}
}