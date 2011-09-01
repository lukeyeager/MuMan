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

import com.muman.android.views.GameView;

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
		super.onCreate(savedInstanceState);
		
		System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");
		//int flags = WindowManager.LayoutParams.FLAG_FULLSCREEN; // | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        //getWindow().setFlags(flags, flags);
		
		setContentView(R.layout.game);

		// Try loading levelId from savedInstanceState
		String levelPath = (savedInstanceState == null) ? null : savedInstanceState.getString(LEVEL);
		if (levelPath == null) {
			// Otherwise, try loading levelId from the Intent
			Bundle extras = getIntent().getExtras();
			levelPath = (extras != null) ? extras.getString(LEVEL) : null;
		}
		if (levelPath == null) {
			setResult(-1);
			finish();
		} else {
			mGameView = (GameView) findViewById(R.id.gameview);
			mGameView.loadChildren(this);
			mGameView.loadLevel(levelPath);
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
	
	public void createPopup(int type) {
		Intent i = new Intent(this, PopupActivity.class);
		i.putExtra(PopupActivity.TYPE, type);
		startActivityForResult(i, 0);
	}

	@Override
	public void onBackPressed() {
		
		setResult(-1);
		finish();
		if (true)
			return;
		
		if (mGameView != null) {
			Intent i = new Intent(this, PopupActivity.class);
			i.putExtra(PopupActivity.TYPE, PopupActivity.REQUEST_PAUSE);
			startActivityForResult(i, PopupActivity.REQUEST_PAUSE);
		} else { 
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case PopupActivity.REQUEST_PAUSE:
		case PopupActivity.REQUEST_WIN:
		case PopupActivity.REQUEST_LOSE:
		default:
			break;
		}
		
		switch (resultCode) {
		case PopupActivity.RESULT_EXIT:
			setResult(0);
			finish();
			break;
		case PopupActivity.RESULT_RELOAD:
			mGameView.reloadLevel();
			break;
		case PopupActivity.RESULT_ERROR:
		default:
			setResult(-1);
			finish();
			break;
		}
		
	}
	
}
