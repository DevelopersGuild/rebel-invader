package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.StructureComponent;

public class RowProviderWall extends RowProvider {

    @Override
    protected void makeRow(Level level, int row, Entity player) {
        for (int x = 0; x < Level.LEVEL_WIDTH; x += StructureComponent.WIDTH) {
            if (rand.nextDouble() < 0.5) {
                level.createStructure(x, row * StructureComponent.HEIGHT, player, pick(Assets.wallRegions));
            }
        }
    }

}
