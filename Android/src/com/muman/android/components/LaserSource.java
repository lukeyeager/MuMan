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

import com.muman.android.utils.ImageManager;

/**
 * This is the first component of a laser beam. The beam continues in LaserBeam components
 * @author Luke
 *
 */
public class LaserSource extends Component {

	/**
	 * An Enum for storing the direction in which this laser extends
	 * @author Luke
	 *
	 */
	public static enum Direction {
		UP, DOWN, RIGHT, LEFT
	}
	
	/**
	 * The direction of this laser
	 */
	private Direction mDirection;
	
	/**
	 * Getter for mDirection
	 * @return
	 */
	public Direction getDirection() { return mDirection; }
	
	/**
	 * Default constructor
	 * @param direction The direction for this laser
	 */
	public LaserSource(Direction direction) {
		mDirection = direction;
	}

	@Override
	public boolean onCollision(Player player) {
		player.die();
		return true;
	}

	@Override
	public int getImage() {
		return ImageManager.IMAGE_LASER_SOURCE;
	}

	@Override
	public boolean stopsLaser() {
		return false;
	}

	/**
	 * Turns off a laser (must also remove all LaserBeams generated from this source).
	 */
	public void turnOff() {
		//TODO: remove LaserBeams
	}

	@Override
	public Integer getDrawWeight() {
		return 30;
	}

}
