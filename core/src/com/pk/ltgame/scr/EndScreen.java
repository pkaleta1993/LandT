package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pk.ltgame.LandTerrorGame;

/**
 * Klasa ekranu kończącego grę.
 * @author pkale
 */
public class EndScreen extends AbstractScreen {
    private Texture splashImg;
    private Texture splashBGImg;
    private Label win;
    private final BitmapFont font = new BitmapFont();
    private String winText;

    /**
     * Obiekt gry.
     */
    public static LandTerrorGame newGame;
    
    /**
     * Konstruktor ekranu końcowego.
     * @param game Obiekt gry.
     * @param winBoolean True, jeżeli wygrana gracza; False, jeżeli przegrana.
     */
    public EndScreen( final LandTerrorGame game, boolean winBoolean) {
        super(game);
        init(winBoolean);
        Timer.schedule(new Task(){
        @Override
        public void run() {
            Gdx.app.exit();
        }
        }, 4);
    }  
    
    /**
     * Inicjalizacja  generowanego tekstu o końcu gry.
     * @param winBoolean True, jeżeli wygrana gracza; False, jeżeli przegrana.
     */
    private void init(boolean winBoolean){
        if(winBoolean==false){
            winText = "Przegrana";
        } else {
            winText = "Wygrana";
        }
        font.getData().setScale(4, 4);
    }
    
    @Override
    public void render(float delta){
        super.render(delta);
        spriteBatch.begin();
        font.draw(spriteBatch, winText, Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2+25);
        spriteBatch.end();
        }
}
