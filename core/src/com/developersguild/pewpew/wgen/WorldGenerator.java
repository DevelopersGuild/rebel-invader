package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.RandomXS128;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.StructureComponent;

public class WorldGenerator {

    private final Level level;

    private final int seed;
    public IRowProvider[] providers = new IRowProvider[]{
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderSpaceJunk(),
            new RowProviderWall(),
            new RowProviderAlienHabitat(),
            new RowProviderAlienHabitat(),
            new RowProviderAlienHabitat(),
            new RowProviderFleetDock(),
            new RowProviderAlienHabitat(),
            new RowProviderAlienHabitat(),
            //TODO order the world providers - this determines world generation
    };
    int lastHeight = 8;

    //New-style wgen
    private RandomXS128 r = new RandomXS128();

    public WorldGenerator(Level level) {
        this.level = level;
        seed = (int) (Math.random() * Integer.MAX_VALUE);
    }

    public void provideWorld(float heightNeeded, Entity player) {
        provideWorldNew(heightNeeded, player);
    }

    //Coherent random numbers
    public float getSliding5Random(int row) {
        float sum = 0;
        for (int i = 0; i < 5; i++) {
            r.setSeed(row + i + seed);
            sum += r.nextFloat();
        }
        return sum;
    }

    //Gets a density, weighting towards higher as height goes up
    public float getDensity(int height) {
        return getSliding5Random(height) + height * 0.1f;
    }

    public IRowProvider getProvider(int height) {
        int density = (int) getDensity(height);
        //Bounds checking
        if (density >= providers.length) {
            density = providers.length - 1;
        }
        return providers[density];
    }

    public void provideWorldNew(float height, Entity player) {
        int heightAsInt = (int) ((height + Level.SCREEN_HEIGHT * 1.2) / StructureComponent.HEIGHT);
        if (heightAsInt >= lastHeight + 1) { //assumes we never miss more than one row
            lastHeight++;
            getProvider(heightAsInt).createRow(level, heightAsInt, player);
        }
    }
}