package io.developersguild.rebelinvader.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 1/10/2016.
 */
public class BackgroundComponent implements Component {
    public static final float STARS_VELOCITY_MULTIPLIER = 1f;
    public static final float NEBULA_VELOCITY_MULTIPLIER = STARS_VELOCITY_MULTIPLIER / 4f;
    public static final int TYPE_STARS = 1;
    public static final int TYPE_NEBULA = 2;

    public int type = 0;
}
