package com.pk.ltgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.pk.ltgame.scr.MenuScreen;
import com.pk.ltgame.scr.SplashScreen;

/**
 * Klasa odpowiedzialna za uruchamianie gry i ustawienie podstawowych właściwości.
 * @author pkale
 */
public class LandTerrorGame extends Game {

    /**
     * Nazwa aplikacji.
     */
    public final static String GAME_NAME = "Land Terror";

    /**
     * Szerokość okna aplikacji.
     */
    public final static int WIDTH = 1024;
    
    /**
     * Wysokość okna aplikacji.
     */
    public final static int HEIGHT = 768;
    private Music music;
    private boolean paused;
     /**
     * Kontener preferencji
     */
    private Preferences p;
	
	@Override
	public void create () {
            music = Gdx.audio.newMusic(Gdx.files.internal("Komiku-07-Theplacethatnevergetold.mp3"));
            p = Gdx.app.getPreferences("settings");
            float musicVolume = 1f;
            musicVolume = p.getFloat("volume");
            boolean musicOff = false;
            musicOff = p.getBoolean("switch");
            setVolume(musicVolume);
            turnOffMusic(musicOff);
            music.play();
            this.setScreen(new SplashScreen(this));
        
	}

    /**
     *
     * @return Zwraca stan pauzy dla aplikacji.
     */
    public boolean isPaused() {
            return paused;
        }

    /**
     *
     * @param vol Ustawia głośność aplikacji - od 0 do 1.
     */
    public void setVolume(float vol) {
     music.setVolume(vol);
    }

    /**
     *
     * @param switchMusic Ustawia odtwarzanie muzyki. 0 - graj, 1 - zatrzymaj.
     */
    public void turnOffMusic(boolean switchMusic){
        if(switchMusic) music.stop(); else music.play();
    }

    /**
     * Resetowanie stanu gry.
     */
    public void reNew(){
         LandTerrorGame newGame = new LandTerrorGame();     
         newGame.create();
         this.screen = newGame.screen;
         this.setScreen(new MenuScreen(newGame));
         this.create();
     }
    /**
     *
     * @param paused Ustawia stan pauzy. True dla zatrzymania, a false dla odtwarzania.
     */
    public void setPaused(boolean paused) {
            this.paused = paused;
        }
}
