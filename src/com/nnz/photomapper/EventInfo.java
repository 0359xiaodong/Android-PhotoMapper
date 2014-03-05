package com.nnz.photomapper;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class EventInfo {
	
	private LatLng latlng;
	private String caption;
	private String location;
	private Bitmap file;
	
	
	public EventInfo(LatLng latlng, String caption, String location, Bitmap file) {
		super();
		this.latlng = latlng;
		this.caption = caption;
		this.location = location;
		this.file = file;
	}
	public LatLng getLatlng() {
		return latlng;
	}
	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Bitmap getFile() {
		return file;
	}
	public void setFile(Bitmap file) {
		this.file = file;
	}
	
}
