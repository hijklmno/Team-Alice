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
		super.onCreate(savedInstanceState);
		System.out.println("ASKDHOAHDIAHDOASDOSAJDOAJOSIJDAIJ");
		System.out.println("ASKDHOAHDIAHDOASDOSAJDOAJOSIJDAIJ");
		System.out.println("ASKDHOAHDIAHDOASDOSAJDOAJOSIJDAIJ");
		System.out.println("ASKDHOAHDIAHDOASDOSAJDOAJOSIJDAIJ");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		context = this.getApplicationContext();
		activity = this;
		
//		new Loader().execute(null, null, null);
		new Loader().execute();
		initial = true;
//		Thread fragmentThread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					setContentView(R.layout.activity_loading);
//					loading = (TextView) findViewById(R.id.loading);
//					
//				} catch (Exception e) {
//
//				}
//			}
//		});
//
//		fragmentThread.start();
//
//		try {
//			fragmentThread.join();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		 setContentView(R.layout.activity_loading);
//		loading = (TextView) findViewById(R.id.loading);

//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					// Facebook stuff
//					AppFacebookAccess.setActiveSession();
//					AppFacebookAccess.getNameAndID();
//
//					// Parse stuff
//					AppParseAccess.initialize(activity,
//							context.getString(R.string.ParseAppID),
//							context.getString(R.string.ParseClientKey));
//					AppParseAccess.loadOrAddNewUser(
//							AppFacebookAccess.getFacebookId(),
//							AppFacebookAccess.getFacebookName());
//				} catch (Exception e) {
//
//				}
//			}
//		});
//
//		thread.start();
//
//		try {
//			thread.join();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		gotoMainAndEnd();
//
	}

	private class Loader extends AsyncTask<String, Integer, Long> {

		@Override
		protected Long doInBackground(String... arg0) {
			AppFacebookAccess.setActiveSession();
			AppFacebookAccess.getNameAndID();

			// Parse stuff
			AppParseAccess.initialize(activity,
					context.getString(R.string.ParseAppID),
					context.getString(R.string.ParseClientKey));
			AppParseAccess.loadOrAddNewUser(
					AppFacebookAccess.getFacebookId(),
					AppFacebookAccess.getFacebookName());
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			TextView loading = (TextView) findViewById(R.id.loading);
			loading.setText("LOLOLOLL");
		}
		
		protected void onPostExecute(Long result) {
			if (initial) {
				System.out.println("MUHFUCKAWAAAAAAAAAAAAAAAAAAAAAAT");
				System.out.println("MUHFUCKAWAAAAAAAAAAAAAAAAAAAAAAT");
				System.out.println("MUHFUCKAWAAAAAAAAAAAAAAAAAAAAAAT");
				System.out.println("MUHFUCKAWAAAAAAAAAAAAAAAAAAAAAAT");
				initial = false;
				gotoMainAndEnd();
			}
		}
	}
	private void gotoMainAndEnd() {
		System.out.println("loading...");
		System.out.println("loading...");
		System.out.println("loading...");
		System.out.println("loading...");
		System.out.println("loading...");
		Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
		intent.putExtra("FacebookSession", Session.getActiveSession());
		startActivity(intent);
		finish();
	}

}
