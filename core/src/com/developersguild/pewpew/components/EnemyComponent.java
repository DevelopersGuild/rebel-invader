package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Vihan on 2/6/2016.
 */
public class EnemyComponent implements Component {
    public static final float WIDTH = 2f;
    public static final float HEIGHT = 2f;
    public static final float VELOCITY_X = 100f;
    public static final float VELOCITY_Y = 50f;
    public static final float STARTING_HEALTH = 50f;
    public static final float DAMAGE = 0.5f;
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    public float maxHealth;
    public float currentHealth;
    public Entity target;
}
