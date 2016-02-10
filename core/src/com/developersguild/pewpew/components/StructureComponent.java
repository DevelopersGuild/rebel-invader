package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureComponent implements Component {
    // Change width and height appropriately once sprite is running
    public static final float STARTING_HEALTH = 100f;
    public static final float WIDTH = 2.0f;
    public static final float HEIGHT = 2.0f;
    public static final float DAMAGE = 1f;

    public float maxHealth;
    public float currentHealth;
}
