package com.nnz.photomapper;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapFragment extends MapFragment {

	public Marker placeMarker(EventInfo eventInfo) {
		Marker m = getMap().addMarker(
				new MarkerOptions()
						.position(eventInfo.getLatlng())
						.title(eventInfo.getCaption())
						.snippet(eventInfo.getLocation()));

		return m;
	}
}
