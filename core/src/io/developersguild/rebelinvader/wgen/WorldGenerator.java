package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.RandomXS128;

import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.StructureComponent;

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
            new RowProviderFleetDock(),
            new RowProviderFleetDock(),
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

    public static void main(String[] args) {
        WorldGenerator gen = new WorldGenerator(null);
        for (int height = 0; height < 100; height++) {
            for (int i = 0; i < gen.getDensity(height); i++) {
                System.out.print('*');
            }
            System.out.println();
        }
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

    //Gets a density. It's randomly fuzzed, with a sine term to make it start and stop.
    public double getDensity(int height) {
        return getSliding5Random(height) + height * 0.1f + Math.sin(height / 4) * height * 0.1f;
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