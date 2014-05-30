package cse110.TeamNom.projectnom;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

public class AppFacebookAccess {

	private static String FB_ID;
	private static String FB_Name;
	private static Session session;
	private static ArrayList<String> allFriends;
	
	public static void setActiveSession() {
		session = Session.getActiveSession();
	}

	// TODO: return list of my facebook friends
	public static String[] loadMyFriends() {
		allFriends = new ArrayList<String>();

		/* make the API call */
		new Request(session, "/me/friends", null, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						try {
							String incomingText = response.getRawResponse();
							Log.d("incomingText", incomingText);
							JSONObject json = new JSONObject(incomingText);
//							JSONObject friendsList = json.getJSONObject("friends");
							JSONArray dataList = json.getJSONArray("data");
							Log.d("dataListSIze", Integer.toString(dataList.length()));
							for (int i = 0; i < dataList.length(); i++) {
								String friend_name = ((JSONObject) dataList.get(i)).getString("name");
								String friend_id = ((JSONObject) dataList.get(i)).getString("id");
								System.out.println(friend_name + ":" + friend_id);
//								allFriends.add(friend_id);
								friendsIDBuffer(friend_id);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).executeAsync();
		
		return dumpfriendsIDBuffer();
	}

	private static void friendsIDBuffer(String friend_id) {
		allFriends.add(friend_id);
		System.out.println("Added: " + friend_id + ", allFriends size: " + Integer.toString(allFriends.size()));
	}
	
	private static String[] dumpfriendsIDBuffer() {
		System.out.println("allFriends size: " + allFriends.size());
		String[] newArr = new String[allFriends.size()];
		String[] friendsStrArr = (String[]) allFriends.toArray(newArr);
		allFriends.clear();
		return friendsStrArr;
	}
	public static void getNameAndID() {
		new Request(session, "/me", null, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						String incomingText = response.getRawResponse();

						try {
							JSONObject json = new JSONObject(incomingText);
							// id working
							FB_ID = (String) json.get("id");
							FB_Name = (String) json.get("name");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}).executeAndWait();
	}

	public static String getFacebookId() {
		return FB_ID;
	}

	public static String getFacebookName() {
		return FB_Name;
	}
}
