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

import com.muman.android.components.LaserSource.Direction;
import com.muman.android.utils.ImageManager;

/**
 * An extension from a LaserSource, this continues the beam across the screen until it hits another component or the edge of the screen.
 * @author Luke
 *
 */
public class LaserBeam extends Component {

	/**
	 * The direction of this beam
	 */
	private Direction mDirection;
	
	/**
	 * Getter for mDirection
	 * @return
	 */
	public Direction getDirection() { return mDirection; }
	
	/**
	 * Default constructor
	 * @param direction
	 */
	public LaserBeam(Direction direction) {
		mDirection = direction;
	}

	@Override
	public boolean onCollision(Player player) {
		player.die();
		return true;
	}

	@Override
	public int getImage() {
		//TODO: return different images depending on direction
		return ImageManager.IMAGE_LASER_BEAM;
	}

	@Override
	public boolean stopsLaser() {
		return false;
	}

	@Override
	public Integer getDrawWeight() {
		return 31;
	}

}
