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
    public static Music musicWin;
    public static Sound click;
    public static Sound shot;
    public static Sound crash;
    public static Sound launch;
    public static Sound explosion;
    public static Sound powerup;

    public static BitmapFont font;

    public static Texture riBanner;

    public static Texture dgLogo;

    public static Texture playText;

    public static Texture exitText;

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

    public static Texture healthBarRed;
    public static TextureRegion healthRegionRed;

    public static Texture healthBarGreen;
    public static TextureRegion healthRegionGreen;

    public static Texture healthBarBlue;
    public static TextureRegion healthRegionBlue;

    public static Texture[] enemies;
    public static TextureRegion[] enemyRegions;

    public static Texture explosionTex;
    public static TextureRegion explosionTexRegion;

    public static Texture missileButton;
    public static TextureRegion missileButtonRegion;

    public static Texture missileIcon;
    public static TextureRegion missileIconRegion;

    public static Texture detonateIcon;
    public static TextureRegion detonateIconRegion;

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);

        riBanner = loadTexture("rebelinvader-banner.png");

        dgLogo = loadTexture("dglogo.png");

        playText = loadTexture("play.png");

        exitText = loadTexture("exit.png");

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

        healthBarRed = loadTexture("health_bar_red.png");
        healthRegionRed = new TextureRegion(healthBarRed, 0, 0, 95, 5);

        healthBarGreen = loadTexture("health_bar_green.png");
        healthRegionGreen = new TextureRegion(healthBarGreen, 0, 0, 95, 5);

        healthBarBlue = loadTexture("health_bar_blue.png");
        healthRegionBlue = new TextureRegion(healthBarBlue, 0, 0, 95, 5);

        enemies = new Texture[ENEMY_SPRITES];
        enemyRegions = new TextureRegion[ENEMY_SPRITES];
        for (int i = 0; i < ENEMY_SPRITES; i++) {
            enemies[i] = loadTexture(i + 1 + ".png");
            enemyRegions[i] = new TextureRegion(enemies[i], 0, 0, 32, 32);
        }

        explosionTex = loadTexture("explosion.png");
        explosionTexRegion = new TextureRegion(explosionTex, 0, 0, 128, 128);

        missileButton = loadTexture("missilebutton.png");
        missileButtonRegion = new TextureRegion(missileButton, 0, 0, 50, 50);

        missileIcon = loadTexture("missileicon.png");
        missileIconRegion = new TextureRegion(missileIcon, 0, 0, 50, 50);

        detonateIcon = loadTexture("detonate.png");
        detonateIconRegion = new TextureRegion(detonateIcon, 0, 0, 50, 50);

        musicMenu = Gdx.audio.newMusic(Gdx.files.internal("sawsquarenoise_-_01_-_RottenMage_SpaceJacked_OST_01.mp3"));
        musicMenu.setVolume(0.5f);
        musicMenu.setLooping(true);

        musicGame = Gdx.audio.newMusic(Gdx.files.internal("sawsquarenoise_-_05_-_RottenMage_SpaceJacked_OST_05.mp3"));
        musicGame.setVolume(0.5f);
        musicGame.setLooping(true);

        musicWin = Gdx.audio.newMusic(Gdx.files.internal("sawsquarenoise_-_16_-_RottenMage_SpaceJacked_OST_JINGLE_01.mp3"));
        musicWin.setVolume(0.5f);

        click = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        shot = Gdx.audio.newSound(Gdx.files.internal("shot.wav"));

        crash = Gdx.audio.newSound(Gdx.files.internal("crash.wav"));

        launch = Gdx.audio.newSound(Gdx.files.internal("missilesound.wav"));

        explosion = Gdx.audio.newSound(Gdx.files.internal("explosionsound.wav"));

        powerup = Gdx.audio.newSound(Gdx.files.internal("Powerup.wav"));
    }
}
