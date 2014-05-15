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
        buttonLogout = (Button)rootView.findViewById(R.id.buttonLogout);
        
        buttonLogout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) { onClickLogout(); }
        });
        
        return rootView;
    }
	
	private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            Intent intent = new Intent(getActivity(), SplashMain.class);
            intent.putExtra("logoutCall", "logout");
            startActivity(intent);
        }
    }
}



