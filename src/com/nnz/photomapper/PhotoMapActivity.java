package com.nnz.photomapper;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nnz.photomapper.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PhotoMapActivity extends Activity {
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_map);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		try {
			setPhotoMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static double doubleOrNull(String s) {
		try {
			return s != null ? Double.parseDouble(s) : 0;
		} catch (Exception e) {
			return 0;
		}
	}

	private void setPhotoMap() throws IOException {
		PhotoItem[] photoItems = PhotoHelper.PhotoRead();
		for (PhotoItem photoItem : photoItems) {
			// if(photoItem.get("location")!=null)
			// {
			// String title=photoItem.get("caption");
			// String
			// imageUrl="http://137.132.247.137/images/thumb-"+photoItem.get("file");
			// Bitmap bitMap=PhotoHelper.getRemoteImage(imageUrl);
			// String[] locationString=photoItem.get("location").split(",");
			// if(locationString.length==2)
			// {
			// double latitude=doubleOrNull(locationString[0]);
			// double longitude=doubleOrNull(locationString[1]);
			// LatLng location=new LatLng(latitude, longitude);
			// Marker sgm=map.addMarker(new
			// MarkerOptions().position(location).title(title).icon(BitmapDescriptorFactory.fromBitmap(bitMap)));
			// }
			// }

			String titleCaption = photoItem.get("caption");
			String imageUrl = "http://137.132.247.137/images/thumb-"
					+ photoItem.get("file");
			
			Bitmap bitMap = PhotoHelper.getRemoteImage(imageUrl);
			List<Address> foundGeocode = null;
			/*
			 * find the addresses by using getFromLocationName() method with the
			 * given address
			 */
			foundGeocode = new Geocoder(this).getFromLocationName(
					photoItem.get("location"), 1);
			if (!foundGeocode.isEmpty()) {
				foundGeocode.get(0).getLatitude(); // getting latitude
				foundGeocode.get(0).getLongitude();// getting longitude
				LatLng location = new LatLng(foundGeocode.get(0).getLatitude(),
						foundGeocode.get(0).getLongitude());
				Marker sgm = map.addMarker(new MarkerOptions()
						.position(location).title(titleCaption)
						.icon(BitmapDescriptorFactory.fromBitmap(bitMap)).snippet(imageUrl));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_map, menu);
		return true;
	}


}
