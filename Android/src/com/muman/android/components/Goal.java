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
 * The purpose of each level is for the Player to reach this component
 * @author Luke
 *
 */
public class Goal extends Component {

	/**
	 * The level that this Goal is a part of
	 */
	private Level level;
	
	/**
	 * Default constructor
	 * @param lvl The current level must be saved so that an end-of-game scenario can be triggered in onCollision()
	 */
	public Goal(Level lvl) {
		level = lvl;
	}
	
	/**
	 * This collision triggers an end-of-game scenario
	 */
	@Override
	public void onCollision(Player player) {
		player.stop();
		level.win();
	}

	@Override
	public int getImage() {
		return ImageManager.IMAGE_GOAL;
	}

}
