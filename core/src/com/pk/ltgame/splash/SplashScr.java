/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.splash;

import com.badlogic.gdx.graphics.Texture;
import com.pk.ltgame.LandTerrorGame;

/**
 *
 * @author pkale
 */
public class SplashScr extends AbstractScreen {
    private Texture splashImg;
    
    public SplashScr(LandTerrorGame game) {
     super(game);
     init();
 }   
 
    private void init(){
        splashImg = new Texture("badlogic.jpg");
    }
    @Override
    public void render(float delta){
        super.render(delta);
        
        spriteBatch.begin();
        spriteBatch.draw(splashImg, 0, 0);
        spriteBatch.end();
    }
}
