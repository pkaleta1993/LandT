/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.pk.ltgame.hex.Point;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.hex.Hex;
import com.pk.ltgame.hex.Layout;
import com.pk.ltgame.hex.OffsetCoord;
import com.pk.ltgame.hex.FractionalHex;
import static com.pk.ltgame.hex.FractionalHex.hexRound;
import static com.pk.ltgame.hex.Layout.hexToPixel;
import static com.pk.ltgame.hex.Layout.pixelToHex;
import static com.pk.ltgame.hex.OffsetCoord.qoffsetToCube;
import static com.pk.ltgame.hex.OffsetCoord.roffsetToCube;
import static com.pk.ltgame.hex.OffsetCoord.qoffsetFromCube;
import com.pk.ltgame.hud.GameHUD;
import com.pk.ltgame.hud.TechHUD;
import com.pk.ltgame.inputs.GameInput;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.players.AIPlayer;
import com.pk.ltgame.players.HumanPlayer;
import com.pk.ltgame.scr.AbstractScreen;
import com.pk.ltgame.scr.EndScreen;
import com.pk.ltgame.scr.GameScreen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


/**
 *
 * @author pkale
 */
public class TiledMapStage extends Stage {

    private TiledMap tiledMap;
    private FractionalHex pCube;
    private GameHUD gameHUDInputProcessor;
    private TechHUD techHUDInputProcessor;
    private Point p;
    private AIPlayer AIplayer;
    private Hex pHex;
    private Hex hexNB;
    public static int mapXCells, mapYCells;
    private TileBuildings buildings;
    private Units units;
    public static Hex[][] cubeArr;
     private TextureAtlas atlas;
     private GameScreen gameAccess;

    /**
     *
     */
    protected Skin skin;
    private ImageButton buttonMove, buttonFarm, buttonCraft, buttonHire, buttonMelee, buttonRange, buttonSpecial;

    /**
     *
     */
    public static int selectedUnit,

    /**
     *
     */
    selectedUnitHire,

    /**
     *
     */
    selectedHireIndex;
    private static Stage stage;
    //private Stage stageInputProcessor;

    /**
     *
     */
    public Point[][] hexPixArr;
    private OffsetCoord clickOffset;
    private InputMultiplexer inputMux;
    public static ArrayList<Hex> hexNeighbors = new ArrayList<Hex>();
    public static ArrayList<TileBuildings> buildingsList = new ArrayList<TileBuildings>();
    private static ArrayList<Units> unitsList = new ArrayList<Units>();
    private static ArrayList<ImageButton> buttonUnitsMoveList = new ArrayList<ImageButton>();
    private static ArrayList<ImageButton> buttonUnitsFarmList = new ArrayList<ImageButton>();
    private static ArrayList<ImageButton> buttonUnitsCraftList = new ArrayList<ImageButton>();
    private static ArrayList<ImageButton> buttonUnitsHireList = new ArrayList<ImageButton>();
    private static ArrayList<ImageButton> buttonUnitsMeleeList = new ArrayList<ImageButton>();
    private static ArrayList<ImageButton> buttonUnitsRangeList = new ArrayList<ImageButton>();
    private static ArrayList<ImageButton> buttonUnitsSpecialList = new ArrayList<ImageButton>();
    private static  ArrayList<HumanPlayer> playersList = new ArrayList<HumanPlayer>();
    private static ArrayList<AIPlayer> aiplayersList = new ArrayList<AIPlayer>();
    private static ArrayList<OffsetCoord> allHexagonals;
    private static ArrayList<OffsetCoord> allInvisible;
    public int tileBuildingI = 0;
     Point layoutSize = new Point(99,99);
        Point layoutOrigin = new Point(99,172);
          Layout gameLayout = new Layout(Layout.flat, layoutSize, layoutOrigin);
          private final float mKill = 0.45f;
        private final float rKill = 0.45f;
        private final float sKill = 0.1f;

    /**
     *
     * @param tiledMap
     * @param playersList
     * @param aiplayersList
     * @param gameHUD
     * @param game
     */
    public TiledMapStage(TiledMap tiledMap, ArrayList<HumanPlayer> playersList,ArrayList<AIPlayer> aiplayersList , GameHUD gameHUD, TechHUD techHUD) {
       TiledMapStage.playersList = playersList;
       TiledMapStage.aiplayersList = aiplayersList;
    //   this.gameAccess = gameAccess;
        selectedUnit = -1;
       selectedUnitHire = -1;
        stage = new Stage();
        
        int tileUnitI = 0;
       atlas = new TextureAtlas("units.atlas");
        skin = new Skin(Gdx.files.internal("units.json"), atlas);
        System.out.println("Kontstruktor");
      //  stageInputProcessor = GameHUD.getStage();
        this.tiledMap = tiledMap;
        this.gameHUDInputProcessor = gameHUD;
        this.techHUDInputProcessor = techHUD;
       //  Gdx.input.setInputProcessor(stage);
       //  buildings = new TileBuildings(0,3,-3, playersList.get(0).color);
        addTileBuilding(tileBuildingI, 0, 3, -3, 100, 100, 10, 4, (float) 0.1, playersList.get(0).color, "castle",playersList.get(0).race);
       // tileBuildingI++;
//Gdx.input.setInputProcessor(this.stage);
         //buildingsList.add(buildings);
       //  buildings = new TileBuildings(3,0,-3, playersList.get(0).color);
          addTileBuilding(tileBuildingI, 3, 0, -3, 100, 100, 10, 4, (float)0.1, aiplayersList.get(0).color, "castle", aiplayersList.get(0).race);
          // addTileBuilding(tileBuildingI, 2, 0, -2, 40, 40, 5, 3, 0, playersList.get(0).color, "farm");
      //    tileBuildingI++;
        //  addTileBuilding(tileBuildingI, 1, 2, -3, playersList.get(0).color, "farm");
          //tileBuildingI++;
          //addTileBuilding(tileBuildingI, 1, 0, -1, playersList.get(0).color, "craft");
          //tileBuildingI++;

     //  buildingsList.add(buildings);
       addTileUnit(tileUnitI, 0, 3, -3, 4, 2, 0, 1, playersList.get(0).color);
        tileUnitI++;
      addTileUnit(tileUnitI,3,0,-3, 4, 2, 0, 1, aiplayersList.get(0).color);
       tileUnitI++;
      addTileUnit(tileUnitI,3,-2,-1, 4, 2, 0, 1, playersList.get(0).color);
        tileUnitI++;
        addTileUnit(tileUnitI,1,1,-2, 4, 2, 0, 1, playersList.get(0).color);
        tileUnitI++;
        System.out.println("Liczba obiektów w konstruktorze: " + unitsList.size());
        this.gameHUDInputProcessor.turnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            nextTurn();
           gameHUDInputProcessor.addTurn();
            }
        });
        
        this.techHUDInputProcessor.buttonDouble.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(techHUDInputProcessor.techPoints >=1){
                    techHUDInputProcessor.payByTechPoints(1);
                    gameHUDInputProcessor.payTech(1);
                   // stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsMeleeList.get(selectedUnitHire), true));
                   //System.out.println("Ilość aktorów: " + techHUDInputProcessor.stage.getActors().get(0).);
                    //techHUDInputProcessor.stage.getActors().removeIndex(techHUDInputProcessor.stage.getActors().indexOf(techHUDInputProcessor.buttonDouble, true));
                    techHUDInputProcessor.deleteTechnology(techHUDInputProcessor.buttonDouble);
                    techHUDInputProcessor.setTrue(0);
                }
            }
           
        });
        this.techHUDInputProcessor.buttonBuildings.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(techHUDInputProcessor.techPoints >=2){
                    techHUDInputProcessor.payByTechPoints(2);
                    gameHUDInputProcessor.payTech(2);
                    techHUDInputProcessor.deleteTechnology(techHUDInputProcessor.buttonBuildings);
                    techHUDInputProcessor.setTrue(1);
                }
            }
           
        });
        this.techHUDInputProcessor.buttonUnits.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(techHUDInputProcessor.techPoints >=2){
                    techHUDInputProcessor.payByTechPoints(2);
                    gameHUDInputProcessor.payTech(2);
                    techHUDInputProcessor.deleteTechnology(techHUDInputProcessor.buttonUnits);
                    techHUDInputProcessor.setTrue(2);
                }
            }
           
        });
      // units = new Units(1,0,3,-3, playersList.get(0).color);
       //unitsList.add(units);

     //  buttonMove = new ImageButton(skin);
      // buttonMove.setSize(Gdx.graphics.getWidth()/24f, Gdx.graphics.getWidth()/24f);
   // buttonMove.setPosition(20, 90);
   /* buttonMove.addListener( new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println("Kliknietooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        };
    });*/
   /* buttonMove.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               //((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                  //   System.out.println("Kliknietooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                     Gdx.app.log("unitButton", "kliknieto");
// game.setScreen(new MenuScreen(game));
            }
        });
   */
       // stage.addActor(buttonMove);
        //buttonUnitsList.add(buttonMove);
        // Gdx.input.setInputProcessor(stage);
       /* InputMultiplexer obsługuje kilka inputProcessorów, a dokładniej rzecz ujmując - sam działa jako inputProcessor,
        tyle że rozsyła wszystkie akcje do potomnych procesów.
        */
        GameInput inputProcessor = new GameInput();
        //  inputProcessor inputStage = new inputProcessor();
         InputMultiplexer inputMux = new InputMultiplexer();
         inputMux.addProcessor(stage);
        inputMux.addProcessor(gameHUDInputProcessor.stage);
        inputMux.addProcessor(techHUDInputProcessor.stage);
       inputMux.addProcessor(inputProcessor);

         Gdx.input.setInputProcessor(inputMux);
        for (MapLayer layer : tiledMap.getLayers()) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
            createActorsForLayer(tiledLayer);
        }
       
         this.allHexagonals = setUpAllOffsetCoords(cubeArr, mapXCells, mapYCells);
         this.allInvisible =  getAllInvisible();
               
         saveGame();

    }
    
    private void saveGame(){
        FileHandle save = Gdx.files.local("data/aa.json");
       Json json = new Json();
       //JSONObject 
      // json.toJson(playersList, save);
      TileBuildings building = buildingsList.get(0);
       json.toJson(this.gameHUDInputProcessor.gold,GameHUD.class, save);
       Units unitToSave;
      
       for(int i=0;i<unitsList.size();i++)
       {
           unitToSave = unitsList.get(i);
          // System.out.println("<<<<<<<<<<<<<"+unitToSave.meleeUnits);
          // json.toJson(unitToSave, Objects.class,save);
           
      }
       //json.toJson(aiplayersList,save);
       //json.toJson(playersList, save);
     //  json.toJson( unitsList, ArrayList.class, save);
      // json.to
      // json.toJson(buildingsList,save);
      //// json.
                   //json.toJson(tiledMapS.)
    }
    private Point getMouseClickPoint(double x, double y){
       return new Point(x,y);
    }

    /**
     *
     */
    public TiledMapStage() {
         this.allHexagonals = setUpAllOffsetCoords(cubeArr, mapXCells, mapYCells);
    }

    /**
     *
     * @param id
     * @param q
     * @param r
     * @param s
     * @param hp
     * @param gold
     * @param food
     * @param tPoints
     * @param color
     * @param texture
     */
    public final void addTileBuilding(int id, int q, int r, int s, int maxhp, float hp, int gold, int food, float tPoints, String color, String texture, String race) {

        buildings = new TileBuildings(id, q,r,s, maxhp, hp, gold, food, tPoints, color, texture, race);
         buildingsList.add(buildings);
         tileBuildingI++;

    }

    /**
     *
     * @param id
     * @param q
     * @param r
     * @param s
     * @param meleeU
     * @param rangeU
     * @param specialU
     * @param mv
     * @param color
     */
    public  void addTileUnit(int id, int q, int r, int s, int meleeU, int rangeU, int specialU, float mv, String color) {
        System.out.println("Liczba obiektów w addTileUnit: " + unitsList.size());
        units = new Units(id, q,r,s, meleeU, rangeU, specialU, mv, color);
        unitsList.add(units);

        buttonMove = new ImageButton(skin,"move");
        buttonFarm = new ImageButton(skin, "farm");
        buttonCraft = new ImageButton(skin, "craft");
        buttonHire = new ImageButton(skin, "hire");
        buttonMelee = new ImageButton(skin, "melee");
        buttonRange = new ImageButton(skin, "range");
        buttonSpecial = new ImageButton(skin, "special");
        buttonMove.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
        buttonFarm.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
        buttonCraft.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
        buttonHire.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
        buttonMelee.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonRange.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonSpecial.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonUnitsMoveList.add(buttonMove);
        buttonUnitsFarmList.add(buttonFarm);
        buttonUnitsCraftList.add(buttonCraft);
        buttonUnitsHireList.add(buttonHire);
        buttonUnitsMeleeList.add(buttonMelee);
        buttonUnitsRangeList.add(buttonRange);
        buttonUnitsSpecialList.add(buttonSpecial);
        buttonMove.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {

                hexNeighbors.clear();
              if(selectedUnit != buttonUnitsMoveList.indexOf((event.getTarget()).getParent()) )
              {
                 selectedUnit = buttonUnitsMoveList.indexOf((event.getTarget()).getParent());
                  System.out.println("Selected to teraz: " + selectedUnit);
                 getNeighbors(selectedUnit);
              } else {
                  selectedUnit = -1;
                }
            }
        });
        buttonFarm.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
           //TODO
           int discountGold = 0;
           int discountFood = 0;
           if(techHUDInputProcessor.technology.get(1) == true){
               discountGold = 3;
               discountFood = 1;
           }
          if(playersList.get(0).gold>= 25-discountGold && playersList.get(0).food >= 10-discountFood){
           Units thisUnit = unitsList.get(buttonUnitsFarmList.indexOf(event.getTarget().getParent()));
           //System.out.println("punkty ruchu: " + thisUnit.move);
           if(thisUnit.move>=1)
           {
           if(containTileBuilding(thisUnit, "farm") == false)
           {
               if(containTileBuilding(thisUnit,"castle") == false)
               {
               TileBuildings thisBuilding = containTileBuildingObject(thisUnit, "craft");

               if(thisBuilding !=null)
               {
                   System.out.println("thisBuilding != null");
                    buildingsList.get(buildingsList.indexOf(thisBuilding)).textureName = "farm";
                    buildingsList.get(buildingsList.indexOf(thisBuilding)).updateTexture();
                    buildingsList.get(buildingsList.indexOf(thisBuilding)).dayGold=5;
                    buildingsList.get(buildingsList.indexOf(thisBuilding)).dayFood=3;

                    if(buildingsList.get(buildingsList.indexOf(thisBuilding)).HP > 36){
                        buildingsList.get(buildingsList.indexOf(thisBuilding)).HP = 40;
                        buildingsList.get(buildingsList.indexOf(thisBuilding)).updateFillBar(buildingsList.get(buildingsList.indexOf(thisBuilding)).HP/buildingsList.get(buildingsList.indexOf(thisBuilding)).maxHP);
                    } else {
                        buildingsList.get(buildingsList.indexOf(thisBuilding)).HP+=5;
                        buildingsList.get(buildingsList.indexOf(thisBuilding)).updateFillBar(buildingsList.get(buildingsList.indexOf(thisBuilding)).HP/buildingsList.get(buildingsList.indexOf(thisBuilding)).maxHP);
                    }
                    playersList.get(0).payFor(25-discountGold, 10-discountFood);
                    gameHUDInputProcessor.addGold(-25+discountGold);
                     gameHUDInputProcessor.addFood(-10+discountFood);
                     thisUnit.move-=1;
                    System.out.println(buildingsList.get(buildingsList.indexOf(thisBuilding)).textureName);
               } else {
                   addTileBuilding(tileBuildingI, thisUnit.q, thisUnit.r, thisUnit.s, 40, 40, 5, 3, 0, playersList.get(0).color, "farm", playersList.get(0).race);
                    playersList.get(0).payFor(25-discountGold, 10-discountFood);
                    gameHUDInputProcessor.addGold(-25+discountGold);
                     gameHUDInputProcessor.addFood(-10+discountFood);
                     thisUnit.move-=1;

               }

           }
           }
          }
          }
            }
        });

        buttonCraft.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO
                 int discountGold = 0;
           int discountFood = 0;
           if(techHUDInputProcessor.technology.get(1) == true){
               discountGold = 2;
               discountFood = 2;
           }
             if(playersList.get(0).gold>= 15-discountGold && playersList.get(0).food >= 15-discountFood){
           Units thisUnit = unitsList.get(buttonUnitsCraftList.indexOf(event.getTarget().getParent()));
            if(thisUnit.move>=1)
           {
           if(containTileBuilding(thisUnit, "craft") == false)
           {
               if(containTileBuilding(thisUnit,"castle") == false)
               {
               TileBuildings thisBuilding = containTileBuildingObject(thisUnit, "farm");
               if(thisBuilding !=null)
               {
                   System.out.println("thisBuilding != null");
                    buildingsList.get(buildingsList.indexOf(thisBuilding)).textureName = "craft";
                     buildingsList.get(buildingsList.indexOf(thisBuilding)).updateTexture();
                     buildingsList.get(buildingsList.indexOf(thisBuilding)).dayFood=2;
                     buildingsList.get(buildingsList.indexOf(thisBuilding)).dayGold=10;
                     if(buildingsList.get(buildingsList.indexOf(thisBuilding)).HP > 36){
                        buildingsList.get(buildingsList.indexOf(thisBuilding)).HP = 40;
                    } else {
                        buildingsList.get(buildingsList.indexOf(thisBuilding)).HP+=5;
                    }
                     playersList.get(0).payFor(15-discountGold, 15-discountFood);
                    gameHUDInputProcessor.addGold(-15+discountGold);
                     gameHUDInputProcessor.addFood(-15+discountFood);
                     thisUnit.move-=1;
                    System.out.println(buildingsList.get(buildingsList.indexOf(thisBuilding)).textureName);
               } else {
                   addTileBuilding(tileBuildingI, thisUnit.q, thisUnit.r, thisUnit.s, 40, 40, 10, 2, 0, playersList.get(0).color, "craft", playersList.get(0).race);
                    playersList.get(0).payFor(15-discountGold, 15-discountFood);
                    gameHUDInputProcessor.addGold(-15+discountGold);
                     gameHUDInputProcessor.addFood(-15+discountGold);
                     thisUnit.move-=1;
               }

           }
           }
            }
             }
            }
        });

        buttonHire.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO
             if(selectedUnitHire != buttonUnitsHireList.indexOf((event.getTarget()).getParent()) )
              {
                 selectedUnitHire = buttonUnitsHireList.indexOf((event.getTarget()).getParent());

                System.out.println(selectedUnitHire);
              //  stage.addActor(buttonMelee);
                stage.addActor(buttonUnitsMeleeList.get(selectedUnitHire));
                stage.addActor(buttonUnitsRangeList.get(selectedUnitHire));
                stage.addActor(buttonUnitsSpecialList.get(selectedUnitHire));

              } else {
                // buttonUnitsMeleeList.get(selectedUnit).addAction(Actions.removeActor());
                 //buttonMelee.addAction(Actions.removeActor());
               //  buttonMelee.removeActor(buttonMelee);
                // stage.getActors().removeValue(buttonMelee, true);
                 System.out.println("Index aktora: " + stage.getActors().indexOf(buttonUnitsMeleeList.get(selectedUnitHire), true));
               stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsMeleeList.get(selectedUnitHire), true));
               stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsRangeList.get(selectedUnitHire), true));
               stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsSpecialList.get(selectedUnitHire), true));
                  selectedUnitHire = -1;
                }
            }
        });

         buttonMelee.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {

           int discountGold = 0;
           int discountFood = 0;
           if(techHUDInputProcessor.technology.get(2) == true){
               discountGold = 1;
               discountFood = 0;
           }
            if(playersList.get(0).gold>=10-discountGold && playersList.get(0).food >=3-discountFood){
                selectedHireIndex = buttonUnitsMeleeList.indexOf((event.getTarget()).getParent());
                playersList.get(0).payFor(10-discountGold, 3-discountFood);
                gameHUDInputProcessor.addGold(-10+discountGold);
                gameHUDInputProcessor.addFood(-3+discountFood);
                unitsList.get(selectedHireIndex).meleeUnits = unitsList.get(selectedHireIndex).meleeUnits+1;
                unitsList.get(selectedHireIndex).updateArmy();
               // selectedUnitHire = selectedHireIndex;
             }
            }
        });
          buttonRange.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO
 int discountGold = 0;
           int discountFood = 0;
           if(techHUDInputProcessor.technology.get(2) == true){
               discountGold = 2;
               discountFood = 0;
           }
                if(playersList.get(0).gold>=15-discountGold && playersList.get(0).food >=2-discountFood){
                selectedHireIndex = buttonUnitsRangeList.indexOf((event.getTarget()).getParent());
                playersList.get(0).payFor(15-discountGold, 2-discountFood);
                gameHUDInputProcessor.addGold(-15+discountGold);
                gameHUDInputProcessor.addFood(-2+discountFood);
                unitsList.get(selectedHireIndex).rangeUnits = unitsList.get(selectedHireIndex).rangeUnits+1;
                unitsList.get(selectedHireIndex).updateArmy();
               // selectedUnitHire = selectedHireIndex;
             }
            }
        });
           buttonSpecial.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO
            int discountGold = 0;
           int discountFood = 0;
           if(techHUDInputProcessor.technology.get(2) == true){
               discountGold = 3;
               discountFood = 1;
           }
              if(playersList.get(0).gold>=30-discountGold && playersList.get(0).food >=8-discountFood){
                selectedHireIndex = buttonUnitsSpecialList.indexOf((event.getTarget()).getParent());
                playersList.get(0).payFor(30-discountGold, 8-discountFood);
                gameHUDInputProcessor.addGold(-30+discountGold);
                gameHUDInputProcessor.addFood(-8+discountFood);
                unitsList.get(selectedHireIndex).specialUnits = unitsList.get(selectedHireIndex).specialUnits+1;
                unitsList.get(selectedHireIndex).updateArmy();
               // selectedUnitHire = selectedHireIndex;
             }
            }
        });
           //System.out.println("Kolorrrrrrrrrrrrrrrrrrrrrrrrrr: "+color);
          if(color == "RED"){
            stage.addActor(buttonMove);
          stage.addActor(buttonFarm);
           stage.addActor(buttonCraft);
            stage.addActor(buttonHire);
          }

           // stage.addActor(buttonRange);
            //stage.addActor(buttonSpecial);

    }

    /**
     *
     */
    public void nextTurn(){

        System.out.println("Nastepna tura");
        turnUnits(unitsList);
        turnPayday(buildingsList);
        turnBuildings(buildingsList);
        for(int i=0;i<playersList.size();i++){
            //TODO działania dla graczy;
        }
        for(int i=0;i<aiplayersList.size();i++){
            //TODO działania dla graczy AI;
            aiplayersList.get(i).makeADecision(unitsList, buildingsList,this.mapXCells,this.mapYCells,this);
        }


    }

    /**
     *
     * @param buildingsList
     */
    public void turnPayday(ArrayList<TileBuildings> buildingsList){
        //TODO - dodawanie zlota i jedzenia za kazdy tile
        for(TileBuildings buildingsL: buildingsList){
           if(buildingsL.playerColor == "RED"){
            playersList.get(0).gold += buildingsL.dayGold;
            playersList.get(0).food += buildingsL.dayFood;
            playersList.get(0).techpoints += buildingsL.dayTechPoints;
            gameHUDInputProcessor.addGold(buildingsL.dayGold);
            gameHUDInputProcessor.addFood(buildingsL.dayFood);
            gameHUDInputProcessor.addtechPoints(buildingsL.dayTechPoints);
            techHUDInputProcessor.updateTech(buildingsL.dayTechPoints);
            if(techHUDInputProcessor.technology.get(0) == true && buildingsL == buildingsList.get(0))
            {
                playersList.get(0).gold += buildingsL.dayGold;
                playersList.get(0).food += buildingsL.dayFood;
                gameHUDInputProcessor.addGold(buildingsL.dayGold);
                gameHUDInputProcessor.addFood(buildingsL.dayFood);
            }
           } else {
               aiplayersList.get(0).gold += buildingsL.dayGold;
               aiplayersList.get(0).food += buildingsL.dayFood;
               aiplayersList.get(0).techpoints += buildingsL.dayTechPoints;
           }
        }
    }

    /**
     *
     * @param unitsList
     */
    public void turnUnits(ArrayList<Units> unitsList){
        //TODO - reset punktów ruchu
        for(Units unitsL: unitsList){
            unitsL.move =1;
        }
    }
    public void turnBuildings(ArrayList<TileBuildings> buildingsList){
        for(TileBuildings buildingsL: buildingsList){
            if(buildingsL.textureName=="farm" || buildingsL.textureName == "craft"){
                if(buildingsL.HP<40&& buildingsL.HP>=38){
                    buildingsL.HP = 40;
                    buildingsL.updateFillBar(buildingsL.HP/buildingsL.maxHP);

                } else if(buildingsL.HP<38){
                    buildingsL.HP +=2;
                    buildingsL.updateFillBar(buildingsL.HP/buildingsL.maxHP);
                }
            }

        }
    }
    /**
     *
     * @param q
     * @param r
     * @param s
     * @return
     */
    public boolean containCubeArr(int q,int r,int s){
       //System.out.println("Jest w containCubeArr");System.out.println("Rozmiar x: "+mapXCells);
       
        for(int x = 0;x<mapXCells;x++)
        {
             
            for(int y = 0;y<mapYCells;y++)
            {
                //System.out.println("X: "+x+"  Y: "+ y);
                  //System.out.println("Dla x: "+x+ " oraz y: " + y);
                if(cubeArr[x][y].q == q && cubeArr[x][y].r == r && cubeArr[x][y].s == s){
                   // System.out.println("Dla x: "+x+ " oraz y: " + y + " jest sąsiad");
                   // System.out.println("Wspolrzedne q: " + cubeArr[x][y].q + " oraz r:"+cubeArr[x][y].r+ " oraz s:"+cubeArr[x][y].s);
                 //   System.out.println("Zwracam true");
                    return true;

                }
            }
        }
        return false;

    }

    /**
     *
     * @param unit
     * @param newBuildingType
     * @return
     */
    public TileBuildings containTileBuildingObject(Units unit, String newBuildingType){
        for (int i=0;i<buildingsList.size();i++) {
         if(buildingsList.get(i).q == unit.q && buildingsList.get(i).r == unit.r && buildingsList.get(i).s == unit.s && buildingsList.get(i).textureName == newBuildingType){

        return buildingsList.get(i);
        }
        }
        return null;
    }

    public TileBuildings containTileEnemyBuildingObject(Hex targetHex, String playerColor){
        //System.out.println("Rozmiar buildingsList: " + buildingsList.size());
        for(int i=0;i<buildingsList.size();i++)
        {
            if(buildingsList.get(i).q == targetHex.q && buildingsList.get(i).r == targetHex.r && buildingsList.get(i).s == targetHex.s && playerColor != buildingsList.get(i).playerColor){
                return buildingsList.get(i); // enemyBuilding
            }
        }
        return null;
    }
    /**
     *
     * @param unit
     * @param newBuildingType
     * @return
     */
    public boolean containTileBuilding(Units unit, String newBuildingType){

        for (int i=0;i<buildingsList.size();i++) {
           if(buildingsList.get(i).q == unit.q && buildingsList.get(i).r == unit.r && buildingsList.get(i).s == unit.s && buildingsList.get(i).textureName == newBuildingType){

               return true;
           }
        }
        return false;
    }
    
    public boolean containTileBuilding(Hex hex, String newBuildingType){

        for (int i=0;i<buildingsList.size();i++) {
           if(buildingsList.get(i).q == hex.q && buildingsList.get(i).r == hex.r && buildingsList.get(i).s == hex.s && buildingsList.get(i).textureName == newBuildingType){

               return true;
           }
        }
        return false;
    }

    /**
     *
     * @param targetHex
     * @param playerColor
     * @return
     */
    public Units containTileEnemyUnit(Hex targetHex, String playerColor){
      //  System.out.println("*********** W containTileEnemyUnit");
        for(int i=0;i<unitsList.size();i++)
        {
        //   System.out.println("********************if("+unitsList.get(i).q + " == "+ targetHex.q+" && " +unitsList.get(i).r + " == "+ targetHex.r + " && "+unitsList.get(i).s + " == "+ targetHex.s+ " && " + playerColor + " != "+unitsList.get(i).playerColor);
            if(unitsList.get(i).q == targetHex.q && unitsList.get(i).r == targetHex.r && unitsList.get(i).s == targetHex.s && playerColor != unitsList.get(i).playerColor){
             System.out.println("//// Zawiera enemyUnit \\\\\"");
                return unitsList.get(i); // enemyUnit
            }
        }
        return null;
    }

    /**
     *
     * @param targetHex
     * @param playerColor
     * @return
     */
    public Units containTileUnit(Hex targetHex, String playerColor){
        for(int i=0;i<unitsList.size();i++)
        {
            if(unitsList.get(i).q == targetHex.q && unitsList.get(i).r == targetHex.r && unitsList.get(i).s == targetHex.s && playerColor == unitsList.get(i).playerColor){
                return unitsList.get(i); // allyUnit
            }
        }
        return null;
    }

    /**
     *
     * @param value1
     * @param value2
     * @return
     */
    public int difBetween(int value1, int value2){
        int dif = 0;
        if(value1>=value2)
        dif = value1-value2; else dif = value2-value1;
        return dif;
    }

    /**
     *
     * @param unit
     * @param unitSelected
     * @param value
     */
    public void killCalculation(Units unit, Units unitSelected, float value){
        Units  unitsToKill = unitsList.get(unitsList.indexOf(unit));
        int indexUnitToKill = unitsList.indexOf(unit);
         System.out.println(" melee: " + unitsToKill.meleeUnits + " range: " + unitsToKill.rangeUnits + " special: " + unitsToKill.specialUnits);
        int offset = 0;
        int repeat = 0;
        if(value>0)
        {
          if(0.45*value<=0.5){
              value = 1.2f;
          }
          do{
             //System.out.println("meeleunits warunek: " + unitsToKill.meleeUnits + " >= "+(int)(((repeat*0.55f)+ mKill) * (value+offset)));
             //System.out.println("meeleunits warunek: " + Math.round(((repeat*0.55f)+ mKill) * (value+offset)));
            if(unitsToKill.meleeUnits >= Math.round(((repeat*0.55f)+ mKill) * (value+offset))){
              unitsToKill.meleeUnits -= Math.round(((repeat*0.55f)+ mKill)*(value+offset));
              offset = 0;
            } else {
              offset = Math.round(((repeat*0.55f)+mKill)*(value+offset)) - unitsToKill.meleeUnits;
              unitsToKill.meleeUnits = 0;
            }
            System.out.println(" melee: " + unitsToKill.meleeUnits + " range: " + unitsToKill.rangeUnits + " special: " + unitsToKill.specialUnits+" //// Offset: "+ offset);

            if(unitsToKill.rangeUnits >= Math.round(((repeat*0.55f)+rKill)*(value+offset))){
               unitsToKill.rangeUnits -= Math.round(((repeat*0.55f)+rKill)*(value+offset));
               offset = 0;
            } else {
                offset = Math.round(((repeat*0.55f)+rKill)*(value+offset))-unitsToKill.rangeUnits;
                unitsToKill.rangeUnits = 0;
            }
            System.out.println(" melee: " + unitsToKill.meleeUnits + " range: " + unitsToKill.rangeUnits + " special: " + unitsToKill.specialUnits+" //// Offset: "+ offset);
            if(unitsToKill.specialUnits>= Math.round(sKill*(value+offset))){
                unitsToKill.specialUnits -= Math.round(sKill*(value+offset));
                offset = 0;
            } else {
                offset = Math.round(sKill*(value+offset)) - unitsToKill.specialUnits;
                unitsToKill.specialUnits = 0;
            }
           System.out.println(" melee: " + unitsToKill.meleeUnits + " range: " + unitsToKill.rangeUnits + " special: " + unitsToKill.specialUnits+" //// Offset: "+ offset);
            if(offset>0)
            {
                value=0;
                repeat=1;
            }
            System.out.println("Powtarzam melee: " + unitsToKill.meleeUnits + " range: " + unitsToKill.rangeUnits + " special: " + unitsToKill.specialUnits+" //// Offset: "+ offset);
            }while((((unitsToKill.meleeUnits+unitsToKill.rangeUnits+unitsToKill.specialUnits)>0) && offset != 0));
           // System.out.println("if("+unitsToKill.meleeUnits+" != 0 && " + unitsToKill.rangeUnits + " != 0 && "+ unitsToKill.specialUnits + " != 0)");
           System.out.println("if("+unitsToKill.meleeUnits+unitsToKill.rangeUnits+unitsToKill.specialUnits+")>0");
           if((unitsToKill.meleeUnits+unitsToKill.rangeUnits+unitsToKill.specialUnits)>0){
            unitsList.get(indexUnitToKill).meleeUnits = unitsToKill.meleeUnits;
            unitsList.get(indexUnitToKill).rangeUnits = unitsToKill.rangeUnits;
            unitsList.get(indexUnitToKill).specialUnits = unitsToKill.specialUnits;
            unitsList.get(indexUnitToKill).updateArmy();
            System.out.println("Wchodzi do ifa");
            } else {

               
               System.out.println("Index unitToKill: "+indexUnitToKill);
               System.out.println("Liczba buttonów: " + buttonUnitsMoveList.size()+"Usuwany index: "+indexUnitToKill);
               //System.out.println("Index aktora buttonMove: " + stage.getActors().indexOf(buttonUnitsMoveList.get(indexUnitToKill), true));
              // System.out.println("Index aktora buttonMove: " + stage.getActors().size);
               if(unitsList.get(indexUnitToKill).playerColor == "RED")
               {
                    stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsCraftList.get(indexUnitToKill), true));
                    stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsFarmList.get(indexUnitToKill), true));
                    stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsMoveList.get(indexUnitToKill), true));
                    stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsHireList.get(indexUnitToKill), true));
                    if(stage.getActors().indexOf(buttonUnitsMeleeList.get(indexUnitToKill), true) != -1)
                    {
                        stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsMeleeList.get(indexUnitToKill), true));
                        stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsRangeList.get(indexUnitToKill), true));
                        stage.getActors().removeIndex(stage.getActors().indexOf(buttonUnitsSpecialList.get(indexUnitToKill), true));
                    }
                }
                buttonUnitsMoveList.remove(indexUnitToKill);
               //System.out.println("Index aktora buttonMove: " + stage.getActors().size);
              // System.out.println("Liczba buttonów po remove(): " + buttonUnitsMoveList.size());
                buttonUnitsCraftList.remove(indexUnitToKill);
                buttonUnitsFarmList.remove(indexUnitToKill);
                buttonUnitsHireList.remove(indexUnitToKill);
                buttonUnitsMeleeList.remove(indexUnitToKill);
                buttonUnitsRangeList.remove(indexUnitToKill);
                buttonUnitsSpecialList.remove(indexUnitToKill);

                unitsList.remove(indexUnitToKill);
                selectedUnit = unitsList.indexOf(unitSelected);

                System.out.println("Wchodzi do else");
            }

        }
    }

    /**
     *
     * @param unitAttack
     * @param unitAttacked
     */
    public void attackUnit(Units unitAttack, Units unitAttacked){

        System.out.println("Wroga jednostka zaatakowana");
        /*
        float attackValue = (unitAttack.meleeUnits - unitAttacked.meleeUnits)*(float)0.6 + (unitAttack.meleeUnits*(float)0.4-unitAttacked.rangeUnits*(float)0.7)+(unitAttack.meleeUnits*(float)0.1-unitAttacked.specialUnits*(float)3); //melee
        System.out.println(attackValue);
       attackValue += (unitAttack.rangeUnits - unitAttacked.rangeUnits)*(float)0.6+(unitAttack.rangeUnits*(float)0.05-unitAttacked.specialUnits*(float)4); //range
       System.out.println(attackValue);
       attackValue += (unitAttack.specialUnits - unitAttacked.specialUnits)*2; //special
       System.out.println(attackValue);
       float attackedValue = (unitAttacked.meleeUnits - unitAttack.meleeUnits)*(float)0.6 + (unitAttacked.meleeUnits*(float)0.4-unitAttack.rangeUnits*(float)0.7)+(unitAttacked.meleeUnits*(float)0.1-unitAttack.specialUnits*(float)3); //melee
       attackedValue += (unitAttacked.rangeUnits - unitAttack.rangeUnits)*(float)0.6+(unitAttacked.rangeUnits*(float)0.05-unitAttack.specialUnits*(float)4); //range
       attackedValue += (unitAttacked.specialUnits - unitAttack.specialUnits)*2; //special
       */
        float attackValueTemp = unitAttack.meleeUnits + unitAttack.rangeUnits + unitAttack.specialUnits*3;
        float attackedValueTemp = unitAttacked.meleeUnits + unitAttacked.rangeUnits + unitAttacked.specialUnits*3;
        float attackValue = (attackValueTemp/3)*(attackValueTemp/attackedValueTemp);
        float attackedValue = (attackedValueTemp/3)*(attackedValueTemp/attackValueTemp);
        System.out.println(attackValue+"  VS  "+attackedValue);
        killCalculation(unitAttacked, unitAttack, attackValue);
        killCalculation(unitAttack, unitAttacked, attackedValue);

    }
    public void conquerCalculation(Units unit, TileBuildings buildingAttacked, int attackVal){
        int tileID = buildingsList.indexOf(buildingAttacked);
        if(attackVal>buildingAttacked.HP)
        {
            buildingsList.get(tileID).HP = 20;
            buildingsList.get(tileID).updateFillBar(buildingsList.get(tileID).HP/buildingsList.get(tileID).maxHP);
            buildingsList.get(tileID).playerColor = unit.playerColor;
            buildingsList.get(tileID).updateTexture();
        } else {
            buildingsList.get(tileID).HP -= attackVal;
             buildingsList.get(tileID).updateFillBar(buildingsList.get(tileID).HP/buildingsList.get(tileID).maxHP);
        }
    }

    public void attackTile(Units unitAttack, TileBuildings buildingAttacked){
        int attackValue = unitAttack.meleeUnits+unitAttack.rangeUnits+4*unitAttack.specialUnits;
        conquerCalculation(unitAttack, buildingAttacked, attackValue);
    }
    /**
     *
     * @param selectedID
     * @param hexCompare
     */
    public void moveUnit(int selectedID, Hex hexCompare, String playerColor) {
       // System.out.println("Rozmiar HexNeighbor: "+hexNeighbors.size());
        // System.out.println("Hex w moveUnit - Q: " + hexCompare.q + " R: "+hexCompare.r+" S: "+hexCompare.s);
         //System.out.println("Selected: " + selectedID);
         //System.out.println("Hex id: "+ hexCompare);
         // System.out.println("Rozmiar HexNeighbor: "+hexNeighbors.size());
      //   for(int i=0;i<hexNeighbors.size();i++)
      //   {
      //       System.out.println("HexNeighbor "+i+": "+hexNeighbors.get(i));
      //   }
      //   System.out.println("Liczba obiektów w hexNeighbors: "+ hexNeighbors.size());
       //  System.out.println("Hex w moveUnit lista - Q: "+ hexNeighbors.get(0).q + " R: " + hexNeighbors.get(0).r);
       //System.out.println("Czy hexNeighbors zawiera hex? - "+hexNeighbors.contains(hexCompare));
       for(int i=0; i < hexNeighbors.size();i++){
         //  System.out.println("if("+hexNeighbors.get(i).q+" == " + hexCompare.q + ") && ("+hexNeighbors.get(i).r+" == " + hexCompare.r + " && ("+hexNeighbors.get(i).s+" == " + hexCompare.s+")");
           // System.out.println("Rozmiar HexNeighbor: "+hexNeighbors.size());
           if((hexNeighbors.get(i).q == hexCompare.q) && (hexNeighbors.get(i).r == hexCompare.r) && (hexNeighbors.get(i).s == hexCompare.s))
          {
             // System.out.println("Target tile się zgadza");
             // if(!unitsList.contains(hexCompare))
           //   {
              //  System.out.println("hexNeighbors zawiera hex");
              if(unitsList.get(selectedID).move >=1){
                  Units enemyUnit = containTileEnemyUnit(hexNeighbors.get(i), playerColor);
                  Units allyUnit = containTileUnit(hexNeighbors.get(i), playerColor);
                  TileBuildings enemyBuilding = containTileEnemyBuildingObject(hexNeighbors.get(i), playerColor);
               //   System.out.println("EnemyUnit: " + enemyUnit);
                 // System.out.println("EnemyBuilding: " + enemyBuilding);
              if(enemyUnit != null)
              {
                  //System.out.println("EnemyUnit != null");
                  //System.out.println("attackUnit("+ unitsList.get(selectedID)+", "+ enemyUnit +")");
                  attackUnit(unitsList.get(selectedID),enemyUnit);
                  selectedID = selectedUnit;
                  if(selectedID != -1) unitsList.get(selectedID).move-=1;
              } else if(allyUnit != null) {

              } else if(enemyBuilding !=null) {
                //  System.out.println("atakujemy tile");
                  attackTile(unitsList.get(selectedID), enemyBuilding);
                  unitsList.get(selectedID).move-=1;
              } else{
               unitsList.get(selectedID).q = hexCompare.q;
               //System.out.println(unitsList.get(0).r + " = "+ hexCompare.r);
               unitsList.get(selectedID).r = hexCompare.r;
               //System.out.println(unitsList.get(0).s + " = "+ hexCompare.s);
               unitsList.get(selectedID).s = hexCompare.s;
               unitsList.get(selectedID).move-=1;
              }
              //System.out.println("Rozmiar unitsList: "+this.unitsList.size());
                //System.out.println(unitsList.get(0).q + " = "+ hexCompare.q);



           //   }
          
           

              }
          }
       }

        /* if(hexNeighbors.contains(hexCompare)){
             System.out.println("hexNeighbors zawiera hex");
           unitsList.get(selectedID).q = hexCompare.q;
           unitsList.get(selectedID).r = hexCompare.r;
           unitsList.get(selectedID).s = hexCompare.s;
         
        
         if(buildingsList.get(0).playerColor != "RED"){
               LandTerrorGame game = new LandTerrorGame(); 
              game.setScreen(new EndScreen(game, false));
//game.setScreen(new MenuSettingsScreen(game));
           } else if(buildingsList.get(1).playerColor != "BLUE"){
               LandTerrorGame game = new LandTerrorGame(); 
               
               game.setScreen(new EndScreen(game, true));
           }
       }*/
        
        
     //  gameAccess.checkEnd(buildingsList);
     //gameAccess.getOC(getAllInvisible());
     allInvisible = getAllInvisible();
     System.out.println("Rozmiar listy: "+allInvisible.size());
    
    }

    /**
     *
     * @param selectedIndex
     */
    public void getNeighbors(int selectedIndex){
        hexNeighbors.clear();
        //pCube =   pixelToHex(gameLayout, p);
       // System.out.println("Jest w getNeighbors a selectedIndex = " + selectedIndex);
       pHex = new Hex(unitsList.get(selectedIndex).q,unitsList.get(selectedIndex).r,unitsList.get(selectedIndex).s);
      //System.out.println("getNeighbors - Q: " + pHex.q + " R: " + pHex.r + "S: " + pHex.s);
       //System.out.println("Jest w getNeighbors przed forem");
       for(int i=0; i<6;i++)
         {
         //    System.out.println("Jest w getNeighbors w forze");
             hexNB = Hex.neighbor(pHex, i);
        //     System.out.println("HexNB q= " + hexNB.q + " r= " + hexNB.r + "s= " + hexNB.s);
             if(containCubeArr(hexNB.q,hexNB.r,hexNB.s) == true){
             hexNeighbors.add(hexNB);
        //              System.out.println("Liczba obiektów w hexNeighbors: "+ hexNeighbors.size());

             }
       //  System.out.println("Sasiedni hex nr " + i + " Q: " + hexNB.q + " R: " + hexNB.r + " S: " +hexNB.s);
         }
    }
    
    public ArrayList<OffsetCoord> getNeighbors(int q, int r, int s){
        ArrayList<OffsetCoord> allVisible = new ArrayList<OffsetCoord>();
        Hex checkVisible = null;
             //   System.out.println("Dostaję: ("+q+","+r+","+s+");");

        //OffsetCoord checkVisibleOC = null;
        for(int i=0; i<6;i++)
         {
         
             checkVisible = Hex.neighbor(new Hex(q,r,s), i);
            // System.out.println("checkVisible: ("+checkVisible.q+", "+checkVisible.r+", "+checkVisible.s+");");
             if(this.containCubeArr(checkVisible.q,checkVisible.r,checkVisible.s) == true){
              //   System.out.println("I taki hex istnieje");
          //   checkVisibleOC.col = qoffsetFromCube(OffsetCoord.EVEN,new Hex(q,r,s)).col;
             if(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).col % 2 == 0){
             // System.out.println("i col tego sasiada %2 = 0");
          
             allVisible.add(new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).row)));
                } else {
                 allVisible.add(new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).row)));
            // System.out.println("i col tego sasiada %2 != 0");
             }
             }
      
         }
           /*     System.out.println("...Rozmiar allVisible: "+allVisible.size());
        for(int i=0;i<allVisible.size();i++)
        {
            System.out.println("**"+ i+" col: "+allVisible.get(i).col + "  row: "+allVisible.get(i).row);
        }*/
        return allVisible;
    }
    public ArrayList<OffsetCoord> returnInvisible(){
        return allInvisible;
    }
    public ArrayList<OffsetCoord> getAllVisible(){
        ArrayList<OffsetCoord> allVisibleOC = new ArrayList<OffsetCoord>();
        ArrayList<OffsetCoord> allVisibleSublist = new ArrayList<OffsetCoord>();
        OffsetCoord objectOC;
        for(int i=0;i<buildingsList.size();i++)
        {
            if(buildingsList.get(i).playerColor == "RED")
            {
                 if(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col % 2 == 0){
            
                 objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row));
            
                } else {
            
             objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row));
            
             }
              //  System.out.println("//objectOC: ("+objectOC.col+", "+objectOC.row+")");
                allVisibleSublist = getNeighbors(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s);
                allVisibleSublist.add(objectOC);
                //System.out.println("//allVisible id 2:  ("+allVisibleSublist.get(2).col+", "+allVisibleSublist.get(2).row+")");
                
               // System.out.println("Rozmiar allVisiblesublist buildings: "+allVisibleSublist.size());
                //allVisibleOC.addAll(getNeighbors(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s));
                for(int j=0;j<allVisibleSublist.size();j++)
                {
                    if(!allVisibleOC.contains(allVisibleSublist.get(j)))
                    {
                        allVisibleOC.add(allVisibleSublist.get(j));
                       // System.out.println("Dodaję: ("+allVisibleSublist.get(j).col+", "+allVisibleSublist.get(j).row+")");
                        
                        
                    }
                    
                }
            }   
             
        }
        for(int i=0;i<unitsList.size();i++)
        {
            if(unitsList.get(i).playerColor == "RED")
            {
                if(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col % 2 == 0){
            
                 objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row));
            
                } else {
            
             objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row));
            
             }
                allVisibleSublist = getNeighbors(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s);
                allVisibleSublist.add(objectOC);
               // System.out.println("Rozmiar allVisiblesublist units: "+allVisibleSublist.size());
                //allVisibleOC.addAll(getNeighbors(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s));
                for(int j=0;j<allVisibleSublist.size();j++)
                {
                    if(!eqOffset(allVisibleOC, allVisibleSublist.get(j)))
                    {
                        allVisibleOC.add(allVisibleSublist.get(j));
                    }
                    
                }
            }   
             
        }
       /* System.out.println("Rozmiar allVisibleOC: "+allVisibleOC.size());
        for(int i=0;i<allVisibleOC.size();i++)
        {
            System.out.println("**"+ i+" col: "+allVisibleOC.get(i).col + "  row: "+allVisibleOC.get(i).row);
        }*/
        return allVisibleOC;        
    }
    
    private ArrayList<OffsetCoord> getAllInvisible(){
        ArrayList<OffsetCoord> allInvisibleH = new ArrayList<OffsetCoord>();
        ArrayList<OffsetCoord> allVisible = getAllVisible();
        ArrayList<Integer> removeId = new ArrayList<Integer>();
// allInvisibleH.addAll(allHexagonals);
       allInvisibleH.addAll(allHexagonals);
         System.out.println("allHexagonals.size(): "+allHexagonals.size());
         System.out.println("allInvisibleH.size(): "+allInvisibleH.size());
         System.out.println("getAllVisible().size(): "+allVisible.size());
         for(int i=0;i<allInvisibleH.size();i++)
         {
             for(int j=0;j<allVisible.size();j++)
             {
                 if(allInvisibleH.get(i).col == allVisible.get(j).col && allInvisibleH.get(i).row == allVisible.get(j).row){
                     removeId.add(i);
                 }
             }
         }
         removeId.sort(Collections.reverseOrder());
        /* for(int i=0;i<removeId.size();i++)
         {
             System.out.println(removeId.get(i).intValue()+", ");
         }
         */
       // allInvisibleH.removeAll(allVisible);
       for(int i=0;i<removeId.size();i++)
       {
           allInvisibleH.remove(removeId.get(i).intValue());
       }
        System.out.println("getAllInvisible().size(): "+allInvisibleH.size());
        for(int i=0;i<allInvisibleH.size();i++)
        {
            System.out.println("**"+ i+" col: "+allInvisibleH.get(i).col + "  row: "+allInvisibleH.get(i).row);
        }
        return allInvisibleH;
        
    }
    
    private ArrayList<OffsetCoord> setUpAllOffsetCoords(Hex[][] cube, int mapX, int mapY){
        ArrayList<OffsetCoord> allOffsetCoords = new ArrayList<OffsetCoord>();
        for(int x=0;x<mapX;x++)
        {
            for(int y=0;y<mapY;y++)
            {
                if(x%2 == 0)
                {
                  
                    allOffsetCoords.add(new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(cube[x][y].q,cube[x][y].r,cube[x][y].s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(cube[x][y].q,cube[x][y].r,cube[x][y].s)).row)));
               
                } else {
                     allOffsetCoords.add(new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(cube[x][y].q,cube[x][y].r,cube[x][y].s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(cube[x][y].q,cube[x][y].r,cube[x][y].s)).row)));
                }
            }
        }
        System.out.println("Rozmiar setUp: "+allOffsetCoords.size());
        
        return allOffsetCoords;
    }
    private boolean eqOffset(ArrayList<OffsetCoord> firstOC, OffsetCoord secondOC){
     //   if(firstOC.col == secondOC.col && firstOC.row == secondOC.col) return true; else return false;        
        for(int i=0;i<firstOC.size();i++)
        {
            
                if(firstOC.get(i).col == secondOC.col && firstOC.get(i).row == secondOC.row) return true;
            
        }
        return false;
    
    }
    

    /**
     *
     * @param x
     * @param y
     */
    public void mouseClickToHex( double x, double y){
         p = getMouseClickPoint(x, y);


         pCube =   pixelToHex(gameLayout, p);
         pHex = hexRound(pCube);
         System.out.println("Selected przed warunkiem = "+ selectedUnit);
         if(selectedUnit>=0){
             Gdx.app.log("mouseClick", "Jest w selected");
             System.out.println("Selected w warunku = "+ selectedUnit);
             System.out.println("Rozmiar HexNeighbor w mouseClickToHex: "+hexNeighbors.size());
             System.out.println("Rozmiar unitsList przed moveUnit: "+unitsList.size());
             moveUnit(selectedUnit, pHex, playersList.get(0).color);

              System.out.println("Selected = "+ selectedUnit);

             selectedUnit = -1;
         } else {
         if(pHex.q %2 == 0){
             clickOffset = qoffsetFromCube(OffsetCoord.EVEN,pHex);
         } else {
         clickOffset = qoffsetFromCube(OffsetCoord.EVEN,pHex); //ODD nie zadziała, bo libGDX trzyma dane o komórkach jako koordynaty row-col liczone od 0
         }
         System.out.println("A klikniecie na hex to hex(frac): " + pCube.q + "," + pCube.r + ","+pCube.s );
         System.out.println("A klikniecie na hex to hex: " + pHex.q + "," + pHex.r + ","+pHex.s );
         System.out.println("Zaś na offset to x: " + clickOffset.col + " oraz y: " + clickOffset.row);

       for(int i=0; i<6;i++)
         {

             hexNB = Hex.neighbor(pHex, i);
         System.out.println("Sasiedni hex nr " + i + " Q: " + hexNB.q + " R: " + hexNB.r + " S: " +hexNB.s);
         }
         }
    }

    private void createActorsForLayer(TiledMapTileLayer tiledLayer) {


        cubeArr = new Hex[tiledLayer.getWidth()][tiledLayer.getHeight()];
         hexPixArr = new Point[tiledLayer.getWidth()][tiledLayer.getHeight()];
         mapXCells = tiledLayer.getWidth();
         mapYCells = tiledLayer.getHeight();
         //Point layoutSize = new Point(99,99);
       // Point layoutOrigin = new Point(99,172);
        Point layoutSize = new Point(99,99);
        Point layoutOrigin = new Point(99,172);
        Layout gameLayout = new Layout(Layout.flat, layoutSize, layoutOrigin);
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
                OffsetCoord coords = new OffsetCoord(x, y);

                if( x%2 == 0 )
                {

                  cubeArr[x][y] = qoffsetToCube(OffsetCoord.EVEN, coords);
                  // cubeArr[x][y] = roffsetToCube(OffsetCoord.EVEN, coords);
                  //  cubeArr[x][y] = roffsetToCube(OffsetCoord.EVEN, coords);
                     System.out.println("Hex w Arr - Q: " + cubeArr[x][y].q + " R : " + cubeArr[x][y].r  + " S: " + cubeArr[x][y].s);
                hexPixArr[x][y] = hexToPixel(gameLayout, cubeArr[x][y]);
                System.out.println("Hex w hexPixArr[ " + x + "]["+y+"] - : " + hexPixArr[x][y].x + " R: " +hexPixArr[x][y].y);
                        //pixelToHex(gameLayout,hexToPixel(gameLayout, hex));

                } else  {
               cubeArr[x][y] =  qoffsetToCube(OffsetCoord.EVEN, coords); // ODD wyłapuje jeden niżej więcej
               //cubeArr[x][y] =  roffsetToCube(OffsetCoord.ODD, coords);
               //cubeArr[x][y] =  roffsetToCube(OffsetCoord.ODD, coords);
             System.out.println("ODD Hex w Arr - Q: " + cubeArr[x][y].q + " R : " + cubeArr[x][y].r  + " S: " + cubeArr[x][y].s);
                hexPixArr[x][y] = hexToPixel(gameLayout, cubeArr[x][y]);
              System.out.println("Hex w hexPixArr[ " + x + "]["+y+"] - : " + hexPixArr[x][y].x + " R: " +hexPixArr[x][y].y);
                }

                addActor(actor);


            }
        }

        System.out.println("Myszka X: "+ Gdx.input.getX( )+ " A Y:" + Gdx.input.getY());
       // p = new Point(Gdx.input.getX( ),Gdx.input.getY( ));
       // pCube =   pixelToHex(gameLayout, p);
    //   System.out.println("A klikniecie na hex to hex(frac): " + pCube.q + "," + pCube.r + ","+pCube.s );
     //  System.out.println("Hex w Arr - Q: " + cubeArr[2][2].q + " R : " + cubeArr[2][2].r  + " S: " + cubeArr[2][2].s);
    //   System.out.println("Hex w HexPixArr - Q: " + hexPixArr[0][0].x + " R : " + hexPixArr[0][0].y);
     System.out.println("qOffsetFromCube: " + qoffsetFromCube(OffsetCoord.EVEN,new Hex(0,3,-3)).col + " Row: " + qoffsetFromCube(OffsetCoord.EVEN,new Hex(0,3,-3)).row);
    }

    /**
     *
     * @param y
     * @return
     */
    public int swapCoords(int y){

        y = mapYCells - 1 - y;
        return y;
    }

    /**
     *
     */
    public void renderObjects(){
        //System.out.println("Liczba obiektów: " + unitsList.size());
        for(int i=0;i < buildingsList.size();i++){

                 //   buildings =  buildingsList.get(i);
                  if( (qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col)%2 == 0 )
                {
                    buildingsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].x-99, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].y - 172));
                 //  System.out.println("Bez-SwapCoordsy: "+qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(1).q,buildingsList.get(1).r,buildingsList.get(1).s)).row);
                } else {
                      buildingsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].x-99, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].y ));
                  }
        }

            for(int i=0;i< unitsList.size();i++){
               // System.out.println("I: " + i);
                //System.out.println("Rozmiar unitsList: " + unitsList.size());
               if(i==2){
                 //  System.out.print("hexPixArr["+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col+"]");
                  //System.out.print("["+1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row + "]");
                  //System.out.println(".x");
                   //System.out.println("Na odd:  " + (1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row));
                  //System.out.println("Hex PixArr[3][2] X:"+hexPixArr[3][2].x + " Y: " + hexPixArr[3][2].y);
               }
                if( (qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col)%2 == 0)
                {
                  //System.out.println("I w if wynosi: " + i);
                 unitsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x-54, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 157));
                 buttonUnitsMoveList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x - 60));
                 buttonUnitsMoveList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 115));
                 buttonUnitsFarmList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x - 40));
                 buttonUnitsFarmList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 85));
                 buttonUnitsCraftList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x));
                 buttonUnitsCraftList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 85));
                 buttonUnitsHireList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 20));
                 buttonUnitsHireList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 115));
                // if(selectedUnitHire != -1 ){
                 buttonUnitsMeleeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 45));
                 buttonUnitsMeleeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 80));
                 buttonUnitsRangeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 55));
                 buttonUnitsRangeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 110));
                 buttonUnitsSpecialList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 45));
                 buttonUnitsSpecialList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 140));

              //   }


                } else {

                //  System.out.println("I w if else wynosi: " + i);
               unitsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x-54, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(1+OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y +15));
               buttonUnitsMoveList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x - 60));
               buttonUnitsMoveList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 55));
               buttonUnitsFarmList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x - 40));
               buttonUnitsFarmList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y +85));
               buttonUnitsCraftList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x));
               buttonUnitsCraftList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 85));
               buttonUnitsHireList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x + 20));
               buttonUnitsHireList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 55));
                // if(selectedUnitHire != -1 ){
                 buttonUnitsMeleeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 40));
                 buttonUnitsMeleeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 85));
                 buttonUnitsRangeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 55));
                 buttonUnitsRangeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 55));
                 buttonUnitsSpecialList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 40));
                 buttonUnitsSpecialList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 25));

                // }
                }
        }
      // unitsList.get(2).render(544-44, 469-157);

       // buttonMove.setX(110);
       // buttonMove.setY(220);
          stage.act();
        stage.draw();
        
    }


}