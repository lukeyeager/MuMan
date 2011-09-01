package com.muman.android;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LevelSelectActivity extends ListActivity {
	
	public static final String PATH = "path";
	
	private String levelPath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.level_select);
        
        Bundle extras = getIntent().getExtras();
        levelPath = (extras != null) ? extras.getString(PATH) : null;
        if (levelPath == null) {
			setResult(-1);
			finish();
		} else {
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, getListEntries()));
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String[] entries = getListEntries();
		
		Intent i = new Intent(this, GameActivity.class);
		i.putExtra(GameActivity.LEVEL, levelPath + "/" + entries[position]);
		startActivity(i);
	}

	private String[] getListEntries() {
		AssetManager assets = getApplicationContext().getAssets();
		String[] levels = null;
		try {
			levels = assets.list("Levels/" + levelPath);
			
		} catch (IOException e) {
		}
		
		return levels;
	}
	
	
}
