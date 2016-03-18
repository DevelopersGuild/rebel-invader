/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package io.developersguild.rebelinvader.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.RebelInvader;

public class HighscoresScreen extends ScreenAdapter {
    RebelInvader game;
    Settings settings;
    OrthographicCamera guiCam;
    String[] highScores;
    float xOffset = 0;
    private GlyphLayout layout = new GlyphLayout();

    public HighscoresScreen(RebelInvader game) {
        this.game = game;
        settings.load();

        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        highScores = new String[10];

        // One digit wide rank
        for (int i = 0; i < 9; i++) {
            highScores[i] = i + 1 + ".  " + Settings.highscores[i];
            layout.setText(Assets.font, highScores[i]);
            xOffset = Math.max(layout.width, xOffset);
        }

        // Two digit wide rank
        highScores[9] = 10 + ". " + Settings.highscores[9];
        layout.setText(Assets.font, highScores[9]);
        xOffset = Math.max(layout.width, xOffset);

        // Center the string
        xOffset = 150 - xOffset / 2 + Assets.font.getSpaceWidth() / 2;
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen(game));
            Assets.click.play();
        }
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        guiCam.update();

        game.batch.setProjectionMatrix(guiCam.combined);
        game.batch.disableBlending();
        game.batch.begin();
        game.batch.draw(Assets.bgNebulaRegion, 0, 0, 320, 1067);
        game.batch.end();

        game.batch.enableBlending();
        game.batch.begin();

        // This draws bottom to top
        float y = 130;
        for (int i = 9; i >= 0; i--) {
            Assets.font.draw(game.batch, highScores[i], xOffset, y);
            y += Assets.font.getLineHeight() * 2;
        }
        y += Assets.font.getLineHeight();
        Assets.font.draw(game.batch, "Highscores", xOffset, y);

        game.batch.end();
    }

    @Override
    public void render(float delta) {
        update();
        draw();
    }
}
