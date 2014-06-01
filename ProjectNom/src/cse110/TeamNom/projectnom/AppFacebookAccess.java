package cse110.TeamNom.projectnom;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

/*
 * AppFacebookAccess is a access class for the purpose of layering access to Facebook in the 
 * NOM application. 
 */
public class AppFacebookAccess {

	private static String FB_ID;					// The Facebook ID of current user
	private static String FB_Name;					// The full name of current user
	private static Session session;					// The current active Facebook session
	private static ArrayList<String> allFriends;	// ArrayList of friend's FB IDs
	
	/*
	 * setActiveSession() stores the current active Facebook session.
	 */
	public static void setActiveSession() {
		session = Session.getActiveSession();
	}

	// TODO: return list of my facebook friends
	/*
	 * loadMyFriends() loads the current Facebook user's friends that have also given Facebook
	 * permissions to the NOM application.
	 */
	public static String[] loadMyFriends() {
		allFriends = new ArrayList<String>();

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
							JSONArray dataList = json.getJSONArray("data");
							
							// Loop to add each friend's id to allFriends array
							for (int i = 0; i < dataList.length(); i++) {
								String friend_name = ((JSONObject) dataList.get(i)).getString("name");
								String friend_id = ((JSONObject) dataList.get(i)).getString("id");
								System.out.println(friend_name + ":" + friend_id);
								friendsIDBuffer(friend_id);
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
		
		return dumpfriendsIDBuffer();
	}

	/*
	 * friendsIDBuffer() adds a new friend string to the String array.
	 */
	private static void friendsIDBuffer(String friend_id) {
		allFriends.add(friend_id);
		System.out.println("Added: " + friend_id + ", allFriends size: " + Integer.toString(allFriends.size()));
	}
	
	/*
	 * dumpfriendsIDBuffer() returns a copy of the friends array and then clears the old
	 * friends array.
	 */
	private static String[] dumpfriendsIDBuffer() {
		System.out.println("allFriends size: " + allFriends.size());
		String[] newArr = new String[allFriends.size()];
		String[] friendsStrArr = (String[]) allFriends.toArray(newArr);
		allFriends.clear();
		return friendsStrArr;
	}
	
	/*
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

	/*
	 * getFacebookId() returns the Facebook ID of the current user.
	 */
	public static String getFacebookId() {
		return FB_ID;
	}

	/*
	 * getFacebookName() returns the Facebook name of the current user.
	 */
	public static String getFacebookName() {
		return FB_Name;
	}
}
