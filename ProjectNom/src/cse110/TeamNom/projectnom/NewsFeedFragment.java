package cse110.TeamNom.projectnom;

import java.util.ArrayList;
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
	private static boolean INITIALLOAD = true;
	private static String arr[];
	private static int user_count = 0;
	private static int pict_count = 0;
	private static boolean listEndFlag = false;
	
	private Switch switchButton;
	private ViewPager yourViewPager;
	private CustomListAdapter cLAdapter;
	
	private PullToRefreshListView mPullRefreshListView;
	
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
//					getMoreData();
					new GetMoreDataTask().execute();
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
		boolean flag = false;
		ParseObject pObj;
		ParseFile pf;
		// ALL THE USERS
		ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
		// ALL THE FOOD
		ParseQuery<ParseObject> food = ParseQuery.getQuery("Food_Table_DB");
		// ALWAYS GET 2 USERS
		//query.setLimit(MAXROWS);
		// Get next 2 USERS
		query.orderByDescending("updatedAt");
		List<ParseObject> news;
		try {
			// News get populated
			news = query.find();
			for (; user_count < news.size(); user_count++) {
				// Create the picture list
				System.out.println("IN LOOP: " + user_count);
				if(news.get(user_count).getString("pictures") != null)
					  arr = news.get(user_count).getString("pictures").split(",");
				else
					  continue;
				for(; pict_count < arr.length; pict_count++)
				{
					//System.out.println("ARR_LENGTH: " + arr[pict_count]);
					NewsItem newsData = new NewsItem();
					newsData.setName(news.get(user_count).getString("Name"));
					pObj = food.get(arr[arr.length - 1 - pict_count]);
					newsData.setId(arr[arr.length - 1 - pict_count]);
					pf = pObj.getParseFile("Food_photo");
					newsData.setFile(pf.getData());
					results.add(newsData);
					if(pict_count%MAXROWS == 0 && pict_count != 0)
					{
						flag = true;
						pict_count++;
						break;
					}
				}
				if(flag)
				{
					break;
				}
				if(pict_count == arr.length)
					pict_count = 0;

				if(news.get(user_count).getBoolean("report_image")) {
					System.out.println("hi ray");
				}
				// results.add(newsData);
				else {
					//results.add(newsData);
					System.out.println("hi al");
				}
				// Log.w("populateData", news.get(i).getString("headline"));
			}
			
			//after going through all the friends, set listEndFlag as true
			//for setOnLastItemVisibleListener() to catch
			listEndFlag = true;

		} catch (ParseException e) {
			Log.d("newsfeed", "Error: " + e.getMessage());
		}
		return results;
	}

	public void refresh() {
		listEndFlag = false;
		user_count = 0;
		pict_count = 0;
		Toast.makeText(getActivity(), "DAVID", Toast.LENGTH_LONG)
		.show();
		ArrayList<NewsItem> image_details = getListData();
		cLAdapter.resetResults(image_details);
	}
	
	private class RenewDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			refresh();
			mPullRefreshListView.onRefreshComplete();
			
			super.onPostExecute(result);
		}
	}
	
	private class GetMoreDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
//			try {
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			getMoreData();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			Toast.makeText(getActivity(), "Loading more...", 2)
			.show();
			super.onPostExecute(result);
		}
	}

}
