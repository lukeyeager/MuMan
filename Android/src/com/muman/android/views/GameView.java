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

package com.muman.android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.muman.android.R;

/**
 * The view for GameActivity. This is where most of the important game-time
 * program execution occurs.
 * 
 * @author Luke
 * 
 */
public class GameView extends FrameLayout {
	
	/**
	 * An Enum to store the current state of the game.
	 */
	public static enum State {
		UNINITIALIZED, RUNNING, PAUSED, GAMEOVER
	}

	public State mState = State.UNINITIALIZED;

	public LevelView mLevelView;
	private TextView mStatusBarLevel;
	private TextView mStatusBarMoves;
	private PopupView mPopupView;

	/**
	 * Create a simple handler that we can use to cause animation to happen. We
	 * set ourselves as a target and we can use the sleep() function to cause an
	 * update/invalidate to occur at a later date.
	 * 
	 * @author http://developer.android.com/resources/samples/Snake
	 * 
	 */
	class RefreshHandler extends Handler {

		private static final long mScreenRefreshDelay = 17;

		@Override
		public void handleMessage(Message msg) {
			
			//TODO: calculate how long each step takes to ensure 30 fps
			
			switch (GameView.this.mState) {
			case RUNNING:
				GameView.this.update();
				break;
			case GAMEOVER:
				GameView.this.invalidate();
				break;
			default:
				break;
			}
			sleep(mScreenRefreshDelay);
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private RefreshHandler mRedrawHandler = new RefreshHandler();
	
	public void update() {
		if (mLevelView == null | mLevelView.mLevel == null) {
			return;
		}
		
		switch (mLevelView.mLevel.mState) {
		case RUNNING:
			mLevelView.update();
			mLevelView.invalidate();
	
		    mStatusBarLevel.setText("Level: " + mLevelView.mLevel.id);
			mStatusBarLevel.invalidate();
			
		    mStatusBarMoves.setText("Moves: " + mLevelView.mLevel.mMoves);
		    mStatusBarMoves.invalidate();
		    break;
		case WIN:
			mState = State.PAUSED;
			mPopupView.createWinGamePopup();
			break;
		case LOSE:
			mState = State.PAUSED;
			mPopupView.createLoseGamePopup();
			break;
		default:
			break;
		}
		
		
	}
	
	/** 
	 * Default Constructor
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Loads all children Views for this view
	 * @return true on success
	 */
	public boolean loadChildren() {
		try {
			mLevelView = (LevelView) findViewById(R.id.levelview);
			if (mLevelView == null)
				return false;
			
			mStatusBarLevel = (TextView) findViewById(R.id.statusbar_level);
			if (mStatusBarLevel == null)
				return false;
			
			mStatusBarMoves = (TextView) findViewById(R.id.statusbar_moves);
			if (mStatusBarMoves == null)
				return false;
			
			findViewById(R.id.restart_icon).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							GameView.this.loadLevel(mLevelView.mLevel.id);
						}
					}
				);
			
			mPopupView = (PopupView) findViewById(R.id.popup);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Forces mLevelView to load a new level
	 * @param levelId
	 */
	public void loadLevel(String levelId) {
		mLevelView.loadLevel(levelId);
		mState = State.RUNNING;
		mPopupView.setVisibility(INVISIBLE);
		mRedrawHandler.sleep(0);
	}

	/**
	 * Handles keyboard input and calls the appropriate functions
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		
		if (mLevelView != null && mLevelView.processKey(keyCode, msg)) {
			return true;
		}

		return super.onKeyDown(keyCode, msg);
	}

	/**
	 * Passes all TouchEvents to processTouchEvent, including historical touches
	 * (these can pile up if the refresh rate is slow).
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		processTouchEvent(event.getAction(), event.getX(), event.getY());

		int historySize = event.getHistorySize();
		for (int i = 0; i < historySize; i++) {
			processTouchEvent(event.getAction(), event.getX(), event.getY());
		}
		
		return true;
	}

	/**
	 * Processes an event from the Touchscreen and calls the appropriate
	 * function
	 * 
	 * @param action
	 * @param eventX
	 * @param eventY
	 * @return
	 */
	private boolean processTouchEvent(int action, float eventX, float eventY) {
		if (mLevelView != null && mLevelView.processTouchEvent(action, eventX, eventY)) {
			return true;
		}
		return false;
	}
	
	//==================================
	//
	// Draw
	//
	//==================================

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	    int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    setMeasuredDimension(width, height);
	    
	    if (!isInEditMode() && mLevelView != null) {
		    mLevelView.measure(widthMeasureSpec, heightMeasureSpec);
	    }
	}
	
	// XXX: This code never executes
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (!isInEditMode() && mLevelView != null) {
			mLevelView.draw(canvas);
			mStatusBarLevel.draw(canvas);
			mStatusBarMoves.draw(canvas);
		}
	}
	

}