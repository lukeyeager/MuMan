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

public class Spiker extends Component {

	@Override
	public boolean onCollision(Player player) {
		player.die();
		return true;
	}

	@Override
	public int getImage() {
		return ImageManager.IMAGE_SPIKER;
	}

	@Override
	public boolean stopsLaser() {
		return true;
	}

	@Override
	public Integer getDrawWeight() {
		return 80;
	}

}
