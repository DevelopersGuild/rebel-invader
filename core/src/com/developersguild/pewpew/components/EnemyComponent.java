package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 2/6/2016.
 */
public class EnemyComponent implements Component {
    public static final float WIDTH = 2f;
    public static final float HEIGHT = 2f;
    public static final float VELOCITY_X = 50f;
    public static final float VELOCITY_Y = 50f;
    public static final float STARTING_HEALTH = 50f;
    public static final float DAMAGE = 20f;

    public float maxHealth;
    public float currentHealth;
}
