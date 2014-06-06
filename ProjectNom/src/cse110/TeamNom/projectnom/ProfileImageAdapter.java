package cse110.TeamNom.projectnom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * ProfileImageAdapter is a class that creates an adapter for grid view 
 * layouts. It extends BaseAdapter and basically creates an array of
 * drawable images that get displayed in the gridView
 * @author Jean Park
 *
 */
public class ProfileImageAdapter extends BaseAdapter {
	
	// initialize private variables
	private Context mContext;
	private Drawable[] mThumbIds;
	
	/**
     *  Basic Constructor that sets the local variables to
     *  the parameters
	 * @param c = context of the app/file
	 * @param d= drawable array that holds the images
	 */
    public ProfileImageAdapter(Context c, Drawable[] d){
        mContext = c;
        mThumbIds = d;
    }
 
    /**
     * returns the length of the image array variable mThumbIds
     * returns 0 if array is null
     */
    @Override
    public int getCount() {
    	if (mThumbIds != null) {
    		return mThumbIds.length;
    	}
        return 0;
    }
 
    /**
     * returns the current item or image in the array
     */
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }
 
    /**
     * returns position in array
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    /**
     * returns the mThumbIds or image/drawable array scaled and in context.
     * suppressLint is there because this view is only supported on newer 
     * android software versions
     */
    @SuppressLint("NewApi")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageDrawable(mThumbIds[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;

    }

}
