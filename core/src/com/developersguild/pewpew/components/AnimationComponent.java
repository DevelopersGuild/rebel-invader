package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by Vihan on 1/10/2016.
 */
public class AnimationComponent implements Component {
    public IntMap<Animation> animations = new IntMap<Animation>();
}

