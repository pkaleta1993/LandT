/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pk.ltgame.LandTerrorGame;

/**
 *
 * @author pkale
 */
public class SplashScreen extends AbstractScreen {
    private Texture splashImg;
    private Texture splashBGImg;
    //public static final String splashImgDir = "splash.jpg";
   // public static final String splashBGImgDir = "splashBG.jpg";

    public SplashScreen(final LandTerrorGame game) {
     super(game);
     init();
     Timer.schedule(new Task(){
     @Override
     public void run() {
         game.setScreen(new MenuScreen(game));
         //game.setScreen(new GameScreen(game));
     }
     }, 1);
 }   
 
    private void init(){
        AssetManager manager = new AssetManager();

     
       splashImg = new Texture("splash.png");
       splashBGImg = new Texture("splashBG.png");
    }
    @Override
    public void render(float delta){
        super.render(delta);
        
        spriteBatch.begin();
        spriteBatch.draw(splashBGImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.draw(splashImg, Gdx.graphics.getWidth()/2-265, Gdx.graphics.getHeight()/2-52);
        spriteBatch.end();
    }
}
