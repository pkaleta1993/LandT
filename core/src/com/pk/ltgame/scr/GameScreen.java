/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.hex.OffsetCoord;
import com.pk.ltgame.hud.GameHUD;
import com.pk.ltgame.hud.TechHUD;
import com.pk.ltgame.inputs.GameInput;
import com.pk.ltgame.map.MapLoader;
import com.pk.ltgame.map.TiledMapStage;
import com.pk.ltgame.players.HumanPlayer;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.players.AIPlayer;
import java.util.ArrayList;
/**
 *
 * @author pkale
 */
public class GameScreen extends AbstractScreen {
    private MapLoader mapLoad;
    private TiledMapStage tiledMapS;
    private HumanPlayer playerH;
    private AIPlayer playerAI;
     private GameHUD gamehud;
     private Texture splashgamehudimg;
     private String race; 
     private TechHUD techhud;
      private ParticleEffect party = new ParticleEffect();
    public ArrayList<Boolean> tech;
    private ParticleEffectPool partyPool;
    private PooledEffect effect;
     /**
     *
     */
    protected Stage stageTwo;
    ArrayList<HumanPlayer> playersList = new ArrayList<HumanPlayer>();
   ArrayList<AIPlayer> aiplayersList = new ArrayList<AIPlayer>();
    ArrayList<Units> unitsList = new ArrayList<Units>();
    ArrayList<PooledEffect> effects = new ArrayList<PooledEffect>();
    private static ArrayList<OffsetCoord> partyListCoord = new ArrayList<OffsetCoord>();



    /**
     *
     * @param game
     */
    public GameScreen(LandTerrorGame game, String race) {
        super(game);
        this.race = race;
        init();
    }

 
    
    private void init(){
     
        initMap();
        
        party.load(Gdx.files.internal("data/hexsmoke"),Gdx.files.internal("data"));
       // party.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
       // party.start();
        //party.setEmittersCleanUpBlendFunction(false);
        partyPool = new ParticleEffectPool(party, 1, 200);
        effect = partyPool.obtain();
        //effect.setPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        //effects.add(effect);
        //effect.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()/2-100);
        //effects.add(effect);
        System.out.println("Rozmiar poola: "+effects.size());
       
    }
    private void initMap(){
        tech = new ArrayList<Boolean>();
        tech.add(Boolean.FALSE);
        tech.add(Boolean.FALSE);
        tech.add(Boolean.FALSE);
        splashgamehudimg = new Texture("gamehud.PNG");
       playerH = new HumanPlayer("RED", 100, 10, 0, 1, race, tech); // kolor, zloto, jedzenie, nauka i tura
       playerAI = new AIPlayer("BLUE",150,20,0,1, "Humans");
       playersList.add(playerH);
       aiplayersList.add(playerAI);
        
       gamehud = new GameHUD(playerH.color, playerH.gold, playerH.food, playerH.techpoints, playerH.turn);
       techhud = new TechHUD(tech);
       mapLoad = new MapLoader();
     //tiledMapS = new TiledMapStage(mapLoad.map);
     stage.addActor(mapLoad);
    mapLoad.createMap();
      /*Działa, ale źle
    stage = new TiledMapStage(mapLoad.map);
    */
    
    stage = new TiledMapStage(mapLoad.map, playersList, aiplayersList, gamehud, techhud);
    //tiledMapS = new TiledMapStage(mapLoad.map, playersList, gamehud);
    tiledMapS = (TiledMapStage) stage;
    
      System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!Hex x w hexPixArr: "+ tiledMapS.hexPixArr[1][1].x);
    //stage.addActor(tiledMapS);
      //Stage stageTwo = new TiledMapStage(mapLoad.map);
  
     
    //stage.getViewport().setCamera(camera);
    // Gdx.input.setInputProcessor(stage);
   // FileHandle save = Gdx.files.local("data/aa.json");
     //   Json json = new Json();
                   //  json.toJson(playersList, save);
                   //json.toJson(tiledMapS.)
   
    }
    public void getOC(){
        this.partyListCoord = tiledMapS.returnInvisible();
        this.effects.clear();
        for(int i=0;i<partyListCoord.size();i++)
        {
        this.effects.add(effect);
        }
    }
    
    
    @Override
    public void render(float delta) {
        
       super.render(delta);
       mapLoad.renderMap();
       
      getOC();
       
      
       
       
       stage.getViewport().setCamera(camera);
        
       tiledMapS.renderObjects();
       
       
       stage.draw();
      // party.update(Gdx.graphics.getDeltaTime());
       
       spriteBatch.begin();
      
         
     // party.draw(spriteBatch);
     /* for (int i = effects.size() - 1; i >= 0; i--) {
//      System.out.println(i+": "+effects.get(i).setPosition(i*100, i*100));
    PooledEffect effect = effects.get(i);
    effect.setPosition(198*i+198/2, 172*i+172/2);
    effect.draw(spriteBatch, delta);
    if (effect.isComplete()) {
       
        //effects.removeValue(effect, true);
        effects.remove(effect);
         effect.free();
    }
}
*/
  //System.out.println("Rozmiar partyListCoord w GameScreen: "+ partyListCoord.size());
       for (int i = partyListCoord.size() - 1; i >= 0; i--) {
//      System.out.println(i+": "+effects.get(i).setPosition(i*100, i*100));
    PooledEffect effect = effects.get(i);
    //System.out.println(i+": (hexPixArr["+partyListCoord.get(i).col+"]["+tiledMapS.swapCoords(partyListCoord.get(i).row)+"] = " );
     if(partyListCoord.get(i).col %2 == 0  )
     {
    effect.setPosition((int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].x,(int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].y-91);
     } else {
     effect.setPosition((int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].x,(int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].y+81);
        
     }
    effect.draw(spriteBatch, delta);
    if (effect.isComplete()) {
       
        //effects.removeValue(effect, true);
        effects.remove(effect);
         effect.free();
    }
}
       spriteBatch.draw(splashgamehudimg, 0, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/16, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/16);
      spriteBatch.end();
     // if (party.isComplete()) party.reset();
  
       gamehud.stage.draw();
       
       spriteBatch.setProjectionMatrix(gamehud.stage.getCamera().combined);
       gamehud.update(Gdx.graphics.getDeltaTime());
       
       techhud.stage.draw();
       
              update();
              checkEnd();
             for (int i = effects.size() - 1; i >= 0; i--)
    effects.get(i).free(); //free all the effects back to the pool
//effects.clear(); //clear the current effects array

       
    }
    
    public void checkEnd(){
        //System.out.println("Rozmiar: "+ tiledMapS.buildingsList);
         if(tiledMapS.buildingsList.get(0).playerColor != "RED"){
               
               game.setScreen(new EndScreen(game, false));
//game.setScreen(new MenuSettingsScreen(game));
           } else if(tiledMapS.buildingsList.get(1).playerColor != "BLUE"){
               
               game.setScreen(new EndScreen(game, true));
               
           }
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
