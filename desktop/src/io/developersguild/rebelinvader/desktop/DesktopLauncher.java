package io.developersguild.rebelinvader.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import io.developersguild.rebelinvader.RebelInvader;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Rebel Invader";
        config.width = 480;
        config.height = 800;
        new LwjglApplication(new RebelInvader(), config);
    }
}
