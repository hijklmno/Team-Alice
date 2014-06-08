package cse110.TeamNom.projectnom.tabsadapter;

import cse110.TeamNom.projectnom.CameraFragment;
import cse110.TeamNom.projectnom.NewsFeedFragment;
import cse110.TeamNom.projectnom.ProfileFragment;
import cse110.TeamNom.projectnom.SearchFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/*
 * Class that separates different functionality of the application to
 * different tabs.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
	Fragment CameraFragment = null;
	Fragment ProfileFragment = null;
	Fragment SearchFragment = null;
	Fragment NewsFeedFragment = null;
	
	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		CameraFragment = new CameraFragment();
		ProfileFragment = new ProfileFragment();
		SearchFragment = new SearchFragment();
		NewsFeedFragment = new NewsFeedFragment();
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		case 0:
			// Camera fragment activity
			return NewsFeedFragment;
		case 1:
			// Profile fragment activity
			return CameraFragment;
		case 2:
			// Search fragment activity
			return SearchFragment;
		case 3:
			// NewsFeed fragment activity
			return ProfileFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}
	
}
