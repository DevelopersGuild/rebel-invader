package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Thomas on 1/22/2016.
 */
public class HealthComponent implements Component {
    public static final int IS_PLAYER = 1;
    public static final int IS_ENEMY = 2;
    public static final int IS_STRUCTURE = 3;

    public static final float LEVEL_INC = 0.1f;     // The percent increase of health every level
    public static float healthMultiplier = 1.0f;
    public static int currentLvl = 1;
    public float maxHealth;
    public float currentHealth;
    public Entity target;
    public Vector3 targetPos;
    public int belongsTo;
    public float lengthRatio;
    public float widthRatio;
}
