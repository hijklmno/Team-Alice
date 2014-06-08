package cse110.TeamNom.projectnom;

import java.io.File;
import android.os.Environment;

/**
 * Class that provides a storage location of pictures taken from the users on
 * Froyo devices. Stored these digital camera files in a folder called NOM. 
 */
public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

	/**
	 * Override method that stores the location of photos taken from users
	 * camera.
	 **/
	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File(
		  Environment.getExternalStoragePublicDirectory(
		    Environment.DIRECTORY_PICTURES
		  ), 
		  albumName
		);
	}
}
