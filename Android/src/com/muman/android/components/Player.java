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
import com.muman.android.utils.Coordinate.Direction;

/**
 * Main character, one per level.
 * @author Luke
 *
 */
public class Player {
	
	/**
	 * Player location
	 */
	private Coordinate location;
	/**
	 * Getter for the X-location
	 * @return
	 */
	public int getX() {
		return location.x;
	}
	/**
	 * Getter for the Y-location
	 * @return
	 */
	public int getY() {
		return location.y;
	}
	
	/**
	 * Getter for the player's movement
	 * @return
	 */
	public Direction getDir() {
		return location.dir;
	}
	
	/**
	 * Setter for the player's movement
	 * @param direction
	 */
	public void setDir(Direction direction) {
		location.dir = direction;
	}
	
	/**
	 * Enum to denote the state of the Player.
	 * @author Luke
	 *
	 */
	public static enum State {
		PLAYING, LOST, WON
	}
	
	/**
	 * The current State of the Player.
	 */
	private State mState = State.PLAYING;
	
	/**
	 * Getter for mState. The Level uses this function to decide whether the game should end.
	 */
	public State getState() { return mState; }
	
	/**
	 * Default constructor
	 * @param newX Starting location
	 * @param newY Starting location
	 */ 
	public Player(int newX, int newY) {
		location = new Coordinate(newX, newY, Direction.NONE);
	}
	
	/**
	 * Updates a Player's location according to its movement
	 */
	public void move() {
		location.move();
	}
	
	/**
	 * Stops movement
	 */
	public void stop() {
		location.dir = Direction.NONE;
	}
	
	/**
	 * Causes a Player to enter the WON state
	 */
	public void win() {
		stop();
		mState = State.WON;
	}
	
	/**
	 * Causes a Player to enter the LOST state
	 */
	public void die() {
		stop();
		mState = State.LOST;
	}

}
