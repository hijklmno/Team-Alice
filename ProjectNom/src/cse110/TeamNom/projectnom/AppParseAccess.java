package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * AppParseAccess is a access class for the purpose of layering access to Parse in the 
 * NOM application. The NOM Parse database includes the FacebookAccounts and Food_table
 * tables.
 */
public class AppParseAccess {

	/**
	 * initialize() initializes the Parse access using NOM's application ID and
	 * client key.
	 */
	public static void initialize(final Context context,
			final String applicationId, final String clientKey) {
		// Run new thread so that Parse calls finish before main thread continues
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Initialize Parse
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

	/**
	 * isExistingUser() checks if user is already existing on the
	 * FacebookAccounts table of the NOM Parse database.
	 */
	public static boolean isExistingUser(String FB_ID) {
		// Testing to see if is a user already by checking for FB_ID
		ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
		query.whereEqualTo("facebook_id", FB_ID);

		try {
			List<ParseObject> objects = query.find();

			// Empty objects mean that there was no match
			if (objects.isEmpty()) {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * addNewUser() adds a new user to the FacebookAccounts table of the NOM
	 * Parse database, if there isn't already an account on the
	 * FacebookAccounts.
	 * @throws ParseException 
	 */
	public static void loadOrAddNewUser(String FB_ID, String fullName) throws ParseException {
		if (!AppParseAccess.isExistingUser(FB_ID)) {
			// Adds new Parse user and get the necessary info from Facebook
			ParseObject newUser = new ParseObject("FacebookAccounts");
			newUser.put("facebook_id", FB_ID);
			newUser.put("Name", fullName);
			newUser.put("pictures", "");
			newUser.put("bookmarks", "");
			newUser.put("friends", "");
//			newUser.saveInBackground();
			newUser.save();
		}
	}

	/**
	 * getMyPictureIds() returns a string array by splitting the string from the
	 * pictures column of the FacebookAccounts table in the NOM Parse database.
	 */
	public static String[] getMyPictureIds(String FB_ID) {
		// Gets current user ParseObject
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

		if (currentUser != null) {
			// Obtain picture string from parse
			String pictureString = getPictureString(currentUser);
			
			if (pictureString != null) {
				
				// Split by delimiting with "," to obtain the array of strings
				String[] pictureList = pictureString.split(",");
				return pictureList;
			}
		}

		return null;
	}

	/**
	 * getMyBookmarkIds() returns a string array by splitting the string from the
	 * pictures column of the FacebookAccounts table in the NOM Parse database.
	 */
	public static String[] getMyBookmarkIds(String FB_ID) {
		// Gets current user ParseObject
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

		if (currentUser != null) {
			
			// Obtain bookmark string from Parse
			String bookmarkString = getBookmarkString(currentUser);
			
			if(bookmarkString != null) {
				
				// Split by delimiting with "," to obtain the array of strings
				String[] bookmarkList = bookmarkString.split(",");
				return bookmarkList;
			}
		}

		return null;
	}
	
	/**
	 * getBookmarkString() returns the string from the pictures column of the
	 * FacebookAccounts table of the parseUser in the NOM Parse database.
	 * 
	 * Note: The string should be split with "," to obtain each photo ID.
	 */
	private static String getBookmarkString(ParseObject parseUser) {
		
		if (parseUser != null) {
			// Obtain the "bookmarks" string from Parse
			String list = (String) parseUser.get("bookmarks");

			return list;
		}

		return null;
	}

	/**
	 * getSpecificPicture() returns a PictureDBObject by querying through the
	 * Food_Table_DB table through the objectId column.
	 */
	public static PictureDBObject getSpecificPicture(String picture_id) {
		
		// Query through Food_Table_DB for "objectId"
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		query.whereEqualTo("objectId", picture_id);

		try {
			List<ParseObject> objects = query.find();

			if (objects.isEmpty()) {
				return null;
			} else
				// Return a new PictureDBObject using information from Parse
				return new PictureDBObject(objects.get(0));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * putMyPicture() adds a new ParseObject to the Food_Table_DB.
	 */
	public static void putMyPicture(byte[] data, final String FB_ID,
			String caption, String resName, double latitude, double longitude) {
		// Create new ParseObject in "Food_Table_DB"
		final ParseObject object = new ParseObject("Food_Table_DB");

		// Save the photo byte array in Food_Table_DB
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

		// Store the necessary info in Parse
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

		// When saving to Parse must also update user's picture string
		object.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {

				// Get the objectID of the picture
				String pictureID = object.getObjectId();

				// Get the current user's ParseObject
				ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

				// Add the objectID to the user's pictures string
				if (currentUser != null) {
					String pictureString = (String) currentUser.get("pictures");

					// Null or empty means no comma is added
					if (pictureString == null || pictureString.equals("")) {
						pictureString = "";
						pictureString += pictureID;
					} else {
						// else add a comma with the pictureID
						pictureID = "," + pictureID;
						pictureString += pictureID;
					}
					// Save the pictureString to the current user
					currentUser.put("pictures", pictureString);
					currentUser.saveInBackground();
				}
			}
		});
	}

	/**
	 * getCurrentUser() returns the ParseObject of the FacebookAccounts table of
	 * the NOM Parse database by querying with FB_ID.
	 */
	public static ParseObject getCurrentUser(String FB_ID) {
		// Query through "FacebookAccounts" for "faecbook_id"
		ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
		query.whereEqualTo("facebook_id", FB_ID);

		try {
			// List of object that were returned from Parse
			List<ParseObject> objects = query.find();

			// Return null if list is empty
			if (objects.isEmpty()) {
				return null;
			} else
				// Return what should be the only object in list
				return objects.get(0);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * getPictureString() returns the string from the pictures column of the
	 * FacebookAccounts table of the parseUser in the NOM Parse database.
	 * 
	 * Note: The string should be split with "," to obtain each photo ID.
	 */
	private static String getPictureString(ParseObject parseUser) {
		if (parseUser != null) {
			// Get "pictures" from Parse table "FacebookAccounts"
			// in the parseUser row
			String list = (String) parseUser.get("pictures");

			return list;
		}

		return null;
	}

	/**
	 * getFoodObject() returns a ParseObject by querying through the
	 * Food_Table_DB for the objectID corresponding to foodID.
	 */
	public static ParseObject getFoodObject(String foodID) {
		// Query through "Food_Table_DB"
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");

		try {
			// Get the ParseObject with objectID of foodID
			ParseObject photoObject = query.get(foodID);

			if (photoObject != null) {
				// Return that ParseObject
				return photoObject;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * getPhoto() returns the ParseFile of the photo from the Food_photo column
	 * of the photoObject ParseObject.
	 */
	public static ParseFile getPhoto(ParseObject photoObject) {
		// Get "Food_Photo" from photoObject
		ParseFile photo = (ParseFile) photoObject.get("Food_photo");

		if (photo != null) {
			// Return "Food_Photo"
			return photo;
		}

		return null;
	}

	/**
	 * getFriends() returns a string array by splitting the string from the
	 * friends column of the FacebookAccounts NOM Parse database.
	 */
	public static String[] getFriends(String FB_ID) {
		// Get the current user corresponding to FB_ID
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);

		if (currentUser != null) {
			// Get "friends" from currentUser on Parse
			String friendsString = (String) currentUser.get("friends");
			// Split the list with "," and return it
			String[] friendsList = friendsString.split(",");

			return friendsList;
		}

		return null;
	}

	/**
	 * getFriendsPictureWithLimits() returns an ArrayList of PictureDBObject with the
	 * limit of count and skipping by offset. Each PictureDBObject is created by querying
	 * through "Food_Table_DB" on Parse for picture_ids in "FACEBOOK_ID"
	 */
	public static ArrayList<PictureDBObject> getFriendsPictureWithLimits(
			String[] picture_ids, int count, int offset) {
		// ArrayList that will be populated and returned
		ArrayList<PictureDBObject> customList = new ArrayList<PictureDBObject>();

		// Set parameters for query
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		query.whereContainedIn("FACEBOOK_ID", Arrays.asList(picture_ids));
		query.orderByDescending("updatedAt");
		query.setLimit(count);
		query.setSkip(offset);

		try {
			List<ParseObject> objects = query.find();

			// Return null if no objects in list
			if (objects.isEmpty()) {
				return null;
			} else {
				// Add new PictureDBObjects to customList
				for (int i = 0; i < objects.size(); i++) {
					customList.add(new PictureDBObject(objects.get(i)));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return customList;
	}

	/**
	 * incrementNomCount() increments the Nom number of the object with imageID
	 * in the Food_Table_DB by 1.
	 */
	public static void incrementNomCount(String FB_ID, String imageID) {
		// Query through "Food_Table_DB"
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		
		// Get the current user's objectID
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);
		String userID = currentUser.getObjectId();

		try {
			// Get the ParseObject with an objectID matching imageID
			ParseObject photoObject = query.get(imageID);

			if (photoObject != null) {
				// Increment the "Like" number
				photoObject.increment("Like");
				
				// Get the "Like_id" string from photoObject
				String nomString = photoObject.getString("Like_id");
				
				// Add on the objectID of the current user to the string
				// and add a "," to split the string
				if (nomString == null || nomString.equals("")) {
					nomString = "";
					nomString += userID;
				} else {
					String addString = "," + userID;
					nomString += addString;
				}
				
				// Save the new string to "Like_id" on Parse
				photoObject.put("Like_id", nomString);
				photoObject.saveInBackground();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * bookmarkImage() adds the imageID to the bookmarks string of the current
	 * Parse user.
	 */
	public static void bookmarkImage(String FB_ID, String imageID) {
		// Query through "Food_Table__DB"
		 ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		 
		 // Get the current user's objectID
		 ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);
		 String userID = currentUser.getObjectId();
		
		 try {
			 // Get the ParseObject from "Food_Table_DB" with an objectID
			 // matching imageID
			 ParseObject photoObject = query.get(imageID);
		
			 if (photoObject != null) {
				 // Increment the "Bookmark" number on Parse
				 photoObject.increment("Bookmark");
			 
				 // Get the "Bookmark_id" string from photoObject
				 String bookmarkString = photoObject.getString("Bookmark_id");
				
				 // Add the current user's objectID to the bookmarkString
				 // while also splitting with "," if necessary
				 if (bookmarkString == null || bookmarkString.equals("")) {
					 bookmarkString = "";
					 bookmarkString += userID;
				 } else {
					 String addString = "," + userID;
					 bookmarkString += addString;
				 }
				
				 
				 // Save the photoObject to Parse
				 photoObject.put("Bookmark_id", bookmarkString);
				 photoObject.saveInBackground();
			 }
		 } catch (ParseException e) {
			 e.printStackTrace();
		 }

		
		// Add the photoObject's objectID to the user's bookmarks string
		if (currentUser != null) {
			
			// Get the "bookmarks" string from currentUser ParseObject
			String pictureString = (String) currentUser.get("bookmarks");

			// Append the photoObject's objectID on while also adding a
			// "," if necessary to split the string
			if (pictureString == null || pictureString.equals("")) {
				pictureString = "";
				pictureString += imageID;
			} else {
				String addString = "," + imageID;
				pictureString += addString;
			}
			
			// Save the new "bookmarks" string to currentUser on Parse
			currentUser.put("bookmarks", pictureString);
			currentUser.saveInBackground();
		}
	}

	/**
	 * setFlag() sets the flag of the object with imageID.
	 */
	public static void setFlag(String imageID) {
		// Query through "Food_Table_DB" on Parse
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");

		try {
			// Get the ParseObject with an objectID matching imageID
			ParseObject photoObject = query.get(imageID);

			if (photoObject != null) {
				// Set the flag in "report_image" and save to Parse
				photoObject.put("report_image", true);
				photoObject.saveInBackground();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * isLiked() checks if the ParseObject with FB_ID has already liked the
	 * ParseObject that corresponds with imageID.
	 */
	public static boolean isLiked(String FB_ID, String imageID) {
		// Get current user's objectID
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);
		String userID = currentUser.getObjectId();
		
		// Query through Food_Table_DB
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		
		try {
			// Get the ParseObject thats objectID is imageID
			ParseObject photoObject = query.get(imageID);
			
			if (photoObject != null) {
				// Get the string of IDs in Like_id
				String nomString = photoObject.getString("Like_id");
				
				if(nomString != null) {
					// Split the string by delimiting with ","
					String[] nomList = nomString.split(",");
					
					// Return whether nomList has an element matching userID
					return Arrays.asList(nomList).contains(userID);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * isBookmarked() checks if the ParseObject with FB_ID has already liked the
	 * ParseObject that corresponds with imageID.
	 */
	public static boolean isBookmarked(String FB_ID, String imageID) {
		// Get the current user's objectID
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);
		String userID = currentUser.getObjectId();
		
		// Query through Food_Table_DB
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		
		try {
			// Get the ParseObject that's objectID matches imageID
			ParseObject photoObject = query.get(imageID);
			
			if (photoObject != null) {
				// Get the string of user objectIDs from Bookmark_id
				String bookmarkString = photoObject.getString("Bookmark_id");
				
				if(bookmarkString != null) {
					// Split the string by delimiting with ","
					String[] bookmarkList = bookmarkString.split(",");
				
					// Return whether the array contains an element that matches userID
					return Arrays.asList(bookmarkList).contains(userID);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * unlikeImage() decrements the "Like" number from the ParseObject in "Food_Table_DB"
	 * with an objectID that matches imageID. It also removes FB_ID from the "Like_id" 
	 * string in the same ParseObject.
	 */
	public static void unlikeImage(String FB_ID, String imageID) {
		// Get the current user's objectID
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);
		String userID = currentUser.getObjectId();
		
		// Query through Food_Table_DB
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
				
				try {
					// Get the ParseObject that's objectID matches imageID
					ParseObject photoObject = query.get(imageID);
					
					if (photoObject != null) {
						// Get the string of user objectIDs from Like_id
						String likeString = photoObject.getString("Like_id");
						
						if(likeString != null) {
							// Split the string by delimiting with ","
							String[] likeList = likeString.split(",");
							String newString = "";
						
							// Changes likeList to a LinkedList to facilitate removing userID
							List<String> list = new LinkedList<String>(Arrays.asList(likeList));
							// Remove userID from list
							list.remove(userID);
							
							// Combines list back together into a string array with each
							// string split with a ","
							for(int i = 0; i < list.size(); i++) {
								
								if (newString == null || newString.equals("")) {
									newString = "";
									newString += list.get(i);
								} else {
									String addString = "," + list.get(i);
									newString += addString;
								}
							}
							
							// Save new "Like_id" and "Like"
							photoObject.put("Like_id", newString);
							photoObject.increment("Like", -1);
							photoObject.saveInBackground();
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
	}
	
	/**
	 * unbookmarkImage() decrements the "Bookmark" number from the ParseObject in "Food_Table_DB"
	 * with an objectID that matches imageID. It also removes FB_ID from the "Bookmark_id" 
	 * string in the same ParseObject.
	 */
	public static void unbookmarkImage(String FB_ID, String imageID) {
		// Get the current user's objectID
		ParseObject currentUser = AppParseAccess.getCurrentUser(FB_ID);
		String userID = currentUser.getObjectId();
		// Query through Food_Table_DB
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		try {
			// Get the ParseObject that's objectID matches imageID
			ParseObject photoObject = query.get(imageID);
			if (photoObject != null) {
				// Get the string of user objectIDs from Bookmark_id
				String bookmarkString = photoObject.getString("Bookmark_id");
				
				// Get the "bookmarks" string from currentUser
				String userBookmarks = currentUser.getString("bookmarks");
				
				// Split the userBookmarks with ","
				String[] userBookmarkArr = userBookmarks.split(",");
				
				// Change userBookmarkArr to a LinkedList to facilitate removing an element
				List<String> userBookmarkList = new LinkedList<String>(Arrays.asList(userBookmarkArr));
				// Remove the objectID of photoObject from the LinkedList
				userBookmarkList.remove(photoObject.getObjectId());
				
				// String that will be concatenated and added to parse
				String newUserBookmarkStr = null;
				
				// Combine userBookmarkList for new "bookmarks" string while
				// adding a "," between each element
				for (int i = 0; i < userBookmarkList.size(); i++) {
					
					if (newUserBookmarkStr == null || newUserBookmarkStr.equals("")) {
						newUserBookmarkStr = "";
						newUserBookmarkStr += userBookmarkList.get(i);
					} else {
						String addString = "," + userBookmarkList.get(i);
						newUserBookmarkStr += addString;
					}
				}
				
				// Save newUserBookmarkStr to "bookmarks" in currentUser on Parse
				currentUser.put("bookmarks", newUserBookmarkStr);
				currentUser.saveInBackground();
				
				
				if (bookmarkString != null) {
					// Split the string by delimiting with ","
					String[] bookmarkList = bookmarkString.split(",");
					
					// Create a LinkedList from bookmarkList to facilitate removing an element
					List<String> list = new LinkedList<String>(Arrays.asList(bookmarkList));
					// Remove userID from the list
					list.remove(userID);
					
					// The new string that will be saved to "Bookmark_id" on Parse
					String newString = "";
					
					// Create the new string by appending each element from list together
					// while adding a "," in between
					for (int i = 0; i < list.size(); i++) {
						if (newString == null || newString.equals("")) {
							newString = "";
							newString += list.get(i);
						} else {
							String addString = "," + list.get(i);
							newString += addString;
						}
					}
					
					// Save "Bookmark_Id" and "Bookmark" to photoObject and Parse
					photoObject.put("Bookmark_id", newString);
					photoObject.increment("Bookmark", -1);
					photoObject.saveInBackground();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * getPictureFiles() returns an ArrayList of PictureDBObjects that are within a certain latitude and longitude
	 * range of the lat and lon. The query's parameters are set with limit of count and it skips offset. Each
	 * PictureDBObject is created from each ParseObject from "Food_Table_DB" that is returned from the query.
	 */
	public static ArrayList<PictureDBObject> getPictureFiles(double lat, double lon, int count, int offset) {
		// ArrayList of PictureDBObjects that will be returned
		ArrayList<PictureDBObject> customList = new ArrayList<PictureDBObject>();
		
		// 69.2 miles = 1 degree
		double radius = 5 / 69.2;
		
		// Create query for "Food_Table_Db" and set parameters
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Food_Table_DB");
		query.orderByDescending("updatedAt");
		query.setLimit(count);
		query.setSkip(offset);
		
		query.whereGreaterThan("Latitude", lat - radius);
		query.whereLessThan("Latitude", lat + radius);
		query.whereGreaterThan("Longitude", lon - radius);
		query.whereLessThan("Longitude", lon + radius);
		
		try {
			List<ParseObject> objects = query.find();

			if (objects.isEmpty()) {
				return null;
			} else {
				// Add each new PictureDBObject to customList to be returned
				for (int i = 0; i < objects.size(); i++) {
					customList.add(new PictureDBObject(objects.get(i)));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return customList;
	}
}
