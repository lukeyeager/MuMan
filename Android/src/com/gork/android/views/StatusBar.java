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
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gork.android.R;

/**
 * View to supplement GameView, hovers below and gives relevant game info.
 * @author Luke
 *
 */
public class StatusBar extends LinearLayout {
	
	private GameView mGameView;
	private TextView mLevelView;
	private TextView mMovesView;

	public StatusBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mGameView = (GameView) findViewById(R.id.gameview);
	}

	public StatusBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		

		mGameView = (GameView) findViewById(R.id.gameview);
		mLevelView = (TextView) findViewById(R.id.statusbar_level);
		mMovesView = (TextView) findViewById(R.id.statusbar_moves);
	}

	/* (non-Javadoc)
	 * @see android.widget.TextView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		int level = mGameView.mCurrentLevel.level;
		mLevelView.setText("Level: " + level);
		
		int moves = mGameView.mCurrentLevel.moves;
		mMovesView.setText("Moves: " + moves);
	}

	
}
