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
    
    private Bitmap[] mImages;
    
    private AssetManager mAssets;
    private String screenSize;
    
	/**
	 * Default Constructor
	 * @param context
	 * @param tileSize The size in pixels of each square tile
	 */
    public ImageManager(Context context) {
    	mAssets = context.getAssets();
    	screenSize = context.getResources().getString(R.string.screen_size);
    	
    	mImages = new Bitmap[5];
    	loadImage(IMAGE_BLANK, "blank_tile");
    	loadImage(IMAGE_PLAYER, "player");
    	loadImage(IMAGE_WALL, "wall");
    	loadImage(IMAGE_GOAL, "goal");
    	loadImage(IMAGE_SPIKER, "spiker");
    	
    	mAssets = null;
    }
    
	/**
	 * Function to set the specified Drawable as the tile for a particular
	 * integer key.
	 * 
	 * @param key
	 * @param tile
	 */
	private void loadImage(int key, String fileName) {
		String filePath = "Images/" + screenSize + "/" + fileName + ".png";
		
		BufferedInputStream buf;
		try {
			buf = new BufferedInputStream(mAssets.open(filePath));
			Bitmap bitmap = BitmapFactory.decodeStream(buf);
			mImages[key] = bitmap;
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
