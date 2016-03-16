package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;

import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.StructureComponent;

public class RowProviderSpaceJunk extends RowProvider {

    @Override
    protected void createRowImpl(Level level, int row, float y, Entity player) {
        for (int x = 0; x < Level.LEVEL_WIDTH; x += StructureComponent.WIDTH) {
            if (rand.nextDouble() < 0.05) {
                level.createStructure(x, y, player, pick(Assets.terrainRegions));
            }
        }
    }

}
