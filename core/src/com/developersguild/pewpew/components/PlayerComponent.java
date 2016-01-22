package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 1/10/2016.
 */
public class PlayerComponent implements Component {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_DEAD = 1;
    public static final float MOVE_VELOCITY_X = 2500.f;
    public static final float MOVE_VELOCITY_Y = 50.0f;
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;

    // What can I even use this for?
    public float heightSoFar = 0.0f;
}
