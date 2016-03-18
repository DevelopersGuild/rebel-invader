package io.developersguild.rebelinvader.components;

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
    public static final float TOUCH_DAMAGE = 20f;
    public static final float BULLET_DAMAGE = 20f;
    public static final float TARGETING_RADIUS = 8f;
    public static final int STATE_ALIVE = 0;
    public static final int STATE_DEAD = 1;
    public static final float BULLETS_PER_SECOND = 0.5f;
    public static final float FIRE_RATE = 1f / BULLETS_PER_SECOND;
    public static final int SCORE_VALUE = 10;
    public static final float POWER_VALUE = 10;

    public Entity target;
    public float maxHealth;
    public float currentHealth;
    public float shootTimer = 0f;
    public boolean killedByPlayer = false;
}
