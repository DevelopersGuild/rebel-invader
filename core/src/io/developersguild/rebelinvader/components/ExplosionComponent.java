package io.developersguild.rebelinvader.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by Thomas on 3/12/2016.
 */
public class ExplosionComponent implements Component {
    public static final float WIDTH = 3.0f;
    public static final float HEIGHT = 3.0f;
    public static final float EXPLOSION_DAMAGE = 50f;

    public static final float REMOVAL_TIME = 0.3f;
    public float currentTime = 0.0f;

    public Entity origin;
}
