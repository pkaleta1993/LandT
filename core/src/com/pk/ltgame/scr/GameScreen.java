/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.hud.GameHUD;
import com.pk.ltgame.inputs.GameInput;
import com.pk.ltgame.map.MapLoader;
import com.pk.ltgame.map.TiledMapStage;
import com.pk.ltgame.players.HumanPlayer;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import java.util.ArrayList;
/**
 *
 * @author pkale
 */
public class GameScreen extends AbstractScreen {
    private MapLoader mapLoad;
    private TiledMapStage tiledMapS;
    private HumanPlayer playerH;
     private GameHUD gamehud;
     private Texture splashgamehudimg;
      
    protected Stage stageTwo;
    ArrayList<HumanPlayer> playersList = new ArrayList<HumanPlayer>();
   
    ArrayList<Units> unitsList = new ArrayList<Units>();
    public GameScreen(LandTerrorGame game) {
        super(game);
        init();
    }

 
    
    private void init(){
     
        initMap();
     
    
 
    
    }
    private void initMap(){
        
        splashgamehudimg = new Texture("gamehud.PNG");
       playerH = new HumanPlayer("RED", 100, 10, 0, 1); // kolor, zloto, jedzenie, nauka i tura
       playersList.add(playerH);
       gamehud = new GameHUD(playerH.color, playerH.gold, playerH.food, playerH.techpoints, playerH.turn);
       mapLoad = new MapLoader();
     //tiledMapS = new TiledMapStage(mapLoad.map);
     stage.addActor(mapLoad);
    mapLoad.createMap();
      /*Działa, ale źle
    stage = new TiledMapStage(mapLoad.map);
    */
    
    stage = new TiledMapStage(mapLoad.map, playersList, gamehud);
    tiledMapS = new TiledMapStage(mapLoad.map, playersList, gamehud);
    
      System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!Hex x w hexPixArr: "+ tiledMapS.hexPixArr[1][1].x);
    //stage.addActor(tiledMapS);
      //Stage stageTwo = new TiledMapStage(mapLoad.map);
  
     
    //stage.getViewport().setCamera(camera);
    // Gdx.input.setInputProcessor(stage);
 
   
    }
    @Override
    public void render(float delta) {
       super.render(delta);
       mapLoad.renderMap();
       update();
      
       
      
       spriteBatch.begin();
      spriteBatch.draw(splashgamehudimg, 0, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/16, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/16);
         spriteBatch.end();
       
       gamehud.stage.draw();
       stage.getViewport().setCamera(camera);
       spriteBatch.setProjectionMatrix(gamehud.stage.getCamera().combined); 
       tiledMapS.renderObjects();
       stage.draw();
       update();
       gamehud.update(Gdx.graphics.getDeltaTime());
       
    }
    private void update(){
        
       // System.out.println(Gdx.graphics.getDeltaTime());
        stage.act();
    }
     @Override
    public void dispose() {
        
        splashgamehudimg.dispose();
    }
}
