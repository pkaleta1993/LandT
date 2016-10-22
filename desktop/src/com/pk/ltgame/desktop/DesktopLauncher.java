package com.pk.ltgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pk.ltgame.LandTerrorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.title = LandTerrorGame.GAME_NAME;
                config.height = LandTerrorGame.HEIGHT;
                config.width = LandTerrorGame.WIDTH;
                config.resizable = false;
                new LwjglApplication(new LandTerrorGame(), config);
	}
}
