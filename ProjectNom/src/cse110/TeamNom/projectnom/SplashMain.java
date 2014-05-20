package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

/**
 * The SplashMain class stores the implementation of the splash login screen.
 * 
 */
public class SplashMain extends Activity {
	private static final String URL_PREFIX_FRIENDS = "https://graph.facebook.com/me/friends?access_token=";
	private static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add("user_friends");
            add("public_profile");
        }
    };
    
    private TextView textInstructionsOrLink;
    private Button buttonLoginLogout;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();

    /**
     * Called when activity is created. Checks if Facebook session is already active. If
     * not, it'll create a new session and open the session when the user logs in.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        buttonLoginLogout = (Button)findViewById(R.id.buttonLoginLogout);
        textInstructionsOrLink = (TextView)findViewById(R.id.instructionsOrLink);

        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        // Checks for logout call from MainActivity
        Bundle passedData = getIntent().getExtras();
        if (passedData != null && passedData.getString("logoutCall") == "logout") {
        	onClickLogout();
        }
        
        // Checks for sessions
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
                Log.d("FacebookSessionSplash", "restored session");
            }
            if (session == null) {
                session = new Session(this);
                Log.d("FacebookSessionSplash", "created new session");
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
                
                System.out.println("CREATED_TOKEN_LOADED");
            }
            Log.d("FacebookSessionIfNull", "nulled!");
            
        }

        updateView();
    }

    // Called when activity is started
    @Override
    public void onStart() {
        super.onStart();
        Bundle passedData = getIntent().getExtras();
        
        if (passedData != null && passedData.getString("logoutCall") == "logout") {
        	onClickLogout();
        }
        
        Session.getActiveSession().addCallback(statusCallback);
    }

    // Called when activity is resumed
    public void onResume() {
    	super.onResume();
    	Bundle passedData = getIntent().getExtras();
    	
        if (passedData != null && passedData.getString("logoutCall") == "logout") {
        	onClickLogout();
        }
        
        Session.getActiveSession().addCallback(statusCallback);
    }
    
    // Called when activity is stopped
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

    // Saves session state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    // View with a login/logout button depending on session.
    private void updateView() {
        Session session = Session.getActiveSession();
        
        // Checks if session is opened but this state should not happen
        if (session != null && session.isOpened()) {
            textInstructionsOrLink.setText(URL_PREFIX_FRIENDS + session.getAccessToken());
            buttonLoginLogout.setText("Logout...but we shouldn't see this");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogout(); }
            });
            
            gotoMainAndEnd();
        } else {
        	
        	// Login button
            textInstructionsOrLink.setText("Logged out...press dat");
            buttonLoginLogout.setText("Login nao");
            buttonLoginLogout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) { onClickLogin(); }
            });
        }
    }

    /**
     * onClickLogin() is called when the login button is pressed. If user already
     * has a session then user will be brought straight to home page. Otherwise
     * a Facebook login screen will pop up.
     */
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        Context context = this.getApplicationContext();
            
        if (session == null || session.isClosed()) {
        	Session.setActiveSession(session);
//            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback)); 
        	Session.openActiveSession(this, true, PERMISSIONS, statusCallback);
            Toast.makeText(context, "Loading Facebook. Please wait...", 3).show();
        } else {
            Session.openActiveSession(this, true, PERMISSIONS, statusCallback);
        }
    }

    // Logs out user by closing session
    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
        }
    }
    
    // Sends user to home page
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