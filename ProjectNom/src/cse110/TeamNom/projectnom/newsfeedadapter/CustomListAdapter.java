package cse110.TeamNom.projectnom.newsfeedadapter;

import java.util.ArrayList;

import com.ocpsoft.pretty.time.PrettyTime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
		// Create a new holder to contain all the elements to be populated per row
		final ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
			R.layout.newsfeed_list_row_layout, null);
			holder = new ViewHolder();
			holder.captionTextView = (TextView) convertView.findViewById(R.id.NewsFeedCaption);
			holder.subCaptionTextView = (TextView) convertView.findViewById(R.id.NewsFeedsubCaption);
			holder.dateTextView = (TextView) convertView.findViewById(R.id.NewsFeedDate);
			holder.imageView = (ImageView) convertView.findViewById(R.id.NewsFeedThumbImage);
			holder.nameTextView = (TextView) convertView.findViewById(R.id.NewsFeedName);
			holder.bookmark = (Button) convertView.findViewById(R.id.NewsFeedMmm);
			holder.like = (Button) convertView.findViewById(R.id.NewsFeedNOM);
			holder.Report = (Button) convertView.findViewById(R.id.NewsFeedReport);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Get the respective picture object 
		final PictureDBObject pictureObj = (PictureDBObject) listData.get(position);
		
		// Defining the text for the caption
		holder.captionTextView.setText(pictureObj.getRestID());
		
		// Defining the text for the subcaption
		holder.subCaptionTextView.setText(pictureObj.getCaption());
		
		// Defining the date
		PrettyTime ptime = new PrettyTime();
		holder.dateTextView.setText(ptime.format(pictureObj.getUpdatedDate()));
		
		// Determine whether a picture is reported; if not, then load image
		if (pictureObj.isReported()) {
			holder.imageView.setImageResource(R.drawable.ic_action_error);
		} else {
			holder.imageView.setImageBitmap(uncompressImage(pictureObj.getPicture()));
		}
		
		// Defining the name
		holder.nameTextView.setText(pictureObj.getFacebookName());
		
		// Define the Mmm button settings
		holder.bookmark.setTag(pictureObj.getImageID());
		
		// Set the number count and highlight bookmark button if chosen
//		Log.d("UserObjectID: ", AppParseAccess.getUserObjectID());
		if (pictureObj.isBookmarked()) {
			Log.d("isBookmarked: ", "true");
			holder.bookmark.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_favorite_yellow, 0, 0, 0);
			holder.bookmark.refreshDrawableState();
		}
		else {
			Log.d("isBookmarked: ", "false");
			holder.bookmark.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_important, 0, 0, 0);
			holder.bookmark.refreshDrawableState();
		}
		
		// Set text for the bookmark button
		holder.bookmark.setText(Integer.toString(pictureObj.getBookmarkCount()));
		
		// Define the on click listener for the bookmark button
		holder.bookmark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!pictureObj.isBookmarked()) {
					// If not bookmarked, then bookmark image (submitted to parse)
					AppParseAccess.bookmarkImage(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());		
					pictureObj.setBookmarkCount(pictureObj.getBookmarkCount() + 1);
					holder.bookmark.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_favorite_yellow, 0, 0, 0);
					holder.bookmark.setText(Integer.toString(pictureObj.getBookmarkCount()));
					holder.bookmark.refreshDrawableState();
					pictureObj.setBookmark(true);
				} else {
					// If bookmarked, then unbookmark image (submitted to parse)
					AppParseAccess.unbookmarkImage(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());
					pictureObj.setBookmarkCount(pictureObj.getBookmarkCount() - 1);
					holder.bookmark.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_important, 0, 0, 0);
					holder.bookmark.setText(Integer.toString(pictureObj.getBookmarkCount()));
					holder.bookmark.refreshDrawableState();
					pictureObj.setBookmark(false);
				}
			}
		});
		
		
		//Defining the Nom button settings
		holder.like.setTag(pictureObj.getImageID());
		
		// Set the number count and highlight like button if chosen
		if (pictureObj.isLiked()) {
			holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_liked, 0, 0, 0);
			holder.like.refreshDrawableState();
		}
		else {
			holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_like, 0, 0, 0);
			holder.like.refreshDrawableState();
		}
		
		// Set text for the bookmark button
		holder.like.setText(Integer.toString(pictureObj.getLikeCount()));
		
		// Define the on click listener for the like button
		holder.like.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!pictureObj.isLiked()) {
					// If not liked, then like image (submitted to parse)
					AppParseAccess.incrementNomCount(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());
					pictureObj.setLikeCount(pictureObj.getLikeCount() + 1);
					holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_liked, 0, 0, 0);
					holder.like.setText(Integer.toString(pictureObj.getLikeCount()));
					holder.like.refreshDrawableState();
					pictureObj.setLike(true);
				} else {
					// If liked, then unlike image (submitted to parse)
					AppParseAccess.unlikeImage(AppFacebookAccess.getFacebookId(), pictureObj.getImageID());
					pictureObj.setLikeCount(pictureObj.getLikeCount() - 1);
					holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_like, 0, 0, 0);
					holder.like.setText(Integer.toString(pictureObj.getLikeCount()));
					holder.like.refreshDrawableState();
					pictureObj.setLike(false);
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
		Button like;
		Button bookmark;
		Button Report;
	}
}
