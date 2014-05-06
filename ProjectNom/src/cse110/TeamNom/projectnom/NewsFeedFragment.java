package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import cse110.TeamNom.projectnom.newsfeedadapter.CustomListAdapter;
import cse110.TeamNom.projectnom.newsfeedadapter.NewsItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;



public class NewsFeedFragment extends Fragment implements OnClickListener{
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
		query.setLimit(2);
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
				Log.w("populateData", news.get(i).getString("headline"));
			}
			
		} catch (ParseException e) {
			Log.d("newsfeed", "Error: " + e.getMessage());
		}
		
		
			
//		query.findInBackground(new FindCallback<ParseObject>() {
//		    public void done(List<ParseObject> news, ParseException e) {
//		        if (e == null) {
////		            Log.d("score", "Retrieved " + news.size() + " scores");
////		        	results = populateData(news);
//
//					for (int i = 0; i < news.size(); i++) {
//						NewsItem newsData = new NewsItem();
//						newsData.setHeadline(news.get(i).getString("headline"));
//						newsData.setReporterName(news.get(i).getString("reporter"));
//						newsData.setDate(news.get(i).getString("date"));
//						newsData.setUrl(news.get(i).getString("url"));
//						results.add(newsData);
//						Log.w("populateData", news.get(i).getString("headline"));
//					}
//					
//		        } else {
//		            Log.d("newsfeed", "Error: " + e.getMessage());
//		        }
//		    }
//		});

		
		
//		NewsItem newsData = new NewsItem();
//		newsData.setHeadline("Dance of Democracy");
//		newsData.setReporterName("Pankaj Gupta");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh5.ggpht.com/_hepKlJWopDg/TB-_WXikaYI/AAAAAAAAElI/715k4NvBM4w/s144-c/IMG_0075.JPG");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("Major Naxal attacks in the past");
//		newsData.setReporterName("Pankaj Gupta");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh4.ggpht.com/_4f1e_yo-zMQ/TCe5h9yN-TI/AAAAAAAAXqs/8X2fIjtKjmw/s144-c/IMG_1786.JPG");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("BCCI suspends Gurunath pending inquiry ");
//		newsData.setReporterName("Rajiv Chandan");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh3.ggpht.com/_GEnSvSHk4iE/TDSfmyCfn0I/AAAAAAAAF8Y/cqmhEoxbwys/s144-c/_MG_3675.jpg");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("Life convict can`t claim freedom after 14 yrs: SC");
//		newsData.setReporterName("Pankaj Gupta");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh6.ggpht.com/_ZN5zQnkI67I/TCFFZaJHDnI/AAAAAAAABVk/YoUbDQHJRdo/s144-c/P9250508.JPG");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("Indian Army refuses to share info on soldiers mutilated at LoC");
//		newsData.setReporterName("Pankaj Gupta");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh4.ggpht.com/_XjNwVI0kmW8/TCOwNtzGheI/AAAAAAAAC84/SxFJhG7Scgo/s144-c/0014.jpg");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("French soldier stabbed; link to Woolwich attack being probed");
//		newsData.setReporterName("Sudeep Nanda");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh6.ggpht.com/_Nsxc889y6hY/TBp7jfx-cgI/AAAAAAAAHAg/Rr7jX44r2Gc/s144-c/IMGP9775a.jpg");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("Life convict can`t claim freedom after 14 yrs: SC");
//		newsData.setReporterName("Pankaj Gupta");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh6.ggpht.com/_ZN5zQnkI67I/TCFFZaJHDnI/AAAAAAAABVk/YoUbDQHJRdo/s144-c/P9250508.JPG");
//		results.add(newsData);
//
//		newsData = new NewsItem();
//		newsData.setHeadline("Dance of Democracy");
//		newsData.setReporterName("Pankaj Gupta");
//		newsData.setDate("May 26, 2013, 13:35");
//		newsData.setUrl("http://lh5.ggpht.com/_hepKlJWopDg/TB-_WXikaYI/AAAAAAAAElI/715k4NvBM4w/s144-c/IMG_0075.JPG");
//		results.add(newsData);

		return results;
	}
	
//		public ArrayList<NewsItem> populateData(List<ParseObject> list) {
//			ArrayList<NewsItem> results = new ArrayList<NewsItem>();
//			
//			
//			return results;
//		}
		
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



