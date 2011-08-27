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

package com.muman.android.components;

import com.muman.android.utils.Coordinate;

/**
 * Represents a game level, holding a Player and a matrix of Components
 * @author Luke
 */
public class Level {
	
	/**
	 * The identifier for this level
	 */
	public String id;
	/**
	 * A state variable which keeps track of the number of moves made during this level
	 */
	public int mMoves;
	
	/**
	 * The Player for this level
	 */
	public Player player;
	/**
	 * A matrix of components for this level
	 */
	public Component[][] components;
	
	/**
	 * State of a Level
	 */
	public static enum State {
		RUNNING, WIN, LOSE
	}
	/**
	 * The current state of the level
	 */
	public State mState;

	/**
	 * Default Constructor
	 * @param levelId Which level to load.
	 */
	public Level(String levelId) {
		id = levelId;
		mMoves = 0;
		components = new Component[maxTileWidth][maxTileHeight];
		mState = State.RUNNING;
	}
	
	
	// === DIMENSIONS ===
	
	/**
	 * Maximum width for any level
	 */
	public static final int maxTileWidth = 15;
	/**
	 * The width of the current level
	 */
	private Integer mTileWidth;
	/**
	 * Getter for mTileWidth
	 * @return
	 */
	public int getWidth() { return mTileWidth; }
	/**
	 * Maximum height for any level
	 */
	public static final int maxTileHeight = 9;
	/**
	 * The height of the current level
	 */
	private Integer mTileHeight;
	/**
	 * Getter for mTileHeight
	 */
	public int getHeight() { return mTileHeight; }
	/**
	 * Setter for mTileWidth and mTileHeight
	 * @param width
	 * @param height
	 */
	public void setDimensions(int width, int height) {
		if (width > maxTileWidth | width < 1 
				| height > maxTileHeight | height < 1) {
			throw new RuntimeException("Invalid dimensions");
		}
		mTileWidth = width;
		mTileHeight = height;
		components = new Component[width][height];
	}
	
	
	// === GAMEPLAY ===
	
	/**
	 * Updates the whole level, including the Player if he's moving.
	 */
	public void update() {
		
		if (mState != State.RUNNING) {
			return;
		}
		
		// The next position for Player
		Coordinate next = new Coordinate(player.getX(), player.getY());
		
		switch (player.movement){
		case UP:
			next.y--;
			break;
		case DOWN:
			next.y++;
			break;
		case RIGHT:
			next.x++;
			break;
		case LEFT:
			next.x--;
			break;
		case NONE:
		default:
			return;
		}
		if (next.y < 0 | next.y >= mTileHeight
				| next.x < 0 | next.x >= mTileWidth) {
			playerOutOfBounds();
		} else {
			if (components[next.x][next.y] == null) {
				player.move();
			} else {
				components[next.x][next.y].onCollision(player);
			}
		}
	}
	
	/**
	 * Handles UP input to level
	 */
	public void onInputUp() {
		if (player.movement == Player.Movement.NONE) {
			mMoves++;
			player.movement = Player.Movement.UP;
		}
	}

	/**
	 * Handles DOWN input to level
	 */
	public void onInputDown() {
		if (player.movement == Player.Movement.NONE) {
			mMoves++;
			player.movement = Player.Movement.DOWN;
		}
	}

	/**
	 * Handles LEFT input to level
	 */
	public void onInputLeft() {
		if (player.movement == Player.Movement.NONE) {
			mMoves++;
			player.movement = Player.Movement.LEFT;
		}
	}

	/**
	 * Handles RIGHT input to level
	 */
	public void onInputRight() {
		if (player.movement == Player.Movement.NONE) {
			mMoves++;
			player.movement = Player.Movement.RIGHT;
		}
	}
	
	/**
	 * Called when a player hits a boundary
	 */
	private void playerOutOfBounds() {
		player.stop();
		mState = State.LOSE;
	}
	
	/**
	 * Called when a player reaches the Goal
	 */
	public void win() {
		player.stop();
		mState = State.WIN;
	}
	
	/**
	 * Called when a player loses, for any reason
	 */
	public void lose() {
		player.stop();
		mState = State.LOSE;
	}

}
