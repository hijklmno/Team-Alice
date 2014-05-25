package cse110.TeamNom.projectnom.newsfeedadapter;

import java.util.ArrayList;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import cse110.TeamNom.projectnom.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

//import com.example.customizedlist.R;

public class CustomListAdapter extends BaseAdapter {

private ArrayList<NewsItem> listData;

private LayoutInflater layoutInflater;

public CustomListAdapter(Context context, ArrayList<NewsItem> listData) {
this.listData = listData;
layoutInflater = LayoutInflater.from(context);
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
convertView = layoutInflater.inflate(R.layout.newsfeed_list_row_layout, null);
holder = new ViewHolder();
//holder.headlineView = (TextView) convertView.findViewById(R.id.title);
holder.reporterNameView = (TextView) convertView.findViewById(R.id.reporter);
holder.reportedDateView = (TextView) convertView.findViewById(R.id.date);
holder.reportedCaption = (TextView) convertView.findViewById(R.id.captionNews);
holder.imageView = (ImageView) convertView.findViewById(R.id.thumbImage);
holder.mmm = (Button) convertView.findViewById(R.id.Mmm);
holder.nom = (Button) convertView.findViewById(R.id.NOM);
holder.report = (Button) convertView.findViewById(R.id.reportpls);
convertView.setTag(holder);
} else {
holder = (ViewHolder) convertView.getTag();
}

NewsItem newsItem = (NewsItem) listData.get(position);

//holder.headlineView.setText(newsItem.getHeadline());
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
holder.report.setTag(newsItem.getReporterName());
//holder.report.setOnClickListener();

// holder.imageView.setImageBitmap(uncompressImage(image));

//	 if (holder.imageView != null) {
//	 new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
//	 }

		return convertView;
		}

	private Bitmap uncompressImage(byte array[]) {
	System.out.println("heeloeeellelelele");
	Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
	return bmp;
	}
	
	static class ViewHolder {
		TextView headlineView;
		TextView reporterNameView;
		TextView reportedDateView;
		TextView reportedCaption;
		ImageView imageView;
		Button nom;
		Button mmm;
		Button report;
	}
}
