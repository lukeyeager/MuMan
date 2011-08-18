package com.gork.android.components;

import com.gork.android.utils.ImageManager;


public class Goal extends Component {

	private Level level;
	
	public Goal(Level lvl) {
		level = lvl;
	}
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
