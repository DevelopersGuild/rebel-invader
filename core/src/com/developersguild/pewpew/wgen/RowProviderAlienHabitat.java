package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.StructureComponent;

public class RowProviderAlienHabitat extends RowProvider {

    @Override
    protected void makeRow(Level level, int row, Entity player) {
        int x = 0;
        for (; x < Level.LEVEL_WIDTH; x += StructureComponent.WIDTH) {
            level.createStructure(x, row * StructureComponent.HEIGHT, player, pick(Assets.cityRegions));
        }
        level.createStructure(x, row * StructureComponent.HEIGHT, player, pick(Assets.cityRegions));
    }

}
