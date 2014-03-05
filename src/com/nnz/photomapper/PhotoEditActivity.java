package com.nnz.photomapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoEditActivity extends Activity {
	
	TextView captionEdit;
	ImageView imageEdit;
	TextView locationEdit;
	int photoId;
	String captionPhoto;
	String locationPhoto;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_edit);
		photoId=Integer.parseInt(getIntent().getStringExtra("id"));
		captionEdit=(TextView)findViewById(R.id.imageCaptionEdit);
		locationEdit=(TextView)findViewById(R.id.imageLocationEdit);
		imageEdit=(ImageView) findViewById(R.id.imagephotoEdit);
		captionEdit.setText(getIntent().getStringExtra("caption"));
		locationEdit.setText(getIntent().getStringExtra("location"));
		String imageUrl="http://137.132.247.137/images/"+getIntent().getStringExtra("file");
		imageEdit.setImageBitmap(PhotoHelper.getRemoteImage(imageUrl));
	}
	public void onUpdatePhoto(View v)
	{
		captionPhoto=captionEdit.getText().toString();
		locationPhoto=locationEdit.getText().toString();
		update(photoId,captionPhoto,locationPhoto);
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_edit, menu);
		return true;
	}
	void update(int id, String caption, String location) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		   List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		   qparams.add(new BasicNameValuePair("id", String.valueOf(id)));
		   qparams.add(new BasicNameValuePair("caption", caption));
		   qparams.add(new BasicNameValuePair("location", location));
		   URI uri=null;
		try {
			uri = URIUtils.createURI("http", "137.132.247.137", -1, "/python/update", 
			      URLEncodedUtils.format(qparams, "UTF-8"), null);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		   HttpGet httpget = new HttpGet(uri);
		   HttpResponse response;
		try {
			response = new DefaultHttpClient().execute(httpget);
			InputStream inp = response.getEntity().getContent();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		}

}
