package cse110.TeamNom.projectnom;

import com.facebook.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProfileFragment extends Fragment {
	private Button buttonLogout;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        
        //start configuration for the logout button
        buttonLogout = (Button)rootView.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) { onClickLogout(); }
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
}



