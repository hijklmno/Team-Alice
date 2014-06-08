package cse110.TeamNom.projectnom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * Fragment that displays the search tab of the
 * application. 
 */
public class SearchFragment extends Fragment {
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_searchlayout, container, false);
         
        return rootView;
    }
}



