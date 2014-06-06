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

/**
 * fragment for user profile
 */
public class ProfileFragment extends Fragment {
	// check if first load
	private static boolean INITIALLOAD = true;
	
	// variables for fragment
	private Button buttonLogout;
	private TextView profileUser;
	private ProfilePictureView profilePictureView;
	private Switch switchBut;
	private GridView gridV;
	private GridView bookmarks;
	private Button refresh;
	
	// variables for grid images
	public Drawable[] pics;
	public Drawable[] book;
	
	private static Bitmap profileBitmap;
	
	/**
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		
		profileUser = (TextView) rootView.findViewById(R.id.profileUser);
		profileUser.setText( AppFacebookAccess.getFacebookName());
		
		// start configuration for the logout button
		buttonLogout = (Button) rootView.findViewById(R.id.buttonLogout);
		switchBut = (Switch) rootView.findViewById(R.id.profileToggle);
		gridV = (GridView) rootView.findViewById(R.id.grid_view);
		bookmarks = (GridView) rootView.findViewById(R.id.grid_view2);
		refresh = (Button) rootView.findViewById(R.id.profileRefresh);
		
		gridV.setAdapter(new ProfileImageAdapter(getActivity(), pics));
		bookmarks.setAdapter(new ProfileImageAdapter(getActivity(), book));
		
		
		if (INITIALLOAD) {
			onClickLoadMyPictures();
			onClickLoadMyBookmarks();
		}
		
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
		buttonLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickLogout();
			}
		});
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				gridV.setAdapter(new ProfileImageAdapter(getActivity(), pics));
				bookmarks.setAdapter(new ProfileImageAdapter(getActivity(), book));
				
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

	/**
	 * 
	 */
	private void onClickFacebookPicUpdate() {
		getFacebookProfilePicture();
		ImageView profPic = (ImageView) getView().findViewById(
				R.id.imageViewProfile);
		profPic.setImageBitmap(profileBitmap);
	}

	/**
	 * 
	 */
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

	/**
	 * 
	 */
	@SuppressLint("NewApi")
	private void onClickLoadMyPictures() {
		
		String[] pictureIDs = AppParseAccess.getMyPictureIds(AppFacebookAccess.getFacebookId());
		
		if (pictureIDs == null) {
			return;
		}
		
		pics = new Drawable[pictureIDs.length];
		
		for (int i = 0; i < pictureIDs.length; i++) {
			PictureDBObject object = AppParseAccess.getSpecificPicture(pictureIDs[i]);
			if (object != null) {
				byte[] data = object.getPicture();
				
				pics[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
			}
		}
	}
	
	/**
	 * 
	 */
	@SuppressLint("NewApi")
	private void onClickLoadMyBookmarks() {
		// bookmarks gallery
		String[] bookmarkIDs = AppParseAccess.getMyBookmarkIds(AppFacebookAccess.getFacebookId());
		
		if (bookmarkIDs == null) {
			return;
		}
		
		book = new Drawable[bookmarkIDs.length];
		
		for (int i = 0; i < bookmarkIDs.length; i++) {
			PictureDBObject object = AppParseAccess.getSpecificPicture(bookmarkIDs[i]);
			if (object != null) {
				byte[] data = object.getPicture();
	
				book[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));

			}
		}
	}
}
