package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;
import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.StructureComponent;

public class RowProviderSpaceJunk extends RowProvider {

    @Override
    protected void makeRow(Level level, int row, Entity player) {
        for (int x = 0; x < Level.LEVEL_WIDTH; x += StructureComponent.WIDTH) {
            if (rand.nextDouble() < 0.05) {
                level.createStructure(x, row * StructureComponent.HEIGHT, player, pick(Assets.terrainRegions));
            }
        }
    }

}
