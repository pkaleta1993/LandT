/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pk.ltgame.LandTerrorGame;


/**
 *
 * @author pkale
 */
public abstract class AbstractScreen implements Screen {

protected LandTerrorGame game;
protected Stage stage;
private OrthographicCamera camera;

protected SpriteBatch spriteBatch;

public AbstractScreen(LandTerrorGame game) {
    this.game = game;
    createCamera();
    stage = new Stage(new StretchViewport(LandTerrorGame.WIDTH, LandTerrorGame.HEIGHT, camera));
    spriteBatch = new SpriteBatch();
    Gdx.input.setInputProcessor(stage);
}

private void createCamera() {
    camera = new OrthographicCamera();
    camera.setToOrtho(false, LandTerrorGame.WIDTH, LandTerrorGame.HEIGHT);
    camera.update();
}
@Override
public void render(float delta) {
    clearScreen();
    camera.update();
    spriteBatch.setProjectionMatrix(camera.combined);
}
@Override
public void show(){}

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
