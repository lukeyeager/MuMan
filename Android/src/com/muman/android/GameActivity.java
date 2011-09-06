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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

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
	
	public MediaPlayer mediaPlayer = null;

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
		
		mediaPlayer = MediaPlayer.create(this, R.raw.music_game);
		mediaPlayer.start();
		mediaPlayer.setLooping(true);
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
	protected void onStop() {
		super.onStop();
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.game_dialog);
		
		switch (id) {
		case DIALOG_WIN:
			return createDialogWin(dialog);
		case DIALOG_LOSE:
			return createDialogLose(dialog);
		case DIALOG_PAUSE:
			return createDialogPause(dialog);
		default:
			return null;
		}
	}
	
	/**
	 * Creates a Win dialog
	 * @param dialog 
	 * @return
	 */
	private Dialog createDialogWin(Dialog dialog) {
		dialog.setTitle("You win!");
		
		// No text needs to be shown
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		if (text != null) {
			text.setVisibility(View.VISIBLE);
			text.setText("You must be the best person ever.");
		}
		
		ImageView reload = (ImageView) dialog.findViewById(R.id.dialog_reload);
		if (reload != null) {
			reload.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							GameActivity.this.dismissDialog(DIALOG_WIN);
							GameActivity.this.mGameView.reloadLevel();
						}
					});
		}
		
		ImageView menu = (ImageView) dialog.findViewById(R.id.dialog_menu);
		if (menu != null) {
			menu.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							GameActivity.this.finish();
						}
					});
		}
		
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
			
	/**
	 * Creates a Lose dialog
	 * @param dialog 
	 * @return
	 */
	private Dialog createDialogLose(Dialog dialog) {
		dialog.setTitle("You Lost!");
		
		// No text needs to be shown
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		if (text != null) {
			text.setVisibility(View.VISIBLE);
			text.setText("You must be the worst person ever.");
		}
		
		ImageView reload = (ImageView) dialog.findViewById(R.id.dialog_reload);
		if (reload != null) {
			reload.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							GameActivity.this.dismissDialog(DIALOG_LOSE);
							GameActivity.this.mGameView.reloadLevel();
						}
					});
		}
		
		ImageView menu = (ImageView) dialog.findViewById(R.id.dialog_menu);
		if (menu != null) {
			menu.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							GameActivity.this.finish();
						}
					});
		}
		
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
	
	/**
	 * Creates a Pause dialog
	 * @param dialog 
	 * @return
	 */
	private Dialog createDialogPause(Dialog dialog) {
		dialog.setTitle("Game Paused");
		
		// No text needs to be shown
		TextView text = (TextView) dialog.findViewById(R.id.dialog_text);
		if (text != null) {
			text.setVisibility(View.INVISIBLE);
		}
		
		ImageView reload = (ImageView) dialog.findViewById(R.id.dialog_reload);
		if (reload != null) {
			reload.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							GameActivity.this.dismissDialog(DIALOG_PAUSE);
							GameActivity.this.mGameView.reloadLevel();
						}
					});
		}
		
		ImageView menu = (ImageView) dialog.findViewById(R.id.dialog_menu);
		if (menu != null) {
			menu.setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							GameActivity.this.finish();
						}
					});
		}
		
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
