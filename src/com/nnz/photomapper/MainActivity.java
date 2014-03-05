package com.nnz.photomapper;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.nnz.photomapper.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	PhotoAdapter adapter;
	ListView ls;
	PhotoItem[] photoItems;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ls=(ListView) findViewById(R.id.imagelist);
		photoItems=PhotoHelper.PhotoRead();
		 adapter=new PhotoAdapter(this, photoItems);
		ls.setAdapter(adapter);
		registerForContextMenu(ls);
		ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				PhotoItem photoItem=photoItems[arg2];
				Intent intent=new Intent(getApplicationContext(),FullImageActivity.class);
				intent.putExtra("file", photoItem.get("file"));
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(item.getItemId())
		{
		case R.id.action_photo:
			intent=new Intent(this,PhotInfoActivity.class);
			startActivity(intent);
			return true;
		case R.id.CustomMapper:
			intent=new Intent(this,CustomMarker.class);
			startActivity(intent);
			return true;
		case R.id.action_photomapMenu:
			intent=new Intent(this,PhotoMapActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//Intent intent;
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	            .getMenuInfo();
		switch(item.getItemId())
		{
		case R.id.contentmenu_delete:
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
			PhotoItem photoItem=photoItems[info.position];
			Toast.makeText(this, photoItem.get("id").toString(), Toast.LENGTH_LONG).show();
			Log.i("delete", photoItem.get("caption"));
			//Uri uri=Uri.parse("http://137.132.247.137/python/delete?id="+photoItem.get("id"));
			HttpGet httpGet=new HttpGet("http://137.132.247.137/python/delete?id="+photoItem.get("id"));
			HttpClient client=new DefaultHttpClient();
			try {
				client.execute(httpGet);
				//adapter.clear();
				//adapter.addAll(PhotoHelper.PhotoRead());
				photoItems=PhotoHelper.PhotoRead();
				 adapter=new PhotoAdapter(this, photoItems);
				ls.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				ls.invalidateViews();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		case R.id.contentmenu_edit:
			PhotoItem photoItemEdit=photoItems[info.position];
			Intent intent=new Intent(this,PhotoEditActivity.class);
			intent.putExtra("id", photoItemEdit.get("id").toString());
			intent.putExtra("caption", photoItemEdit.get("caption"));
			intent.putExtra("file", photoItemEdit.get("file"));
			intent.putExtra("location", photoItemEdit.get("location"));
			startActivity(intent); 
			return true;
		}
		return super.onContextItemSelected(item);
	
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		photoItems=PhotoHelper.PhotoRead();
		 adapter=new PhotoAdapter(this, photoItems);
		ls.setAdapter(adapter);
		super.onResume();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.content_menu, menu);
	}

	
	
	

}
