package com.developersguild.pewpew.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.developersguild.pewpew.PewPew;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pew Pew";
		config.width = 480;
		config.height = 800;
		new LwjglApplication(new PewPew(), config);
	}
}
