/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.pk.ltgame.hex.Point;
import com.badlogic.gdx.scenes.scene2d.Stage;
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


/**
 *
 * @author pkale
 */
public class TiledMapStage extends Stage {
    
    private TiledMap tiledMap;
    private FractionalHex pCube;
    private Point p;
    private Hex pHex;
    private Hex hexNB;
    private Hex[][] cubeArr;
    private Point[][] hexPixArr;
    private OffsetCoord clickOffset;
    public TiledMapStage(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        
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
               // System.out.println("Hex w hexPixArr - Q: " + hexPixArr[x][y].x + " R: " +hexPixArr[x][y].y);
                        //pixelToHex(gameLayout,hexToPixel(gameLayout, hex));
                
                } else {
               cubeArr[x][y] =  qoffsetToCube(OffsetCoord.ODD, coords); 
               //cubeArr[x][y] =  roffsetToCube(OffsetCoord.ODD, coords); 
               //cubeArr[x][y] =  roffsetToCube(OffsetCoord.ODD, coords); 
               System.out.println("ODD Hex w Arr - Q: " + cubeArr[x][y].q + " R : " + cubeArr[x][y].r  + " S: " + cubeArr[x][y].s);
                hexPixArr[x][y] = hexToPixel(gameLayout, cubeArr[x][y]);
               //  System.out.println("Hex w hexPixArr - Q: " + hexPixArr[x][y].x + " R: " +hexPixArr[x][y].y);
                }
                
                addActor(actor);
            
               
            }
        }
        
        System.out.println("Myszka X: "+ Gdx.input.getX( )+ " A Y:" + Gdx.input.getY());
       // p = new Point(Gdx.input.getX( ),Gdx.input.getY( ));
       // pCube =   pixelToHex(gameLayout, p);
       // System.out.println("A klikniecie na hex to hex(frac): " + pCube.q + "," + pCube.r + ","+pCube.s );
       // System.out.println("Hex w Arr - Q: " + cubeArr[2][2].q + " R : " + cubeArr[2][2].r  + " S: " + cubeArr[2][2].s);
      // System.out.println("Hex w HexPixArr - Q: " + hexPixArr[0][0].x + " R : " + hexPixArr[0][0].y);
     
    }
    

}