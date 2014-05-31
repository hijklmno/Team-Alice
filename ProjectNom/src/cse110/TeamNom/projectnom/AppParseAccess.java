package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/*
 * AppParseAccess is a access class for the purpose of layering access to Parse in the 
 * NOM application. The NOM Parse database includes the FacebookAccounts and Food_table
 * tables.
 */
public class AppParseAccess {

	/*
	 * initialize() initializes the Parse access using NOM's application ID and
	 * client key.
	 */
	public static void initialize(final Context context, final String applicationId,
			final String clientKey) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					/* make the API call */
					Parse.initialize(context, applicationId, clientKey);
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
	 * isExistingUser() checks if user is already existing on the
	 * FacebookAccounts table of the NOM Parse database.
	 */
	public static boolean isExistingUser(String FB_ID) {
		// testing to see if is a user already
		ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
		query.whereEqualTo("facebook_id", FB_ID);

		try {
			List<ParseObject> objects = query.find();

			if (objects.isEmpty()) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return true;
	}

	/*
	 * addNewUser() adds a new user to the FacebookAccounts table of the NOM
	 * Parse database, if there isn't already an account on the FacebookAccounts.
	 */
	public static void loadOrAddNewUser(String FB_ID, String fullName) {
		if (!AppParseAccess.isExistingUser(FB_ID)) {
			ParseObject newUser = new ParseObject("FacebookAccounts");
			newUser.put("facebook_id", FB_ID);
			newUser.put("Name", fullName);
			newUser.saveInBackground();
		}
	}

	/*
	 * getMyPictureIds() returns a string array by splitting the string from the
	 * pictures column of the FacebookAccounts table in the NOM Parse database.
	 */
	public static String[] getMyPictureIds(String FB_ID) {
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

		if (currentUser != null) {
			String pictureString = getPictureString(currentUser);
			String[] pictureList = pictureString.split(",");

			return pictureList;
		}

		return null;
	}

	/*
	 * getSpecificPicture() returns a PictureDBObject by querying through the
	 * Food_Table_DB table through the objectId column.
	 */
	public static PictureDBObject getSpecificPicture(String picture_id) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		query.whereEqualTo("objectId", picture_id);

		try {
			List<ParseObject> objects = query.find();

			if (objects.isEmpty()) {
				return null;
			} else
				return new PictureDBObject(objects.get(0));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * putMyPicture() adds a new ParseObject to the Food_Table_DB.
	 */
	public static void putMyPicture(byte[] data, final String FB_ID,
			String caption, String resName, double latitude, double longitude) {
		final ParseObject object = new ParseObject("Food_Table_DB");

		ParseFile file = new ParseFile(data);
		file.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {

				} else {
					System.err.println("ParseException: " + e);
				}
			}
		});

		object.put("Food_photo", file);
		object.put("Restaurant_Id", resName);
		object.put("Food_Name", caption);
		object.put("Like", 0);
		object.put("Bookmark", 0);
		object.put("Longitude", longitude);
		object.put("Latitude", latitude);
		object.put("roundLongitude", (double) Math.round(longitude * 10) / 10);
		object.put("roundLatitude", (double) Math.round(latitude * 10) / 10);
		object.put("report_image", false);

		object.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {

				String pictureID = object.getObjectId();

				ParseQuery<ParseObject> query = ParseQuery
						.getQuery("FacebookAccounts");
				ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

				if (currentUser != null) {
					String pictureString = (String) currentUser.get("pictures");

					if (pictureString == null || pictureString.equals("")) {
						pictureString = "";
						pictureString += pictureID;
					} else {
						pictureID = "," + pictureID;
						pictureString += pictureID;
					}
					currentUser.put("pictures", pictureString);
					currentUser.saveInBackground();
				}
			}
		});
	}

	/*
	 * getCurrentUser() returns the ParseObject of the FacebookAccounts table of
	 * the NOM Parse database by querying with FB_ID.
	 */
	public static ParseObject getCurrentUser(String FB_ID) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
		query.whereEqualTo("facebook_id", FB_ID);

		try {
			List<ParseObject> objects = query.find();

			if (objects.isEmpty()) {
				return null;
			} else
				return objects.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * getPictureString() returns the string from the pictures column of the
	 * FacebookAccounts table of the parseUser in the NOM Parse database.
	 * 
	 * Note: The string should be split with "," to obtain each photo ID.
	 */
	public static String getPictureString(ParseObject parseUser) {
		if (parseUser != null) {
			String list = (String) parseUser.get("pictures");

			return list;
		}

		return null;
	}

	/*
	 * getFoodObject() returns a ParseObject by querying through the
	 * Food_Table_DB for the objectID corresponding to foodID.
	 */
	public static ParseObject getFoodObject(String foodID) {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");

		try {
			ParseObject photoObject = query.get(foodID);

			if (photoObject != null) {
				return photoObject;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * getPhoto() returns the ParseFile of the photo from the Food_photo column
	 * of the photoObject ParseObject.
	 */
	public static ParseFile getPhoto(ParseObject photoObject) {
		ParseFile photo = (ParseFile) photoObject.get("Food_photo");

		if (photo != null) {
			return photo;
		}

		return null;
	}

	/*
	 * getFriends() returns a string array by splitting the string from the
	 * friends column of the FacebookAccounts NOM Parse database.
	 */
	public static String[] getFriends(String FB_ID) {
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

		if (currentUser != null) {
			String friendsString = (String) currentUser.get("friends");
			String[] friendsList = friendsString.split(",");

			return friendsList;
		}

		return null;
	}
	
	/* 
	 * Get a list of pictures given a list of friends
	 */
	public static ArrayList<PictureDBObject> getFriendsPictureWithLimits(String[] picture_ids, int count, int offset) {
		ArrayList<PictureDBObject> customList = new ArrayList<PictureDBObject>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		query.whereContainedIn("FACEBOOK_ID", Arrays.asList(picture_ids));
		query.orderByDescending("updatedAt");
		query.setLimit(count);
		query.setSkip(offset);
		
		try {
			List<ParseObject> objects = query.find();

			if (objects.isEmpty()) {
				return null;
			} else {
				for (int i = 0; i < objects.size(); i++) {
					customList.add(new PictureDBObject(objects.get(i)));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return customList;
	}

	//TODO
	public static void incrementNomCount(String imageID) {
		System.out.println(imageID + " nom + 1!!");
	}
	
	public static void incrementMmmCount(String imageID) {
		System.out.println(imageID + " mmm + 1!!");
	}

	public static void setFlag(String imageID) {
		System.out.println(imageID + " flagged!");
		
	}
}
