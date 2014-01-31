package com.example.chathead;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ListView mVidList;
	ArrayList<String> itemList;
	Intent serviceIntent;
	ComponentName service;
	ArrayList<String> urlList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mVidList = (ListView) findViewById(R.id.videoList);
		itemList = new ArrayList<String>();
		urlList = new ArrayList<String>();
		populateUrlList();
		populateVideoList();
		serviceIntent = new Intent(getApplicationContext(), ChatHeadService.class);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
			    android.R.layout.simple_list_item_1, itemList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			    View view = super.getView(position, convertView, parent);
			    TextView text = (TextView) view.findViewById(android.R.id.text1);
			    
			    text.setTextColor(Color.BLACK);
			  
			    return view;
			  }
			};
		
		mVidList.setAdapter(adapter);
		
		mVidList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
					stopService(serviceIntent);
					serviceIntent.putExtra("url", urlList.get(position));
					Toast.makeText(getApplicationContext(), ""+urlList.get(position), 500).show();
					service = startService(serviceIntent);
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
	public void populateUrlList(){
		urlList.add("rtsp://46.249.213.87:554/playlists/9xm_hvga.hpl.3gp");
		urlList.add(" rtsp://46.249.213.87:554/playlists/imtv_hvga.hpl.3gp");
		urlList.add("rtsp://46.249.213.87:554/playlists/guitarist_hvga.hpl.3gp");
		urlList.add("rtsp://46.249.213.87:554/playlists/clubbing-tv_hvga.hpl.3gp");
		urlList.add("rtsp://46.249.213.87:554/playlists/4musictv_hvga.hpl.3gp");
		urlList.add("rtsp://46.249.213.87:554/playlists/ftv_hvga.hpl.3gp");
		urlList.add("rtsp://46.249.213.87:554/playlists/aajtak_hvga.hpl.3gp");
		urlList.add("rtsp://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");
	}

	public void populateVideoList(){

		itemList.add("9XM");
		itemList.add("MTV");
		itemList.add("Guitarist TV");
		itemList.add("Clubbing TV");
		itemList.add("4 Music TV");
		itemList.add("Fashion TV");
		itemList.add("AAJ TAK");
		itemList.add("Android Video");
		
	}
	
}
