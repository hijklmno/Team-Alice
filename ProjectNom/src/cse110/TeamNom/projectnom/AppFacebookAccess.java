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
	private static String[] friendsStrArr;

	public static void setActiveSession() {
		session = Session.getActiveSession();
	}

	// TODO: return list of my facebook friends
	public static String[] getMyFriends() {
		final ArrayList<String> allFriends = new ArrayList<String>();

		/* make the API call */
		new Request(session, "/me/friends", null, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {

						try {
							String incomingText = response.getRawResponse();
							Log.d("incomingText", incomingText);
							JSONObject json = new JSONObject(incomingText);
							JSONObject friendsList = json
									.getJSONObject("friends");
							JSONArray dataList = friendsList
									.getJSONArray("data");
							Log.d("dataListSIze",
									Integer.toString(dataList.length()));
							for (int i = 0; i < dataList.length(); i++) {
								JSONObject friendObject = dataList
										.getJSONObject(i);
								allFriends.add(friendObject.getString("id"));
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

		String[] newArr = new String[allFriends.size()];
		friendsStrArr = (String[]) allFriends.toArray(newArr);
		return friendsStrArr;
	}

	public static void getNameAndID() {
		System.out.println("getting name and id");
		if (session == Session.getActiveSession()) {
			System.out.println("same current session");
		}
		new Request(session, "/me", null, HttpMethod.GET,
				new Request.Callback() {
					public void onCompleted(Response response) {
						/* handle the result */
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
