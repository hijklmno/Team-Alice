package cse110.TeamNom.projectnom;

import java.io.File;
import android.os.Environment;

/**
 * Class that provides a storage location of pictures taken from the users.
 * Stored these digital camera files in a folder called NOM. 
 */

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	/**
	 * Override method that stores the location of photos taken from users
	 * camera.
	 **/
	@Override
	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}
