package com.developersguild.pewpew;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Vihan on 1/10/2016.
 */
public class Assets {
    public static BitmapFont font;

    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static Texture bullet;
    public static TextureRegion bulletRegion;

    public static Texture roof;
    public static TextureRegion roofRegion;

    public static Texture ship;
    public static Animation shipNormal;

    public static Texture healthBar;
    public static TextureRegion healthRegion;

    public static Texture[] enemies;
    public static TextureRegion[] enemyRegions;

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);

        background = loadTexture("background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);

        bullet = loadTexture("bullet.png");
        bulletRegion = new TextureRegion(bullet, 0, 0, 8, 8);

        roof = loadTexture("roof.png");
        roofRegion = new TextureRegion(roof, 0, 0, 64, 64);

        ship = loadTexture("ship.png");
        shipNormal = new Animation(0.02f, new TextureRegion(ship, 0, 0, 96, 96));
        shipNormal.setPlayMode(Animation.PlayMode.LOOP);

        healthBar = loadTexture("health_bar.png");
        healthRegion = new TextureRegion(healthBar, 0, 0, 95, 5);

        enemies = new Texture[15];
        enemyRegions = new TextureRegion[15];
        for (int i = 0; i < 15; i++) {
            enemies[i] = loadTexture(i + 1 + ".png");
            enemyRegions[i] = new TextureRegion(enemies[i], 0, 0, 32, 32);
        }
    }
}
