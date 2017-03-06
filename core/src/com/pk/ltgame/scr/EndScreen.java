/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pk.ltgame.LandTerrorGame;

/**
 *
 * @author pkale
 */
public class EndScreen extends AbstractScreen {
    private Texture splashImg;
    private Texture splashBGImg;
    private Label win;
    private Stage stage;
    private BitmapFont font = new BitmapFont();
    private String winText;
    public static LandTerrorGame newGame;
    
    //public static final String splashImgDir = "splash.jpg";
   // public static final String splashBGImgDir = "splashBG.jpg";

    /**
     *
     * @param game
     */
    public EndScreen( final LandTerrorGame game, boolean winBoolean) {
    super(game);
    //newGame = new LandTerrorGame();
      
     
     init(winBoolean);
    // super.game = new LandTerrorGame();
    // super().stage = new Stage;
    // newGame = game;
   //  game.dispose();
     Timer.schedule(new Task(){
     @Override
     public void run() {
      // game.reNew();
      // Gdx.app.exit();
      //LandTerrorGame newGame = new LandTerrorGame();
       //game.create();
       //newGame.create();
       //newGame.setScreen(newGame.getScreen());
       
     //  newGame.setScreen(new MenuScreen(newGame));
     //  game.setScreen(newGame.screen);
    // newGame.setScreen(new MenuScreen(newGame));
        Gdx.app.exit();
         //game.setScreen(new GameScreen(game));
     }
     }, 4);
 }   
 
    private void init(boolean winBoolean){
        AssetManager manager = new AssetManager();
        if(winBoolean==false){
       // win = new Label(String.format("Przegrana"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        winText = "Przegrana";
        } else {
          // win = new Label(String.format("Wygrana"), new Label.LabelStyle(new BitmapFont(), Color.WHITE)); 
          winText = "Wygrana";
        }
       // win.setHeight(200);
        font.getData().setScale(4, 4);
        
        //stage.addActor(win);
       splashImg = new Texture("splash.png");
       splashBGImg = new Texture("splashBG.png");
       
      
    }
    @Override
    public void render(float delta){
        super.render(delta);
        
        spriteBatch.begin();
        //spriteBatch.draw(splashBGImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       // spriteBatch.draw(splashImg, Gdx.graphics.getWidth()/2-265, Gdx.graphics.getHeight()/2-52);
        font.draw(spriteBatch, winText, Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2+25);
        
        spriteBatch.end();
        //stage.draw();
        
    }
}
