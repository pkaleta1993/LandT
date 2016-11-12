/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.inputs.GameInput;
import com.pk.ltgame.map.MapLoader;
import com.pk.ltgame.map.TiledMapStage;
/**
 *
 * @author pkale
 */
public class GameScreen extends AbstractScreen {
    private MapLoader mapLoad;
    private TiledMapStage tiledMapS;
    protected Stage stageTwo;
    public GameScreen(LandTerrorGame game) {
        super(game);
        init();
    }
    
    private void init(){
     initMap();
    }
    private void initMap(){
      mapLoad = new MapLoader();
     //tiledMapS = new TiledMapStage(mapLoad.map);
     stage.addActor(mapLoad);
    mapLoad.createMap();
      /*Działa, ale źle
    stage = new TiledMapStage(mapLoad.map);
    */
    stage = new TiledMapStage(mapLoad.map);
    //stage.addActor(tiledMapS);
      //Stage stageTwo = new TiledMapStage(mapLoad.map);
    GameInput inputProcessor = new GameInput();
    Gdx.input.setInputProcessor(inputProcessor);
    //  stage.getViewport().setCamera(camera);
    // Gdx.input.setInputProcessor(stage);
      
    }
    @Override
    public void render(float delta) {
       super.render(delta);
       mapLoad.renderMap();
       update();
       spriteBatch.begin();
       stage.draw();
        
       stage.getViewport().setCamera(camera);
        
       spriteBatch.end();
    }
    private void update(){
        
        stage.act();
    }
}
