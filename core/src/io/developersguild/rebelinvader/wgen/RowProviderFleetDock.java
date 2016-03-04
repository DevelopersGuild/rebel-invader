package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;
import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.Level;

public class RowProviderFleetDock extends RowProvider {

    @Override
    protected void makeRow(Level level, int row, Entity player) {
        level.createEnemy(rand.nextFloat() * Level.LEVEL_WIDTH, Level.SCREEN_HEIGHT * row, player, rand.nextInt(Assets.ENEMY_SPRITES));
    }

}
