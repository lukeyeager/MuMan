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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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

	public static final int DIALOG_PAUSE = 1;
	public static final int DIALOG_WIN = 2;
	public static final int DIALOG_LOSE = 3;
	
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

	@Override
	protected Dialog onCreateDialog(int id) {
		
		switch (id) {
		case DIALOG_WIN:
			return createDialogWin();
		case DIALOG_LOSE:
			return createDialogLose();
		case DIALOG_PAUSE:
			return createDialogPause();
		default:
			return null;
		}
	}
	
	/**
	 * Creates a Win dialog
	 * @return
	 */
	private Dialog createDialogWin() {
		AlertDialog.Builder builder;
		
		builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to win?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                GameActivity.this.finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		return builder.create();
	}
			
	/**
	 * Creates a Lose dialog
	 * @return
	 */
	private Dialog createDialogLose() {
		AlertDialog.Builder builder;
		builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to win?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                GameActivity.this.finish();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		return builder.create();
	}
	
	/**
	 * Creates a Pause dialog
	 * @return
	 */
	private Dialog createDialogPause() {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_game_pause);
		dialog.setTitle("Game Paused");
		dialog.setOnCancelListener(
				new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						dialog.dismiss();
						GameActivity.this.finish();
					}
					
				});
		return dialog;
	}

	@Override
	public void onBackPressed() {
		showDialog(DIALOG_PAUSE);
	}
}
