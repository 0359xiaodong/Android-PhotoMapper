package com.nnz.photomapper;

import java.util.List;

import com.nnz.photomapper.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class PhotoAdapter extends ArrayAdapter<PhotoItem>{

	private final Activity context;
	private final PhotoItem[] photoItem;
	public PhotoAdapter(Activity context,PhotoItem[] photoItem)
	{
		super(context,R.layout.list_row,photoItem);
		this.context=context;
		this.photoItem=photoItem;
		Log.i("in adapter", "i am here adapter");
	}
	
	@Override
	public View getView(int postion,View connvertView,ViewGroup parent)
	{
		LayoutInflater inflater=context.getLayoutInflater();
		View rowView=inflater.inflate(R.layout.list_row	,null);
		TextView title=(TextView) rowView.findViewById(R.id.title);
		TextView location=(TextView)rowView.findViewById(R.id.location);
		title.setText(photoItem[postion].get("caption"));
		location.setText(photoItem[postion].get("location"));
		String imageUrl="http://137.132.247.137/images/thumb-"+photoItem[postion].get("file");
		ImageView imageView=(ImageView) rowView.findViewById(R.id.list_image);
		imageView.setImageBitmap(PhotoHelper.getRemoteImage(imageUrl));
		Log.i("url", imageUrl);
		return rowView;
	}

}
