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

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gork.android.R;
import com.gork.android.components.Level;
import com.gork.android.components.Player;
import com.gork.android.utils.ImageManager;
import com.gork.android.utils.LevelXmlParser;

/**
 * The view for GameActivity. This is where most of the important game-time
 * program execution occurs.
 * 
 * @author Luke
 * 
 */
public class GameView extends View {

	/**
	 * An Enum to store the current state of the game.
	 */
	public static enum State {
		INIT, RUNNING, PAUSED, ERROR
	}

	public State mState;

	public Level mCurrentLevel;

	protected static int mTileSize = 48;

	private int mXOffset;
	private int mYOffset;

	private ImageManager mImageManager = new ImageManager(getContext(),
			mTileSize);

	private final Paint mPaint = new Paint();

	private TextView mLevelView;
	private TextView mMovesView;

	// The x-coordinate of
	private float beginMotionX = 0;
	private float beginMotionY = 0;

	/**
	 * Create a simple handler that we can use to cause animation to happen. We
	 * set ourselves as a target and we can use the sleep() function to cause an
	 * update/invalidate to occur at a later date.
	 * 
	 * @author http://developer.android.com/resources/samples/Snake
	 * 
	 */
	class RefreshHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// Don't update unless the game is Running
			if (GameView.this.mState == GameView.State.RUNNING) {
				GameView.this.update();
				GameView.this.invalidate();
			}
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	private RefreshHandler mRedrawHandler = new RefreshHandler();
	private long mScreenRefreshDelay = 100;

	//TODO: Is this constructor really necessary?
	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.TileView);
		// mTileSize = a.getInt(R.styleable.TileView_tileSize, 12);
		// a.recycle();
		mState = State.INIT;

		mLevelView = (TextView) findViewById(R.id.statusbar_level);
		mMovesView = (TextView) findViewById(R.id.statusbar_moves);
	}

	/** 
	 * Default Constructor
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.TileView);
		// mTileSize = a.getInt(R.styleable.TileView_tileSize, 12);
		// a.recycle();
		mState = State.INIT;

		mLevelView = (TextView) findViewById(R.id.statusbar_level);
		mMovesView = (TextView) findViewById(R.id.statusbar_moves);
	}

	/**
	 * Loads a level
	 * @param levelId
	 */
	public void loadLevel(String levelId) {
		mXOffset = 10;
		mYOffset = 10;

		try {
			mCurrentLevel = new Level(levelId);
			LevelXmlParser handler = new LevelXmlParser(mCurrentLevel);
			XMLReader xr = XMLReaderFactory.createXMLReader();
			
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			AssetManager assets = getContext().getAssets();
			
			xr.parse(new InputSource(assets.open("Levels/level_" + levelId + ".xml")));
			
			mState = State.RUNNING;
			update();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mLevelView != null) {
			mLevelView.setText("Level: " + mCurrentLevel.level);
		} else {
			mLevelView = (TextView) findViewById(R.id.statusbar_level);
		}
		if (mMovesView != null) {
			mMovesView.setText("Moves: " + mCurrentLevel.moves);
		} else {
			mMovesView = (TextView) findViewById(R.id.statusbar_moves);
		}

		if (mCurrentLevel != null) {
			// Draw tiles
			for (int x = 0; x < mCurrentLevel.getWidth(); x += 1) {
				for (int y = 0; y < mCurrentLevel.getHeight(); y += 1) {
					float screenX = mXOffset + x * mTileSize;
					float screenY = mYOffset + y * mTileSize;
					int image = ImageManager.IMAGE_BLANK;
					if (mCurrentLevel.components[x][y] != null) {
						image = mCurrentLevel.components[x][y].getImage();
					}
					canvas.drawBitmap(mImageManager.getImage(image), screenX,
							screenY, mPaint);
				}
			}

			// Draw player
			canvas.drawBitmap(
					mImageManager.getImage(ImageManager.IMAGE_PLAYER),
					mXOffset + mCurrentLevel.player.getX() * mTileSize,
					mYOffset + mCurrentLevel.player.getY() * mTileSize,
					mPaint);
		}
	}

	/**
	 * Called periodically due to our RefreshHandler. Updates the level and
	 * calls any other necessary update functions.
	 */
	public void update() {
		if (mCurrentLevel != null) {
			mCurrentLevel.update();
		}
		mRedrawHandler.sleep(mScreenRefreshDelay);
	}

	/**
	 * Handles keyboard input and calls the appropriate functions
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			onInputUp();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			onInputDown();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			onInputLeft();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			onInputRight();
		}
		
		update();

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

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			beginMotionX = eventX;
			beginMotionY = eventY;
			return true;

		case MotionEvent.ACTION_MOVE:
			return true;

		case MotionEvent.ACTION_UP:
			if (beginMotionX == 0 | beginMotionY == 0) {
				debug("Touch up found when no touch down registered!");
				return false;
			}
			float diffX = eventX - beginMotionX;
			float diffY = eventY - beginMotionY;
			beginMotionX = 0;
			beginMotionY = 0;

			if (Math.abs(diffX) > Math.abs(diffY)) {
				if (diffX > 0) {
					onInputRight();
				} else {
					onInputLeft();
				}
			} else {
				if (diffY > 0) {
					onInputDown();
				} else {
					onInputUp();
				}
			}
			update();

		default:
			debug("Ignored touch event: " + action + " at (" + eventX + ','
					+ eventY + ')');
			return false;
		}
	}

	private boolean onInputUp() {
		if (mCurrentLevel.player.movement == Player.Movement.NONE) {
			mCurrentLevel.moves++;
			mCurrentLevel.player.movement = Player.Movement.UP;
		}
		return (true);
	}

	private boolean onInputDown() {
		if (mCurrentLevel.player.movement == Player.Movement.NONE) {
			mCurrentLevel.moves++;
			mCurrentLevel.player.movement = Player.Movement.DOWN;
		}
		return (true);
	}

	private boolean onInputLeft() {
		if (mCurrentLevel.player.movement == Player.Movement.NONE) {
			mCurrentLevel.moves++;
			mCurrentLevel.player.movement = Player.Movement.LEFT;
		}
		return (true);
	}

	private boolean onInputRight() {
		if (mCurrentLevel.player.movement == Player.Movement.NONE) {
			mCurrentLevel.moves++;
			mCurrentLevel.player.movement = Player.Movement.RIGHT;
		}
		return (true);
	}

	private void debug(String msg) {
		Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
	}

}