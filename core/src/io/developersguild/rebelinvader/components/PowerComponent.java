package io.developersguild.rebelinvader.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by MW on 04-Mar-16.
 */
public class PowerComponent implements Component {
    public static final float DURATION = 5;
    public static final float BULLET_RATE_MULTIPLIER = 2;

    public float maxPower;
    public float currentPower;
    public Entity target;
    public Vector3 targetPos;
    public float lengthRatio;
    public float widthRatio;

    public boolean doRender;
}
