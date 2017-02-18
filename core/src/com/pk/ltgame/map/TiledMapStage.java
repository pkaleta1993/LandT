/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.pk.ltgame.hex.Point;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
import com.pk.ltgame.inputs.GameInput;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.players.HumanPlayer;
import java.util.ArrayList;


/**
 *
 * @author pkale
 */
public class TiledMapStage extends Stage {
    
    private TiledMap tiledMap;
    private FractionalHex pCube;
    private GameHUD gameHUDInputProcessor;
    private Point p;
    private Hex pHex;
    private Hex hexNB;
    private TileBuildings buildings;
    private Units units;
    private Hex[][] cubeArr; 
     private TextureAtlas atlas;
     protected Skin skin;
    private ImageButton button;
    private Stage stage;
    //private Stage stageInputProcessor;
    public Point[][] hexPixArr;
    private OffsetCoord clickOffset;
    ArrayList<TileBuildings> buildingsList = new ArrayList<TileBuildings>();
    ArrayList<Units> unitsList = new ArrayList<Units>();
    private final ArrayList<HumanPlayer> playersList = new ArrayList<HumanPlayer>();
    public TiledMapStage(TiledMap tiledMap, ArrayList<HumanPlayer> playersList, GameHUD gameHUD) {
        stage = new Stage(); 
       
      //  stageInputProcessor = GameHUD.getStage();
        this.tiledMap = tiledMap;
        this.gameHUDInputProcessor = gameHUD;
       //  Gdx.input.setInputProcessor(stage);
         buildings = new TileBuildings(0,3,-3, playersList.get(0).color);
        
//Gdx.input.setInputProcessor(this.stage);
         buildingsList.add(buildings);
         buildings = new TileBuildings(3,0,-3, playersList.get(0).color);
       buildingsList.add(buildings);
       units = new Units(0,3,-3, playersList.get(0).color);
       unitsList.add(units); 
        atlas = new TextureAtlas("units.atlas");
        skin = new Skin(Gdx.files.internal("units.json"), atlas);

       button = new ImageButton(skin);
       button.setSize(Gdx.graphics.getWidth()/20f, Gdx.graphics.getWidth()/20f);
   // button.setPosition(20, 90);
   /* button.addListener( new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println("Kliknietooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        };
    });*/
    button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
               //((Game)Gdx.app.getApplicationListener()).setScreen(new GameScreen(game));
                  //   System.out.println("Kliknietooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");  
                     Gdx.app.log("unitButton", "kliknieto");
// game.setScreen(new MenuScreen(game));
            }
        });
   
        stage.addActor(button);
        // Gdx.input.setInputProcessor(stage);
       /* InputMultiplexer obsługuje kilka inputProcessorów, a dokładniej rzecz ujmując - sam działa jako inputProcessor,
        tyle że rozsyła wszystkie akcje do potomnych procesów.
        */ 
        GameInput inputProcessor = new GameInput();
        //  inputProcessor inputStage = new inputProcessor();
         InputMultiplexer inputMux = new InputMultiplexer();
         inputMux.addProcessor(stage);
        inputMux.addProcessor(gameHUDInputProcessor.stage);
       inputMux.addProcessor(inputProcessor);
       
         Gdx.input.setInputProcessor(inputMux);
        for (MapLayer layer : tiledMap.getLayers()) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
            createActorsForLayer(tiledLayer);
        }
        
    }
    private Point getMouseClickPoint(double x, double y){
       return new Point(x,y);
    }
    
    public TiledMapStage() {
        
    }

    public void mouseClickToHex( double x, double y){
         p = getMouseClickPoint(x, y); 
       Point layoutSize = new Point(99,99);
        Point layoutOrigin = new Point(99,172);
          Layout gameLayout = new Layout(Layout.flat, layoutSize, layoutOrigin);
         pCube =   pixelToHex(gameLayout, p);
         pHex = hexRound(pCube);
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
    
    private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
    
     
        cubeArr = new Hex[tiledLayer.getWidth()][tiledLayer.getHeight()];
         hexPixArr = new Point[tiledLayer.getWidth()][tiledLayer.getHeight()];
         
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
               
                if( x%2 == 0)
                {
               
                  cubeArr[x][y] = qoffsetToCube(OffsetCoord.EVEN, coords);
                  // cubeArr[x][y] = roffsetToCube(OffsetCoord.EVEN, coords);
                  //  cubeArr[x][y] = roffsetToCube(OffsetCoord.EVEN, coords);
                     System.out.println("Hex w Arr - Q: " + cubeArr[x][y].q + " R : " + cubeArr[x][y].r  + " S: " + cubeArr[x][y].s);
                hexPixArr[x][y] = hexToPixel(gameLayout, cubeArr[x][y]);
                System.out.println("Hex w hexPixArr - Q: " + hexPixArr[x][y].x + " R: " +hexPixArr[x][y].y);
                        //pixelToHex(gameLayout,hexToPixel(gameLayout, hex));
                
                } else {
               cubeArr[x][y] =  qoffsetToCube(OffsetCoord.ODD, coords); 
               //cubeArr[x][y] =  roffsetToCube(OffsetCoord.ODD, coords); 
               //cubeArr[x][y] =  roffsetToCube(OffsetCoord.ODD, coords); 
               System.out.println("ODD Hex w Arr - Q: " + cubeArr[x][y].q + " R : " + cubeArr[x][y].r  + " S: " + cubeArr[x][y].s);
                hexPixArr[x][y] = hexToPixel(gameLayout, cubeArr[x][y]);
                 System.out.println("Hex w hexPixArr - Q: " + hexPixArr[x][y].x + " R: " +hexPixArr[x][y].y);
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
    
    public void renderObjects(){
        
        for(int i=0;i < buildingsList.size();i++){
           
                 //   buildings =  buildingsList.get(i);
                   buildingsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row].x-84, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.EVEN,new Hex(buildingsList.get(i).q,buildingsList.get(i).r,buildingsList.get(i).s)).row].y - 127));
                  
        }
        for(int i=0;i< unitsList.size();i++){
             unitsList.get(i).render((float) hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row].x-44, (float) (hexPixArr[qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).col][qoffsetFromCube(OffsetCoord.EVEN,new Hex(unitsList.get(i).q,unitsList.get(i).r,unitsList.get(i).s)).row].y - 157));
                   
        }
        button.setX(110);
        button.setY(220);
          stage.act();
        stage.draw();
    }
    

}