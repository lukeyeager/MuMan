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

package com.gork.android.views;

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
		UNINITIALIZED, RUNNING, PAUSED
	}

	public State mState = State.UNINITIALIZED;

	public LevelView mLevel;
	private TextView mStatusBarLevel;
	private TextView mStatusBarMoves;

	/**
	 * Create a simple handler that we can use to cause animation to happen. We
	 * set ourselves as a target and we can use the sleep() function to cause an
	 * update/invalidate to occur at a later date.
	 * 
	 * @author http://developer.android.com/resources/samples/Snake
	 * 
	 */
	class RefreshHandler extends Handler {

		private static final long mScreenRefreshDelay = 100;

		@Override
		public void handleMessage(Message msg) {
			// Don't update unless the game is Running
			if (GameView.this.mState == GameView.State.RUNNING) {
				GameView.this.update();
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
		mLevel.update();
		mLevel.invalidate();

	    mStatusBarLevel.setText("Level: " + mLevel.mLevel.level);
		mStatusBarLevel.invalidate();
		
	    mStatusBarMoves.setText("Moves: " + mLevel.mLevel.moves);
	    mStatusBarMoves.invalidate();
	}
	
	/** 
	 * Default Constructor
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setLevelView(View view) {
		mLevel = (LevelView) view;
	}
	
	public void setStatusBar(View level, View moves) {
		mStatusBarLevel = (TextView) level;
		mStatusBarMoves = (TextView) moves;
	}

	/**
	 * 
	 * @param levelId
	 */
	public void loadLevel(String levelId) {
		mLevel.loadLevel(levelId);
		mState = State.RUNNING;
		// Start refresh loop
		mRedrawHandler.sleep(0);
	}

	/**
	 * Handles keyboard input and calls the appropriate functions
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		
		if (mLevel != null && mLevel.processKey(keyCode, msg)) {
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
		if (mLevel != null && mLevel.processTouchEvent(action, eventX, eventY)) {
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
	    
	    if (!isInEditMode() && mLevel != null) {
		    mLevel.measure(widthMeasureSpec, heightMeasureSpec);
	    }
	}
	
	// XXX: This code never executes
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (!isInEditMode() && mLevel != null) {
			mLevel.draw(canvas);
			mStatusBarLevel.draw(canvas);
			mStatusBarMoves.draw(canvas);
		}
	}
	

}