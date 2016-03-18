package io.developersguild.rebelinvader.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureComponent implements Component {
    // Change width and height appropriately once sprite is running
    public static final float STARTING_HEALTH = 25.0f;
    public static final float WIDTH = 1.0f;
    public static final float HEIGHT = 1.0f;
    public static final float DAMAGE = 10f;
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;
    public static final int SCORE_VALUE = 5;
    public static final float POWER_VALUE = 5;

    public float maxHealth;
    public float currentHealth;
    public Entity target;
    public boolean killedByPlayer = false;
}
