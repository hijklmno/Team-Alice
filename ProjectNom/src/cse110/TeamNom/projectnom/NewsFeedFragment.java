package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import cse110.TeamNom.projectnom.newsfeedadapter.CustomListAdapter;
import cse110.TeamNom.projectnom.newsfeedadapter.NewsItem;

public class NewsFeedFragment extends Fragment {

	// Initialization
	private static int MAXROWS = 4;
	private static int OFFSET = 0;
	private static boolean INITIALLOAD = true;
	private static String arr[];
	private static int user_count = 0;
	private static int pict_count = 0;
	private static boolean listEndFlag = false;
	
	private Switch switchButton;
	private ViewPager yourViewPager;
	private CustomListAdapter cLAdapter;
	
	private PullToRefreshListView mPullRefreshListView;
	private String[] friends_list = new String[MAXROWS];
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_newsfeed, container,
		false);

		switchButton = (Switch) rootView.findViewById(R.id.newsFeedToggle);
		switchButton
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							// friends
						}
						else {
							// popular
						}
					}
				});

		if (INITIALLOAD) {
			getNewsFeedData(rootView);
		}

		OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				Toast.makeText(getActivity(), "DAVID", Toast.LENGTH_LONG)
				.show();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Toast.makeText(getActivity(), "scrolled", Toast.LENGTH_LONG)
				.show();
			}

			@Override
			public void onPageSelected(int pos) {
				Toast.makeText(getActivity(), "selected", Toast.LENGTH_LONG)
				.show();
			}
		};

		yourViewPager = new ViewPager(getActivity());
		yourViewPager.setOnPageChangeListener(mPageChangeListener);
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		INITIALLOAD = false;
		PullToRefreshListView mPullRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.custom_list);
		mPullRefreshListView.setAdapter(cLAdapter);
	}
	
	private void getMoreData() {
		ArrayList<NewsItem> newResults = getListData();
		cLAdapter.updateResults(newResults);
	}
	
	private void getNewsFeedData(View rootView) {
		friends_list = AppFacebookAccess.loadMyFriends();
		
		ArrayList<NewsItem> image_details = getListData();
		mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.custom_list);
		cLAdapter = new CustomListAdapter(getActivity(), image_details);
		mPullRefreshListView.setAdapter(cLAdapter);
//		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> a, View v, int position,
//			long id) {
//				Object o = mPullRefreshListView.get(position);
//				NewsItem newsData = (NewsItem) o;
////				Toast.makeText(getActivity(), "Selected :" + " " + newsData,
////				Toast.LENGTH_LONG).show();
//				Toast.makeText(getActivity(), "DAVID", Toast.LENGTH_LONG)
//				.show();
//			}
//		});
		//enable bottom and top scroll
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
				if (listEndFlag == false) {
//					new GetMoreDataTask().execute();
					getMoreData();
				}
				else {
					Toast.makeText(getActivity(), "No More Data", 2)
					.show();
				}
			}
		});
	}

	private ArrayList<NewsItem> getListData() {
		ArrayList<NewsItem> results = new ArrayList<NewsItem>();
		
		ArrayList<PictureDBObject> pictureArray = AppParseAccess.getFriendsPictureWithLimits(friends_list, MAXROWS, OFFSET);
		
		Log.d("pictureArrayLen", Integer.toString(pictureArray.size()));
		for (int i = 0; i < pictureArray.size(); i++) {
			Log.d("pictureArray", pictureArray.get(i).getCreatedDate().toString());
			
			String name = pictureArray.get(i).getCaption();
			byte[] img = pictureArray.get(i).getPicture();
			Date created = pictureArray.get(i).getCreatedDate();
			
			NewsItem newsData = new NewsItem();
			
			newsData.setName(name);
			newsData.setDate(created.toString());
			newsData.setFile(img);
			
			results.add(newsData);
		}
		
		OFFSET += MAXROWS;
		return results;
	}

	public void refresh() {
		listEndFlag = false;
		user_count = 0;
		pict_count = 0;
		
		OFFSET = 0;
		friends_list = AppFacebookAccess.loadMyFriends();
		Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_LONG)
		.show();
		ArrayList<NewsItem> image_details = getListData();
		cLAdapter.resetResults(image_details);
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
