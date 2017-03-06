package com.pk.ltgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pk.ltgame.scr.GameScreen;
import com.pk.ltgame.scr.MenuScreen;
import com.pk.ltgame.scr.SplashScreen;

/**
 *
 * @author pkale
 */
public class LandTerrorGame extends Game {

    /**
     *
     */
    public final static String GAME_NAME = "Land Terror";

    /**
     *
     */
    public final static int WIDTH = 1024;

    /**
     *
     */
    public final static int HEIGHT = 768;
        
        private boolean paused;
	
	
	@Override
	public void create () {
	this.setScreen(new SplashScreen(this));
        
	}

    /**
     *
     * @return
     */
    public boolean isPaused() {
            return paused;
        }
     public void reNew(){
         LandTerrorGame newGame = new LandTerrorGame();
         // GameScreen game = new GameScreen(this);
     
         newGame.create();
         this.screen = newGame.screen;
         this.setScreen(new MenuScreen(newGame));
         this.create();
            //this.create();
        // this.setScreen(newGame.getScreen());
         //newGame.create();
         //this.dispose();
        // this.create();
     }
    /**
     *
     * @param paused
     */
    public void setPaused(boolean paused) {
            this.paused = paused;
        }
}
