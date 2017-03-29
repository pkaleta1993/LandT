package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.hex.OffsetCoord;
import com.pk.ltgame.hud.GameHUD;
import com.pk.ltgame.hud.TechHUD;
import com.pk.ltgame.map.MapLoader;
import com.pk.ltgame.map.TiledMapStage;
import com.pk.ltgame.players.HumanPlayer;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.players.AIPlayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa ekranu rozgrywki.
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
    private Texture img;
    private Sprite pool;
    /**
     * Lista technologii.
     */
    public ArrayList<Boolean> tech;
    private ParticleEffectPool partyPool;
    private PooledEffect effect;
     /**
     * 
     */
    ArrayList<HumanPlayer> playersList = new ArrayList<HumanPlayer>();
    ArrayList<AIPlayer> aiplayersList = new ArrayList<AIPlayer>();
    ArrayList<Units> unitsList = new ArrayList<Units>();
    ArrayList<TileBuildings> buildingsList = new ArrayList<TileBuildings>();
    ArrayList<PooledEffect> effects = new ArrayList<PooledEffect>();
    private static ArrayList<OffsetCoord> partyListCoord = new ArrayList<OffsetCoord>();

    private SpriteBatch batch;

    /**
     * Konstruktor ekranu rozgrywki.
     * @param game Obiekty gry.
     * @param race Rasa gracza.
     */
    public GameScreen(LandTerrorGame game, String race) {
        super(game);
        this.race = race;
        init();
    }
    
    /**
     * Konstruktor wczytywania ekranu rozgrywki.
     * @param game Obiekt gry.
     * @param saveData Plik z zapisem rozgrywki.
     */
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
                String race = e.getAttribute("race");
                Units unit = new Units(id, q, r, s, meleeUnits, rangeUnits, specialUnits, move, playerColor, race);
                unitsList.add(unit);
            }
            this.unitsList = unitsList;
        } catch (IOException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initLoad(this.gamehud, this.playersList, this.aiplayersList, this.buildingsList, this.unitsList);
    }

    /**
     * Inicjalizacja ekranu rozgrywki.
     */
    private void init(){
     
        initMap();
        batch = new SpriteBatch();
        img = new Texture(Gdx.files.internal("overHex.png"));
        pool = new Sprite(img);     
    }
    
    /**
     * Inicjalizacja wczytania ekranu rozgrywki.
     * @param hud
     * @param playersList
     * @param aiplayersList
     * @param buildingsList
     * @param unitsList 
     */
    private void initLoad(GameHUD hud, ArrayList<HumanPlayer> playersList, ArrayList<AIPlayer> aiplayersList, ArrayList<TileBuildings> buildingsList, ArrayList<Units> unitsList){
        batch = new SpriteBatch();
        img = new Texture(Gdx.files.internal("overHex.png"));
        pool = new Sprite(img); 
        splashgamehudimg = new Texture("gamehud.PNG");
        this.playerH = playersList.get(0);
        this.playerAI = aiplayersList.get(0);
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
    
    /**
     * Inicjalizacja mapy wraz z obiektami sceny.
     */
    private void initMap(){
        tech = new ArrayList<Boolean>();
        tech.add(Boolean.FALSE);
        tech.add(Boolean.FALSE);
        tech.add(Boolean.FALSE);
        splashgamehudimg = new Texture("gamehud.PNG");
        playerH = new HumanPlayer("RED", 30, 10, 0, 1, race, tech); // kolor, zloto, jedzenie, nauka i tura
        playerAI = new AIPlayer("BLUE",30,10,0,1, "Ludzie");
        if(playerH.race.equals("Ludzie"))
        {
            playerH.payFor(-20, -8);
        }
        if(playerAI.race.equals("Ludzie"))
        {
            playerAI.payFor(-20, -8);
        }
        playersList.add(playerH);
        aiplayersList.add(playerAI);
        gamehud = new GameHUD(playerH.color, playerH.gold, playerH.food, playerH.techpoints, playerH.turn);
        techhud = new TechHUD(tech, 0);
        mapLoad = new MapLoader();
        stage.addActor(mapLoad);
        mapLoad.createMap();
        stage = new TiledMapStage(mapLoad.map, playersList, aiplayersList, gamehud, techhud);
        tiledMapS = (TiledMapStage) stage;
    
    }
    
    /**
     *Pobieranie koordynatów offsetowych ze sceny mapy.
     */
    public void getOC(){
        this.partyListCoord = tiledMapS.returnInvisible();
       
    }
    
    /**
     * Sprawdzanie zwycięstwa dominacyjnego(zajęcie stolicy).
     */
    public void checkEnd(){
        if(tiledMapS.buildingsList.get(0).playerColor.equals("RED")){
            if(!tiledMapS.buildingsList.get(1).playerColor.equals("BLUE")){
                game.setScreen(new EndScreen(game, true));
            }
        } else {
            game.setScreen(new EndScreen(game, false));
        }
    }
    
    /**
     * Sprawdzenie zwycięstwa jednostkowego(70 jednostek w armii).
     * @param end Kolor gracza zwycięskiego lub null, jeżeli takiego nie ma.
     */
    public void checkUnitsEnd(String end) {
         if(end != null && !end.isEmpty()){ 
             if(end.equals("RED")) game.setScreen(new EndScreen(game, true)); else if(end.equals("BLUE")) game.setScreen(new EndScreen(game, false));
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
        batch.begin();
        for (int i = partyListCoord.size() - 1; i >= 0; i--) {
            if(partyListCoord.get(i).col %2 == 0  )
            {
                batch.draw(img, (int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].x-99, (int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].y-176, 198, 172);
            } else {
                batch.draw(img, (int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].x-99, (int)tiledMapS.hexPixArr[partyListCoord.get(i).col][partyListCoord.get(i).row].y-4, 198, 172);
            }
        }
        batch.end();
        spriteBatch.begin();
        spriteBatch.draw(splashgamehudimg, 0, Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/16, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/16);
        spriteBatch.end();
        gamehud.stage.draw();
        spriteBatch.setProjectionMatrix(gamehud.stage.getCamera().combined);
        gamehud.update(Gdx.graphics.getDeltaTime());
        techhud.stage.draw();
        update();
        checkEnd();
        checkUnitsEnd(tiledMapS.unitsEnd());
    }
    
    /**
     * Aktualizacja gry aktorów.
     */
    private void update(){
       stage.act();
    }
    
    @Override
    public void dispose() {
        splashgamehudimg.dispose();
    }
}
