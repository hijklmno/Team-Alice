package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.Session;
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
	private TextView loading;
	private boolean initial = true;
	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		context = this.getApplicationContext();
		activity = this;
		
		setContentView(R.layout.fragment_loading);
		
		System.out.println("Loading loader.");
		new Loader().execute();
		initial = true;
		super.onCreate(savedInstanceState);
	}

	private class Loader extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... arg0) {
			AppFacebookAccess.setActiveSession();
			AppFacebookAccess.getNameAndID();
			System.out.println("Facebook name and ID retrieved.");
			
			// Parse stuff
			AppParseAccess.initialize(activity,
					context.getString(R.string.ParseAppID),
					context.getString(R.string.ParseClientKey));
			
			try {
				AppParseAccess.loadOrAddNewUser(
						AppFacebookAccess.getFacebookId(),
						AppFacebookAccess.getFacebookName());
				System.out.println("Parse user configuration done.");
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Long result) {
			System.out.println("Done execution.");
			Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
			intent.putExtra("FacebookSession", Session.getActiveSession());
			startActivity(intent);
			finish();
		}
	}

}