package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureComponent implements Component {
    // Change width and height appropriately once sprite is running
    public static final float STARTING_HEALTH = 100f;
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;
    public static final float DAMAGE = 1f;
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;

    public float maxHealth;
    public float currentHealth;
    public Entity target;
}
