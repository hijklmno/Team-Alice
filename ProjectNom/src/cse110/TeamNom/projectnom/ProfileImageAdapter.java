package cse110.TeamNom.projectnom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ProfileImageAdapter extends BaseAdapter {
	
	private Context mContext;
	 
	private Drawable[] mThumbIds;
	
    // Constructor
    public ProfileImageAdapter(Context c, Drawable[] d){
        mContext = c;
        mThumbIds = d;
    }
 
    @Override
    public int getCount() {
    	if (mThumbIds != null) {
    		return mThumbIds.length;
    	}
        return 0;
    }
 
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @SuppressLint("NewApi")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        //imageView.setImageResource(mThumbIds[position]);
        //imageView.setBackground(mThumbIds[position]);
        imageView.setImageDrawable(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
        return imageView;

    }

}
