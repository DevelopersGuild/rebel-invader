package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by thomastran on 3/4/2016.
 */
public class MissileComponent implements Component {
    public static final float WIDTH = 0.25f;
    public static final float HEIGHT = 0.25f;
    public static final float PLAYER_MISSILE_VELOCITY = 250f;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_HIT = 1;

    public Entity origin;
}
