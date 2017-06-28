package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.pk.ltgame.hex.Point;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.XmlWriter;
import com.pk.ltgame.hex.Hex;
import com.pk.ltgame.hex.Layout;
import com.pk.ltgame.hex.OffsetCoord;
import com.pk.ltgame.hex.FractionalHex;
import static com.pk.ltgame.hex.FractionalHex.hexRound;
import static com.pk.ltgame.hex.Layout.pixelToHex;
import static com.pk.ltgame.hex.OffsetCoord.qoffsetToCube;
import static com.pk.ltgame.hex.OffsetCoord.qoffsetFromCube;
import com.pk.ltgame.hud.GameHUD;
import com.pk.ltgame.hud.TechHUD;
import com.pk.ltgame.inputs.GameInput;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.players.AIPlayer;
import com.pk.ltgame.players.HumanPlayer;
import com.pk.ltgame.scr.GameScreen;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.pk.ltgame.hex.Layout.hexToPix;


/**
 * Klasa sceny. Zajmuje się zachowaniami obiektów na mapie.
 * @author pkale
 */
public final class TiledMapStage extends Stage {

    private TiledMap tiledMap;
    private FractionalHex pCube;
    private GameHUD gameHUDInputProcessor;
    private TechHUD techHUDInputProcessor;
    private Point p;
    private AIPlayer AIplayer;
    private Hex pHex;
    private Hex hexNB;

    /**
     * Szerokość mapy.
     */
    public static int mapXCells,

    /**
     * Wysokość mapy.
     */
    mapYCells;
    private TileBuildings buildings;
    private Units units;

    /**
     * Tabela dla koordynatów w postaci kubicznej.
     */
    public static Hex[][] cubeArr;
    private TextureAtlas atlas;
    private GameScreen gameAccess;

    /**
     * Kontener dla obrazów.
     */
    protected Skin skin;
    private ImageButton buttonMove, buttonFarm, buttonCraft, buttonHire, buttonMelee, buttonRange, buttonSpecial;

    /**
     * Wybrany oddział.
     */
    public static int selectedUnit,

    /**
     * Wybrany oddział do menu zakupu jednostek.
     */
    selectedUnitHire,

    /**
     * Wybrany oddział do zakupu jednostek
     */
    selectedHireIndex;
    private static Stage stage;
  
    /**
     * Tabela dla współrzędnych Hexów.
     */
    public Point[][] hexPixArr;
    private OffsetCoord clickOffset;
    private InputMultiplexer inputMux;

    /**
     * Lista sąsiadów dla wybranego Hexa.
     */
    public static ArrayList<Hex> hexNeighbors = new ArrayList<Hex>();

    /**
     * Lista budynków.
     */
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

    /**
     * Id budynku.
     */
    public int tileBuildingI = 0;
    Point layoutSize = new Point(99,99);
    Point layoutOrigin = new Point(99,172);
    Layout gameLayout = new Layout(Layout.flat, layoutSize, layoutOrigin);
    private final float mKill = 0.45f;
    private final float rKill = 0.45f;
    private final float sKill = 0.1f;

    /**
     * Tworzenie sceny dla mapy.
     * @param tiledMap Mapa.
     * @param playersList Lista graczy.
     * @param aiplayersList Lista graczy AI.
     * @param gameHUD HUD statystyk.
     * @param techHUD HUD technologii.
     */
    public TiledMapStage(TiledMap tiledMap, ArrayList<HumanPlayer> playersList,ArrayList<AIPlayer> aiplayersList , GameHUD gameHUD, TechHUD techHUD) {
       TiledMapStage.playersList = playersList;
       TiledMapStage.aiplayersList = aiplayersList;
       selectedUnit = -1;
       selectedUnitHire = -1;
       stage = new Stage();
       int tileUnitI = 0;
       atlas = new TextureAtlas("units.atlas");
       skin = new Skin(Gdx.files.internal("units.json"), atlas);
       this.tiledMap = tiledMap;
       this.gameHUDInputProcessor = gameHUD;
       this.techHUDInputProcessor = techHUD;
       addTileBuilding(tileBuildingI, 0, 3, -3, 100, 100, 8, 3, (float) 0.1, playersList.get(0).color, "castle",playersList.get(0).race);
       addTileBuilding(tileBuildingI, 3, -2, -1, 100, 100, 8, 3, (float)0.1, aiplayersList.get(0).color, "castle", aiplayersList.get(0).race);
       if(playersList.get(0).race.equals("Nieumarli"))
       {
            addTileUnit(tileUnitI, 0, 3, -3, 1, 1, 0, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
            addTileUnit(tileUnitI, 0, 3,-3, 1, 1, 0, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
            addTileUnit(tileUnitI, 0, 3,-3, 1, 1, 0, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
        } else if(playersList.get(0).race.equals("Demony")){
            addTileUnit(tileUnitI, 0, 3, -3, 5, 3, 1, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
            addTileUnit(tileUnitI, 0, 3,-3, 5, 3, 1, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
        }  else {
            addTileUnit(tileUnitI, 0, 3, -3, 4, 2, 0, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
            addTileUnit(tileUnitI, 0, 3,-3, 4, 2, 0, 1, playersList.get(0).color, playersList.get(0).race);
            tileUnitI++;
        }
        addTileUnit(tileUnitI,3,0,-3, 4, 2, 0, 1, aiplayersList.get(0).color, aiplayersList.get(0).race);
        tileUnitI++;
        addTileUnit(tileUnitI,3,0,-3, 4, 2, 0, 1, aiplayersList.get(0).color, aiplayersList.get(0).race);
        tileUnitI++;
        this.gameHUDInputProcessor.turnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            nextTurn();
            gameHUDInputProcessor.addTurn();
            }
        });
        this.techHUDInputProcessor.buttonSave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               saveGame();
            }   
        });
        this.techHUDInputProcessor.buttonDouble.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
            if(techHUDInputProcessor.techPoints >=1){
                techHUDInputProcessor.payByTechPoints(1);
                gameHUDInputProcessor.payTech(1);
                TiledMapStage.playersList.get(0).payByTechPoints(1);
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
                    TiledMapStage.playersList.get(0).payByTechPoints(2);
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
                    TiledMapStage.playersList.get(0).payByTechPoints(2);
                    techHUDInputProcessor.deleteTechnology(techHUDInputProcessor.buttonUnits);
                    techHUDInputProcessor.setTrue(2);
                }
            }
        });
        GameInput inputProcessor = new GameInput();
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
    }

    /**
     * Wczytanie sceny.
     * @param map Mapa.
     * @param playersList Lista graczy.
     * @param aiplayersList Lista graczy AI.
     * @param hud HUD dla statystyk.
     * @param buildingsList Lista budynków.
     * @param unitsList Lista jednostek.
     * @param thud HUD dla technologii.
     */
    public TiledMapStage(TiledMap map, ArrayList<HumanPlayer> playersList, ArrayList<AIPlayer> aiplayersList, GameHUD hud, ArrayList<TileBuildings> buildingsList, ArrayList<Units> unitsList, TechHUD thud) {
        this.playersList = playersList;
        this.aiplayersList = aiplayersList;
        this.atlas = new TextureAtlas("units.atlas");
        this.skin = new Skin(Gdx.files.internal("units.json"), atlas);
        this.tileBuildingI = 0;
        for(int i = 0;i<buildingsList.size();i++)
        {
           addTileBuilding(buildingsList.get(i).id,buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s,buildingsList.get(i).maxHP,buildingsList.get(i).HP,buildingsList.get(i).dayGold,buildingsList.get(i).dayFood,buildingsList.get(i).dayTechPoints,buildingsList.get(i).playerColor,buildingsList.get(i).textureName,buildingsList.get(i).race);
        }
        selectedUnit = -1;
        selectedUnitHire = -1;
        stage = new Stage();
        int tileUnitI = unitsList.size();
        for(int i=0;i<unitsList.size();i++)
        {
            addTileUnit(unitsList.get(i).id,unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s,unitsList.get(i).meleeUnits,unitsList.get(i).rangeUnits,unitsList.get(i).specialUnits,unitsList.get(i).move,unitsList.get(i).playerColor, unitsList.get(i).race);
        }
        this.tiledMap = map;
        this.gameHUDInputProcessor = hud;
        this.techHUDInputProcessor = thud;
        this.gameHUDInputProcessor.turnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextTurn();
                gameHUDInputProcessor.addTurn();
            }
        });
         this.techHUDInputProcessor.buttonSave.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
               saveGame();
            }
        });
        this.techHUDInputProcessor.buttonDouble.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(techHUDInputProcessor.techPoints >=1){
                    techHUDInputProcessor.payByTechPoints(1);
                    gameHUDInputProcessor.payTech(1);
                    TiledMapStage.playersList.get(0).payByTechPoints(1);
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
                    TiledMapStage.playersList.get(0).payByTechPoints(2);
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
                    TiledMapStage.playersList.get(0).payByTechPoints(2);
                    techHUDInputProcessor.deleteTechnology(techHUDInputProcessor.buttonUnits);
                    techHUDInputProcessor.setTrue(2);
                }
            }           
        });
      
        GameInput inputProcessor = new GameInput();
        InputMultiplexer inputMux = new InputMultiplexer();
        inputMux.addProcessor(stage);
        inputMux.addProcessor(this.gameHUDInputProcessor.stage);
        inputMux.addProcessor(this.techHUDInputProcessor.stage);
        inputMux.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(inputMux);
        for (MapLayer layer : tiledMap.getLayers()) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
            createActorsForLayer(tiledLayer);
        }
        this.allHexagonals = setUpAllOffsetCoords(cubeArr, mapXCells, mapYCells);
        allInvisible =  getAllInvisible();
    }  
 
    private void saveGame(){
        try {
            FileHandle save = Gdx.files.local("data/save");
            StringWriter writer = new StringWriter();
            XmlWriter xml = new XmlWriter(writer);
            xml.element("root");
            xml.element("GameHUD").attribute("playerColor", gameHUDInputProcessor.player).attribute("gold", gameHUDInputProcessor.gold).attribute("food", gameHUDInputProcessor.food).attribute("techPoints", gameHUDInputProcessor.techPoints).attribute("turn", gameHUDInputProcessor.turn);
            xml.pop();
            for(int i=0;i<playersList.size();i++)
            {
                xml.element("HumanPlayer").attribute("color", playersList.get(i).color).attribute("gold", playersList.get(i).gold).attribute("food", playersList.get(i).food).attribute("techpoints", playersList.get(i).techpoints).attribute("turn", playersList.get(i).turn).attribute("race", playersList.get(i).race).attribute("tech1", playersList.get(i).technology.get(0)).attribute("tech2", playersList.get(i).technology.get(1)).attribute("tech3", playersList.get(i).technology.get(2));
                xml.pop();
            }
            for(int i=0;i<aiplayersList.size();i++)
            {
                xml.element("AIPlayer").attribute("color", aiplayersList.get(i).color).attribute("gold", aiplayersList.get(i).gold).attribute("food", aiplayersList.get(i).food).attribute("techpoints", aiplayersList.get(i).techpoints).attribute("turn", aiplayersList.get(i).turn).attribute("race", aiplayersList.get(i).race);
                xml.pop();
            }
            for(int i=0;i<buildingsList.size();i++)
            {
                xml.element("TileBuildings").attribute("id", buildingsList.get(i).id).attribute("q", buildingsList.get(i).q).attribute("r", buildingsList.get(i).r).attribute("s", buildingsList.get(i).s).attribute("maxHP", buildingsList.get(i).maxHP).attribute("HP", buildingsList.get(i).HP).attribute("dayGold", buildingsList.get(i).dayGold).attribute("dayFood", buildingsList.get(i).dayFood).attribute("dayTechPoints", buildingsList.get(i).dayTechPoints).attribute("playerColor", buildingsList.get(i).playerColor).attribute("textureName", buildingsList.get(i).textureName).attribute("race", buildingsList.get(i).race);
                xml.pop();
            }
            for(int i=0;i<unitsList.size();i++)
            {
                xml.element("Units").attribute("id", unitsList.get(i).id).attribute("q", unitsList.get(i).q).attribute("r", unitsList.get(i).r).attribute("s", unitsList.get(i).s).attribute("meleeUnits", unitsList.get(i).meleeUnits).attribute("rangeUnits", unitsList.get(i).rangeUnits).attribute("specialUnits", unitsList.get(i).specialUnits).attribute("move", unitsList.get(i).move).attribute("playerColor", unitsList.get(i).playerColor).attribute("race", unitsList.get(i).race);
                xml.pop();
            }
            xml.pop();
            save.writeString(writer.toString(), false);
        } catch (IOException ex) {
            Logger.getLogger(TiledMapStage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Point getMouseClickPoint(double x, double y){
       return new Point(x,y);
    }

    /**
     * Konstruktor dostępu z ustawianiem koordynatów offsetowych dla pól.
     */
    public TiledMapStage() {
         this.allHexagonals = setUpAllOffsetCoords(cubeArr, mapXCells, mapYCells);
    }

    /**
     * Dodawanie budynku z pliku zapisu.
     * @param id Identyfikator obiektu.
     * @param q Koordynat q.
     * @param r Koordynat r.
     * @param s Koordynat s.
     * @param maxhp Ilość maksymalnych punktów życia.
     * @param hp Ilość aktualnych punktów życia.
     * @param gold Złoto na turę.
     * @param food Jedzenie na turę.
     * @param tPoints Ilość punktów nauki.
     * @param color Kolor gracza.
     * @param texture Nazwa tekstury.
     * @param race Rasa.
     */
    public final void addTileBuilding(int id, int q, int r, int s, int maxhp, float hp, int gold, int food, float tPoints, String color, String texture, String race) {
        buildings = new TileBuildings(id, q,r,s, maxhp, hp, gold, food, tPoints, color, texture, race);
        buildingsList.add(buildings);
        tileBuildingI++;
    }

    /**
     * Dodawanie jednostki z pliku zapisu.
     * @param id Identyfikator obiektu.
     * @param q Koordynat q.
     * @param r Koordynat r.
     * @param s Koordynat s.
     * @param meleeU Ilośc jednostek bliskiego zasięgu.
     * @param rangeU Ilość jednostek dalekiego zasięgu.
     * @param specialU Ilość jednostek specjalnych.
     * @param mv Ilość punktów ruchu.
     * @param color Kolor jednostki.
     * @param race Rasa gracza do którego jednostka należy.
     */
    public  void addTileUnit(int id, int q, int r, int s, int meleeU, int rangeU, int specialU, float mv, String color, String race) {
        units = new Units(id, q,r,s, meleeU, rangeU, specialU, mv, color, race);
        units.updateArmy();
        unitsList.add(units);
        buttonMove = new ImageButton(skin,"move");
        buttonFarm = new ImageButton(skin, "farm");
        buttonCraft = new ImageButton(skin, "craft");
        buttonHire = new ImageButton(skin, "hire");
        buttonMelee = new ImageButton(skin, "melee");
        buttonRange = new ImageButton(skin, "range");
        buttonSpecial = new ImageButton(skin, "special");
        buttonMove.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonFarm.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonCraft.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonHire.setSize(Gdx.graphics.getWidth()/40f, Gdx.graphics.getWidth()/40f);
        buttonMelee.setSize(Gdx.graphics.getWidth()/50f, Gdx.graphics.getWidth()/50f);
        buttonRange.setSize(Gdx.graphics.getWidth()/50f, Gdx.graphics.getWidth()/50f);
        buttonSpecial.setSize(Gdx.graphics.getWidth()/50f, Gdx.graphics.getWidth()/50f);
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
                    getNeighbors(selectedUnit);
                } else {
                    selectedUnit = -1;
                }
            }
        });
        buttonFarm.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int discountGold = 0;
                int discountFood = 0;
                if(techHUDInputProcessor.technology.get(1) == true){
                    discountGold = 3;
                    discountFood = 1;
                }
                if(playersList.get(0).gold>= 25-discountGold && playersList.get(0).food >= 10-discountFood){
                Units thisUnit = unitsList.get(buttonUnitsFarmList.indexOf(event.getTarget().getParent()));
                if(thisUnit.move>=1)
                {
                     if(containTileBuilding(thisUnit, "farm") == false)
                     {
                         if(containTileBuilding(thisUnit,"castle") == false)
                             {
                             TileBuildings thisBuilding = containTileBuildingObject(thisUnit, "craft");
                             if(thisBuilding !=null)
                             {
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
                if(selectedUnitHire != buttonUnitsHireList.indexOf((event.getTarget()).getParent()) )
                {
                    selectedUnitHire = buttonUnitsHireList.indexOf((event.getTarget()).getParent());
                    stage.addActor(buttonUnitsMeleeList.get(selectedUnitHire));
                    stage.addActor(buttonUnitsRangeList.get(selectedUnitHire));
                    stage.addActor(buttonUnitsSpecialList.get(selectedUnitHire));
                } else {
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
            }
            }
        });
        buttonRange.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
            }
            }
        });
        buttonSpecial.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
                }
            }
        });
        
        if(color.equals("RED")){
            stage.addActor(buttonMove);
            stage.addActor(buttonFarm);
            stage.addActor(buttonCraft);
            stage.addActor(buttonHire);
        }
    }

    /**
    * Wykonanie akcji przejścia do następnej tury dla jednostek, budynków oraz graczy AI i przejście do następnej tury.
    */
    public void nextTurn(){
        ArrayList<Integer> nc = new ArrayList<Integer>();
        for(int i=0;i<aiplayersList.size();i++){
            aiplayersList.get(i).makeADecision(unitsList, buildingsList,this.mapXCells,this.mapYCells,this);
        }
        for(int i=0;i<playersList.size();i++){
           
        }
        turnUnits(this.unitsList);
        turnPayday(this.buildingsList);
        turnBuildings(this.buildingsList);
    }

    /**
     * Sprawdzenie końca rozgrywki dla zwycięstwa przez zebranie armii.
     * @return Kolor gracza lub nic.
     */
    public String unitsEnd(){
        for(int i=0;i<aiplayersList.size();i++){
           
            
            int countUnits = 0;
            for(int j=0;j<unitsList.size();j++)
            {
                if(unitsList.get(j).playerColor.equals(aiplayersList.get(i).color))
                {
                    countUnits += unitsList.get(j).meleeUnits + unitsList.get(j).rangeUnits + unitsList.get(j).specialUnits;
                }
            }
            if(countUnits>= 70) return aiplayersList.get(i).color;
        }
        for(int i=0;i<playersList.size();i++){
            int countUnits = 0;
            for(int j=0;j<unitsList.size();j++)
            {
                if(unitsList.get(j).playerColor.equals(playersList.get(i).color))
                {
                    countUnits += unitsList.get(j).meleeUnits + unitsList.get(j).rangeUnits + unitsList.get(j).specialUnits;
                }
            }
            if(countUnits>= 70) return playersList.get(i).color;
        }
        
        return null;
    }

    /**
     * Wypłata złota, jedzenia i punktów nauki.
     * @param buildingsList Lista budynków.
     */
    
    public void turnPayday(ArrayList<TileBuildings> buildingsList){
        for(TileBuildings buildingsL: buildingsList){
            if(buildingsL.playerColor.equals("RED")){
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
     * Reset punków ruchu na koniec tury.
     * @param unitsList Lista jednostek.
     */
    public void turnUnits(ArrayList<Units> unitsList){
        for(Units unitsL: unitsList){
            unitsL.move =1;
        }
    }

    /**
     * Odbudowa budynków na koniec tury.
     * @param buildingsList Lista budynków
     */
    public void turnBuildings(ArrayList<TileBuildings> buildingsList){
        for(TileBuildings buildingsL: buildingsList){
            if(buildingsL.textureName.equals("farm") || buildingsL.textureName.equals("craft")){
                if(buildingsL.HP<40&& buildingsL.HP>=36){
                    buildingsL.HP = 40;
                    buildingsL.updateFillBar(buildingsL.HP/buildingsL.maxHP);
                } else if(buildingsL.HP<36){
                    buildingsL.HP +=4;
                    buildingsL.updateFillBar(buildingsL.HP/buildingsL.maxHP);
                }
            }
        }
    }
    
    /**
     * Sprawdzenie, czy pola mapy zawierają Hex.
     * @param q Koordynat q Hexa.
     * @param r Koordynat r Hexa.
     * @param s Koordynat s Hexa.
     * @return True, jeżeli zawiera; False, jeżeli nie zawiera.
     */
    public boolean containCubeArr(int q,int r,int s){
        for(int x = 0;x<mapXCells;x++)
        {
            for(int y = 0;y<mapYCells;y++)
            {
                if(cubeArr[x][y].q == q && cubeArr[x][y].r == r && cubeArr[x][y].s == s){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sprawdzenie, czy pole na którym jest jednostka zawiera dany budynek.
     * @param unit Wybrana jednostka,
     * @param newBuildingType Typ budynku.
     * @return Budynek na Hexie lub nic.
     */
    public TileBuildings containTileBuildingObject(Units unit, String newBuildingType){
        for (int i=0;i<buildingsList.size();i++) {
            if(buildingsList.get(i).q == unit.q && buildingsList.get(i).r == unit.r && buildingsList.get(i).s == unit.s && buildingsList.get(i).textureName.equals(newBuildingType)){
                return buildingsList.get(i);
            }
        }
        return null;
    }

    /**
     * Sprawdzenie, czy pole zawiera budynek przeciwnika.
     * @param targetHex Hex dla którego sprawdzany jest budynek.
     * @param playerColor Kolor aktualnego gracza.
     * @return Budynek na Hexie lub nic.
     */
    public TileBuildings containTileEnemyBuildingObject(Hex targetHex, String playerColor){
        //System.out.println("Rozmiar buildingsList: " + buildingsList.size());
        for(int i=0;i<buildingsList.size();i++)
        {
            if(buildingsList.get(i).q == targetHex.q && buildingsList.get(i).r == targetHex.r && buildingsList.get(i).s == targetHex.s && !playerColor.equals(buildingsList.get(i).playerColor)){
                return buildingsList.get(i);
            }
        }
        return null;
    }
    /**
     * Sprawdzenie, czy pole na którym znajduje się jednostka zawiera dany budynek.
     * @param unit Wybrana jednostka.
     * @param newBuildingType Typ budynku.
     * @return True, jeżeli zawiera; False, jeżeli nie zawiera.
     */
    public boolean containTileBuilding(Units unit, String newBuildingType){

        for (int i=0;i<buildingsList.size();i++) {
            if(buildingsList.get(i).q == unit.q && buildingsList.get(i).r == unit.r && buildingsList.get(i).s == unit.s && buildingsList.get(i).textureName.equals(newBuildingType)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sprawdzenie, czy pole zawiera dany budynek.
     * @param hex Wybrany Hex.
     * @param newBuildingType Typ budynku.
     * @return True, jeżeli zawiera; False, jeżeli nie zawiera.
     */
    public boolean containTileBuilding(Hex hex, String newBuildingType){

        for (int i=0;i<buildingsList.size();i++) {
            if(buildingsList.get(i).q == hex.q && buildingsList.get(i).r == hex.r && buildingsList.get(i).s == hex.s && buildingsList.get(i).textureName.equals(newBuildingType)){
                return true;
            }
        }
        return false;
    }

    /**
     * Sprawdzenie, czy pole zawiera wrogą jednostkę.
     * @param targetHex Wybrany Hex.
     * @param playerColor Kolor aktualnego gracza.
     * @return Jednostka na Hexie lub nic.
     */
    public Units containTileEnemyUnit(Hex targetHex, String playerColor){
        for(int i=0;i<unitsList.size();i++)
        {
            if(unitsList.get(i).q == targetHex.q && unitsList.get(i).r == targetHex.r && unitsList.get(i).s == targetHex.s && !playerColor.equals(unitsList.get(i).playerColor)){
                return unitsList.get(i);
            }
        }
        return null;
    }

    /**
     * Sprawdzenie, czy Hex zawiera jednostkę danego gracza.
     * @param targetHex Wybrany Hex.
     * @param playerColor Wybrany kolor gracza.
     * @return Jednostka na Hexie lub nic.
     */
    public Units containTileUnit(Hex targetHex, String playerColor){
        for(int i=0;i<unitsList.size();i++)
        {
            if(unitsList.get(i).q == targetHex.q && unitsList.get(i).r == targetHex.r && unitsList.get(i).s == targetHex.s && playerColor.equals(unitsList.get(i).playerColor)){
                return unitsList.get(i); // allyUnit
            }
        }
        return null;
    }

    /**
     * Obliczenie różnicy między dwiema wartościami.
     * @param value1 Pierwsza wartość.
     * @param value2 Druga wartość.
     * @return Różnica między wartościami.
     */
    public int difBetween(int value1, int value2){
        int dif = 0;
        if(value1>=value2)
        dif = value1-value2; else dif = value2-value1;
        return dif;
    }

    /**
     * Obliczanie ilości jednostek do usunięcia i ich usunięcie.
     * @param unit Jednostka do atakowania.
     * @param unitSelected Jednostka atakująca.
     * @param value Wartość ataku.
     */
    public void killCalculation(Units unit, Units unitSelected, float value){
        Units  unitsToKill = unitsList.get(unitsList.indexOf(unit));
        int indexUnitToKill = unitsList.indexOf(unit);
        int offset = 0;
        int repeat = 0;
        float economyPlayerSituation = 0;
        float selectedPlayerSituation = 0;
        float playerTreasury = 0;
        float selectedPlayerTreasury = 0;
        for(int i=0;i<playersList.size();i++)
        {
            if(playersList.get(i).color.equals(unit.playerColor)){
                playerTreasury = playersList.get(i).food*3 + playersList.get(i).gold;
            } else if(playersList.get(i).color.equals(unitSelected.playerColor)){
                selectedPlayerTreasury = playersList.get(i).food*3 + playersList.get(i).gold;
            }
        }
        for(int i=0;i<playersList.size();i++)
        {
            if(playersList.get(i).color.equals(unit.playerColor)){
                playerTreasury = playersList.get(i).food*3 + playersList.get(i).gold;
            } else if(playersList.get(i).color.equals(unitSelected.playerColor)){
                selectedPlayerTreasury = playersList.get(i).food*3 + playersList.get(i).gold;
            }
        }
        for(int i=0;i<buildingsList.size();i++)
        {
            if(buildingsList.get(i).playerColor.equals(unit.playerColor))
            {
                economyPlayerSituation += buildingsList.get(i).dayGold + buildingsList.get(i).dayFood*3;
            } else if(buildingsList.get(i).playerColor.equals(unitSelected.playerColor))
            {
                selectedPlayerSituation += buildingsList.get(i).dayGold + buildingsList.get(i).dayFood*3;
            }
        }
        float valueMultiplySimulation = ((selectedPlayerSituation/economyPlayerSituation) + (selectedPlayerTreasury/playerTreasury))/2;
        if(valueMultiplySimulation > 1.2f) valueMultiplySimulation = 1.2f;
        if(valueMultiplySimulation < 0.8f) valueMultiplySimulation = 0.8f;

        value = value*valueMultiplySimulation;
        if(value>0)
        {
          if(0.45*value<=0.5){
            value = 1.2f;
          }
          do{
            if(unitsToKill.meleeUnits >= Math.round(((repeat*0.55f)+ mKill) * (value+offset))){
                unitsToKill.meleeUnits -= Math.round(((repeat*0.55f)+ mKill)*(value+offset));
                offset = 0;
            } else {
                offset = Math.round(((repeat*0.55f)+mKill)*(value+offset)) - unitsToKill.meleeUnits;
                unitsToKill.meleeUnits = 0;
            }
            if(unitsToKill.rangeUnits >= Math.round(((repeat*0.55f)+rKill)*(value+offset))){
               unitsToKill.rangeUnits -= Math.round(((repeat*0.55f)+rKill)*(value+offset));
               offset = 0;
            } else {
                offset = Math.round(((repeat*0.55f)+rKill)*(value+offset))-unitsToKill.rangeUnits;
                unitsToKill.rangeUnits = 0;
            }
            if(unitsToKill.specialUnits>= Math.round(sKill*(value+offset))){
                unitsToKill.specialUnits -= Math.round(sKill*(value+offset));
                offset = 0;
            } else {
                offset = Math.round(sKill*(value+offset)) - unitsToKill.specialUnits;
                unitsToKill.specialUnits = 0;
            }
            if(offset>0)
            {
                value=0;
                repeat=1;
            }
            }while((((unitsToKill.meleeUnits+unitsToKill.rangeUnits+unitsToKill.specialUnits)>0) && offset != 0));
            if((unitsToKill.meleeUnits+unitsToKill.rangeUnits+unitsToKill.specialUnits)>0){
                unitsList.get(indexUnitToKill).meleeUnits = unitsToKill.meleeUnits;
                unitsList.get(indexUnitToKill).rangeUnits = unitsToKill.rangeUnits;
                unitsList.get(indexUnitToKill).specialUnits = unitsToKill.specialUnits;
                unitsList.get(indexUnitToKill).updateArmy();
            } else {
                if(unitsList.get(indexUnitToKill).playerColor.equals("RED"))
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
                buttonUnitsCraftList.remove(indexUnitToKill);
                buttonUnitsFarmList.remove(indexUnitToKill);
                buttonUnitsHireList.remove(indexUnitToKill);
                buttonUnitsMeleeList.remove(indexUnitToKill);
                buttonUnitsRangeList.remove(indexUnitToKill);
                buttonUnitsSpecialList.remove(indexUnitToKill);
                unitsList.remove(indexUnitToKill);
                selectedUnit = unitsList.indexOf(unitSelected);
            }
        }
    }

    /**
     * Obliczanie wartości ataku jednostek.
     * @param unitAttack Jednostka atakująca.
     * @param unitAttacked Jednostka atakowana.
     */
    public void attackUnit(Units unitAttack, Units unitAttacked){
        float attackValueTemp = unitAttack.meleeUnits + unitAttack.rangeUnits + unitAttack.specialUnits*3;
        float attackedValueTemp = unitAttacked.meleeUnits + unitAttacked.rangeUnits + unitAttacked.specialUnits*3;
        float attackValue = (attackValueTemp/3)*(attackValueTemp/attackedValueTemp);
        float attackedValue = (attackedValueTemp/3)*(attackedValueTemp/attackValueTemp);
        killCalculation(unitAttacked, unitAttack, attackValue);
        killCalculation(unitAttack, unitAttacked, attackedValue);
    }

    /**
     * Obliczanie ataku na wrogi budynek i zaatakowanie.
     * @param unit Jednostka atakująca.
     * @param buildingAttacked Atakowany budynek.
     * @param attackVal Wartość ataku.
     */
    public void conquerCalculation(Units unit, TileBuildings buildingAttacked, int attackVal){
        int tileID = buildingsList.indexOf(buildingAttacked);
        if(attackVal>buildingAttacked.HP)
        {
            buildingsList.get(tileID).HP = 20;
            buildingsList.get(tileID).updateFillBar(buildingsList.get(tileID).HP/buildingsList.get(tileID).maxHP);
            buildingsList.get(tileID).playerColor = unit.playerColor;
            buildingsList.get(tileID).race = unit.race;
            buildingsList.get(tileID).updateTexture();
        } else {
            buildingsList.get(tileID).HP -= attackVal;
             buildingsList.get(tileID).updateFillBar(buildingsList.get(tileID).HP/buildingsList.get(tileID).maxHP);
        }
    }

    /**
     * Atak na budynek.
     * @param unitAttack Jednostka atakująca.
     * @param buildingAttacked Atakowany budynek.
     */
    public void attackTile(Units unitAttack, TileBuildings buildingAttacked){
        int attackValue = unitAttack.meleeUnits+unitAttack.rangeUnits+4*unitAttack.specialUnits;
        conquerCalculation(unitAttack, buildingAttacked, attackValue);
    }
    
    /**
     * Ruch jednostką.
     * @param selectedID Wybrana jednostka.
     * @param hexCompare Hex do wykonania ruchu.
     * @param playerColor Kolor gracza.
     */
    public void moveUnit(int selectedID, Hex hexCompare, String playerColor) {
        for(int i=0; i < hexNeighbors.size();i++){
            if((hexNeighbors.get(i).q == hexCompare.q) && (hexNeighbors.get(i).r == hexCompare.r) && (hexNeighbors.get(i).s == hexCompare.s))
            {
                if(unitsList.get(selectedID).move >=1){
                    Units enemyUnit = containTileEnemyUnit(hexNeighbors.get(i), playerColor);
                    Units allyUnit = containTileUnit(hexNeighbors.get(i), playerColor);
                    TileBuildings enemyBuilding = containTileEnemyBuildingObject(hexNeighbors.get(i), playerColor);
                    if(enemyUnit != null)
                    {
                        attackUnit(unitsList.get(selectedID),enemyUnit);
                        selectedID = selectedUnit;
                        if(selectedID != -1) unitsList.get(selectedID).move-=1;
                    } else if(allyUnit != null) {
                    } else if(enemyBuilding !=null) {
                        attackTile(unitsList.get(selectedID), enemyBuilding);
                        unitsList.get(selectedID).move-=1;
                    } else{
                        unitsList.get(selectedID).q = hexCompare.q;
                        unitsList.get(selectedID).r = hexCompare.r;
                        unitsList.get(selectedID).s = hexCompare.s;
                        unitsList.get(selectedID).move-=1;
                    }
                }
            }
        }
        allInvisible = getAllInvisible();
    }

    /**
     * Pobieranie sąsiednich pól dla wybranej jednostki.
     * @param selectedIndex Indeks wybranej jednostki.
     */
    public void getNeighbors(int selectedIndex){
        hexNeighbors.clear();
        pHex = new Hex(unitsList.get(selectedIndex).q,unitsList.get(selectedIndex).r,unitsList.get(selectedIndex).s);
        for(int i=0; i<6;i++)
        {
            hexNB = Hex.neighbor(pHex, i);
            if(containCubeArr(hexNB.q,hexNB.r,hexNB.s) == true){
                hexNeighbors.add(hexNB);
            }
        }
    }
    
    /**
     * Pobieranie sąsiednich pól dla podanych koordynatów.
     * @param q Koordynat q.
     * @param r Koordynat r.
     * @param s Koordynat s.
     * @return Lista sąsiednich pól.
    */
    public ArrayList<OffsetCoord> getNeighbors(int q, int r, int s){
        ArrayList<OffsetCoord> allVisible = new ArrayList<OffsetCoord>();
        Hex checkVisible = null;
        for(int i=0; i<6;i++)
        {
            checkVisible = Hex.neighbor(new Hex(q,r,s), i);
            if(this.containCubeArr(checkVisible.q,checkVisible.r,checkVisible.s) == true){
                if(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).col % 2 == 0){
                    allVisible.add(new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).row)));
                } else {
                    allVisible.add(new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,checkVisible).row)));
                }
            }
        }
        return allVisible;
    }

    /**
     * Zwraca pola niewidoczne.
     * @return Lista pól niewidocznych.
     */
    public ArrayList<OffsetCoord> returnInvisible(){
        return allInvisible;
    }

    /**
     * Zwraca pola widoczne.
     * @return Lista pól widocznych.
    */
    public ArrayList<OffsetCoord> getAllVisible(){
        ArrayList<OffsetCoord> allVisibleOC = new ArrayList<OffsetCoord>();
        ArrayList<OffsetCoord> allVisibleSublist = new ArrayList<OffsetCoord>();
        OffsetCoord objectOC;
        for(int i=0;i<buildingsList.size();i++)
        {
            if(buildingsList.get(i).playerColor.equals("RED"))
            {
                if(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col % 2 == 0){
                    objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row));
                } else {
                    objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row));
                }
            allVisibleSublist = getNeighbors(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s);
            allVisibleSublist.add(objectOC);
            for(int j=0;j<allVisibleSublist.size();j++)
            {
                    if(!eqOffset(allVisibleOC, allVisibleSublist.get(j)))
                    {
                        allVisibleOC.add(allVisibleSublist.get(j));
                    }
            }
            }   
             
        }
        for(int i=0;i<unitsList.size();i++)
        {
            if(unitsList.get(i).playerColor.equals("RED"))
            {
                if(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col % 2 == 0){
                    objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row));
                } else {
                    objectOC = new OffsetCoord(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col,swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row));
                }
                allVisibleSublist = getNeighbors(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s);
                allVisibleSublist.add(objectOC);
                for(int j=0;j<allVisibleSublist.size();j++)
                {
                    if(!eqOffset(allVisibleOC, allVisibleSublist.get(j)))
                    {
                        allVisibleOC.add(allVisibleSublist.get(j));
                    }
                }
            }  
        }
        return allVisibleOC;        
    }
    
    /**
    * Zebranie listy pól niewidocznych na podstawie listy pól.
    * @return Lista pól niewidocznych.
    */
    private ArrayList<OffsetCoord> getAllInvisible(){
        ArrayList<OffsetCoord> allInvisibleH = new ArrayList<OffsetCoord>();
        ArrayList<OffsetCoord> allVisible = getAllVisible();
        ArrayList<Integer> removeId = new ArrayList<Integer>();
        allInvisibleH.addAll(allHexagonals);
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
        for(int i=0;i<removeId.size();i++)
        {
           allInvisibleH.remove(removeId.get(i).intValue());
        }
        return allInvisibleH;
        
    }
    
    /**
     * Ustawianie koordynatów offsetowych.
     * @param cube Tabela koordynatów kubicznych.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     * @return Lista koordynatów offsetowych.
     */
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
        return allOffsetCoords;
    }
    
    /**
     * Sprawdzenie, czy koordynaty offsetowe są takie same.
     * @param firstOC Pierwsze koordynaty.
     * @param secondOC Drugie koordynaty.
     * @return True, jeżeli równe; False, jeżeli nie są równe.
     */
    private boolean eqOffset(ArrayList<OffsetCoord> firstOC, OffsetCoord secondOC){
        for(int i=0;i<firstOC.size();i++)
        {
            if(firstOC.get(i).col == secondOC.col && firstOC.get(i).row == secondOC.row) return true;
        }
        return false;
    }
    

    /**
     * Przetwarza współrzędne kliknięcia na odpowiadający im Hex.
     * @param x Współrzędna x.
     * @param y Współrzędna y.
     */
    public void mouseClickToHex( double x, double y){
        p = getMouseClickPoint(x, y);
        pCube =   pixelToHex(gameLayout, p);
        pHex = hexRound(pCube);
        if(selectedUnit>=0){
            moveUnit(selectedUnit, pHex, playersList.get(0).color);
            selectedUnit = -1;
        } else {
            if(pHex.q %2 == 0){
                clickOffset = qoffsetFromCube(OffsetCoord.EVEN,pHex);
            } else {
                clickOffset = qoffsetFromCube(OffsetCoord.EVEN,pHex); //ODD nie zadziała, bo libGDX trzyma dane o komórkach jako koordynaty row-col liczone od 0
        }
        for(int i=0; i<6;i++)
        {
            hexNB = Hex.neighbor(pHex, i);
        }
        }
    }

    /**
     * Tworzenie aktorów dla warstw mapy.
     * @param tiledLayer Warstwa mapy.
     */
    private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
        cubeArr = new Hex[tiledLayer.getWidth()][tiledLayer.getHeight()];
        hexPixArr = new Point[tiledLayer.getWidth()][tiledLayer.getHeight()];
        mapXCells = tiledLayer.getWidth();
        mapYCells = tiledLayer.getHeight();
        Point layoutSize = new Point(99,99);
        Point layoutOrigin = new Point(99,172);
        Layout gameLayout = new Layout(Layout.flat, layoutSize, layoutOrigin);
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor = new TiledMapActor(cell);
                OffsetCoord coords = new OffsetCoord(x, y);
                if( x%2 == 0 )
                {
                    cubeArr[x][y] = qoffsetToCube(OffsetCoord.EVEN, coords);
                    hexPixArr[x][y] = hexToPix(gameLayout, cubeArr[x][y]);
                } else  {
                    cubeArr[x][y] =  qoffsetToCube(OffsetCoord.EVEN, coords); // ODD wyłapuje jeden niżej więcej
                    hexPixArr[x][y] = hexToPix(gameLayout, cubeArr[x][y]);
                }
            addActor(actor);
            }
        }
    }

    /**
     * Zamiana współrzędnej offsetowej koordynatu.
     * @param y Offset y.
     * @return Zamieniony offset.
     */
    public int swapCoords(int y){
        y = mapYCells - 1 - y;
        return y;
    }

    /**
     * Renderowanie obiektów sceny.
     */
    public void renderObjects(){
        for(int i=0;i < buildingsList.size();i++){
            if( (qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col)%2 == 0 )
            {
                buildingsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].x-99, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].y - 172));
            } else {
                buildingsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].x-99, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row)].y ));
            }
        }
        for(int i=0;i< unitsList.size();i++){
            if( (qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col)%2 == 0)
            {
                unitsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x-54, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 157));
                buttonUnitsMoveList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x - 60));
                buttonUnitsMoveList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 115));
                buttonUnitsFarmList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x - 40));
                buttonUnitsFarmList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 85));
                buttonUnitsCraftList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x));
                buttonUnitsCraftList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 85));
                buttonUnitsHireList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 20));
                buttonUnitsHireList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 115));
                buttonUnitsMeleeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 45));
                buttonUnitsMeleeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 80));
                buttonUnitsRangeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 55));
                buttonUnitsRangeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 110));
                buttonUnitsSpecialList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 45));
                buttonUnitsSpecialList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y - 140));
            } else {
                unitsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x-54, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(1+OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y +15));
                buttonUnitsMoveList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x - 60));
                buttonUnitsMoveList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 55));
                buttonUnitsFarmList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x - 40));
                buttonUnitsFarmList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y +85));
                buttonUnitsCraftList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x));
                buttonUnitsCraftList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 85));
                buttonUnitsHireList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row+1].x + 20));
                buttonUnitsHireList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(1+qoffsetFromCube(OffsetCoord.ODD,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 55));
                buttonUnitsMeleeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 40));
                buttonUnitsMeleeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 85));
                buttonUnitsRangeList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 55));
                buttonUnitsRangeList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 55));
                buttonUnitsSpecialList.get(i).setX((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].x  + 40));
                buttonUnitsSpecialList.get(i).setY((float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][swapCoords(qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row)].y + 25));
            }
        }
        stage.act();
        stage.draw();
    }
}