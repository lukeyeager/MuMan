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

package com.muman.android.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.muman.android.R;

/**
 * A helpful class to store loaded Bitmaps and return them for drawing as needed.
 * @author Luke
 *
 */
public class ImageManager {

    public static final int IMAGE_BLANK = 0;
    public static final int IMAGE_PLAYER = 1;
    public static final int IMAGE_WALL = 2;
    public static final int IMAGE_GOAL = 3;
    public static final int IMAGE_SPIKER = 4;
    
    private Integer mTileSize;
    
    private Bitmap[] mImages;
    
	/**
	 * Default Constructor
	 * @param context
	 * @param tileSize The size in pixels of each square tile
	 */
    public ImageManager(Context context, int tileSize) {
    	mTileSize = tileSize;

    	Resources r = context.getResources();
    	mImages = new Bitmap[5];
    	loadImage(IMAGE_BLANK, r.getDrawable(R.drawable.blank_tile));
    	loadImage(IMAGE_PLAYER, r.getDrawable(R.drawable.player));
    	loadImage(IMAGE_WALL, r.getDrawable(R.drawable.wall));
    	loadImage(IMAGE_GOAL, r.getDrawable(R.drawable.goal));
    	loadImage(IMAGE_SPIKER, r.getDrawable(R.drawable.spiker));
    }
    
	/**
	 * Function to set the specified Drawable as the tile for a particular
	 * integer key.
	 * 
	 * @param key
	 * @param tile
	 */
	private void loadImage(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, mTileSize, mTileSize);
		tile.draw(canvas);

		mImages[key] = bitmap;
	}
    
    /**
     * Get an image by index
     * @param index This should be ImageManager.IMAGE_BLANK or something similar
     * @return The requested Bitmap
     */
    public Bitmap getImage(int index) {
    	return mImages[index];
    }
    
}
