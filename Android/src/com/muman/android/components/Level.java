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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import com.muman.android.utils.Coordinate;
import com.muman.android.utils.Coordinate.Direction;

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
		if (player == null | components == null)
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
	 * @param levelId Which level has been loaded.
	 */
	public Level(String levelId) {
		id = levelId;
		mMoves = 0;
	}
	
	// === COMPONENTS ===
	
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
		
		if (c.getClass() == LaserSource.class)
			addLaser(((LaserSource) c).getID(), x, y);

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
		if (c.getClass() == LaserSource.class)
			removeLaser(((LaserSource) c).getID());
		
		return components.get(x).get(y).remove(c);
	}
	
	
	// === DIMENSIONS ===
	
	/**
	 * Maximum width for any level
	 */
	public static final int maxTileWidth = 15;
	/**
	 * The width of the current level
	 */
	private Integer mTileWidth = 0;
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
	private Integer mTileHeight = 0;
	/**
	 * Getter for mTileHeight
	 */
	public int getHeight() { return mTileHeight; }
	/**
	 * Setter for mTileWidth and mTileHeight
	 * @param width
	 * @param height
	 * @return True on success
	 */
	public boolean setDimensions(int width, int height) {
		if (width > maxTileWidth | width < 1 
				| height > maxTileHeight | height < 1) {
			return false;
		}
		mTileWidth = width;
		mTileHeight = height;
		components = new Vector<Vector<TreeSet<Component>>>(width);
		
		for (int i=0; i<width; i++) {
			components.add(i, new Vector<TreeSet<Component>>(height));
			for(int j=0; j<height; j++) {
				components.get(i).add(j, new TreeSet<Component>());
			}
		}
		return true;
	}
	
	// === LASERS ===
	
	/**
	 * A list of Laser ID's for active Lasers
	 */
	private ArrayList<Integer> laserIDs;
	/**
	 * A list of Laser Coordinates for active Lasers
	 */
	private ArrayList<Coordinate> laserCoords;
	
	/**
	 * Adds a Laser by ID and coordinate to the lists laserIDs and laserCoords
	 * @param id
	 * @param x
	 * @param y
	 */
	private void addLaser(int id, int x, int y) {
		if (laserIDs == null)
			laserIDs = new ArrayList<Integer>();
		if (laserCoords == null)
			laserCoords = new ArrayList<Coordinate>();
		
		// A Laser with ID=0 does not have a switch, so we don't need to keep track of it
		if (id == 0)
			return;
		
		for (int i=0; i<laserIDs.size(); i++) {
			if (laserIDs.get(i) == id)
				throw new RuntimeException("Two Lasers with the same ID have been declared");
		}
		laserIDs.add(id);
		laserCoords.add(new Coordinate(x,y));
	}
	
	/**
	 * Removes a Laser by id from the internal lists
	 * @param id
	 */
	private void removeLaser(int id) {
		if (laserIDs == null | laserCoords == null)
			return;
		
		for (int i=0; i<laserIDs.size(); i++) {
			if (laserIDs.get(i) == id) {
				laserIDs.remove(i);
				laserCoords.remove(i);
				return;
			}
		}
	}
	
	public Coordinate findLaserById(int id) {
		if (laserIDs == null | laserCoords == null)
			return null;
		
		for (int i=0; i<laserIDs.size(); i++) {
			if (laserIDs.get(i) == id) {
				return laserCoords.get(i);
			}
		}
		
		return null;
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
		
		if (player.getDir() == Direction.NONE)
			return;
		
		next.dir = player.getDir();
		next.move();
		
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
					
					next.move();
					
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
		if (player.getDir() == Direction.NONE) {
			mMoves++;
			player.setDir(Direction.UP);
		}
	}

	/**
	 * Handles DOWN input to level
	 */
	public void onInputDown() {
		if (player.getDir() == Direction.NONE) {
			mMoves++;
			player.setDir(Direction.DOWN);
		}
	}

	/**
	 * Handles LEFT input to level
	 */
	public void onInputLeft() {
		if (player.getDir() == Direction.NONE) {
			mMoves++;
			player.setDir(Direction.LEFT);
		}
	}

	/**
	 * Handles RIGHT input to level
	 */
	public void onInputRight() {
		if (player.getDir() == Direction.NONE) {
			mMoves++;
			player.setDir(Direction.RIGHT);
		}
	}
	
	/**
	 * Called when a player hits a boundary
	 */
	private void playerOutOfBounds() {
		player.die();
	}

}
