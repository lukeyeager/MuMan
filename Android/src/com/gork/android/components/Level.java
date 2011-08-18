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
			components[6][0] = new Wall();
			components[8][1] = new Goal(this);
			break;
		default:
			throw new RuntimeException("Unknown level: " + levelId);
		}
	}
	
	/**
	 * Updates the whole level, including the Player if he's moving.
	 */
	public void update() {
		
		// The next position for Player
		Coordinate next = new Coordinate(player.getX(), player.getY());
		
		switch (player.movement){
		case UP:
			next.y--;
			break;
		case DOWN:
			next.y++;
			break;
		case RIGHT:
			next.x++;
			break;
		case LEFT:
			next.x--;
			break;
		case NONE:
		default:
			return;
		}
		if (next.y < 0 | next.y >= mTileHeight
				| next.x < 0 | next.x >= mTileWidth) {
			collideWithWall();
		} else {
			if (components[next.x][next.y] == null) {
				player.move();
			} else {
				components[next.x][next.y].onCollision(player);
			}
		}
	}
	
	private void collideWithWall() {
		player.stop();
	}
	
	public void win() {
		
	}

}
