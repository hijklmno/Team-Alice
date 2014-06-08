package cse110.TeamNom.projectnom;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

/*
 * GPS class activity that handles setting up the location
 * fields of a restaurant.
 */
public class GPSActivity extends Activity {
	TextView textview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);

		// Check if GPS enabled
		GPSFragment gpsTracker = new GPSFragment(this);

		if (gpsTracker.canGetLocation())
		{
			// Set latitude field to a string
			String stringLatitude = String.valueOf(gpsTracker.latitude);
			textview = (TextView)findViewById(R.id.fieldLatitude);
			textview.setText(stringLatitude);
			// Set longitude field to a string
			String stringLongitude = String.valueOf(gpsTracker.longitude);
			textview = (TextView)findViewById(R.id.fieldLongitude);
			textview.setText(stringLongitude);
			// Set country field to a string
			String country = gpsTracker.getCountryName(this);
			textview = (TextView)findViewById(R.id.fieldCountry);
			textview.setText(country);
			// Set locality field to a string
			String city = gpsTracker.getLocality(this);
			textview = (TextView)findViewById(R.id.fieldCity);
			textview.setText(city);
			// Set postal code to a string
			String postalCode = gpsTracker.getPostalCode(this);
			textview = (TextView)findViewById(R.id.fieldPostalCode);
			textview.setText(postalCode);
			// Set addressline to a string
			String addressLine = gpsTracker.getAddressLine(this);
			textview = (TextView)findViewById(R.id.fieldAddressLine);
			textview.setText(addressLine);
		}
		else
		{
			// can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
			gpsTracker.showSettingsAlert();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gps, menu);
		return true;
	}
}
