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
	String Food_name;
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
	}
	
	/**
	 * setLikeStatus takes in a string of ids seperated by commas and checks if
	 * the current user's Facebook ID is within them. If yes, then it sets
	 * isLiked to true. Otherwise, isLiked is set to false.
	 * 
	 * @param ids
	 */
	public void setLikeStatus(String[] ids) {

		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == AppFacebookAccess.getFacebookId()) {
				isLiked = true;

				return;
			}
		}

		isLiked = false;
	}
	
	// Accessor method return whether I have liked this picture
	public boolean isLiked() {
		return isLiked;
	}
	
	// Method which takes in the image string of ids which liked this picture,
	// and checks whether I have liked it
	public void setBookmarkStatus(String[] ids) {

		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == AppFacebookAccess.getFacebookId()) {
				isBookmarked = true;
				return;
			}
		}
		isBookmarked = false;
	}
	
	// Accessor method return whether I have bookmarked this picture
	public boolean isBookmarked() {
		return isBookmarked;
	}
	
	// Mutator method to set the facebook name for later retrieval
	public void setFacebookName(String name) {
		FACEBOOK_NAME = name;
	}
	
	// Accessor method to return the facebook name 
	public String getFacebookName() {
		return FACEBOOK_NAME;
	}
	
	// Mutator method to set the image id for later retrieval
	public void setImageID(String id) {
		image_id = id;
	}
	
	public String getImageID() {
		return image_id;
	}
	
	public void setFacebookID(String id) {
		FACEBOOK_ID = id;
	}
	
	public String getFacebookID() {
		return FACEBOOK_ID;
	}
	
	public void setPicture(byte[] picture) {
		Food_photo = picture.clone();
	}

	public byte[] getPicture() {
		return Food_photo;
	}

	public void setLikeCount(int count) {
		Like = count;
	}

	public int getLikeCount() {
		return Like;
	}

	public void setLikeIDs(String ids) {
		if (ids != null) {
			Like_id = ids.split(",");
		}
	}
	
	public String[] getLikeIDs() {
		return Like_id;
	}
	
	public void setBookmarkIDs(String ids) {
		if (ids != null) {
			Bookmark_id = ids.split(",");
		}
	}
	
	public String[] getBookmarkIDs() {
		return Bookmark_id;
	}
	
	public void setBookmarkCount(int count) {
		Bookmark = count;
	}

	public int getBookmarkCount() {
		return Bookmark;
	}

	public void reportImage() {
		report_image = true;
	}

	public void unreportImage() {
		report_image = false;
	}

	public void setImageReportStatus(boolean status) {
		report_image = status;
	}
	
	public boolean isReported() {
		return report_image;
	}

	public void setRestID(String caption) {
		Food_name = caption;
	}

	public String getRestID() {
		return Food_name;
	}
	
	public void setLatitude(double lat) {
		Latitude = lat;
	}
	
	public double getLatitude() {
		return Latitude;
	}
	
	public void setLongitude(double longit) {
		Longitude = longit;
	}
	
	public double getLongitude() {
		return Longitude;
	}
	
	public void setCreatedDate(Date created) {
		createdAt = created;
	}
	
	public Date getCreatedDate() {
		return createdAt;
	}
	
	public void setUpdatedDate(Date updated) {
		updatedAt = updated;
	}
	
	public Date getUpdatedDate() {
		return updatedAt;
	}
}

