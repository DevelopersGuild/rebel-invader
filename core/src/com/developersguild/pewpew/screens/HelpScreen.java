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
    String message = "Press 'A' to move left.";
    String messageb = "Tilt device left to move left.";
    String message2 = "Press 'D' to move right.";
    String message2b = "Tilt device right to move right.";
    String message3 = "Press 'W' to shoot.";
    String message3b = "Tap screen to shoot.";
    String message4 = "Kill enemies to win points.";
    String message5 = "Reach the end to win!";



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
        game.batch.draw(Assets.bgNebula, 0, 0, 320, 480);
        game.batch.end();

        game.batch.begin();
        Application.ApplicationType appType = Gdx.app.getType();
        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            layout.setText(Assets.font, messageb);
            Assets.font.draw(game.batch, messageb, 80, 300);
            layout.setText(Assets.font, message2b);
            Assets.font.draw(game.batch, message2b, 80, 290);
            layout.setText(Assets.font, message3b);
            Assets.font.draw(game.batch, message3b, 80, 280);
            layout.setText(Assets.font, message4);
            Assets.font.draw(game.batch, message4, 80, 270);
            layout.setText(Assets.font, message5);
            Assets.font.draw(game.batch, message5, 80, 260);
        } else {
            layout.setText(Assets.font, message);
            Assets.font.draw(game.batch, message, 80, 300);
            layout.setText(Assets.font, message2);
            Assets.font.draw(game.batch, message2, 80, 290);
            layout.setText(Assets.font, message3);
            Assets.font.draw(game.batch, message3, 80, 280);
            layout.setText(Assets.font, message4);
            Assets.font.draw(game.batch, message4, 80, 270);
            layout.setText(Assets.font, message5);
            Assets.font.draw(game.batch, message5, 80, 260);
        }


        game.batch.end();

    }

    @Override
    public void render (float delta) {
        update();
        draw();
    }
}
