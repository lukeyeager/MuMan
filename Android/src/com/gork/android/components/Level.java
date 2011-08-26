package com.gork.android.components;

import com.gork.android.utils.Coordinate;

public class Level {
	
	public static final int maxTileWidth = 15;
	public static final int maxTileHeight = 9;
	private Integer mTileWidth;
	private Integer mTileHeight;
	
	public Player player;
	public Component[][] components;
	
	public int moves;
	public String level;

	/**
	 * Default Constructor
	 * @param levelId Which level to load.
	 */
	public Level(String levelId) {
		level = levelId;
		moves = 0;
		components = new Component[maxTileWidth][maxTileHeight];
	}
	
	public void setDimensions(int width, int height) {
		if (width > maxTileWidth | width < 1 
				| height > maxTileHeight | height < 1) {
			throw new RuntimeException("Invalid dimensions");
		}
		mTileWidth = width;
		mTileHeight = height;
		components = new Component[width][height];
	}
	
	public int getWidth() { return mTileWidth; }
	public int getHeight() { return mTileHeight; }
	
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
	
	public void onInputUp() {
		if (player.movement == Player.Movement.NONE) {
			moves++;
			player.movement = Player.Movement.UP;
		}
	}

	public void onInputDown() {
		if (player.movement == Player.Movement.NONE) {
			moves++;
			player.movement = Player.Movement.DOWN;
		}
	}

	public void onInputLeft() {
		if (player.movement == Player.Movement.NONE) {
			moves++;
			player.movement = Player.Movement.LEFT;
		}
	}

	public void onInputRight() {
		if (player.movement == Player.Movement.NONE) {
			moves++;
			player.movement = Player.Movement.RIGHT;
		}
	}
	
	private void collideWithWall() {
		player.stop();
	}
	
	public void win() {
		
	}
	

}
