package cse110.TeamNom.projectnom;

import cse110.TeamNom.projectnom.tabsadapter.TabsPagerAdapter;
import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
 * MainActivity for our application that sets up the tabs
 * for our application.
 */
public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private EditText mSearchTerm;
	private EditText mSearchLocation;

	// Tab titles
	private String[] tabs = { "Newsfeed", "Camera", "Search", "Profile" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTag("stringName").setTabListener(this));
		}

		/**
		 * On swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			/*
			 * Method that goes to the selected tab
			 */
			@Override
			public void onPageSelected(int position) {
				// On changing the page make corresponding tab selected
				actionBar.setSelectedNavigationItem(position);
			}
			/*
			 * Method that goes to scrolled tab
			 */
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
		// On tab selected show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	/*
	 * Method that searches for restaurant with user input.
	 */
	public void search(View v) {
		mSearchTerm = (EditText) findViewById(R.id.searchTerm);
		mSearchLocation = (EditText) findViewById(R.id.searchLocation);
		String term = mSearchTerm.getText().toString();
		String location = mSearchLocation.getText().toString();
		// Check to indicate that user input term
		if (term.matches("")) {
			Toast toast = Toast
					.makeText(this,"Please enter a search term! ",
							Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		// Check to indicate that user input a location
		if (location.matches("")) {
			Toast toast = Toast
					.makeText(this,"Please enter a location! ",
							Toast.LENGTH_LONG);
			toast.show();
			return;
		}
		// Intent that leads to calling Yelps search functionality.
		Intent intent = new Intent(this, YelpSearchListActivity.class);
		intent.setData(new Uri.Builder().appendQueryParameter("term", term)
				.appendQueryParameter("location", location).build());
		startActivity(intent);
	}

	/*
	 * Method that displays the creators of the application. Basically an easter
	 * egg.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			final CharSequence teamnom[] = new CharSequence[] {
					"William Huang", "Ryan Fu", "Tiffany Wang", "Alice Chen",
					"David Ung", "Watson Yim", "Trent Stevens", "Rex Tong",
					"Raymond Tran", "Jean Park", "Exit" };
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
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
												+ " is the best",
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