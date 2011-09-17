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

/**
 * Abstract class from which all components are derived.
 * @author Luke
 *
 */
public abstract class Component implements Comparable<Component> {
	
	/**
	 * Each component defines what happens when it collides with a Player
	 * @param player
	 * @return True if the player's motion stops
	 * 			(So, no more components' onCollisions need to be processed at this location)
	 */
	public abstract boolean onCollision(Player player);
	
	/**
	 * Each component defines which image is drawn by ImageManager
	 */
	public abstract int getImage();

	/**
	 * Each component defines whether it stops a Laser beam 
	 * @return
	 */
	public abstract boolean stopsLaser();
	
	/**
	 * Each component defines its "weight" relative to other components so we can decide what order to draw them in
	 * 
	 * Wall			90	<br>
	 * Spiker		80	<br>
	 * Goal			61	<br>
	 * LaserKey		60	<br>
	 * Player		50	<br>
	 * LaserBeam	31	<br>
	 * LaserSource	30	<br>
	 * 
	 * @return Weight
	 * 
	 */
	public abstract Integer getDrawWeight();

	@Override
	public int compareTo(Component another) {
		return this.getDrawWeight().compareTo(another.getDrawWeight());
	}
	
}
