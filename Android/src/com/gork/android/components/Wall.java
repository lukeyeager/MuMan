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

import com.gork.android.utils.ImageManager;

/**
 * One of the basic components. A Wall simply blocks movement.
 * @author Luke
 *
 */
public class Wall extends Component {

	@Override
	public void onCollision(Player p) {
		p.stop();
	}

	@Override
	public int getImage() {
		return ImageManager.IMAGE_WALL;
	}

}
