/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.pk.ltgame.LandTerrorGame;

/**
 *
 * @author pkale
 */
public class SplashScr extends AbstractScreen {
    private Texture splashImg;
    private Texture splashBGImg;
    //public static final String splashImgDir = "splash.jpg";
   // public static final String splashBGImgDir = "splashBG.jpg";

    public SplashScr(LandTerrorGame game) {
     super(game);
     init();
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
