package cse110.TeamNom.projectnom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectionFragment extends Fragment {
	private static final String TAG = "SelectionFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.splash_selection, 
	            container, false);
	    
	    Log.w("UD", "selection");
//	    Intent intent = new Intent();
//        intent.setClass(getActivity(), MainActivity.class);
//        getActivity().startActivity(intent);
	    return view;
	}
}
