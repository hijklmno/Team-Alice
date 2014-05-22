package cse110.TeamNom.projectnom;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends ListActivity{
	enum Activities {
		//HELLO("Yelp Search Button", Hello.class),
		SEARCH("Yelp Search 2", SearchBarActivity.class);

		public final String name;
		public final Class<? extends Activity> activity;

		private Activities(String name, Class<? extends Activity> activity) {
			this.name = name;
			this.activity = activity;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	private final Activities[] activities = Activities.values();

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getListView().setAdapter(new ArrayAdapter<Activities>(this, android.R.layout.simple_list_item_1, activities));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Class<? extends Activity> activityClass = activities[position].activity;
		startActivity(new Intent(this, activityClass));
	}
}
