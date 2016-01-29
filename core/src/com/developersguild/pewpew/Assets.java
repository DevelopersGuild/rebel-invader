package com.developersguild.pewpew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Vihan on 1/10/2016.
 */
public class Assets {
    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static Texture roof;
    public static TextureRegion roofRegion;

    public static Texture ship;
    public static Animation shipNormal;

    public static Texture healthBar;
    public static TextureRegion healthRegion;

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load() {
        background = loadTexture("background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);

        roof = loadTexture("roof.png");
        roofRegion = new TextureRegion(roof, 0, 0, 96, 128);

        ship = loadTexture("ship.png");
        shipNormal = new Animation(0.02f, new TextureRegion(ship, 0, 0, 96, 96));
        shipNormal.setPlayMode(Animation.PlayMode.LOOP);

        healthBar = loadTexture("health_bar.png");
        healthRegion = new TextureRegion(healthBar, 0, 0, 75, 5);
    }
}
