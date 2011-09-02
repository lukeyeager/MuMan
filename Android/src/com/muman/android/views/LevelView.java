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

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.muman.android.R;
import com.muman.android.components.Level;
import com.muman.android.utils.GameException;
import com.muman.android.utils.ImageManager;
import com.muman.android.utils.LevelXmlParser;

/**
 * Holds all data for the current level, including Player and all Components
 * @author Luke
 *
 */
public class LevelView extends View {
	
	/**
	 * An Enum to store the current state of the level.
	 */
	public static enum State {
		INIT, RUNNING, PAUSED, ERROR
	}
	
	public State mState;
	
	Level mLevel = null;

	/**
	 * The size (in pixels) of each square tile
	 */
	public int mTileSize;
	/**
	 * The size (in pixels) of the border surrounding the Level
	 */
	private int mBorderSize;

	private ImageManager mImageManager;

	private final Paint mPaint = new Paint();
	
	public LevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
	
		mTileSize = context.getResources().getInteger(R.integer.tile_size);
		mBorderSize = context.getResources().getInteger(R.integer.border_width);
		mImageManager = new ImageManager(context);
    }
	
	/**
	 * Loads a level from an XML file
	 * @param levelPath The path to an XML file under assets/Levels
	 */
	public void loadLevel(String levelPath) {
		try {
			mLevel = new Level(levelPath);
			LevelXmlParser handler = new LevelXmlParser(mLevel);
			XMLReader xr = XMLReaderFactory.createXMLReader();
			
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			
			AssetManager assets = getContext().getAssets();
			
			xr.parse(new InputSource(assets.open("Levels/" + levelPath)));
			
			mState = State.RUNNING;
			update();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called periodically due to our RefreshHandler. Updates the level and
	 * calls any other necessary update functions.
	 */
	public void update() {
		if (mLevel != null) {
			mLevel.update();
		}
	}

	//==================================
	//
	// Input
	//
	//==================================

	/**
	 * Invoked by GameView when a touch event occurs
	 */
	public boolean processKey(int keyCode, KeyEvent msg) {

		boolean recognized = false;
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			mLevel.onInputUp();
			recognized = true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			mLevel.onInputDown();
			recognized = true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			mLevel.onInputLeft();
			recognized = true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			mLevel.onInputRight();
			recognized = true;
		}
		
		if (recognized) {
			update();
		}
		
		return recognized;
	}
	
	/**
	 * X-component of initial touch location for a swipe
	 */
	private float beginMotionX = 0;
	/**
	 * Y-component of initial touch location for a swipe
	 */
	private float beginMotionY = 0;

	/**
	 * Invoked by GameView when a touch event occurs. 
	 */
	public boolean processTouchEvent(int action, float eventX, float eventY) throws GameException {

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			beginMotionX = eventX;
			beginMotionY = eventY;
			return true;

		case MotionEvent.ACTION_MOVE:
			return true;

		case MotionEvent.ACTION_UP:
			if (beginMotionX == 0 | beginMotionY == 0) {
				throw new GameException("Touch up found when no touch down registered!");
			}
			float diffX = eventX - beginMotionX;
			float diffY = eventY - beginMotionY;
			beginMotionX = 0;
			beginMotionY = 0;

			if (mLevel != null) {
				if (Math.abs(diffX) > Math.abs(diffY)) {
					if (diffX > 0) {
						mLevel.onInputRight();
					} else {
						mLevel.onInputLeft();
					}
				} else {
					if (diffY > 0) {
						mLevel.onInputDown();
					} else {
						mLevel.onInputUp();
					}
				}
				update();
			}

		default:
			//debug("Ignored touch event: " + action + " at (" + eventX + ',' + eventY + ')');
			return false;
		}
	}
	
	//==================================
	//
	// Draw
	//
	//==================================
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// Defaults just for viewing in Eclipse
		int width = 150;
		int height = 100;
		if (!isInEditMode() && mLevel != null) {
		    width = mLevel.getWidth() * mTileSize + 2 * mBorderSize;
		    height = mLevel.getHeight() * mTileSize + 2 * mBorderSize;
		}
	    setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (!isInEditMode() && mLevel != null) {
			// Draw components
			for (int x = 0; x < mLevel.getWidth(); x += 1) {
				for (int y = 0; y < mLevel.getHeight(); y += 1) {
					float screenX = x * mTileSize + mBorderSize;
					float screenY = y * mTileSize + mBorderSize;
					int image = ImageManager.IMAGE_BLANK;
					if (mLevel.components[x][y] != null) {
						image = mLevel.components[x][y].getImage();
					}
					canvas.drawBitmap(mImageManager.getImage(image), screenX,
							screenY, mPaint);
				}
			}
	
			// Draw player
			canvas.drawBitmap(
					mImageManager.getImage(ImageManager.IMAGE_PLAYER),
					mLevel.player.getX() * mTileSize + mBorderSize,
					mLevel.player.getY() * mTileSize + mBorderSize,
					mPaint);
		}
	}
	
}
