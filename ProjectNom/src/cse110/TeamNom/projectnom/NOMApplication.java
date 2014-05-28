package cse110.TeamNom.projectnom;

import com.parse.Parse;

import android.app.Application;
import android.content.res.Configuration;


public class NOMApplication extends Application {

	static String facebookID = null;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "k6xrLx1ka30TdyjSmZZRF2XVkyrvEJJq38YtZbKW", "KTchPGVBZhFSaCOetY7XbBWyaQN262o2T04b60RC");
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	public void setfacebookID(String newID) {
		facebookID = newID;
	}
	
	public String getfacebookID() {
		return facebookID;
	}
}
