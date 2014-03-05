package com.nnz.photomapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomMarker extends Activity {

	private MainMapFragment mapFragment;
	private HashMap<Marker, EventInfo> eventMarkerMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_marker);
		mapFragment = new MainMapFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.map, mapFragment);
		ft.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setUpEventSpots();
	}

	private HashMap<Marker, EventInfo> getInfo() {
		HashMap<Marker, EventInfo> customMarker = new HashMap<Marker, EventInfo>();
		PhotoItem[] photoItems = PhotoHelper.PhotoRead();
		for (PhotoItem photoItem : photoItems) {
			String titleLocation=photoItem.get("location");
			String titleCaption = photoItem.get("caption");
			String imageUrl = "http://137.132.247.137/images/thumb-"
					+ photoItem.get("file");
			Bitmap bitMap = PhotoHelper.getRemoteImage(imageUrl);
			List<Address> foundGeocode = null;
			/*
			 * find the addresses by using getFromLocationName() method with the
			 * given address
			 */
			try {
				foundGeocode = new Geocoder(this).getFromLocationName(
						photoItem.get("location"), 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!foundGeocode.isEmpty()) {
				foundGeocode.get(0).getLatitude(); // getting latitude
				foundGeocode.get(0).getLongitude();// getting longitude
				LatLng location = new LatLng(foundGeocode.get(0).getLatitude(),
						foundGeocode.get(0).getLongitude());
				// Marker sgm = map.addMarker(new MarkerOptions()
				// .position(location).title(titleCaption)
				// .icon(BitmapDescriptorFactory.fromBitmap(bitMap)));
				EventInfo e=new EventInfo(location,titleCaption,titleLocation,bitMap);
				Marker m=mapFragment.placeMarker(e);
				customMarker.put(m, e);
			}
		}
		return customMarker;
	}

	private void setUpEventSpots() {
	
		eventMarkerMap=getInfo();
		// add the onClickInfoWindowListener
		mapFragment.getMap().setOnInfoWindowClickListener(
				new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						EventInfo eventInfo = eventMarkerMap.get(marker);
						// Toast.makeText(getBaseContext(),
						// "The date of " + eventInfo.getName() + " is " +
						// eventInfo.getSomeDate().toLocaleString(),
						// Toast.LENGTH_LONG).show();

					}
				});
		mapFragment.getMap().setMyLocationEnabled(true);

		mapFragment.getMap().setInfoWindowAdapter(new InfoWindowAdapter() {

			private final View window = getLayoutInflater().inflate(
					R.layout.content, null);

			@Override
			public View getInfoWindow(Marker marker) {
				EventInfo eventInfo = eventMarkerMap.get(marker);

				String title = marker.getTitle();
				TextView txtTitle = ((TextView) window
						.findViewById(R.id.txtInfoWindowTitle));
				if (title != null) {
					// Spannable string allows us to edit the formatting of the
					// text.
					SpannableString titleText = new SpannableString(title);
					titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
							titleText.length(), 0);
					txtTitle.setText(titleText);
				} else {
					txtTitle.setText("");
				}

				TextView txtType = ((TextView) window
						.findViewById(R.id.txtInfoWindowEventType));
				if (eventInfo.getLocation() != null)
					txtType.setText(eventInfo.getLocation());

				ImageView imageView = ((ImageView) window
						.findViewById(R.id.ivInfoWindowMain));
				imageView.setImageBitmap(eventInfo.getFile());

				return window;
			}

			@Override
			public View getInfoContents(Marker marker) {
				// this method is not called if getInfoWindow(Marker) does not
				// return null
				return null;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_marker, menu);
		return true;
	}

}
