package com.nnz.photomapper;

import com.nnz.photomapper.R;
import com.nnz.photomapper.R.layout;
import com.nnz.photomapper.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class FullImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);
		String imageName=getIntent().getStringExtra("file");
		Toast.makeText(this, imageName, Toast.LENGTH_LONG).show();
		String imageUrl="http://137.132.247.137/images/"+imageName;
		ImageView imageView=(ImageView) findViewById(R.id.fullImage);
		imageView.setImageBitmap(PhotoHelper.getRemoteImage(imageUrl));
		//Bitmap b=Bitmap.createScaledBitmap(PhotoHelper.getRemoteImage(imageUrl), 400,90, true);
		//imageView.setImageBitmap(b);
		//imageView.setScaleType(ScaleType.MATRIX);
		//Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
		//imageView.setImageBitmap(Bitmap.createScaledBitmap(PhotoHelper.getRemoteImage(imageUrl), 120, 120, false));
		//Bitmap bm=getResizedBitmap(PhotoHelper.getRemoteImage(imageUrl),120,120);
		//imageView.setImageBitmap(bm);
		
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_image, menu);
		return true;
	}

}
