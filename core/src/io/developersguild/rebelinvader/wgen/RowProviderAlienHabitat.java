package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;
import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.StructureComponent;

public class RowProviderAlienHabitat extends RowProvider {

    @Override
    protected void createRowImpl(Level level, int row, float y, Entity player) {
        int x = 0;
        for (; x < Level.LEVEL_WIDTH; x += StructureComponent.WIDTH) {
            level.createStructure(x, y, player, pick(Assets.cityRegions));
        }
        level.createStructure(x, y, player, pick(Assets.cityRegions));
    }

}
