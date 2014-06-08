package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cse110.TeamNom.projectnom.newsfeedadapter.CustomListAdapter;

public class NewsFeedFragment extends Fragment {

	// Initialization
	private static int fMAXROWS = 4;
	private static int fOFFSET = 0;
	private static int MAXROWS = 4;
	private static int OFFSET = 0;
	private static boolean listEndFlag = false;
	private static boolean listEndFlagAll = false;
	private Switch switchButton;
	private PullToRefreshListView mPullRefreshListView;
	private ViewPager yourViewPager;

	private CustomListAdapter cLAdapterFriends;
	private String[] friends_list;

	private CustomListAdapter cLAdapterAll;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_newsfeed, container,
				false);

		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.custom_list);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				new RenewDataTask().execute();
			}
		});

		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (switchButton.isChecked()) {
					if (listEndFlag == false) {
						Toast.makeText(getActivity(), "Loading more friends data...", Toast.LENGTH_SHORT).show();
						getMoreData();
					}
					else {
						Toast.makeText(getActivity(), "No more friends data", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					if (listEndFlagAll == false) {
						Toast.makeText(getActivity(), "Loading more all data...", Toast.LENGTH_SHORT).show();
						getMoreData();
					}
					else {
						Toast.makeText(getActivity(), "No more all data", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		switchButton = (Switch) rootView.findViewById(R.id.newsFeedToggle);
		switchButton
		.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (cLAdapterFriends != null) {
						mPullRefreshListView.setAdapter(cLAdapterFriends);
					}
					else {
						getFriendsData(rootView);
					}
				}
				else {
					if (cLAdapterAll != null) {
						mPullRefreshListView.setAdapter(cLAdapterAll);
					}
					else {
						getProxData(rootView);
					}
					// popular
				}
			}
		});

		mPullRefreshListView.setAdapter(cLAdapterFriends);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		getFriendsData(getView());
		getProxData(getView());
		mPullRefreshListView.setAdapter(cLAdapterFriends);
	}

	private void getProxData(View rootView) {
		ArrayList<PictureDBObject> all_img_details = getAllListData();
		cLAdapterAll = new CustomListAdapter(getActivity(), all_img_details);
	}

	private ArrayList<PictureDBObject> getAllListData() {
		GPSFragment gps = new GPSFragment(getActivity());
//		Toast.makeText(getActivity(), "Getting nearby 50 miles of food...", 2).show();
		// hard code 50 mile radius
		int radius = 50;

		// if current latitude and longitude is 0 (something is wrong with GPS), default to san diego
		double currentLat = gps.getLatitude();
		double currentLong = gps.getLongitude();

		if (currentLat == 0) {
			Toast.makeText(getActivity(), "GPS Error, defaulting to San Diego", 2).show();
			currentLat = 32.88;
		}
		if (currentLong == 0) {
			Toast.makeText(getActivity(), "GPS Error, defaulting to San Diego", 2).show();
			currentLong = -117.2;
		}

		ArrayList<PictureDBObject> pictureArray = AppParseAccess.getPictureFiles(currentLat, currentLong, radius, MAXROWS, OFFSET);

		if (pictureArray != null) {
			Log.d("ProxPictureArrayLen", Integer.toString(pictureArray.size()));
			for (int i = 0; i < pictureArray.size(); i++) {
				Log.d("ProxPictureArray", pictureArray.get(i).getCreatedDate().toString());
			}
			OFFSET += MAXROWS;
		}
		else {
			Toast.makeText(getActivity(), "No more posts nearby", 2).show();
			listEndFlagAll = true;
		}
		return pictureArray;
	}

	private void getFriendsData(View rootView) {
		ArrayList<PictureDBObject> friends_img_details = getListData();
		cLAdapterFriends = new CustomListAdapter(getActivity(), friends_img_details);
	}

	private ArrayList<PictureDBObject> getListData() {
		ArrayList<String> myFriends = AppFacebookAccess.loadMyFriends();
		String[] myFriendsArr = myFriends.toArray(new String[myFriends.size()]);
		ArrayList<PictureDBObject> pictureArray = AppParseAccess.getFriendsPictureWithLimits(myFriendsArr, fMAXROWS, fOFFSET);

		if (pictureArray != null) {
			Log.d("pictureArrayLen", Integer.toString(pictureArray.size()));
			for (int i = 0; i < pictureArray.size(); i++) {
				Log.d("pictureArray", pictureArray.get(i).getCreatedDate().toString());
			}

			fOFFSET += fMAXROWS;
		}
		else {
			listEndFlag = true;
		}

		return pictureArray;
	}

	private void getMoreData() {
		if (switchButton.isChecked()) {
			ArrayList<PictureDBObject> newResults = getListData();
			if (newResults != null) {
				cLAdapterFriends.updateResults(newResults);
			}
			else {
				listEndFlag = true;
			}
		}
		else {
			ArrayList<PictureDBObject> newResults = getAllListData();
			if (newResults != null) {
				cLAdapterAll.updateResults(newResults);
			}
			else {
				listEndFlagAll = true;
			}
		}

	}

	public void refresh() {
		Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_LONG)
		.show();

		if (switchButton.isChecked()) {
			listEndFlag = false;
			fOFFSET = 0;
			ArrayList<PictureDBObject> image_details = getListData();
			if (image_details != null) {
				cLAdapterFriends.resetResults(image_details);
			}
			else {
				cLAdapterFriends.resetResults(new ArrayList<PictureDBObject>());
			}
		}
		else {
			listEndFlagAll = false;
			OFFSET = 0;
			ArrayList<PictureDBObject> image_details = getAllListData();
			if (image_details != null) {
				cLAdapterAll.resetResults(image_details);
			}
			else {
				cLAdapterAll.resetResults(new ArrayList<PictureDBObject>());
			}
		}
	}

	private class RenewDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			refresh();
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

}
