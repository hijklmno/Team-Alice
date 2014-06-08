package cse110.TeamNom.projectnom;

import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

/**
 * AppFacebookAccess is a access class for the purpose of layering access to Facebook in the 
 * NOM application. 
 */
public class AppFacebookAccess {

	private static String FB_ID;					// The Facebook ID of current user
	private static String FB_Name;					// The full name of current user
	private static Session session;					// The current active Facebook session
	private static ArrayList<String> allFriends = new ArrayList<String>();	// ArrayList of friend's FB IDs
	private static Bitmap profileBitmap;
	
	/**
	 * setActiveSession() stores the current active Facebook session.
	 */
	public static void setActiveSession() {
		session = Session.getActiveSession();
	}

	/**
	 * loadMyFriends() loads the current Facebook user's friends that have also given Facebook
	 * permissions to the NOM application.
	 */
	public static ArrayList<String> loadMyFriends() {
		// Create new thread so that the request finishes before main thread continues
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// make the API request call
		new Request(session, "/me/friends", null, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						try {
							String incomingText = response.getRawResponse();
							JSONObject json = new JSONObject(incomingText);
							
							// Get the JSONArray "data" inside json
							JSONArray dataList = json.getJSONArray("data");
							
							// Loop to add each friend's id to allFriends array
							for (int i = 0; i < dataList.length(); i++) {
								String friend_name = ((JSONObject) dataList.get(i)).getString("name");
								String friend_id = ((JSONObject) dataList.get(i)).getString("id");
								System.out.println(friend_name + ":" + friend_id);
								allFriends.add(friend_id);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).executeAndWait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return allFriends;
	}
	
	/**
	 * getNameAndID() stores the current Facebook user's ID and name in the
	 * FB_ID and FB_Name.
	 */
	public static void getNameAndID() {
		// Run new thread so that request finishes before main thread continues
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Make the API request call
					new Request(session, "/me", null, HttpMethod.GET,
							new Request.Callback() {
								public void onCompleted(Response response) {
									String incomingText = response.getRawResponse();
									
									try {
										JSONObject json = new JSONObject(incomingText);
										
										// Store the information in static field variables
										FB_ID = (String) json.get("id");
										FB_Name = (String) json.get("name");
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							}).executeAndWait();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * getFacebookId() returns the Facebook ID of the current user.
	 */
	public static String getFacebookId() {
		return FB_ID;
	}

	/**
	 * getFacebookName() returns the Facebook name of the current user.
	 */
	public static String getFacebookName() {
		return FB_Name;
	}
	
	/**
	 * getFacebookProfilePicture() starts a new thread that gets the current 
	 * logged in user's profile picture and returns that picture as a Bitmap.
	 */
	public static Bitmap getFacebookProfilePicture() {
		
		// Start new thread to make sure request runs ahead of main thread
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// URL that stores the user's profile picture
					URL imageURL = new URL("https://graph.facebook.com/" 
					+ AppFacebookAccess.getFacebookId() + "/picture?type=large");
					// Get the bitmap using a BitmapFactory
					profileBitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
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
		
		return profileBitmap;
	}
	
	public static void clearFacebookSession() {
		FB_ID = null;
		FB_Name = null;
		session = null;	
		allFriends = null;
		profileBitmap = null;
	}
}
