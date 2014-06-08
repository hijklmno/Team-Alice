package cse110.TeamNom.projectnom;

import java.util.Date;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * PictureDBOject is a class that stores information about a ParseObject from
 * the "Food_Table_DB" on Parse.
 */
public class PictureDBObject {
	
	// Field variables which will be populated through a ParseObject
	String image_id;
	byte[] Food_photo;
	int Like;
	String[] Like_id;
	boolean isLiked;
	int Bookmark;
	String[] Bookmark_id;
	boolean isBookmarked;
	boolean report_image;
	String FACEBOOK_ID;
	String FACEBOOK_NAME;
	String Food_Name;
	String Restaurant_Id;
	double Latitude;
	double Longitude;
	Date createdAt;
	Date updatedAt;
	
	/**
	 * The only constructor which should be used for PictureDBOject which takes in
	 * a ParseObject from "Food_Table_DB". It calls many mutator methods to set the
	 * instance fields of PictureDBObject.
	 * 
	 * @param parse
	 */
	public PictureDBObject(ParseObject parse) {
		// Default settings
		Like = 0;
		Bookmark = 0;
		report_image = false;
		Latitude = 0;
		Longitude = 0;
		
		// Store picture
		ParseFile pf;
		pf = (ParseFile) parse.get("Food_photo");
		
		try {
			setPicture(pf.getData());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		// Store image id
		setImageID(parse.getObjectId());
		
		// Store facebook name
		setFacebookName(parse.getString("FACEBOOK_NAME"));
		
		// Store facebook id
		setFacebookID(parse.getString("FACEBOOK_ID"));
		
		// Store like
		setLikeCount(parse.getInt("Like"));

		// Store list of ids which liked the picture
		setLikeIDs(parse.getString("Like_id"));
		
		// Store bookmarks
		setBookmarkCount(parse.getInt("Bookmark"));
		
		// Store list of ids which bookmarked the picture
		setBookmarkIDs(parse.getString("Bookmark_id"));
		
		// Store image reported status
		setImageReportStatus(parse.getBoolean("report_image"));
		
		// Store food name
		setRestID(parse.getString("Restaurant_Id"));
		
		// Store latitude
		setLatitude(parse.getDouble("Latitude"));
		
		// Store longitude
		setLongitude(parse.getDouble("Longitude"));
		
		// Store created at date
		setCreatedDate(parse.getCreatedAt());
		
		// Store updated at date
		setUpdatedDate(parse.getUpdatedAt());
		
		// Check if I liked it
		setLikeStatus(getLikeIDs());
				
		// Check if I bookmarked it
		setBookmarkStatus(getBookmarkIDs());
		
		setFoodName(parse.getString("Food_Name"));
	}
	
	/**
	 * setLikeStatus takes in a string of ids seperated by commas and checks if
	 * the current user's Facebook ID is within them. If yes, then it sets
	 * isLiked to true. Otherwise, isLiked is set to false.
	 * 
	 * @param ids
	 */
	public void setLikeStatus(String[] ids) {
		isLiked = false;
		
		if(ids != null) {
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(AppParseAccess.getUserObjectID())) {
					isLiked = true;
	
					return;
				}
			}
		}
	}
	
	/**
	 * isLiked() is an accessor method for the isLiked boolean.
	 * 
	 * @return
	 */
	public boolean isLiked() {
		return isLiked;
	}
	
	/**
	 * setBookmarkStatus() checks if the current user's Facebook ID is
	 * within ids. If it is then it sets isBookmarked to true. Otherwise,
	 * false.
	 * 
	 * @param ids
	 */
	public void setBookmarkStatus(String[] ids) {
		isBookmarked = false;
		if(ids != null) {
			// Loop through ids to search for a match with Facebook ID
			for (int i = 0; i < ids.length; i++) {
				if (ids[i].equals(AppParseAccess.getUserObjectID())) {
					isBookmarked = true;
					
					return;
				}
			}
		}
	}
	
	public void setBookmark(boolean bool) {
		isBookmarked = bool;
	}
	
	public void setLike(boolean bool) {
		isLiked = bool;
	}
	
	/**
	 * isBookmarked() is an accessor method which returns the current state
	 * of the boolean isBookmarked.
	 * 
	 * @return
	 */
	public boolean isBookmarked() {
		return isBookmarked;
	}
	
	/**
	 * setFacebookName() is a mutator method that sets FACEBOOK_NAME to
	 * name.
	 * 
	 * @param name
	 */
	public void setFacebookName(String name) {
		FACEBOOK_NAME = name;
	}
	
	/**
	 * getFacebookName() is an accessor method that returns the current
	 * state of FACEBOOK_NAME.
	 * 
	 * @return
	 */
	public String getFacebookName() {
		return FACEBOOK_NAME;
	}
	
	/**
	 * setImageID() is a mutator method that sets image_id to id.
	 * 
	 * @param id
	 */
	public void setImageID(String id) {
		image_id = id;
	}
	
	/**
	 * getImageID() is a accessor method that returns the current state
	 * of image_id.
	 * 
	 * @return
	 */
	public String getImageID() {
		return image_id;
	}
	
	/**
	 * setFacebookID() is a mutator method that sets FACEBOOK_ID to
	 * id.
	 * 
	 * @param id
	 */
	public void setFacebookID(String id) {
		FACEBOOK_ID = id;
	}
	
	/**
	 * getFacebookID() is a accessor method that returns the current
	 * state of FACEBOOK_ID.
	 * 
	 * @return
	 */
	public String getFacebookID() {
		return FACEBOOK_ID;
	}
	
	/**
	 * setPicture() is a mutator method that sets Food_photo to
	 * picture using the clone() method.
	 * 
	 * @param picture
	 */
	public void setPicture(byte[] picture) {
		Food_photo = picture.clone();
	}

	/**
	 * getPicture() is a accessor method that returns the current state
	 * of Food_photo.
	 * 
	 * @return
	 */
	public byte[] getPicture() {
		return Food_photo;
	}

	/**
	 * setLikeCount() is a mutator method that sets Like to count.
	 * 
	 * @param count
	 */
	public void setLikeCount(int count) {
		Like = count;
	}

	/**
	 * getLikeCount() is an accessor method that returns the current
	 * state of Like.
	 * 
	 * @return
	 */
	public int getLikeCount() {
		return Like;
	}

	/**
	 * setLikeIDs() is a mutator method that sets Like_id to ids split
	 * with ",".
	 * 
	 * @param ids
	 */
	public void setLikeIDs(String ids) {
		if (ids != null) {
			Like_id = ids.split(",");
		}
	}
	
	/**
	 * getLikeIDs() is an accessor method that returns the current state
	 * of Like_id.
	 * 
	 * @return
	 */
	public String[] getLikeIDs() {
		return Like_id;
	}
	
	/**
	 * setBookmarkIDs() is a mutator method that sets Bookmark_id to
	 * ids split with ","/
	 * 
	 * @param ids
	 */
	public void setBookmarkIDs(String ids) {
		if (ids != null) {
			Bookmark_id = ids.split(",");
		}
	}
	
	/**
	 * getBookmarkIDs() is an accessor method that returns the current
	 * state of Bookmark_id.
	 * 
	 * @return
	 */
	public String[] getBookmarkIDs() {
		return Bookmark_id;
	}
	
	/**
	 * setBookmarkCount() is a mutator method that sets Bookmark to
	 * count.
	 * 
	 * @param count
	 */
	public void setBookmarkCount(int count) {
		Bookmark = count;
	}

	/**
	 * getBookmarkCount() is an accessor method that returns the
	 * current state of Bookmark.
	 * 
	 * @return
	 */
	public int getBookmarkCount() {
		return Bookmark;
	}

	/**
	 * reportImage() is a mutator method that sets report_image to
	 * true.
	 */
	public void reportImage() {
		report_image = true;
	}

	/**
	 * unreportImage() is a mutator method that sets report_image to
	 * false.
	 */
	public void unreportImage() {
		report_image = false;
	}

	/**
	 * setImageReportStatus() is a mutator method that sets report_image
	 * to status.
	 * 
	 * @param status
	 */
	public void setImageReportStatus(boolean status) {
		report_image = status;
	}
	
	/**
	 * isReported() is an accessor method that returns report_image.
	 * 
	 * @return
	 */
	public boolean isReported() {
		return report_image;
	}

	/**
	 * setFoodName() is a mutator method that sets caption to newCaption.
	 * 
	 * @param newFoodName
	 */
	public void setFoodName(String newFoodName) {
		Food_Name = newFoodName;
	}
	
	/**
	 * getFoodName() is an accessor method that returns caption.
	 * @return 
	 */
	public String getFoodName() {
		return Food_Name;
	}
	
	/**
	 * setRestID() is a mutator method that sets Restaurant_Id to caption.
	 * 
	 * @param caption
	 */
	public void setRestID(String rest_id) {
		Restaurant_Id = rest_id;
	}

	/**
	 * getRestID() is an accessor method that returns Restaurant_Id.
	 * 
	 * @return
	 */
	public String getRestID() {
		return Restaurant_Id;
	}
	
	/**
	 * setLatitude() is a mutator method that sets Latitude to lat.
	 * 
	 * @param lat
	 */
	public void setLatitude(double lat) {
		Latitude = lat;
	}
	
	/**
	 * getLatitude() is an accessor method that returns Latitude.
	 * 
	 * @return
	 */
	public double getLatitude() {
		return Latitude;
	}
	
	/**
	 * setLongitude() is a mutator method that sets Longitude to
	 * longit.
	 * 
	 * @param longit
	 */
	public void setLongitude(double longit) {
		Longitude = longit;
	}
	
	/**
	 * getLongitude() is an accessor method that returns Longitude.
	 * 
	 * @return
	 */
	public double getLongitude() {
		return Longitude;
	}
	
	/**
	 * setCreatedDate() is a mutator method that sets createdAt to
	 * created.
	 * 
	 * @param created
	 */
	public void setCreatedDate(Date created) {
		createdAt = created;
	}
	
	/**
	 * getCreatedDate() is an accessor method that returns createdAt.
	 * 
	 * @return
	 */
	public Date getCreatedDate() {
		return createdAt;
	}
	
	/**
	 * setUpdatedDate() is a mutator method that sets updatedAt to
	 * updated.
	 * 
	 * @param updated
	 */
	public void setUpdatedDate(Date updated) {
		updatedAt = updated;
	}
	
	/**
	 * getUpdatedDate() is an accessor method that returns updatedAt.
	 * 
	 * @return
	 */
	public Date getUpdatedDate() {
		return updatedAt;
	}
}

