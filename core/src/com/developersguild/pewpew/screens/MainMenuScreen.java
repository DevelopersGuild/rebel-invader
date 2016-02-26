package com.developersguild.pewpew.screens;

/**
 * Created by Denny on 2/13/2016.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.PewPew;


public class MainMenuScreen extends ScreenAdapter {

    PewPew game;
    OrthographicCamera guiCam;
    Rectangle playBounds;
    Rectangle scoreBounds;
    Rectangle exitBounds;
    Vector3 touchPoint;


    public MainMenuScreen(PewPew game) {
        this.game = game;

        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        playBounds = new Rectangle(160 - 150, 200 + 18, 300, 36);
        scoreBounds = new Rectangle(160 - 150, 200 - 18 - 36, 300, 36);
        exitBounds = new Rectangle(10, 94, 300, 36);
        touchPoint = new Vector3();
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (playBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new GameScreen(game));
                return;
            }
            if (exitBounds.contains(touchPoint.x, touchPoint.y)) {
                Gdx.app.exit();
                return;
            }

            if (scoreBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new HighscoresScreen(game));
                return;
            }
        }
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        game.batch.setProjectionMatrix(guiCam.combined);

        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(Assets.bgNebulaRegion, 0, 0, 320, 1067);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();
        //Draw 'Play' and 'Exit' buttons
        game.batch.draw(Assets.playText, 10, 218, 300, 36);
        game.batch.draw(Assets.scoreText, 10, 156, 300, 36);
        game.batch.draw(Assets.exitText, 10, 94, 300, 36);

        game.batch.end();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }
}


