package com.pk.ltgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pk.ltgame.LandTerrorGame;

/**
 * Klasa uruchamiania wersji na komputery.
 * @author pkale
 */
public class DesktopLauncher {

    /**
     *
     * @param arg argumenty aplikacji
     */
    public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.title = LandTerrorGame.GAME_NAME;
                config.height = LandTerrorGame.HEIGHT;
                config.width = LandTerrorGame.WIDTH;
                config.resizable = false;
         //       config.fullscreen = true;
                new LwjglApplication(new LandTerrorGame(), config);
	}
}
