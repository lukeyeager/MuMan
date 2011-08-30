package com.muman.android.components;

import com.muman.android.utils.ImageManager;

public class Spiker extends Component {
	
	/**
	 * The level that this Spiker is a part of
	 */
	private Level level;
	
	/**
	 * Default constructor
	 * @param lvl The current level must be saved so that an end-of-game scenario can be triggered in onCollision()
	 */
	public Spiker(Level lvl) {
		level = lvl;
	}

	@Override
	public void onCollision(Player player) {
		player.stop();
		level.lose();
	}

	@Override
	public int getImage() {
		return ImageManager.IMAGE_SPIKER;
	}

}
