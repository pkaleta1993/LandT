/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.map.MapLoader;
/**
 *
 * @author pkale
 */
public class GameScreen extends AbstractScreen {
    private MapLoader mapLoad;
    
    public GameScreen(LandTerrorGame game) {
        super(game);
        init();
    }
    
    private void init(){
     initMap();
    }
    private void initMap(){
      mapLoad = new MapLoader();
      stage.addActor(mapLoad);
      mapLoad.createMap();
      
    }
    @Override
    public void render(float delta) {
       super.render(delta);
       mapLoad.renderMap();
       update();
       spriteBatch.begin();
       stage.draw();
       spriteBatch.end();
    }
    private void update(){
        stage.act();
    }
}
