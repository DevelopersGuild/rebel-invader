package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;

public class RowProviderFleetDock extends RowProvider {

	@Override
	protected void makeRow(Level level, int row, Entity player) {
		level.createEnemy(rand.nextFloat()*Level.LEVEL_WIDTH, Level.SCREEN_HEIGHT*row, player, rand.nextInt(Assets.ENEMY_SPRITES));
	}

}
