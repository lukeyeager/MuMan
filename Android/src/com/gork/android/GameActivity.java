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
import android.util.DisplayMetrics;
import android.util.Log;

import com.gork.android.views.*;

/**
 * Activity for the game screen
 * 
 * @author Luke
 *
 */
public class GameActivity extends Activity {

	public static final String LEVEL = "level";
	
	private GameView mGameView;
	private GameView.State savedState = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//try {
				
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
			//int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN; // | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
	        //getWindow().setFlags(flags, flags);
			
			setContentView(R.layout.game);
	
			// Try loading levelId from savedInstanceState
			String levelId = (savedInstanceState == null) ? null : savedInstanceState.getString(LEVEL);
			if (levelId == null) {
				// Otherwise, try loading levelId from the Intent
				Bundle extras = getIntent().getExtras();
				levelId = (extras != null) ? extras.getString(LEVEL) : null;
			}
			if (levelId == null) {
				setResult(-1);
				finish();
			} else {
				mGameView = (GameView) findViewById(R.id.gameview);
				mGameView.setLevelView(findViewById(R.id.levelview));
				mGameView.setStatusBar(findViewById(R.id.statusbar_level), findViewById(R.id.statusbar_moves) );
				mGameView.loadLevel(levelId);
			}
		
		try {
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
	
    @Override
    protected void onPause() {
        // Pause the game along with the activity
        savedState = mGameView.mState;
        mGameView.mState = GameView.State.PAUSED;

        super.onPause();
    }

	@Override
	protected void onResume() {
		super.onResume();

        // Resume the game along with the activity
		if (savedState != null) {
			mGameView.mState = savedState;
		}
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
	}
}
