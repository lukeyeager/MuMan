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
import com.muman.android.utils.ImageManager;

/**
 * This component turns off a laser beam when encountered.
 * @author Luke
 *
 */
public class LaserSwitch extends Component {
	
	private int sourceID = 0;
	private Level level;
	
	public LaserSwitch(int id, Level lvl) {
		sourceID = id;
		level = lvl;
	}

	@Override
	public boolean onCollision(Player player) {
		Coordinate c = level.findLaserById(sourceID);
		if (c != null) {
			Component[] array = level.getComponents(c.x, c.y);
			LaserSource source = null;
			for (int i=0; i<array.length; i++) {
				if (array[i].getClass() == LaserSource.class && ((LaserSource) array[i]).getID() == sourceID) {
					source = (LaserSource) array[i];
					break;
				}
			}
			if (source != null) {
				level.removeComponent(c.x, c.y, source);
				c.dir = source.getDirection();
				c.move();
				while (c.x >= 0 && c.x < level.getWidth() && c.y >= 0 && c.y < level.getHeight()) {
					array = level.getComponents(c.x, c.y);
					boolean foundBeam = false;
					for (int i=0; i<array.length; i++) {
						if (array[i].getClass() == LaserBeam.class && ((LaserBeam) array[i]).getDirection() == c.dir) {
							foundBeam = true;
							level.removeComponent(c.x, c.y, array[i]);
							break;
						}
					}
					if (foundBeam)
						c.move();
					else
						break;
				}
			}
		}
		return false;
	}

	@Override
	public int getImage() {
		return ImageManager.IMAGE_LASER_KEY;
	}

	@Override
	public boolean stopsLaser() {
		return false;
	}

	@Override
	public Integer getDrawWeight() {
		return 60;
	}

}
