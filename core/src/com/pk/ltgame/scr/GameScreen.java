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
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    ArrayList<TileBuildings> buildingsList = new ArrayList<TileBuildings>();
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
    
    public GameScreen(LandTerrorGame game, FileHandle saveData) {
        super(game);
        XmlReader reader = new XmlReader();
        GameHUD gameHUD;
        try {
            Element root = reader.parse(saveData);
            Array<Element> o = root.getChildrenByName("GameHUD");
            for(Element e: o )
            {
                String playerColor = e.getAttribute("playerColor");   
                int gold = Integer.parseInt(e.getAttribute("gold"));
                int food = Integer.parseInt(e.getAttribute("food"));
                float techPoints = Float.parseFloat(e.getAttribute("techPoints"));
                int turn = Integer.parseInt(e.getAttribute("turn"));
                gameHUD = new GameHUD(playerColor, gold, food, techPoints, turn);
                this.gamehud = gameHUD;
            }
            o = root.getChildrenByName("HumanPlayer");
             ArrayList<HumanPlayer> playersList = new ArrayList<HumanPlayer>();
            for(Element e: o)
            {
                 String color = e.getAttribute("color");   
                int gold = Integer.parseInt(e.getAttribute("gold"));
                int food = Integer.parseInt(e.getAttribute("food"));
                float techPoints = Float.parseFloat(e.getAttribute("techpoints"));
                int turn = Integer.parseInt(e.getAttribute("turn"));
                String race = e.getAttribute("race");
                ArrayList<Boolean> technology = new ArrayList<Boolean>();
                technology.add(Boolean.parseBoolean(e.getAttribute("tech1")));
                technology.add(Boolean.parseBoolean(e.getAttribute("tech2")));
                technology.add(Boolean.parseBoolean(e.getAttribute("tech3")));
                HumanPlayer player = new HumanPlayer(color, gold, food, techPoints, turn, race, technology);
                
                playersList.add(player);
            }
            this.playersList = playersList;
            o = root.getChildrenByName("AIPlayer");
             ArrayList<AIPlayer> aiplayersList = new ArrayList<AIPlayer>();
            for(Element e: o)
            {
                String playerColor = e.getAttribute("color");   
                int gold = Integer.parseInt(e.getAttribute("gold"));
                int food = Integer.parseInt(e.getAttribute("food"));
                float techPoints = Float.parseFloat(e.getAttribute("techpoints"));
                int turn = Integer.parseInt(e.getAttribute("turn"));
                String race = e.getAttribute("race");
                AIPlayer aiplayer = new AIPlayer(playerColor, gold, food, techPoints, turn, race);
                aiplayersList.add(aiplayer);
            }
            this.aiplayersList = aiplayersList;
            o = root.getChildrenByName("TileBuildings");
            ArrayList<TileBuildings> buildingsList = new ArrayList<TileBuildings>();
            for(Element e: o)
            {
                int id = Integer.parseInt(e.getAttribute("id"));
                int q = Integer.parseInt(e.getAttribute("q"));
                int r = Integer.parseInt(e.getAttribute("r"));
                int s = Integer.parseInt(e.getAttribute("s"));
                int maxHP = Integer.parseInt(e.getAttribute("maxHP"));
                float HP = Float.parseFloat(e.getAttribute("HP"));
                int dayGold = Integer.parseInt(e.getAttribute("dayGold"));
                int dayFood = Integer.parseInt(e.getAttribute("dayFood"));
                float dayTechPoints = Float.parseFloat(e.getAttribute("dayTechPoints"));
                String playerColor = e.getAttribute("playerColor");
                String textureName = e.getAttribute("textureName");
                String race = e.getAttribute("race");
                
                TileBuildings buildings = new TileBuildings(id, q, r, s, maxHP, HP, dayGold, dayFood, dayTechPoints, playerColor, textureName, race);
                buildingsList.add(buildings);
                
            }
            this.buildingsList = buildingsList;
            o = root.getChildrenByName("Units");
            ArrayList<Units> unitsList = new ArrayList<Units>();
            
            for(Element e: o)
            {
                int id = Integer.parseInt(e.getAttribute("id"));
                int q = Integer.parseInt(e.getAttribute("q"));
                int r = Integer.parseInt(e.getAttribute("r"));
                int s = Integer.parseInt(e.getAttribute("s"));
                int meleeUnits = Integer.parseInt(e.getAttribute("meleeUnits"));
                int rangeUnits = Integer.parseInt(e.getAttribute("rangeUnits"));
                int specialUnits = Integer.parseInt(e.getAttribute("specialUnits"));
                float move = Float.parseFloat(e.getAttribute("move"));
                String playerColor = e.getAttribute("playerColor");
                Units unit = new Units(id, q, r, s, meleeUnits, rangeUnits, specialUnits, move, playerColor);
                unitsList.add(unit);
            }
            this.unitsList = unitsList;
        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initLoad(this.gamehud, this.playersList, this.aiplayersList, this.buildingsList, this.unitsList);
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
    
    private void initLoad(GameHUD hud, ArrayList<HumanPlayer> playersList, ArrayList<AIPlayer> aiplayersList, ArrayList<TileBuildings> buildingsList, ArrayList<Units> unitsList){
        party.load(Gdx.files.internal("data/hexsmoke"),Gdx.files.internal("data"));
        partyPool = new ParticleEffectPool(party, 1, 200);
        effect = partyPool.obtain();
        splashgamehudimg = new Texture("gamehud.PNG");
        this.playerH = playersList.get(0);// new HumanPlayer(playersList.get(0).color,playersList.get(0).gold,playersList.get(0).food,playersList.get(0).techpoints,playersList.get(0).turn,playersList.get(0).race,playersList.get(0).technology);
        this.playerAI = aiplayersList.get(0);//new AIPlayer(aiplayersList.get(0).color,aiplayersList.get(0).gold,aiplayersList.get(0).food,aiplayersList.get(0).techpoints,aiplayersList.get(0).turn, aiplayersList.get(0).race);
        this.playersList = playersList;
        this.aiplayersList = aiplayersList;
        this.buildingsList = buildingsList;
        this.unitsList = unitsList;
        this.mapLoad = new MapLoader();
        this.gamehud = hud;
        this.techhud = new TechHUD(playersList.get(0).technology, playersList.get(0).techpoints);
        stage.addActor(this.mapLoad);
        this.mapLoad.createMap();
        stage = new TiledMapStage(mapLoad.map, this.playersList, this.aiplayersList, this.gamehud, this.buildingsList, this.unitsList, this.techhud);
        tiledMapS = (TiledMapStage) stage;
      
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
       techhud = new TechHUD(tech, 0);
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
     //   System.out.println("Rozmiar poola: "+ this.partyListCoord.size());
        this.effects.clear();
        for(int i=0;i<partyListCoord.size();i++)
        {
        this.effects.add(effect);
        }
    }
    
     public void checkEnd(){
         //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!Hex x w hexPixArr[][]: "+ tiledMapS.hexPixArr[1][1].x);
      //  System.out.println("Rozmiar: "+ tiledMapS.buildingsList.size()+" a kolor: "+tiledMapS.buildingsList.get(0).playerColor);
       // System.out.println("Czy prawda? - "+tiledMapS.buildingsList.get(0).playerColor.equals(new String("RED")));
         if(tiledMapS.buildingsList.get(0).playerColor.equals(new String("RED"))){
           //  System.out.println("-->Jest RED");
             if(!tiledMapS.buildingsList.get(1).playerColor.equals(new String("BLUE"))){
                  //  System.out.println("--->Nie jest blue ");
                 game.setScreen(new EndScreen(game, true));
                 
             }
           } else {
            // System.out.println("-->Nie jest w red");
             game.setScreen(new EndScreen(game, false));
//game.setScreen(new MenuSettingsScreen(game));
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
      /* for (int i = partyListCoord.size() - 1; i >= 0; i--) {
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
}*/
       spriteBatch.draw(splashgamehudimg, 0, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/16, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/16);
      spriteBatch.end();
     // if (party.isComplete()) party.reset();
  
       gamehud.stage.draw();
       
       spriteBatch.setProjectionMatrix(gamehud.stage.getCamera().combined);
       gamehud.update(Gdx.graphics.getDeltaTime());
       

       techhud.stage.draw();
       
              update();
              
             for (int i = effects.size() - 1; i >= 0; i--)
    effects.get(i).free(); //free all the effects back to the pool
//effects.clear(); //clear the current effects array

       checkEnd();
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
