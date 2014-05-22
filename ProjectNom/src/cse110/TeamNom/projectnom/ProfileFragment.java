package cse110.TeamNom.projectnom;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProfileFragment extends Fragment {
	private Button buttonLogout;
	private Button buttonFacebook;
	private TextView textbox;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
    	textbox = (TextView)rootView.findViewById(R.id.facebookDebugBox);
    	
        //start configuration for the logout button
        buttonLogout = (Button)rootView.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) { onClickLogout(); }
        });
        
        buttonFacebook = (Button)rootView.findViewById(R.id.facebookTest);
        buttonFacebook.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) { onClickFacebookDebug(); }
        });
        
        return rootView;
    }
	
	//called when pressing the logout button
	private void onClickLogout() {
        Session session = Session.getActiveSession();
        session.closeAndClearTokenInformation(); //end session and go 
        //create splash page
        Intent intent = new Intent(getActivity(), SplashMain.class);
        intent.putExtra("logoutCall", "logout");
        startActivity(intent);
        getActivity().finish();
    }
	
	private void onClickFacebookDebug() {
		Session session = Session.getActiveSession();
		/* make the API call */
		new Request(
		    session,
		    "/me/friends",
		    null,
		    HttpMethod.GET,
		    new Request.Callback() {
		        public void onCompleted(Response response) {
		            /* handle the result */
		        	textbox.setText(response.getRawResponse());
		        }
		    }
		).executeAsync();
	}
}



