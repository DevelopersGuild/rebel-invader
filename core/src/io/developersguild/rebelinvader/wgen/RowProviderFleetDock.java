package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;

import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.Level;

public class RowProviderFleetDock extends RowProvider {

    private static RowProviderAlienHabitat cityProvider = new RowProviderAlienHabitat();

    @Override
    protected void createRowImpl(Level level, int row, float y, Entity player) {
        cityProvider.createRowImpl(level, row, y, player);
        level.createEnemy(rand.nextFloat() * Level.LEVEL_WIDTH, y, player, rand.nextInt(Assets.ENEMY_SPRITES));
    }

}
