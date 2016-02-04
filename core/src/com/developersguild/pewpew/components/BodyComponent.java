package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Vihan on 1/22/2016.
 */
public class BodyComponent implements Component {
    public static final int PLAYER_HITS_STRUCTURE = 1;

    public Body body;
}
