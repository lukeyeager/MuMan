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
 * Abstract class from which all components are derived (except Player).
 * @author Luke
 *
 */
public abstract class Component {
	
	private Coordinate position;
	
	public int getX() { return position.x; }
	public int getY() { return position.y; }
	
	/**
	 * Each component should define what happens when it collides with a Player
	 * @param player
	 */
	public abstract void onCollision(Player player);
	
	/**
	 * Each component should define which 
	 */
	public abstract int getImage();
}