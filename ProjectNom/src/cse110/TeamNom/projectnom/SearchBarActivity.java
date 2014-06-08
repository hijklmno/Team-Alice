package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/*
 * Class activity that handles term and location
 * search through user input.
 */
public class SearchBarActivity extends Activity {
	private EditText mSearchTerm; // Term that user input
	private EditText mSearchLocation; // Location that user input

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_searchlayout);
		setTitle("Search");
		mSearchTerm = (EditText)findViewById(R.id.searchTerm);
		mSearchLocation = (EditText)findViewById(R.id.searchLocation);
	}
}
