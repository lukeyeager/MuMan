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

/**
 * Holds all data for the current level, including Player and all Components
 * @author Luke
 *
 */
public class Level {
	
	public int mTileWidth = 12;
	public int mTileHeight = 8;
	
	public Player player;
	public Component[][] components;
	
	public int moves = 0;
	public Integer level;

	/**
	 * Default Constructor
	 * @param levelId Which level to load.
	 */
	public Level(int levelId) {
		level = levelId;
		switch (levelId) {
		case 1:
			player = new Player(6,4);
			components = new Component[mTileWidth][mTileHeight];
			break;
		default:
			throw new RuntimeException("Unknown level: " + levelId);
		}
	}
	
	/**
	 * Updates the whole level, including the Player if he's moving.
	 */
	public void update() {
		switch (player.movement){
		case NONE:
			return;
		case UP:
			if (player.getY() < 1) {
				player.stop();
			} else {
				player.move();
			}
			break;
		case DOWN:
			if (player.getY() >= mTileHeight - 1) {
				player.stop();
			} else {
				player.move();
			}
			break;
		case RIGHT:
			if (player.getX() >= mTileWidth - 1) {
				player.stop();
			} else {
				player.move();
			}
			break;
		case LEFT:
			if (player.getX() < 1) {
				player.stop();
			} else {
				player.move();
			}
			break;
		}
	}


}
