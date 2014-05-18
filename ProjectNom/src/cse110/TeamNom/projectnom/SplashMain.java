package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

public class SplashMain extends Activity {
	private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";

    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        
        Bundle passedData = getIntent().getExtras();
        if (passedData != null && passedData.getString("logoutCall") == "logout") {
        	onClickLogout();
        }
        
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
                
                System.out.println("CREATED_TOKEN_LOADED");
            }
        }

        updateView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle passedData = getIntent().getExtras();
        if (passedData != null && passedData.getString("logoutCall") == "logout") {
        	onClickLogout();
        }
        Session.getActiveSession().addCallback(statusCallback);
//        updateView(); 
    }

    public void onResume() {
    	super.onResume();
    	Bundle passedData = getIntent().getExtras();
        if (passedData != null && passedData.getString("logoutCall") == "logout") {
        	onClickLogout();
        }
        Session.getActiveSession().addCallback(statusCallback);
//        updateView();
    }
    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void updateView() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
            buttonLoginLogout.setText("Logout...but we shouldn't see this");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });
            
            gotoMainAndEnd();
        } else {
            textInstructionsOrLink.setText("Logged out...press dat");
            buttonLoginLogout.setText("Login nao");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (session == null || session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));            
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
//        gotoMainAndEnd();
    }

    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
    
    private void gotoMainAndEnd() {
        Intent intent = new Intent(SplashMain.this, MainActivity.class);
        intent.putExtra("FacebookSession", Session.getActiveSession());
        startActivity(intent);
        
        finish();
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
}
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
//
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.content.pm.Signature;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.util.Base64;
//import android.util.Log;
//
//public class SplashMain extends FragmentActivity {
//	private static final int SPLASH = 0;
//	private static final int SELECTION = 1;
//	private static final int FRAGMENT_COUNT = SELECTION +1;
//
//	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
//
//	private boolean isResumed = false;
//	
//	private UiLifecycleHelper uiHelper;
//	private Session.StatusCallback callback = 
//	    new Session.StatusCallback() {
//	    @Override
//	    public void call(Session session, 
//	            SessionState state, Exception exception) {
//	        onSessionStateChange(session, state, exception);
//	    }
//	};
//	
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    
////	    try {
////	    	Log.w("UD", "hey");
////	    	PackageInfo info = getPackageManager().getPackageInfo(
////                    "cse110.TeamNom.projectnom", 
////                    PackageManager.GET_SIGNATURES);
////	    	for (Signature signature : info.signatures) {
////                MessageDigest md = MessageDigest.getInstance("SHA");
////                md.update(signature.toByteArray());
////                Log.w("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
////                }
////	    } catch (NameNotFoundException e) {
////	    	Log.w("UD", "name");
////	    } catch (NoSuchAlgorithmException e) {
////	    	Log.w("UD", "nosuch");
////	    }
//	    
//	    uiHelper = new UiLifecycleHelper(this, callback);
//	    uiHelper.onCreate(savedInstanceState);
//	    
//	    setContentView(R.layout.splash_main);
//
//	    FragmentManager fm = getSupportFragmentManager();
//	    fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
//	    fragments[SELECTION] = fm.findFragmentById(R.id.selectionFragment);
//
//	    FragmentTransaction transaction = fm.beginTransaction();
//	    for(int i = 0; i < fragments.length; i++) {
//	        transaction.hide(fragments[i]);
//	    }
//	    transaction.commit();
//	    Log.w("UD", "all hid");
//	    
//	    
//	}
//	
//	@Override
//	public void onResume() {
//	    super.onResume();
//	    isResumed = true;
//	}
//
//	@Override
//	public void onPause() {
//	    super.onPause();
//	    isResumed = false;
//	}
//	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    super.onActivityResult(requestCode, resultCode, data);
//	    uiHelper.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	public void onDestroy() {
//	    super.onDestroy();
//	    uiHelper.onDestroy();
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//	    super.onSaveInstanceState(outState);
//	    uiHelper.onSaveInstanceState(outState);
//	}
//	
//	private void showFragment(int fragmentIndex, boolean addToBackStack) {
//	    FragmentManager fm = getSupportFragmentManager();
//	    FragmentTransaction transaction = fm.beginTransaction();
//	    for (int i = 0; i < fragments.length; i++) {
//	        if (i == fragmentIndex) {
//	            transaction.show(fragments[i]);
//	        } else {
//	            transaction.hide(fragments[i]);
//	        }
//	    }
//	    if (addToBackStack) {
//	        transaction.addToBackStack(null);
//	    }
//	    transaction.commit();
//	}
//	
//	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
//		Log.w("UD", "sessions");
////		if (state.isOpened()) {
////	        Log.w("UD", "Logged in...");
////	        Intent intent = new Intent(this, MainActivity.class);
////		    startActivity(intent);
////		    finish();
////	    } else if (state.isClosed()) {
////	        Log.w("UD", "Logged out...");
////	    }
//	    // Only make changes if the activity is visible
//	    if (isResumed) {
//	        FragmentManager manager = getSupportFragmentManager();
//	        // Get the number of entries in the back stack
//	        int backStackSize = manager.getBackStackEntryCount();
//	        // Clear the back stack
//	        for (int i = 0; i < backStackSize; i++) {
//	            manager.popBackStack();
//	        }
//	        if (state.isOpened()) {
//	            // If the session state is open:
//	            // Show the authenticated fragment
//	            showFragment(SPLASH, false);
////	        	Intent intent = new Intent(this, MainActivity.class);
////			    startActivity(intent);
////			    finish();
//	        } else if (state.isClosed()) {
//	            // If the session state is closed:
//	            // Show the login fragment
//	            showFragment(SELECTION, false);
//	        }
//	    }
//	}
//	
//	@Override
//	protected void onResumeFragments() {
//		Log.w("UD", "resume");
//	    super.onResumeFragments();
//	    Session session = Session.getActiveSession();
//	    if (session != null && session.isOpened()) {
//	        // if the session is already open,
//	        // try to show the selection fragment
//	        showFragment(SELECTION, false);
//	    } else {
//	        // otherwise present the splash screen
//	        // and ask the person to login.
//	        showFragment(SPLASH, false);
//	    }
//	}
//}
