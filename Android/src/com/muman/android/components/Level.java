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

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

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
	 * A matrix of ArrayLists of components for this level
	 */
	private Vector<Vector<TreeSet<Component>>> components = null;
	
	/**
	 * Adds a Component to the components matrix at the given location
	 * @param x Distance from the left side of the screen (0 - getWidth()-1)
	 * @param y Distance from the top of the screen (0 - getHeight()-1)
	 * @param c Coordinate to add
	 * @return True if the component was added successfully
	 */
	public boolean addComponent(int x, int y, Component c) {
		if (components == null) {
			return false;
		}
		if (x < 0 | x >= mTileWidth | y < 0 | y >= mTileHeight) {
			return false;
		}

		return components.get(x).get(y).add(c);
	}
	
	/**
	 * Get the list of components at a given location
	 * @param x Width-component of the coordinate
	 * @param y Height-component of the coordinate
	 * @return An array of Components
	 */
	public Component[] getComponents(int x, int y) {
		if (components == null) {
			return null;
		}
		
		return components.get(x).get(y).toArray(new Component[0]);
	}
	
	/**
	 * Removes a Component from the components matrix
	 * @param x Width-component of the coordinate
	 * @param y Height-component of the coordinate
	 * @param c Component to remove
	 * @return True if the component was removed
	 */
	public boolean removeComponent(int x, int y, Component c) {
		return components.get(x).get(y).remove(c);
	}
	
	/**
	 * State of a Level
	 */
	public static enum State {
		RUNNING, WIN, LOSE, ERROR
	}
	
	/**
	 * For now, this just returns the state of the player. More options may be added later.
	 * @return
	 */
	public State getState() {
		if (player == null)
			return State.ERROR;
		
		switch (player.getState()) {
		case WON:
			return State.WIN;
		case LOST:
			return State.LOSE;
		case PLAYING:
			return State.RUNNING;
		default:
			return State.ERROR;
		}
	}

	/**
	 * Default Constructor
	 * @param levelId Which level to load.
	 */
	public Level(String levelId) {
		id = levelId;
		mMoves = 0;
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
		components = new Vector<Vector<TreeSet<Component>>>(width);
		
		// TODO: Is there a way to do this in the above declaration? 
		for (int i=0; i<width; i++) {
			int size = components.size();
			components.add(i, new Vector<TreeSet<Component>>(height));
			for(int j=0; j<height; j++) {
				size = components.get(i).size();
				components.get(i).add(j, new TreeSet<Component>());
			}
		}
	}
	
	
	// === GAMEPLAY ===
	
	/**
	 * Updates the whole level, including the Player if he's moving.
	 */
	public void update() {
		
		if (getState() != State.RUNNING) {
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
			if (components.get(next.x).get(next.y).size() == 0) {
				player.move();
			} else {
				Iterator<Component> it = components.get(next.x).get(next.y).iterator();
				while (it.hasNext()) {
					Component c = it.next();
					if (c.onCollision(player)) {
						break;
					}
					
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
						break;
					} else {
						player.move();
					}
				}
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
		player.die();
	}

}
