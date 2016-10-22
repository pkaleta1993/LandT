package com.pk.ltgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pk.ltgame.splash.SplashScr;

public class LandTerrorGame extends Game {
        public final static String GAME_NAME = "Land Terror";
        public final static int WIDTH = 600;
        public final static int HEIGHT = 500;
        
        private boolean paused;
	
	
	@Override
	public void create () {
	this.setScreen(new SplashScr(this));
	}

	
        public boolean isPaused() {
            return paused;
        }
        
        public void setPaused(boolean paused) {
            this.paused = paused;
        }
}
