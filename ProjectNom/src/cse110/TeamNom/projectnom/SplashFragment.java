package cse110.TeamNom.projectnom;

import java.util.ArrayList;
import java.util.List;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.NewPermissionsRequest;



import com.facebook.widget.LoginButton;

//import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SplashFragment extends Fragment {
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.splash_main);
//		addListenerOnButton();	
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.splash_fragment, 
	            container, false);
	    
	    configButton();
	    return view;
	}
	
	private void configButton() {
//		LoginButton NOM = (LoginButton) getActivity().findViewById(R.id.login_button); 
//		Log.w("UD", NOM.getSessionStatusCallback()); 
		Session session = Session.getActiveSession();
		if (session.isOpened()) {
			Log.w("UD", "asdf");
		}
		
	}
//	public void addListenerOnButton() {
//		final Context context = this;
//		button = (Button) findViewById(R.id.button1);
// 
//		button.setOnClickListener(new OnClickListener() {
// 
//			public void onClick(View arg0) {
//			    Intent intent = new Intent(context, MainActivity.class);
//			    startActivity(intent);
//			    finish();
//			}
// 
//		});
//	}
}
