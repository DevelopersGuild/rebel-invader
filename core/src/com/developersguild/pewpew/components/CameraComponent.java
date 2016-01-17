package com.developersguild.pewpew.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Vihan on 1/10/2016.
 */
public class CameraComponent implements Component {
    public Entity target;
    public OrthographicCamera camera;
}
