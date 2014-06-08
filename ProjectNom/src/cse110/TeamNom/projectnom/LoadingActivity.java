package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.parse.ParseException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class LoadingActivity extends Activity {

	private Context context;
	private Activity activity;
	
	protected void onCreate(Bundle savedInstanceState) {
		// Set window to full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Get the application context and activity
		context = this.getApplicationContext();
		activity = this;
		
		// Set the view
		setContentView(R.layout.fragment_loading);
		
		// Start the AsyncTask
		new Loader().execute();
		super.onCreate(savedInstanceState);
	}

	private class Loader extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... arg0) {
			// Initializing Parse and NOM
			AppFacebookAccess.setActiveSession();
			AppFacebookAccess.getNameAndID();
			
			AppParseAccess.initialize(activity,
					context.getString(R.string.ParseAppID),
					context.getString(R.string.ParseClientKey));
			
			try {
				AppParseAccess.loadOrAddNewUser(
						AppFacebookAccess.getFacebookId(),
						AppFacebookAccess.getFacebookName());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		// When everything is done loading, start the MainActivity
		@Override
		protected void onPostExecute(Long result) {
			Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

}