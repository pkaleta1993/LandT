package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pk.ltgame.LandTerrorGame;


/**
 * Klasa abstrakcyjna dla obsługi ekranów.
 * @author pkale
 */
public abstract class AbstractScreen implements Screen {

    /**
     * Obiekt gry.
     */
    protected LandTerrorGame game;

    /**
     * Scena dla ekranu.
     */
    protected Stage stage;
    OrthographicCamera camera;

    /**
     * Sprite dla ekranu.
     */
    protected SpriteBatch spriteBatch;

    /**
     * Konstruktor ekranu.
     * @param game Obiekty gry.
     */
    public AbstractScreen(LandTerrorGame game) {
        this.game = game;
        createCamera();
        stage = new Stage(new StretchViewport(LandTerrorGame.WIDTH, LandTerrorGame.HEIGHT, camera));
        spriteBatch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Stworzenie kamery na ekran.
     */
    private void createCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LandTerrorGame.WIDTH, LandTerrorGame.HEIGHT);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
    }

    
    
    @Override
    public void show(){}

    /**
     * Czyszczenie ekranu.
     */
    private void clearScreen() {
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    
    @Override
    public void resume(){
        game.setPaused(false);
    }
    @Override
    public void pause() {
        game.setPaused(true);
    }
    @Override
    public void hide(){}
    @Override
    public void dispose(){
        game.dispose();
    }
    @Override
 public void resize(int widht, int height){
     
}

}
