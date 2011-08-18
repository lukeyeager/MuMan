/*
Copyright 2011 Luke Yeager and Sam Bryan
$Id$

This file is part of Gork.

Gork is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Gork is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Gork.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
*/

package com.gork.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Activity for the main menu
 * 
 * @author Luke
 *
 */
public class MainMenu extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] entries = new String[] { "Play", "Exit" };
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				entries));
	}

	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		CharSequence entry = ((TextView) v).getText();
		
		if (entry == "Exit") {
			finish();
			
		} else if (entry == "Play") {
			super.onListItemClick(l, v, position, id);
	        Intent i = new Intent(this, GameActivity.class);
	        i.putExtra(GameActivity.LEVEL, "1_1");
	        startActivityForResult(i, 0);
		}
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//if (resultCode == -1) {
			//Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
		//}
	}
	
}