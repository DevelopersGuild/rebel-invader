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
import com.badlogic.gdx.files.FileHandle;

public class Settings {
    public final static int[] highscores = new int[]{1000, 900, 800, 700, 600, 500, 400, 300, 200, 100};
    public final static String file = ".risettings";

    public static void load() {
        try {
            FileHandle filehandle = Gdx.files.external(file);

            String[] strings = filehandle.readString().split("\n");

            for (int i = 0; i < 10; i++) {
                highscores[i] = Integer.parseInt(strings[i]);
            }
        } catch (Throwable e) {
            // :( It's ok we have defaults
        }
    }

    public static void save() {
        try {
            FileHandle filehandle = Gdx.files.external(file);

            filehandle.delete();    // Deletes the old scores

            for (int i = 0; i < 10; i++) {
                filehandle.writeString(Integer.toString(highscores[i]) + "\n", true);
            }
        } catch (Throwable e) {
        }
    }

    public static void addScore(int score) {
        for (int i = 0; i < 10; i++) {
            if (highscores[i] < score) {
                for (int j = 9; j > i; j--)
                    highscores[j] = highscores[j - 1];
                highscores[i] = score;
                break;
            }
        }
    }
}
