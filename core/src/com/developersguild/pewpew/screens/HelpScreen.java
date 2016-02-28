package com.developersguild.pewpew.screens;

/**
 * Created by briantsai on 2/26/16.
 */

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.PewPew;

public class HelpScreen extends ScreenAdapter{
    PewPew game;
    OrthographicCamera guiCam;
    private GlyphLayout layout = new GlyphLayout();
    String[] message = {"Press 'A' to move left.", "Press 'D' to move right.", "Press 'W' to shoot.", "Kill enemies to win points.", "Reach the end to win!"};
    String[] messageb = {"Tilt device left to move left.", "Tilt device right to move right.", "Tap screen to shoot.", "Kill enemies to win points.", "Reach the end to win!"};
    float xOffset = 0;



    public HelpScreen(PewPew game){
        this.game = game;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
    }

    public void update () {
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    public void draw () {
        //draw things on gimp.
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        game.batch.begin();
        game.batch.draw(Assets.bgNebulaRegion, 0, 0, 320, 1067);
        game.batch.end();

        game.batch.begin();
        Application.ApplicationType appType = Gdx.app.getType();
        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            // Get width of largest string
            for (String element : messageb) {
                layout.setText(Assets.font, element);
                xOffset = Math.max(layout.width, xOffset);
            }
            // Center the Strings
            xOffset = 160 - xOffset / 2 + Assets.font.getSpaceWidth() / 2;
            // Draw the Strings
            for (int i = 0; i < 5; i++) {
                Assets.font.draw(game.batch, messageb[i], xOffset, 300 + (i * -10));
            }

        } else {
            // Get width of largest string
            for (String element : message) {
                layout.setText(Assets.font, element);
                xOffset = Math.max(layout.width, xOffset);
            }
            // Center the Strings
            xOffset = 160 - xOffset / 2 + Assets.font.getSpaceWidth() / 2;
            // Draw the Strings
            for (int i = 0; i < 5; i++) {
                Assets.font.draw(game.batch, message[i], xOffset, 300 + (i * -10));
            }
        }

        game.batch.end();

    }

    @Override
    public void render (float delta) {
        update();
        draw();
    }
}
