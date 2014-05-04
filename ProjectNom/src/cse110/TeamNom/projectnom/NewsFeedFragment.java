package cse110.TeamNom.projectnom;

import com.parse.ParseObject;
import com.parse.ParseUser;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;



public class NewsFeedFragment extends Fragment implements OnClickListener{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
  
//        Log.w("UD: ", "test");
        final Button NOM = (Button) rootView.findViewById(R.id.NOM);    
	     final Button Mmm = (Button) rootView.findViewById(R.id.Mmm); 
	     NOM.setOnClickListener(this);
	     Mmm.setOnClickListener(this);
//	     Log.w("UD: ", "after");
        return rootView;
    }

		public void onClick(View v) {
			ParseObject testObject = new ParseObject("TestObject");
			switch (v.getId()){
			case R.id.NOM:
			        testObject.put("NOM", 1);
			        testObject.saveInBackground();
			    break;
			case R.id.Mmm:
			        testObject.put("Mmm", 1);
			        testObject.saveInBackground(); 
				break;
			}
		}

 
}



