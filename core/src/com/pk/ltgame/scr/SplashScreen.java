package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pk.ltgame.LandTerrorGame;

/**
 * Konstruktor ekranu powitalnego.
 * @author pkale
 */
public class SplashScreen extends AbstractScreen {
    private Texture splashImg;
    private Texture splashBGImg;
    
    /**
     * Konstruktor dla ekranu powitalnego.
     * @param game Obiekt gry.
     */
    public SplashScreen(final LandTerrorGame game) {
        super(game);
        init();
        Timer.schedule(new Task(){
            @Override
            public void run() {
                game.setScreen(new MenuScreen(game));
            }
        }, 1);
    }   
    
    /**
     * Inicjalizacja zasobów.
     */
    private void init(){
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
