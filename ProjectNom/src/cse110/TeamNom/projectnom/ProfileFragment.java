package cse110.TeamNom.projectnom;

import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
	private Button buttonLogout;
	private Button buttonFacebook;
	private TextView textbox;
	private static Bitmap profileBitmap;
	
	private ProfilePictureView profilePictureView;
	
	//Gallery variables
	private HorizontalScrollView horizontalScrollview;
	private LinearLayout horizontalOuterLayout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
    	//facebook debug textbox
        textbox = (TextView)rootView.findViewById(R.id.facebookDebugBox);
    	
        //start configuration for the logout button
        buttonLogout = (Button)rootView.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            	onClickLogout();
            }
        });
        
        buttonFacebook = (Button)rootView.findViewById(R.id.facebookTest);
        buttonFacebook.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) { 
        		onClickFacebookDebug();
        		onClickFacebookPicUpdate();
         		onClickLoadMyPictures();
        	}
        });
        
        horizontalScrollview = (HorizontalScrollView) rootView.findViewById(R.id.horizontal_scrollview);
        horizontalOuterLayout = (LinearLayout) rootView.findViewById(R.id.horizontal_outer_layout);
        
        horizontalScrollview.setHorizontalScrollBarEnabled(false);
//        onClickLoadMyPictures();
        return rootView;
    }
	
	public void onStart() {
		super.onStart();
//		onClickLoadMyPictures();
	}
	
	public void onResume() {
		super.onResume();
//		onClickLoadMyPictures();
	}
	
	//called when pressing the logout button
	private void onClickLogout() {
        Session session = Session.getActiveSession();
        session.closeAndClearTokenInformation(); //end session and go 
        //create splash page
        Intent intent = new Intent(getActivity(), SplashMain.class);
        intent.putExtra("logoutCall", "logout");
        startActivity(intent);
        getActivity().finish();
    }
	
	private void onClickFacebookDebug() {
		Session session = Session.getActiveSession();
	
		/* make the API call */
		new Request(
		    session,
		    "/me",
		    null,
		    HttpMethod.GET,
		    new Request.Callback() {
		        public void onCompleted(Response response) {
		            /* handle the result */
		        	String incomingText = response.getRawResponse();
		        	textbox.setText(incomingText);
		        	
		        	try {
						JSONObject json = new JSONObject(incomingText);
						//id working
						final String id = (String) json.get("id");
						final String name = (String) json.get("name");
						
						//testing to see if is a user already
						ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
						query.whereEqualTo("facebook_id", id);
						query.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> objects, ParseException e) {
								Log.d("FacebookFriendQuery", "done");
								textbox.setText(objects.toString());
								if (objects.isEmpty()) {
									ParseObject testObject = new ParseObject("FacebookAccounts");
									testObject.put("facebook_id", id);
									testObject.put("Name", name);
									testObject.saveInBackground();
									Log.d("FacebookFriendQuery", "new user");
								}
							}
						});
						
						
						textbox.setText(name);
					} catch (JSONException e) {
						e.printStackTrace();
					}
		        	
		        }
		    }
		).executeAsync();
	}
	
	private void onClickFacebookPicUpdate() {
		getFacebookProfilePicture();
		ImageView profPic = (ImageView)getView().findViewById(R.id.imageViewProfile);
		profPic.setImageBitmap(profileBitmap);
	}
	
	private void getFacebookProfilePicture(){
		Thread thread = new Thread(new Runnable(){
		    @Override
		    public void run() {
		        try {
		        	System.out.println(MainActivity.FACEBOOK_ID);
		        	URL imageURL = new URL("https://graph.facebook.com/" + MainActivity.FACEBOOK_ID + "/picture?type=large");
					profileBitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		});

		thread.start(); 
	}
	
	//get list of pictures from parse
	private void onClickLoadMyPictures() {
//		// SLEEP 2 SECONDS HERE ...
//	    Handler handler = new Handler(); 
//	    handler.postDelayed(new Runnable() { 
//			public void run() {
//
//			}
//		}, 5000);

		ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");

		query.whereEqualTo("facebook_id", MainActivity.FACEBOOK_ID);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					String list = (String) objects.get(0).get("pictures");
					String[] pictureIDs = list.split(",");

					// debug
					for (int i = 0; i < pictureIDs.length; i++) {
						System.out.println(pictureIDs[i]);
					}

					for (int i = 0; i < pictureIDs.length; i++) {
						ParseQuery<ParseObject> query = ParseQuery
								.getQuery("Food_Table_DB");
						try {
							// search by id, return list of stuff
							query.get(pictureIDs[i]);
							query.findInBackground(new FindCallback<ParseObject>() {
								@SuppressLint("NewApi") @Override
								public void done(List<ParseObject> objects,
										ParseException e) {
									for (int j = 0; j < objects.size(); j++) {
										ParseObject foodObject = objects.get(j);
										
										System.out.println(foodObject.toString());
										
										ParseFile fileObject = (ParseFile)foodObject.get("Food_photo");
										fileObject.getDataInBackground(new GetDataCallback() {
											@Override
											public void done(byte[] data, ParseException e) {
												if (e == null) {
													Log.d("ProfileGallery", "Got image");
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
												else {
													e.printStackTrace();
												}
											}
										});
										
//										byte[] photoData = objects.get(j).getBytes("Food_photo");
										
//										System.out.println(objects.get(j).get);
//										Drawable image = null;
//										image = new BitmapDrawable(BitmapFactory.decodeByteArray(photoData, 0, photoData.length));
										
//										image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(photoData, 0, photoData.length));
//										image = new BitmapDrawable()
									}
								}
							});
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}
				}
				else {
					e.printStackTrace();
				}
			}
		});
	}
}



