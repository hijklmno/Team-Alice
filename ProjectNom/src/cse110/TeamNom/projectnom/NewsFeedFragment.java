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
import com.parse.ParseObject;
import com.parse.ParseQuery;

import cse110.TeamNom.projectnom.newsfeedadapter.CustomListAdapter;
import cse110.TeamNom.projectnom.newsfeedadapter.NewsItem;

public class NewsFeedFragment extends Fragment implements OnClickListener {

	// Initialization
	private static int MAXROWS = 4;
	private static int OFFSETCOUNT = 0;
	private static boolean INITIALLOAD = true;
	
	private Button refreshAlice;
	private Button loadMore;
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
//		refreshAlice = (Button) rootView.findViewById(R.id.refresh);
//		loadMore = (Button) rootView.findViewById(R.id.loadMoar);

		switchButton
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							// friends
						}
						else {
							// popular
						}
					}
				});

		// Log.w("UD: ", "test");
		// final Button NOM = (Button)
		// rootView.findViewWithTag(newsItem.getReporterName());
		// if (NOM == null) {
		// Log.d("NULLLELD", "FSDFSDF");
		// }
		// sadf
		// final Button Mmm = (Button) rootView.findViewById(R.id.Mmm);
		// Mmm.setText("asdf");
		// NOM.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v)
		// {
		// Log.d("NewsFeed", "WorkingNOM");
		// }
		// });
		// Mmm.setOnClickListener(new OnClckListener() {
		//
		// @Override
		// public void onClick(View v)
		// {
		// Log.d("NewsFeed", "WorkingMmm");
		// }
		// });
		// // Log.w("UD: ", "after");
		// View newView = inflater.inflate(R.layout.newsfeed_list_row_layout,
		// container, false);
		// buttonNOM = (Button) newView.findViewById(R.id.NOM);

		if (INITIALLOAD) {
			getNewsFeedData(rootView);
		}
		
//		refreshAlice.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				refresh();
//			}
//		});
//
//		loadMore.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				getMoreData();
//			}
//		});

		OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "DAVID", Toast.LENGTH_LONG)
				.show();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
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
		PullToRefreshListView lv1 = (PullToRefreshListView) getView().findViewById(R.id.custom_list);
		mPullRefreshListView.setAdapter(cLAdapter);
	}
	
	private void getMoreData() {
		OFFSETCOUNT += MAXROWS;
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
				new GetDataTask().execute();
			}
		});
		
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				if (cLAdapter.getCount() == MAXROWS + OFFSETCOUNT) {
					Toast.makeText(getActivity(), "Loading more...", 2)
					.show();
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
		// Log.w("asdf", "asd");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("watson");
		query.setLimit(MAXROWS);
		query.setSkip(OFFSETCOUNT);
		query.orderByAscending("updatedAt");
		List<ParseObject> news;
		try {
			news = query.find();
			for (int i = 0; i < news.size(); i++) {
				NewsItem newsData = new NewsItem();
				newsData.setHeadline(news.get(i).getString("headline"));
				newsData.setReporterName(news.get(i).getString("reporter"));
				newsData.setDate(news.get(i).getString("date"));
				newsData.setUrl(news.get(i).getString("url"));

				/*
				 * 
				 * if(
				 * 
				 * Boolean.parseBoolean(news.get(i).getString("report_image")
				 * 
				 * )==true)
				 */

				if (news.get(i).getBoolean("report_image")) {
					System.out.println("hi ray");
				}
				// results.add(newsData);
				else {
					results.add(newsData);
					System.out.println("hi al");
				}
				// Log.w("populateData", news.get(i).getString("headline"));
			}

		} catch (ParseException e) {
			Log.d("newsfeed", "Error: " + e.getMessage());
		}
		return results;
	}

	public void onClick(View v) {
		ParseObject testObject = new ParseObject("watson");
		switch (v.getId()) {
		case R.id.NOM:
			testObject.put("NOM", 1);
			testObject.saveInBackground();
			break;

		case R.id.Mmm:
			testObject.put("Mmm", 1);
			testObject.saveInBackground();
			break;

		case R.id.Report:
			testObject.put("report_image", true);
			testObject.saveInBackground();
			break;
		}
	}

	public void refresh() {
		Toast.makeText(getActivity(), "DAVID", Toast.LENGTH_LONG)
		.show();
		OFFSETCOUNT = 0;
		ArrayList<NewsItem> image_details = getListData();
		cLAdapter.resetResults(image_details);
		
//		Fragment newsFeed = new NewsFeedFragment();
//		android.support.v4.app.FragmentManager fm = getActivity()
//		.getSupportFragmentManager();
//		fm.beginTransaction().add(R.id.newsfeedFrag, newsFeed).commit();
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			Toast.makeText(getActivity(), "DAVID", Toast.LENGTH_LONG)
			.show();
			OFFSETCOUNT = 0;
			ArrayList<NewsItem> image_details = getListData();
			cLAdapter.resetResults(image_details);
			mPullRefreshListView.onRefreshComplete();
			
			super.onPostExecute(result);
		}
	}
}
