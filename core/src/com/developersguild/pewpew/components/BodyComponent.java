package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Vihan on 1/22/2016.
 */
public class BodyComponent implements Component {
    public static final int PLAYER_STRUCTURE_COLLISION = 1;
    public static final int PLAYER_ENEMY_COLLISION = 2;

    public Body body;
}
