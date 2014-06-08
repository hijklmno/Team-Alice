package cse110.TeamNom.projectnom;

import java.io.File;

/**
 * AlbumStorageDirFactory is an abstract class for the purpose of implementing subclasses
 * that will store the photo and its location in our local device.
 */

abstract class AlbumStorageDirFactory {
	public abstract File getAlbumStorageDir(String albumName);
}
