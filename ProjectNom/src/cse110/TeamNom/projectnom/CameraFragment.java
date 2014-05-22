package cse110.TeamNom.projectnom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraFragment extends Fragment {

	// Final variables used to avoid MAGIC strings.
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int TAKE_PHOTO = 1;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static final String ALBUM_NAME = "NOM";
	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";

	// Misc variables
	private Button picBtn;
	private ImageView mImageView;
	private Bitmap mImageBitmap;
	private EditText title;
	private EditText restaurant;
	private EditText caption;
	private Button subBut;
	private Context context;
	private Boolean uploadBool = false;

	// String variables
	String parseTitle;
	String parseRestaurant;
	String parseCaption;
	
	// The current Photo path
	private String mCurrentPhotoPath;
	private String pathtophoto;

	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

	private File getAlbumDir() {
		File storageDir = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(ALBUM_NAME);
			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	@SuppressLint("SimpleDateFormat") private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		return f;
	}

	private void setPic() {

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		/* Get the size of the ImageView */
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bitmap);
		mImageView.setVisibility(View.VISIBLE);
	}

	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	private void dispatchTakePictureIntent(int actionCode) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(actionCode == TAKE_PHOTO){
			File f = null;
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				mCurrentPhotoPath = null;
			}
		}
		startActivityForResult(takePictureIntent, actionCode);
	}

	private void handlePhoto() {
		if (mCurrentPhotoPath != null) {
			setPic();
			galleryAddPic();
			pathtophoto = mCurrentPhotoPath;
			mCurrentPhotoPath = null;
		}
	}

	Button.OnClickListener mTakePicOnClickListener =
			new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent(TAKE_PHOTO);
		}
	};

	private void resetFragment() {
		title.setVisibility(View.INVISIBLE);
		restaurant.setVisibility(View.INVISIBLE);
		caption.setVisibility(View.INVISIBLE);
		subBut.setVisibility(View.INVISIBLE);
		mImageView.setVisibility(View.INVISIBLE);
		
		//mImageView.setImageBitmap(null);
		title.setText(null);
		restaurant.setText(null);
		caption.setText(null);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Parse.initialize(getActivity(), "k6xrLx1ka30TdyjSmZZRF2XVkyrvEJJq38YtZbKW", "KTchPGVBZhFSaCOetY7XbBWyaQN262o2T04b60RC");

		View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

		picBtn = (Button) rootView.findViewById(R.id.btnCapturePicture);

		title = (EditText) rootView.findViewById(R.id.TitleID);
		restaurant = (EditText) rootView.findViewById(R.id.RestaurantTitle);
		caption = (EditText) rootView.findViewById(R.id.pictureCaption);
		subBut = (Button) rootView.findViewById(R.id.submitButton);
		

		context = getActivity().getApplicationContext();
		  
		title.setVisibility(View.INVISIBLE);
		restaurant.setVisibility(View.INVISIBLE);
		caption.setVisibility(View.INVISIBLE);
		subBut.setVisibility(View.INVISIBLE);
		System.out.println("Check point 1");
		/**
		 * Capture image button click event
		 */
		picBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				mImageView = (ImageView) getActivity().findViewById(R.id.imageView1);
				mImageBitmap = null;

				setBtnListenerOrDisable(
						picBtn,
						mTakePicOnClickListener,
						MediaStore.ACTION_IMAGE_CAPTURE
						);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
					mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
				} else {
					mAlbumStorageDirFactory = new BaseAlbumDirFactory();
				}
				dispatchTakePictureIntent(TAKE_PHOTO);
			}
		});
		subBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				mImageView = (ImageView) getActivity().findViewById(R.id.imageView1);
				mImageBitmap = null;

				
				// TODO
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setCancelable(false);
				builder.setMessage("Are you sure you want to Upload?")
				  .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   try {
	       					compressFile(pathtophoto);
	       				} catch (Exception e) {
	       					// TODO Auto-generated catch block
	       					e.printStackTrace();
	       				}
	    				/**	AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
	    					builder2.setCancelable(false);
	    					builder2.setMessage("Upload Successful")
	    					  .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	    		                   public void onClick(DialogInterface dialog, int id) {
	    		                	  
	    		                   }
	    		               });
	    					builder2.show(); */
	                	 
	                	   CharSequence text = "File Uploaded Successfully";
	                	   int duration = Toast.LENGTH_SHORT;

	                	   Toast toast = Toast.makeText(context, text, duration);
	                	   toast.show();
	                	   
	                	  resetFragment();
	    				}
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // go back
	                   }
	               });
				builder.show();
				
				
			}
		});
		System.out.println("Check point 2 big boiiii ");
		// Checking camera availability
		if (!isDeviceSupportCamera()) {
			Toast.makeText( super.getActivity().getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			super.getActivity().finish();
		}
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PHOTO && resultCode == -1) {

			title.setVisibility(View.VISIBLE);
			restaurant.setVisibility(View.VISIBLE);
			caption.setVisibility(View.VISIBLE);
			subBut.setVisibility(View.VISIBLE);
			mImageView.setVisibility(View.VISIBLE);
			
			// Work with this
			handlePhoto();
		/**	CharSequence colors[] = new CharSequence[] {"Submit","Cancel"};
			// TODO
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(false);
			builder.setTitle("Upload to NOM?");
			builder.setItems(colors, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch(which){
					case 0:{
						try {
							compressFile(pathtophoto);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
					case 1:{
						dialog.cancel();
						break;
					}
					}
				}
			});
			builder.show(); */
		}
	}

	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null) );
		super.onSaveInstanceState(outState);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
						ImageView.VISIBLE : ImageView.INVISIBLE
				);
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/* Method that compresses a file given a path. */
	private void compressFile(String path) throws Exception{
		try {
			System.out.println("inside the compress method: " + path);
			Bitmap bitmap;
			bitmap = BitmapFactory.decodeFile(path);
			if(bitmap == null)
			{
				System.out.println(path + "cannot be converted to a bitmap!");
				return;
			}
			Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
			byte[] data = bos.toByteArray();
			//uncompressFile(data);

			System.out.println("Before parsing");
			ParseObject object = new ParseObject("Food_Table_DB");
			ParseFile file = new ParseFile(data);
			file.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					// TODO Auto-generated method stub
					if (e == null) {

					}
					else {
						System.err.println("ParseException: " + e);
					}
				}
			});

			parseTitle = title.getText().toString();
			parseRestaurant = restaurant.getText().toString();
			parseCaption = caption.getText().toString();
			
			object.put("Food_photo", file);
			object.put("Restaurant_Id", parseRestaurant);
			object.put("Food_Name", parseTitle);
			object.put("Tags",parseCaption);
			object.put("Like", 100);
			object.put("Bookmark", 100);
			//object.put("Boba", "David");
			object.saveInBackground();
			System.out.println("after parsing blue balls");
		}
		catch (Exception e) {
			Log.v("CameraFragment", "Some error compressFile");
		}
		// clear the path string
		//pathtophoto = null;
	}

	// this method is for... dont trip 
	private void uncompressFile(byte array[])
	{
		System.out.println("heeloeeellelelele");
		Bitmap bmp = BitmapFactory.decodeByteArray(array, 0, array.length);
		/* Associate the Bitmap to the ImageView */
		mImageView.setImageBitmap(bmp);
		mImageView.setVisibility(View.VISIBLE);
	}

	private boolean isDeviceSupportCamera() {
		if (super.getActivity().getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	private void setBtnListenerOrDisable(
			Button btn,
			Button.OnClickListener onClickListener,
			String intentName
			) {
		if (isIntentAvailable(super.getActivity().getApplicationContext(), intentName)) {
			btn.setOnClickListener(onClickListener);
		} else {
			btn.setText(
					getText(R.string.cannot).toString() + " " + btn.getText());
			btn.setClickable(false);
		}
	}
}