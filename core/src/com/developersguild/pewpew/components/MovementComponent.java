package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Vihan on 1/10/2016.
 */
public class MovementComponent implements Component {
    public final Vector2 velocity = new Vector2();
    public final Vector2 accel = new Vector2();
}
