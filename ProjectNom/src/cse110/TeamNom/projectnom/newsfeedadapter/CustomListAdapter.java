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

import cse110.TeamNom.projectnom.AppFacebookAccess;
import cse110.TeamNom.projectnom.AppParseAccess;
import cse110.TeamNom.projectnom.PictureDBObject;
import cse110.TeamNom.projectnom.R;

public class CustomListAdapter extends BaseAdapter {
	
	private ArrayList<PictureDBObject> listData;
	private LayoutInflater layoutInflater;
	
	public CustomListAdapter(Context context, ArrayList<PictureDBObject> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	//Updates news feed results with new results
	public void updateResults(ArrayList<PictureDBObject> results) {
		this.listData.addAll(results);
		notifyDataSetChanged();
	}

	public void resetResults(ArrayList<PictureDBObject> results) {
		this.listData = results;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if (listData != null) {
			return listData.size();
		} else {
			return 0;
		}
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
			holder.captionTextView = (TextView) convertView.findViewById(R.id.NewsFeedCaption);
			holder.dateTextView = (TextView) convertView.findViewById(R.id.NewsFeedDate);
			holder.imageView = (ImageView) convertView.findViewById(R.id.NewsFeedThumbImage);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.NewsFeedName);
			holder.mmm = (Button) convertView.findViewById(R.id.NewsFeedMmm);
			holder.nom = (Button) convertView.findViewById(R.id.NewsFeedNOM);
			holder.Report = (Button) convertView.findViewById(R.id.NewsFeedReport);
			
			convertView.setTag(holder);
			
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final PictureDBObject pictureObj = (PictureDBObject) listData.get(position);
		
		holder.captionTextView.setText(pictureObj.getRestID());
		holder.dateTextView.setText(pictureObj.getUpdatedDate().toString());
		if (pictureObj.isReported()) {
			holder.imageView.setImageResource(R.drawable.ic_action_error);
		} else {
			holder.imageView.setImageBitmap(uncompressImage(pictureObj.getPicture()));
		}
		holder.nameTextView.setText(pictureObj.getFacebookName());
		//Define the Mmm button settings
		holder.mmm.setTag(pictureObj.getImageID());
		holder.mmm.setText(Integer.toString(pictureObj.getBookmarkCount()));
		holder.mmm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!AppParseAccess.isBookmarked(AppFacebookAccess.getFacebookId(), pictureObj.getImageID())) {
					AppParseAccess.bookmarkImage(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());		
					pictureObj.setBookmarkCount(pictureObj.getBookmarkCount() + 1);
					holder.mmm.setText(Integer.toString(pictureObj.getBookmarkCount()));
					holder.mmm.refreshDrawableState();
				} else {
					AppParseAccess.unbookmarkImage(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());
					pictureObj.setBookmarkCount(pictureObj.getBookmarkCount() - 1);
					holder.mmm.setText(Integer.toString(pictureObj.getBookmarkCount()));
					holder.mmm.refreshDrawableState();
				}
			}
		});
		
		
		//Define the Nom button settings
		holder.nom.setTag(pictureObj.getImageID());
		holder.nom.setText(Integer.toString(pictureObj.getLikeCount()));
		holder.nom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!AppParseAccess.isLiked(AppFacebookAccess.getFacebookId(), pictureObj.getImageID())) {
					AppParseAccess.incrementNomCount(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());
					pictureObj.setLikeCount(pictureObj.getLikeCount() + 1);
					holder.nom.setText(Integer.toString(pictureObj.getLikeCount()));
					holder.nom.refreshDrawableState();
				} else {
					AppParseAccess.unlikeImage(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());
					pictureObj.setLikeCount(pictureObj.getLikeCount() - 1);
					holder.nom.setText(Integer.toString(pictureObj.getLikeCount()));
					holder.nom.refreshDrawableState();
				}
			}
		});
		
		
		//Define the report button settings
		holder.Report.setTag(pictureObj.getImageID());
		holder.Report.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AppParseAccess.setFlag(pictureObj.getImageID());
				holder.imageView.setImageResource(R.drawable.ic_action_error);
				holder.imageView.refreshDrawableState();
			}
		});
		
		return convertView;
	}

	private Bitmap uncompressImage(byte array[]) {
		Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
		return bmp;
	}

	static class ViewHolder {
		TextView captionTextView;
		TextView subCaptionTextView;
		TextView nameTextView;
		TextView dateTextView;
		ImageView imageView;
		Button nom;
		Button mmm;
		Button Report;
	}
}
