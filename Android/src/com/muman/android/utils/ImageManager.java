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

import java.io.BufferedInputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    public static final int IMAGE_LASER_SOURCE_UP = 5;
    public static final int IMAGE_LASER_SOURCE_DOWN = 6;
    public static final int IMAGE_LASER_SOURCE_LEFT = 7;
    public static final int IMAGE_LASER_SOURCE_RIGHT = 8;
    public static final int IMAGE_LASER_BEAM_HORIZ = 9;
    public static final int IMAGE_LASER_BEAM_VERT = 10;
    public static final int IMAGE_LASER_KEY = 11;
    
    private Bitmap[] mImages;
    
    private AssetManager mAssets;
    private String screenSize;
    
	/**
	 * Default Constructor
	 * @param context
	 * @param tileSize The size in pixels of each square tile
	 * @throws IOException 
	 */
    public ImageManager(Context context) throws IOException {
    	mAssets = context.getAssets();
    	screenSize = context.getResources().getString(R.string.screen_size);
    	
    	mImages = new Bitmap[20];
    	loadImage(IMAGE_BLANK, "blank_tile");
    	loadImage(IMAGE_PLAYER, "player");
    	loadImage(IMAGE_WALL, "wall");
    	loadImage(IMAGE_GOAL, "goal");
    	loadImage(IMAGE_SPIKER, "spiker");
    	loadImage(IMAGE_LASER_SOURCE_UP, "laser_source_up");
    	loadImage(IMAGE_LASER_SOURCE_DOWN, "laser_source_down");
    	loadImage(IMAGE_LASER_SOURCE_LEFT, "laser_source_left");
    	loadImage(IMAGE_LASER_SOURCE_RIGHT, "laser_source_right");
    	loadImage(IMAGE_LASER_BEAM_HORIZ, "laser_beam_horiz");
    	loadImage(IMAGE_LASER_BEAM_VERT, "laser_beam_vert");
    	loadImage(IMAGE_LASER_KEY, "laser_key");
    	
    	mAssets = null;
    }
    
	/**
	 * Function to set the specified Drawable as the tile for a particular
	 * integer key.
	 * 
	 * @param key
	 * @param tile
	 * @throws IOException 
	 */
	private void loadImage(int key, String fileName) throws IOException {
		String filePath = "Images/" + screenSize + "/" + fileName + ".png";
		
		BufferedInputStream buf;
		buf = new BufferedInputStream(mAssets.open(filePath));
		Bitmap bitmap = BitmapFactory.decodeStream(buf);
		
		if (key < 0 || key > mImages.length - 1) {
			throw new RuntimeException("ImageManager.mImages is too small");
		}
		mImages[key] = bitmap;
		buf.close();
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
