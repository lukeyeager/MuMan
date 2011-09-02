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
import android.util.DisplayMetrics;
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

		TextView startText = (TextView) findViewById(R.id.mainmenu_start);
		if (startText != null) {
			startText.setOnClickListener(
					new OnClickListener() {
	
						@Override
						public void onClick(View arg0) {
							Intent i = new Intent(MainMenu.this, LevelPackSelectActivity.class);
							startActivity(i);
						}
					}
				);
		}

		TextView metrics = (TextView) findViewById(R.id.mainmenu_metrics);
		if (metrics != null) {
			metrics.setOnClickListener(
					new OnClickListener() {
	
						@Override
						public void onClick(View arg0) {
							DisplayMetrics metrics = MainMenu.this.getResources().getDisplayMetrics();
							String text = metrics.widthPixels + "x" + metrics.heightPixels + ", DPI: " + metrics.densityDpi;
							Toast.makeText(MainMenu.this, text, Toast.LENGTH_SHORT).show();
						}
						
					}
			);
		}
		
		TextView exitText = (TextView) findViewById(R.id.mainmenu_exit);
		if (exitText != null) {
			exitText.setOnClickListener(
					new OnClickListener() {
	
						@Override
						public void onClick(View arg0) {
							MainMenu.this.finish();
						}
						
					}
			);
		}
		
	}

}