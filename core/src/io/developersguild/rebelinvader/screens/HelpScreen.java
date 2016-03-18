package io.developersguild.rebelinvader.screens;

/**
 * Created by briantsai on 2/26/16.
 */

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.RebelInvader;

public class HelpScreen extends ScreenAdapter {
    RebelInvader game;
    OrthographicCamera guiCam;
    String[] message = {"Press 'A' to move left.", "Press 'D' to move right.", "Press 'W' to shoot.",
            "Press 'SPACEBAR' to launch missile.", "Press 'SPACEBAR' again to detonate", "Wait 10 seconds to reload missile.",
            "Kill enemies to win points.", "Avoid damage (10% score penalty)", "Reach the end to win!"};
    String[] messageb = {"Tilt device left to move left.", "Tilt device right to move right.", "Tap screen to shoot.",
            "Tap missile icon to launch missile.", "Press icon again to detonate", "Wait 10 seconds to reload missile.",
            "Kill enemies to win points.", "Avoid damage (10% score penalty)", "Reach the end to win!"};
    float xOffset = 0;
    private GlyphLayout layout = new GlyphLayout();


    public HelpScreen(RebelInvader game) {
        this.game = game;
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
            Assets.click.play();
        }
    }

    public void draw() {
        //draw things on gimp.
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();
        game.batch.begin();
        game.batch.draw(Assets.bgNebulaRegion, 0, 0, 320, 1067);
        game.batch.end();

        game.batch.begin();
        Assets.font.draw(game.batch, "Help", 320 / 2 - 15, 390);
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
            for (int i = 0; i < messageb.length; i++) {
                Assets.font.draw(game.batch, messageb[i], xOffset, 350 + (i * -30));
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
            for (int i = 0; i < message.length; i++) {
                Assets.font.draw(game.batch, message[i], xOffset, 350 + (i * -30));
            }
        }

        game.batch.end();

    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }
}
