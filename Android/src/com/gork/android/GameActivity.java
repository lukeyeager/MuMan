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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.gork.android.views.GameView;

/**
 * Activity for the game screen
 * 
 * @author Luke
 *
 */
public class GameActivity extends Activity {

	public static final String LEVEL = "level";

	private GameView mGameView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
				
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.game);
	
			// Try loading levelId from savedInstanceState
			Integer levelId = (savedInstanceState == null) ? null : savedInstanceState.getInt(LEVEL);
			if (levelId == null) {
				// Otherwise, try loading levelId from the Intent
				Bundle extras = getIntent().getExtras();
				levelId = (extras != null) ? extras.getInt(LEVEL) : null;
			}
			if (levelId == null) {
				setResult(-1);
				finish();
			} else {
				mGameView = (GameView) findViewById(R.id.gameview);
				mGameView.loadLevel(levelId);
			}
		
		} catch (Exception e) {
			// this is the line of code that sends a real error message to the log
			Log.e("ERROR", "ERROR IN CODE: " + e.toString());
	 
			// this is the line that prints out the location in
			// the code where the error occurred.
			e.printStackTrace();
			
			setResult(-1);
			finish();
		}
	}

}