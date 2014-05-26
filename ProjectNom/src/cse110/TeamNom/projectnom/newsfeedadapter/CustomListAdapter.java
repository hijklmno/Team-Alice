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

import com.parse.ParseObject;

import cse110.TeamNom.projectnom.R;

public class CustomListAdapter extends BaseAdapter {

	private ArrayList<NewsItem> listData;
	private LayoutInflater layoutInflater;

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

		NewsItem newsItem = (NewsItem) listData.get(position);

		// holder.headlineView.setText(newsItem.getHeadline());
		holder.reporterNameView.setText("By, " + newsItem.getReporterName());
		holder.reportedDateView.setText(newsItem.getDate());
		holder.mmm.setTag(newsItem.getReporterName());
		holder.mmm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseObject testObject = new ParseObject("watson");
				testObject.put("MmmString", holder.mmm.getTag());
				testObject.saveInBackground();
			}
		});
		holder.nom.setTag(newsItem.getReporterName());
		holder.nom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseObject testObject = new ParseObject("watson");
				testObject.put("NomString", holder.nom.getTag());
				testObject.saveInBackground();
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

		// holder.imageView.setImageBitmap(uncompressImage(image));

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
