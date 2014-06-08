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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Session;

/**
 * Fragment class for the user profile.
 */
public class ProfileFragment extends Fragment {
	// Variables for fragment
	private Button buttonLogout;
	private TextView profileUser;
	private Switch switchButton;
	private GridView gridV;
	private GridView bookmarks;
	private Button refresh;
	
	// Variables for grid images
	public Drawable[] pics;
	public Drawable[] book;
	
	private static Bitmap profileBitmap;
	
	/**
	 * Setup on how the profile tab will look like.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		
		profileUser = (TextView) rootView.findViewById(R.id.profileUser);
		profileUser.setText( AppFacebookAccess.getFacebookName());
		
		// Start configuration for the logout button
		buttonLogout = (Button) rootView.findViewById(R.id.buttonLogout);
		switchButton = (Switch) rootView.findViewById(R.id.profileToggle);
		gridV = (GridView) rootView.findViewById(R.id.grid_view);
		bookmarks = (GridView) rootView.findViewById(R.id.grid_view2);
		refresh = (Button) rootView.findViewById(R.id.profileRefresh);
		// Loads the picture taken from the user
		loadMyPictures();
		// Loads the bookmark of the user.
		loadMyBookmarks();
		
		gridV.setAdapter(new ProfileImageAdapter(getActivity(), pics));
		bookmarks.setAdapter(new ProfileImageAdapter(getActivity(), book));
		
		bookmarks.setVisibility(View.INVISIBLE);
		
		// Change visibility of galleries depending on state of switchbutton
		switchButton
		.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					// Sets visibility of pictures
					gridV.setVisibility(View.VISIBLE);
					bookmarks.setVisibility(View.INVISIBLE);
				}
				else {
					// Sets visibility of bookmarks
					gridV.setVisibility(View.INVISIBLE);
					bookmarks.setVisibility(View.VISIBLE);
				}
			}
		});
		
		/*
		 * Logout button in the profile.
		 */
		buttonLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onClickLogout();
			}
		});
		
		/*
		 * Refresh the profile pictures of the user
		 */
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
			Toast.makeText(getActivity(), "Refreshing...", 2).show();
			loadMyPictures();
			loadMyBookmarks();
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
	}

	public void onStop() {
		super.onStop();
	}
	// called when pressing the logout button
	private void onClickLogout() {
//		Session session = Session.getActiveSession();
//		session.closeAndClearTokenInformation(); // end session and go
//		// create splash page
		Intent intent = new Intent(getActivity(), SplashMain.class);
		intent.putExtra("logoutCall", "logout");
		startActivity(intent);
		getActivity().finish();
	}

	/**
	 * Update facebook profile picture
	 */
	private void onClickFacebookPicUpdate() {
		getFacebookProfilePicture();
		ImageView profPic = (ImageView) getView().findViewById(
				R.id.imageViewProfile);
		profPic.setImageBitmap(profileBitmap);
	}

	/**
	 * Gets the most recent Facebook profile picture of the user.
	 */
	private void getFacebookProfilePicture() {
		// Start a new thread for network calls
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL imageURL = new URL("https://graph.facebook.com/"
							+ AppFacebookAccess.getFacebookId() + "/picture?type=large");
					profileBitmap = BitmapFactory.decodeStream(imageURL
							.openConnection().getInputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Start the thread
		thread.start();
		try {
			// Join the thread so that the main thread does not run ahead
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that loads the user's taken photos.
	 */
	@SuppressLint("NewApi")
	private void loadMyPictures() {
		
		// Load picture ids
		String[] pictureIDs = AppParseAccess.getMyPictureIds(AppFacebookAccess.getFacebookId());
		
		// Check for null
		if (pictureIDs == null) {
			return;
		}
		
		// Create array of Drawables
		pics = new Drawable[pictureIDs.length];
		
		// Loop through each picture id
		for (int i = 0; i < pictureIDs.length; i++) {
			// Create a PictureDBObject from each picture id
			PictureDBObject object = AppParseAccess.getSpecificPicture(pictureIDs[i]);
			
			if (object != null) {
				// Get the byte array from each object
				byte[] data = object.getPicture();
				
				// Create a BitmapDrawable from each bye array
				pics[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
			}
		}
	}
	
	/**
	 * Load the bookmark photo of other users posts.
	 */
	@SuppressLint("NewApi")
	private void loadMyBookmarks() {
		// Get the bookmark ids from user
		String[] bookmarkIDs = AppParseAccess.getMyBookmarkIds(AppFacebookAccess.getFacebookId());
		
		// Check for null
		if (bookmarkIDs == null) {
			return;
		}
		
		// Create array of Drawables
		book = new Drawable[bookmarkIDs.length];
		
		// Loop through each bookmark id
		for (int i = 0; i < bookmarkIDs.length; i++) {
			// Create a PictureDBObject for each bookmark id
			PictureDBObject object = AppParseAccess.getSpecificPicture(bookmarkIDs[i]);
			
			if (object != null) {
				// Get the byte array from each PictureDBObject
				byte[] data = object.getPicture();
	
				// Create a new BitmapDrawable for each object and store in array
				book[i] = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(data, 0, data.length));
			}
		}
	}
}
