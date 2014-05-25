package cse110.TeamNom.projectnom;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ProfileFragment extends Fragment {
	private Button buttonLogout;
	private Button buttonFacebook;
	private TextView textbox;
	
	private ProfilePictureView profilePictureView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
    	textbox = (TextView)rootView.findViewById(R.id.facebookDebugBox);
    	
//    	ImageView img = (ImageView) rootView.findViewById(R.id.imageViewProfile);
//    	try {
//    	        URL url = new URL("http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg");
//    	        //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
//    	        HttpGet httpRequest = null;
//
//    	        httpRequest = new HttpGet(url.toURI());
//
//    	        HttpClient httpclient = new DefaultHttpClient();
//    	        HttpResponse response = (HttpResponse) httpclient
//    	                .execute(httpRequest);
//
//    	        HttpEntity entity = response.getEntity();
//    	        BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
//    	        InputStream input = b_entity.getContent();
//
//    	        Bitmap bitmap = BitmapFactory.decodeStream(input);
//
//    	        img.setImageBitmap(bitmap);
//
//    	    } catch (Exception ex) {
//
//    	    }
    	
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
		    "/me",
		    null,
		    HttpMethod.GET,
		    new Request.Callback() {
		        public void onCompleted(Response response) {
		            /* handle the result */
		        	String incomingText = response.getRawResponse();
		        	textbox.setText(incomingText);
		        	
		        	try {
						JSONObject json = new JSONObject(incomingText);
						//id working
						final String id = (String) json.get("id");
						final String name = (String) json.get("name");
						
						//testing to see if is a user already
						ParseQuery<ParseObject> query = ParseQuery.getQuery("FacebookAccounts");
						query.whereEqualTo("facebook_id", id);
						query.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> objects, ParseException e) {
								Log.d("FacebookFriendQuery", "done");
								textbox.setText(objects.toString());
								if (objects.isEmpty()) {
									ParseObject testObject = new ParseObject("FacebookAccounts");
									testObject.put("facebook_id", id);
									testObject.put("Name", name);
									testObject.saveInBackground();
									Log.d("FacebookFriendQuery", "new user");
								}
							}
						});
						
						
						textbox.setText(name);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
		        }
		    }
		).executeAsync();
	}
}



