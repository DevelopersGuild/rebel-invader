package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureComponent implements Component {
    // Change width and height appropriately once sprite is running
    public static final float WIDTH = 3.0f;
    public static final float HEIGHT = 4.0f;
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;
    public static final int SIZE_SMALL = 0;
    public static final int SIZE_LARGE = 1;

    public int size = SIZE_SMALL;
}
