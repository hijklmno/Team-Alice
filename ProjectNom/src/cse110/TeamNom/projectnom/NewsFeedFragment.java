package cse110.TeamNom.projectnom;


import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import cse110.TeamNom.projectnom.newsfeedadapter.CustomListAdapter;
import cse110.TeamNom.projectnom.newsfeedadapter.NewsItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;

public class NewsFeedFragment extends Fragment implements OnClickListener{
	private static final int MAXROWS = 2;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
//      Log.w("UD: ", "test");
//        final Button NOM = (Button) rootView.findViewById(R.id.NOM);    
//	    final Button Mmm = (Button) rootView.findViewById(R.id.Mmm); 
//	    NOM.setOnClickListener(this);
//	    Mmm.setOnClickListener(this);
////	    Log.w("UD: ", "after");
//	     
//	     
//	     
	    ArrayList<NewsItem> image_details = getListData();
		final ListView lv1 = (ListView) rootView.findViewById(R.id.custom_list);
		lv1.setAdapter(new CustomListAdapter(getActivity(), image_details));
		lv1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Object o = lv1.getItemAtPosition(position);
				NewsItem newsData = (NewsItem) o;
				Toast.makeText(getActivity(), "Selected :" + " " + newsData,
						Toast.LENGTH_LONG).show();
			}

		});
		
		
		
        return rootView;
    }

	
	
	private ArrayList<NewsItem> getListData() {
		ArrayList<NewsItem> results = new ArrayList<NewsItem>();
//		Log.w("asdf", "asd");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("watson");
		query.setLimit(MAXROWS);
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
				results.add(newsData);
//				Log.w("populateData", news.get(i).getString("headline"));
				
			}
			
		} catch (ParseException e) {
			Log.d("newsfeed", "Error: " + e.getMessage());
		}

		return results;
	}
		
		public void onClick(View v) {
			ParseObject testObject = new ParseObject("TestObject");
			switch (v.getId()){
			case R.id.NOM:
			        testObject.put("NOM", 1);
			        testObject.saveInBackground();
			    break;
			case R.id.Mmm:
			        testObject.put("Mmm", 1);
			        testObject.saveInBackground(); 
				break;
			}
		}

 
}



