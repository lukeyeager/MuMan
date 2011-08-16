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

package com.gork.android.components;

import com.gork.android.utils.Coordinate;

/**
 * Main character, one per level.
 * @author Luke
 *
 */
public class Player {
	
	// Player location
	private Coordinate position;
	
	public int getX() {
		return position.x;
	}
	public int getY() {
		return position.y;
	}

	/**
	 * Enum to hold the different states of Player movement
	 * @author Luke
	 *
	 */
	public static enum Movement {
		NONE, UP, DOWN, RIGHT, LEFT
	}
	
	public Movement movement;
	
	/** Player Constructor
	 * 
	 * @param newX Starting position
	 * @param newY Starting position
	 */ 
	public Player(int newX, int newY) {
		position = new Coordinate(newX, newY);
		movement = Movement.NONE;
	}
	
	/**
	 * Updates a Player's location according to his movement
	 */
	public void move() {
		switch (movement) {
		case UP:
			position.y--;
			break;
		case DOWN:
			position.y++;
			break;
		case RIGHT:
			position.x++;
			break;
		case LEFT:
			position.x--;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Stops movement
	 */
	public void stop() {
		movement = Movement.NONE;
	}

}
