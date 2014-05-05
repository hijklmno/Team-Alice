package cse110.TeamNom.projectnom;



import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

public class CameraFragment extends Fragment {
	
	// Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
 
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
 
    private Uri fileUri; // file url to store image/video
 
    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo;
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
         
        imgPreview = (ImageView) rootView.findViewById(R.id.imgPreview);
      //   videoPreview = (VideoView) findViewById(R.id.videoPreview);
        btnCapturePicture = (Button) rootView.findViewById(R.id.btnCapturePicture);
      //   btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
 

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
                previewCapturedImage();
            }
        });
 
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
	
	
	/**
     * Checking device has camera hardware or not
     */
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
    
	public void captureImage() {
		Intent intent = new Intent(super.getActivity(), CameraActivity.class);
		
		startActivity(intent);

	} 
	

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.GONE);
 
            imgPreview.setVisibility(View.VISIBLE);
 
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
 
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
 
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
 
            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
