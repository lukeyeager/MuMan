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

/**
 * A helpful class to define a 2D location in the game matrix.
 * @author Luke
 *
 */
public class Coordinate implements Cloneable {
	
	public static enum Direction {
		NONE, UP, DOWN, LEFT, RIGHT
	}
	
	public int x;
	public int y;
	
	public Direction dir;

	/**
	 * Default constructor
	 * @param newX
	 * @param newY
	 */
	public Coordinate(int newX, int newY) {
		this(newX, newY, Direction.NONE);
	}

	/**
	 * Constructor, with specified direction
	 * @param newX
	 * @param newY
	 * @param direction
	 */
	public Coordinate(int newX, int newY, Direction direction) {
		x = newX;
		y = newY;
		dir = direction;
	}

	/**
	 * Equality operator
	 * @param other
	 * @return True if both x and y are equal.
	 */
	public boolean equals(Coordinate other) {
		if (x == other.x && y == other.y) {
			return true;
		}
		return false;
	}
	
	/**
	 * Moves a Coordinate according to it's current direction
	 */
	public void move() {
		move(dir);
	}
	
	/**
	 * Moves a Coordinate in a certain direction
	 * @param direction
	 */
	public void move(Direction direction) {
		switch (direction) {
		case UP:
			y--;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		default:
			break;
		}
	}

	@Override
	protected Coordinate clone() {
		try {
			return (Coordinate) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException("Couldn't clone a coordinate");
		}
	}
	
}
