package cse110.TeamNom.projectnom;

import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ProfileFragment extends Fragment {
	// check if first load
	private static boolean INITIALLOAD = true;

	private TextView picturesText;
	private TextView bookmarksText;
	private Button buttonLogout;
	private static Bitmap profileBitmap;
	private TextView profileUser;

	private ProfilePictureView profilePictureView;

	// Gallery variables
	private HorizontalScrollView mScrollView;
	private LinearLayout mOuterLayout;
	private HorizontalScrollView horizontalScrollview;
	private LinearLayout horizontalOuterLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		profileUser = (TextView) rootView.findViewById(R.id.profileUser);
		picturesText = (TextView) rootView.findViewById(R.id.picturesText);
		bookmarksText = (TextView) rootView.findViewById(R.id.bookmarksText);
		profileUser.setText( AppFacebookAccess.getFacebookName());
		
		// start configuration for the logout button
		buttonLogout = (Button) rootView.findViewById(R.id.buttonLogout);
		buttonLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickLogout();
			}
		});

		horizontalScrollview = (HorizontalScrollView) rootView
				.findViewById(R.id.horizontal_scrollview);
		horizontalOuterLayout = (LinearLayout) rootView
				.findViewById(R.id.horizontal_outer_layout);

		horizontalScrollview.setHorizontalScrollBarEnabled(false);

		mScrollView = (HorizontalScrollView) rootView
				.findViewById(R.id.horizontal_scrollview2);
		mOuterLayout = (LinearLayout) rootView
				.findViewById(R.id.horizontal_outer_layout2);
		
		if (INITIALLOAD) {
			onClickLoadMyPictures();
			onClickLoadM();
		}

		return rootView;
	}

	public void onStart() {
		super.onStart();
		onClickFacebookPicUpdate();
	}

	public void onResume() {
		super.onResume();
		INITIALLOAD = false;
	}

	public void onStop() {
		super.onStop();
		INITIALLOAD = true;
	}
	// called when pressing the logout button
	private void onClickLogout() {
		Session session = Session.getActiveSession();
		session.closeAndClearTokenInformation(); // end session and go
		// create splash page
		Intent intent = new Intent(getActivity(), SplashMain.class);
		intent.putExtra("logoutCall", "logout");
		startActivity(intent);
	//	getActivity().finish();
	}

	private void onClickFacebookDebug() {
//		Session session = Session.getActiveSession();
//		textbox.setText(AppFacebookAccess.getMyFriends().toString());
		
		String[] arr = AppFacebookAccess.loadMyFriends();
		Log.d("arrlength", Integer.toString(arr.length));
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
		

//		/* make the API call */
//		new Request(session, "/me", null, HttpMethod.GET,
//				new Request.Callback() {
//					public void onCompleted(Response response) {
//						/* handle the result */
//						String incomingText = response.getRawResponse();
//						textbox.setText(incomingText);
//
//						try {
//							JSONObject json = new JSONObject(incomingText);
//							// id working
//							final String id = (String) json.get("id");
//							final String name = (String) json.get("name");
//
//							// testing to see if is a user already
//							ParseQuery<ParseObject> query = ParseQuery
//									.getQuery("FacebookAccounts");
//							query.whereEqualTo("facebook_id", id);
//							query.findInBackground(new FindCallback<ParseObject>() {
//								@Override
//								public void done(List<ParseObject> objects,
//										ParseException e) {
//									Log.d("FacebookFriendQuery", "done");
//									textbox.setText(objects.toString());
//									if (objects.isEmpty()) {
//										ParseObject testObject = new ParseObject(
//												"FacebookAccounts");
//										testObject.put("facebook_id", id);
//										testObject.put("Name", name);
//										testObject.saveInBackground();
//										Log.d("FacebookFriendQuery", "new user");
//									}
//								}
//							});
//
//							textbox.setText(name);
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//					}
//				}).executeAsync();
	}

	private void onClickFacebookPicUpdate() {
		profileBitmap = AppFacebookAccess.getFacebookProfilePicture();
		ImageView profPic = (ImageView) getView().findViewById(
				R.id.imageViewProfile);
		profPic.setImageBitmap(profileBitmap);
	}

	// get list of pictures from parse
	@SuppressLint("NewApi")
	private void onClickLoadMyPictures() {
		
		String[] pictureIDs = AppParseAccess.getMyPictureIds(AppFacebookAccess.getFacebookId());
		
		if (pictureIDs == null) {
			return;
		}
		
		for (int i = 0; i < pictureIDs.length; i++) {
			PictureDBObject object = AppParseAccess.getSpecificPicture(pictureIDs[i]);
			if (object != null) {
				byte[] data = object.getPicture();
	
				final Button photoGalleryButton = new Button(getActivity());
				Drawable image = null;
				image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
				photoGalleryButton.setBackground(image);
				photoGalleryButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						System.out.println("df");
					}
				});
	
				horizontalOuterLayout.addView(photoGalleryButton);
			}
		}
	}
	
	
	@SuppressLint("NewApi")
	private void onClickLoadM() {
		// bookmarks gallery
		String[] bookmarkIDs = AppParseAccess.getMyBookmarkIds(AppFacebookAccess.getFacebookId());
		
		if (bookmarkIDs == null) {
			return;
		}
		
		for (int i = 0; i < bookmarkIDs.length; i++) {
			PictureDBObject object = AppParseAccess.getSpecificPicture(bookmarkIDs[i]);
			if (object != null) {
				byte[] data = object.getPicture();
	
				final Button photoGalleryButton = new Button(getActivity());
				Drawable image = null;
				image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
				photoGalleryButton.setBackground(image);
				photoGalleryButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						System.out.println("df");
					}
				});
	
				mOuterLayout.addView(photoGalleryButton);
			}
		}
	}
}
