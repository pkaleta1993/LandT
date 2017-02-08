/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;

/**
 *
 * @author pkale
 */

public class MenuScreen extends AbstractScreen{
 // private Texture splashImg;
    private Texture splashBGImg;
    private SpriteBatch batch;
   // protected Stage stage;
    private Viewport viewport;
  //  private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
   private LandTerrorGame game;
   
    public MenuScreen(final LandTerrorGame game)
    {
      
        super(game);
        this.game = game;
         
     //   atlas = new TextureAtlas("menuskin.atlas");
      //  skin = new Skin(Gdx.files.internal("menuskin.json"), atlas);
        atlas = new TextureAtlas("menuskin.atlas");
        skin = new Skin(Gdx.files.internal("menuskin.json"), atlas);

         //splashImg = new Texture("splash.png");
         splashBGImg = new Texture("menuBG.png");
        
         batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

       // stage = new Stage(viewport, batch);

        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);
        
      
    }


    @Override
    public void show() {
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.top();

        //Create buttons
        TextButton playButton = new TextButton("Play", skin,"container_gold");
        TextButton optionsButton = new TextButton("Options", skin,"container_gold");
       TextButton exitButton = new TextButton("Exit", skin,"container_gold");

        //Add listeners to buttons
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               //((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                           game.setScreen(new MenuSettingsScreen(game));            
// game.setScreen(new MenuScreen(game));
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        mainTable.add(playButton);
        mainTable.row();
        mainTable.add(optionsButton);
        mainTable.row();
        mainTable.add(exitButton);

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
       
        super.render(delta);
        
        spriteBatch.begin();
        spriteBatch.draw(splashBGImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //spriteBatch.draw(splashImg, Gdx.graphics.getWidth()/2-265, Gdx.graphics.getHeight()/2-52);
        spriteBatch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
        splashBGImg.dispose();
    }
}