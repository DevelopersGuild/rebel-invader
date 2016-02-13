package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 1/10/2016.
 */
public class PlayerComponent implements Component {
    public static final float STARTING_HEALTH = 100f;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_KNOCKED_BACK=1;
    
    public static final float VELOCITY_X = 2500f;
    public static final float VELOCITY_Y = 50f;
    public static final float WIDTH = 2f;
    public static final float HEIGHT = 2f;

    public float maxHealth;
    public float currentHealth;

    public float heightSoFar = 0.0f;
    public float impactCooldown=0.0f;
}
