
package cse110.TeamNom.projectnom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Class that allows picture to be zoomed in on. 
 * @author Jean Park
 */
public class PicturePopUp {
	
	private Context context;
	
	public PicturePopUp(Uri u, Context c) {
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW); 
		intent.setDataAndType(u,"image/*");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         

		this.context = c;
		
		context.startActivity(intent);
	}

}
