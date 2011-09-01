package com.muman.android;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LevelPackSelectActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.level_pack_select);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, getListEntries()));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String[] entries = getListEntries();
		Intent i = new Intent(this, LevelSelectActivity.class);
		i.putExtra(LevelSelectActivity.PATH, entries[position]);
		startActivity(i);
	}

	private String[] getListEntries() {
		AssetManager assets = getApplicationContext().getAssets();
		String[] campaignFolders = null;
		boolean userLevelsExist = false;
		try {
			campaignFolders = assets.list("Levels/Campaign");
			
		} catch (IOException e) {
		}
		
		try {
			String[] userLevels = assets.list("Levels/User");
			if (userLevels.length > 0) {
				userLevelsExist = true;
			}
		} catch (IOException e) {
		}
		
		String[] entries = new String[campaignFolders.length + (userLevelsExist ? 1 : 0)];
		for (int i=0; i<campaignFolders.length; i++) {
			entries[i] = "Campaign/" + campaignFolders[i];
		}
		if (userLevelsExist) {
			entries[campaignFolders.length - 1] = "User Levels";
		}
		
		return entries;
	}
	
	
}
