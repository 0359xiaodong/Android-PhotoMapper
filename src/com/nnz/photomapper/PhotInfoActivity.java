package com.nnz.photomapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nnz.photomapper.R;

public class PhotInfoActivity extends Activity {
	private String path;
	private final int SELECT_FILE = 1;
	private final int REQUEST_CAMERA = 0;
	private ImageView ivImage;
	private Button btnSetImage;
	private TextView caption;
	private TextView locationText;
	String savePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phot_info);
		ivImage = (ImageView) findViewById(R.id.imagephoto);
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		File f = new File(android.os.Environment.getExternalStorageDirectory(),
//				"temp.jpg");
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//		startActivityForResult(intent, REQUEST_CAMERA);
		locationText = (TextView) findViewById(R.id.imageLocation);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) { 
				Geocoder gc = new Geocoder(getApplicationContext());
				List<Address> loc;
				try {
					loc = gc.getFromLocation(location.getLatitude(),
							location.getLongitude(), 1);
					locationText.setText(loc.get(0).getAddressLine(0)
							.toString());
				} catch (IOException e1) { // TODO Auto-generated catch block
					
					e1.printStackTrace();
				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		
	selectImage();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phot_info, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					Bitmap bm;
					BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

					bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
							btmapOptions);

					// bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
					ivImage.setImageBitmap(bm);

					path = android.os.Environment.getExternalStorageDirectory()
							+ File.separator + "PhotoMapper";
					// f.delete();
					File createfile = new File(
							Environment.getExternalStorageDirectory()
									+ File.separator + "SAPhotoGallery");
					if (!createfile.isDirectory()) {
						createfile.mkdir();
					}
					OutputStream fOut = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					// path=file.getAbsolutePath();
					savePath = file.getAbsolutePath().toString();
					try {
						fOut = new FileOutputStream(file);
						bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
						fOut.flush();
						fOut.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();

				String tempPath = getPath(selectedImageUri, this);
				Bitmap bm;
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				bm = BitmapFactory.decodeFile(tempPath, btmapOptions);

				savePath = getPath(selectedImageUri, this);
				ivImage.setImageBitmap(bm);
			}
		}
		else if(resultCode==RESULT_CANCELED)
		{
			Intent in=new Intent(this,MainActivity.class);
			startActivity(in);
		}
		

	}

	String getLoc(String loc) {
		Geocoder gc = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gc.getFromLocationName(loc, 1);
			return (addresses.get(0).getAddressLine(0).toString());
		} catch (IOException e) {
		}
		return ("");
	}

	String myGeo() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = lm.getLastKnownLocation(myLocation());
		Geocoder gc = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gc.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);

		} catch (IOException e) {
		}
		return (addresses.get(0).getAddressLine(0).toString());
	}

	String myLocation() {
		StringBuffer sb = new StringBuffer();
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);
		// for (String provider : providers) {
		// //sb.append(provider); //sb.append(":");
		lm.requestLocationUpdates(providers.get(0), 1000, 0,
				new LocationListener() {
					public void onLocationChanged(Location location) {
					}

					public void onProviderDisabled(String provider) {
					}

					public void onProviderEnabled(String provider) {
					}

					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}
				});

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = lm.getBestProvider(criteria, true);
		Location loc = lm.getLastKnownLocation(provider);
		if (loc == null)
			sb.append("No location");
		else
			sb.append(String.format("%f,%f", loc.getLatitude(),
					loc.getLongitude()));
				return (sb.toString());
	}

	public void onUploadPhoto(View v) {

		// Log.i("geoloc", geloc());
		String loc = myLocation();
		// String loc=geloc();
		Log.i("myloc", loc);
		caption = (TextView) findViewById(R.id.imageCaption);
		locationText = (TextView) findViewById(R.id.imageLocation);
		// Toast.makeText(this, myGeo(),Toast.LENGTH_LONG).show();
		// Log.i("geo", myLocation()+myGeo());
		// Transfer.uploadFile(caption.getText().toString(),
		// location.getText().toString(), savePath);
		Transfer.uploadFile(caption.getText().toString(), locationText
				.getText().toString(), savePath);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(
							Intent.createChooser(intent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
					Intent intent=new Intent(getApplicationContext(),MainActivity.class);
					startActivity(intent);
				}
			}
		});
		builder.show();
	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//			
//	   switch (item.getItemId()) {
//	   
//	   case R.id:
//		   selectImage();
//	   return true;
//		     
//	   default:
//	      return super.onContextItemSelected(item);
//	
//	   }
//	}
}
