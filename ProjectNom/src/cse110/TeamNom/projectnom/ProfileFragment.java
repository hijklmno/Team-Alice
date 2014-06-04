package cse110.TeamNom.projectnom;

import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;

public class ProfileFragment extends Fragment {
	// check if first load
	private static boolean INITIALLOAD = true;

	private Button buttonLogout;
	private static Bitmap profileBitmap;
	private TextView profileUser;

	private ProfilePictureView profilePictureView;

	private Switch switchBut;
	private GridView gridV;
	private GridView bookmarks;
	public Drawable[] d;
	public Drawable[] b;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		profileUser = (TextView) rootView.findViewById(R.id.profileUser);
		profileUser.setText( AppFacebookAccess.getFacebookName());
		
		// start configuration for the logout button
		buttonLogout = (Button) rootView.findViewById(R.id.buttonLogout);
		buttonLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickLogout();
			}
		});

		switchBut = (Switch) rootView.findViewById(R.id.profileToggle);
		gridV = (GridView) rootView.findViewById(R.id.grid_view);
		bookmarks = (GridView) rootView.findViewById(R.id.grid_view2);
		
		
		if (INITIALLOAD) {
			onClickLoadMyPictures();
			onClickLoadM();
		}
		gridV.setAdapter(new ProfileImageAdapter(getActivity(), d));
		bookmarks.setAdapter(new ProfileImageAdapter(getActivity(), b));
		

		bookmarks.setVisibility(View.INVISIBLE);
		switchBut
		.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// pic
					gridV.setVisibility(View.VISIBLE);
					bookmarks.setVisibility(View.INVISIBLE);
				}
				else {
					gridV.setVisibility(View.INVISIBLE);
					bookmarks.setVisibility(View.VISIBLE);
					// bookmark
				}
			}
		});

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
		getFacebookProfilePicture();
		ImageView profPic = (ImageView) getView().findViewById(
				R.id.imageViewProfile);
		profPic.setImageBitmap(profileBitmap);
	}

	private void getFacebookProfilePicture() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println(AppFacebookAccess.getFacebookId());
					URL imageURL = new URL("https://graph.facebook.com/"
							+ AppFacebookAccess.getFacebookId() + "/picture?type=large");
					profileBitmap = BitmapFactory.decodeStream(imageURL
							.openConnection().getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// get list of pictures from parse
	@SuppressLint("NewApi")
	private void onClickLoadMyPictures() {
		
		String[] pictureIDs = AppParseAccess.getMyPictureIds(AppFacebookAccess.getFacebookId());
		
		if (pictureIDs == null) {
			return;
		}
		
		d = new Drawable[pictureIDs.length];
		
		for (int i = 0; i < pictureIDs.length; i++) {
			PictureDBObject object = AppParseAccess.getSpecificPicture(pictureIDs[i]);
			if (object != null) {
				byte[] data = object.getPicture();
	
				final Button photoGalleryButton = new Button(getActivity());
				Drawable image = null;
				d[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
				photoGalleryButton.setBackground(image);
				photoGalleryButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						System.out.println("df");
					}
				});
	
				System.out.println(d.length);
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
		
		b = new Drawable[bookmarkIDs.length];
		
		for (int i = 0; i < bookmarkIDs.length; i++) {
			PictureDBObject object = AppParseAccess.getSpecificPicture(bookmarkIDs[i]);
			if (object != null) {
				byte[] data = object.getPicture();
	
				final Button photoGalleryButton = new Button(getActivity());
				Drawable image = null;
				b[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
				photoGalleryButton.setBackground(image);
				photoGalleryButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						System.out.println("df");
					}
				});
	
				//mOuterLayout.addView(photoGalleryButton);
			}
		}
	}
}
