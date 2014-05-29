package cse110.TeamNom.projectnom.newsfeedadapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import cse110.TeamNom.projectnom.R;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<NewsItem> listData;
	private LayoutInflater layoutInflater;
	private boolean flag_bookmark = false;
	private boolean flag_like = false;

	public CustomListAdapter(Context context, ArrayList<NewsItem> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	//Updates news feed results with new results
	public void updateResults(ArrayList<NewsItem> results) {
		this.listData.addAll(results);
		notifyDataSetChanged();
	}

	public void resetResults(ArrayList<NewsItem> results) {
		this.listData = results;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.newsfeed_list_row_layout, null);
			holder = new ViewHolder();
			// holder.headlineView = (TextView)
			// convertView.findViewById(R.id.title);
			// holder.flaggedView = (ImageView)
			// convertView.findViewById(R.id.errorOverlay);
			holder.reporterNameView = (TextView) convertView
					.findViewById(R.id.reporter);
			holder.reportedDateView = (TextView) convertView
					.findViewById(R.id.date);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.thumbImage);
			// holder.errorOverlay = (ImageView)
			// convertView.findViewById(R.id.errorOverlay);

			holder.newsCaption = (TextView) convertView
					.findViewById(R.id.captionNews);

			holder.mmm = (Button) convertView.findViewById(R.id.Mmm);
			holder.nom = (Button) convertView.findViewById(R.id.NOM);
			holder.Report = (Button) convertView.findViewById(R.id.Report);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final NewsItem newsItem = (NewsItem) listData.get(position);

		// holder.headlineView.setText(newsItem.getHeadline());
		holder.reporterNameView.setText("By, " + newsItem.getName());
		holder.reportedDateView.setText(newsItem.getDate());
		holder.mmm.setTag(newsItem.getName());
		holder.mmm.setOnClickListener(new View.OnClickListener() {
			/*Transfer this to food table*/
			@Override
			public void onClick(View v) {
				System.out.println("newsItemID: " + newsItem.getID());
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
				query.getInBackground(newsItem.getID(), new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject object, ParseException e) {
					if(newsItem.book_mark == false)
					{
						newsItem.book_mark = true;
					// TODO Auto-generated method stub
				    if (e == null) {
					      // object will be your game score
				    	int number = object.getInt("Bookmark") + 2;
				    	object.put("Bookmark", number);
				    	object.saveInBackground();
					    } else {
					    	System.err.println("ERROR: " + e);
					    }
					
				}
				}
				});
				/////////////////////////////////////////////////
				/*ParseObject testObject = new ParseObject("watson");
				testObject.put("MmmString", holder.mmm.getTag());
				testObject.saveInBackground();*/
			}
		});
		holder.nom.setTag(newsItem.getName());
		holder.nom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//System.out.println("newsItemID: " + newsItem.getID());
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
				query.getInBackground(newsItem.getID(), new GetCallback<ParseObject>() {
				@Override
				public void done(ParseObject object, ParseException e) {
					if(newsItem.like == false)
					{
						newsItem.like = true;
					// TODO Auto-generated method stub
				    if (e == null) {
					      // object will be your game score
				    	int number = object.getInt("Like");
				    	object.put("Like", ++number);
				    	object.saveInBackground();
					    } else {
					    	System.err.println("ERROR: " + e);
					    }
					
				}
				}
				});
				/////////////////////////////////////////////////
				/*ParseObject testObject = new ParseObject("watson");
				testObject.put("NomString", holder.nom.getTag());
				testObject.saveInBackground();*/
			}
		});

		holder.Report.setTag(newsItem.getReport());
		holder.Report.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseObject testObject = new ParseObject("watson");
				testObject.put("report_image", true);
				testObject.saveInBackground();

//				updateResults(listData);
				//change that specific pic's report_image to true
				CharSequence text = "File Reported";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(v.getContext(), text, duration);
				toast.show();

			}
		});

		 holder.imageView.setImageBitmap(uncompressImage(newsItem.getFile()));

		// if (holder.imageView != null) {
		// new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
		// }

		return convertView;
	}

	private Bitmap uncompressImage(byte array[]) {
		System.out.println("heeloeeellelelele");
		Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
		return bmp;
	}

	static class ViewHolder {

		public TextView flaggedView;
		TextView newsCaption;
		TextView headlineView;
		TextView reporterNameView;
		TextView reportedDateView;
		ImageView imageView;
		ImageView errorOverlay;
		Button nom;
		Button mmm;
		Button Report;
	}

}
