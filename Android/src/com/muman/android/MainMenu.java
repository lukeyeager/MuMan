/*
Copyright 2011 Luke Yeager and Sam Bryan
$Id$

This file is part of MuMan.

MuMan is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

MuMan is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with MuMan.  If not, see <http://www.gnu.org/licenses/gpl-3.0.html>.
 */

package com.muman.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for the main menu
 * 
 * @author Luke
 * 
 */
public class MainMenu extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_menu);
		
		((TextView) findViewById(R.id.mainmenu_start)).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(MainMenu.this, LevelPackSelectActivity.class);
						//i.putExtra(GameActivity.LEVEL, "1_3");
						startActivity(i);
					}
					
				}
		);
		
		((TextView) findViewById(R.id.mainmenu_popup)).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent i = new Intent(MainMenu.this, PopupActivity.class);
						i.putExtra(PopupActivity.TYPE, PopupActivity.REQUEST_PAUSE);
						startActivityForResult(i, 0);
					}
					
				}
		);
		
		((TextView) findViewById(R.id.mainmenu_exit)).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
					}
					
				}
		);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode < 0) {
			Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
		}
	}

}