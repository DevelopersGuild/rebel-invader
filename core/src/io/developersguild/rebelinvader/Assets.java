package io.developersguild.rebelinvader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Vihan on 1/10/2016.
 */
public class Assets {

    public static final int ENEMY_SPRITES = 15;
    public static final int TERRAIN_SPRITES = 3;
    public static final int WALL_SPRITES = 4;
    public static final int CITY_SPRITES = 4;
    public static Music musicMenu;
    public static Music musicGame;
    public static Sound click;
    public static Sound shot;
    public static Sound crash;


    public static BitmapFont font;

    public static Texture playText;
    public static TextureRegion playTextRegion;

    public static Texture exitText;
    public static TextureRegion exitTextRegion;

    public static Texture helpText;
    public static TextureRegion helpTextRegion;

    public static Texture scoreText;
    public static TextureRegion scoreTextRegion;

    public static Texture uiButtons;
    public static TextureRegion pauseButton;
    //public static TextureRegion reloadButton;
    //public static TextureRegion menuButton;
    public static TextureRegion playButton;
    //public static TextureRegion exitButton;

    public static Texture bgNebula;
    public static TextureRegion bgNebulaRegion;

    public static Texture bgStars;
    public static TextureRegion bgStarsRegion;

    public static Texture bullet;
    public static TextureRegion bulletRegion;

    public static Texture missile;
    public static TextureRegion missileRegion;

    public static Texture[] terrains;
    public static TextureRegion[] terrainRegions;

    public static Texture[] walls;
    public static TextureRegion[] wallRegions;

    public static Texture[] city;
    public static TextureRegion[] cityRegions;

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

        playText = loadTexture("play.png");
        playTextRegion = new TextureRegion(playText, 160 - 150, 200 + 18, 300, 36);

        exitText = loadTexture("exit.png");
        exitTextRegion = new TextureRegion(exitText, 160 - 150, 200 - 18 - 36, 300, 36);

        helpText = loadTexture("help.png");
        helpTextRegion = new TextureRegion(helpText, 160 - 150, 200 - 18 - 36 * 2, 300, 36);

        scoreText = loadTexture("score.png");
        scoreTextRegion = new TextureRegion(scoreText, 160 - 150, 200 - 18 - 36 * 2, 300, 36);

        uiButtons = loadTexture("ui_buttons.png");
        pauseButton = new TextureRegion(uiButtons, 0, 0, 40, 50);
        playButton = new TextureRegion(uiButtons, 235 - 40 * 2, 0, 40, 50);

        bgNebula = loadTexture("bg_nebula.png");
        bgNebula.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgNebulaRegion = new TextureRegion(bgNebula, 0, 0, 320, 1067);

        bgStars = loadTexture("bg_stars.png");
        bgStars.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        bgStarsRegion = new TextureRegion(bgStars, 0, 0, 320, 480);

        bullet = loadTexture("bullet.png");
        bulletRegion = new TextureRegion(bullet, 0, 0, 8, 8);

        missile = loadTexture("missile.png");
        missileRegion = new TextureRegion(missile, 0, 0, 16, 32);

        terrains = new Texture[TERRAIN_SPRITES];
        terrainRegions = new TextureRegion[TERRAIN_SPRITES];
        for (int i = 0; i < TERRAIN_SPRITES; i++) {
            terrains[i] = loadTexture("terrain" + (i + 1) + ".png");
            terrainRegions[i] = new TextureRegion(terrains[i], 0, 0, 32, 32);
        }

        walls = new Texture[WALL_SPRITES];
        wallRegions = new TextureRegion[WALL_SPRITES];
        for (int i = 0; i < WALL_SPRITES; i++) {
            walls[i] = loadTexture("terrain_wall" + (i + 1) + ".png");
            wallRegions[i] = new TextureRegion(walls[i], 0, 0, 32, 32);
        }

        city = new Texture[CITY_SPRITES];
        cityRegions = new TextureRegion[CITY_SPRITES];
        for (int i = 0; i < CITY_SPRITES; i++) {
            city[i] = loadTexture("terrain_city" + (i + 1) + ".png");
            cityRegions[i] = new TextureRegion(city[i], 0, 0, 32, 32);
        }

        ship = loadTexture("ship.png");
        shipNormal = new Animation(0.02f, new TextureRegion(ship, 0, 0, 96, 96));
        shipNormal.setPlayMode(Animation.PlayMode.LOOP);

        healthBar = loadTexture("health_bar.png");
        healthRegion = new TextureRegion(healthBar, 0, 0, 95, 5);

        enemies = new Texture[ENEMY_SPRITES];
        enemyRegions = new TextureRegion[ENEMY_SPRITES];
        for (int i = 0; i < ENEMY_SPRITES; i++) {
            enemies[i] = loadTexture(i + 1 + ".png");
            enemyRegions[i] = new TextureRegion(enemies[i], 0, 0, 32, 32);
        }

        musicMenu = Gdx.audio.newMusic(Gdx.files.internal("sawsquarenoise_-_01_-_RottenMage_SpaceJacked_OST_01.mp3"));
        musicMenu.setVolume(0.1f);
        musicMenu.setLooping(true);

        musicGame = Gdx.audio.newMusic(Gdx.files.internal("sawsquarenoise_-_05_-_RottenMage_SpaceJacked_OST_05.mp3"));
        musicGame.setVolume(0.1f);
        musicGame.setLooping(true);

        click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        shot = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));

        crash = Gdx.audio.newSound(Gdx.files.internal("crash.wav"));
    }
}
