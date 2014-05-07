package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Splash extends Activity {
	private Button button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_main);
		addListenerOnButton();
		
		
	}

	public void addListenerOnButton() {
		final Context context = this;
		button = (Button) findViewById(R.id.button1);
 
		button.setOnClickListener(new OnClickListener() {
 
			public void onClick(View arg0) {
			    Intent intent = new Intent(context, MainActivity.class);
			    startActivity(intent);
			    finish();
			}
 
		});
	}
}
