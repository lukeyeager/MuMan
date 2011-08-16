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

package com.gork.android.utils;

/**
 * A helpful class to define a 2D location in the game matrix.
 * @author Luke
 *
 */
public class Coordinate {
	public int x;
	public int y;

	/**
	 * Constructor
	 * @param newX
	 * @param newY
	 */
	public Coordinate(int newX, int newY) {
		x = newX;
		y = newY;
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
}