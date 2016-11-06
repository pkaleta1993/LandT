/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static java.lang.Math.sqrt;


/**
 *
 * @author pkale
 */
public class TiledMapStage extends Stage {
    
    private TiledMap tiledMap;
    
    public static Vector2[][] matrix; 
    public static Vector3[][] matrixCube; 
    
    
    public TiledMapStage(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        for (MapLayer layer : tiledMap.getLayers()) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
            createActorsForLayer(tiledLayer);
        }
    }
//Pamiętać o getTile z gdx.maps.tiled.TiledMapTileLayer.Cell;
 
   //To działało, ale jedynie do zwykłych - prostokątnych - komórek
   /* private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
               actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
                        tiledLayer.getTileHeight());
                System.out.println(x);
                addActor(actor);
                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);
            }
        }
    }
    */ 
    
    
    private void showVector2(Vector2[][] m) {
       
        for(int tX=0;tX<m.length;tX++)
        {
            for(int tY=0;tY<m.length;tY++) 
            {
             System.out.print(m[tX][tY]+" ");
        }
            System.out.println();
        }
       
    }
    
       private void showVector3(Vector3[][] m) {
       
        for(int tX=0;tX<m.length;tX++)
        {
            for(int tY=0;tY<m.length;tY++) 
            {
             System.out.print(m[tX][tY]+" ");
        }
            System.out.println();
        }
       
    }
    
    private void Vector2ToCube(Vector2[][] m) { // convert odd-q offset to cube
         String xS ="";
         String yS="";
         String zS="";
        
              matrixCube = new Vector3[m.length][m.length];
              System.out.println("Dlugosc matrix m: " + m.length);
        for(int tX=0;tX<m.length;tX++)
        {
            for(int tY=0;tY<m.length;tY++) 
            {
       /*
        To jest na even-r        
        int x = (int) (m[tX][tY].y - (m[tX][tY].x + ((int)m[tX][tY].x%2)) / 2);
        int z = (int) m[tX][tY].x;
       int y = -x-z;
                */
        int x = (int) m[tX][tY].y;
        int z = (int) (m[tX][tY].x - (m[tX][tY].y - ((int)m[tX][tY].y%2))/2);
        int y = -x-z;
        xS = xS + x;
        yS = yS +y;
        zS = zS +z;
        matrixCube[tX][tY] = new Vector3(x,y,z);
        //System.out.print(" " + x + "  " +y + "  " + z +"" + "\n");
        
       System.out.println("X: " + x + " Y: " + y + " Z: " + z);
            }
            System.out.println();
        }
    }
    private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
      //  int tileX = tiledLayer.getWidth();
      //  int tileY = tiledLayer.getHeight();
      matrix = new Vector2[tiledLayer.getWidth()][tiledLayer.getHeight()];
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
                //matrix = new Vector2(x,y);
                matrix[x][y] = new Vector2(x,y);
               
               actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
                       tiledLayer.getTileHeight());
                //System.out.println("X: " + actor.getX());
                //System.out.println("Y: " + actor.getY());
              //  tileX = tiledLayer.getWidth();
               // System.out.println("Szerokosc : " + tileX);
               //  System.out.println("Wysokosc : " + tileY);
              //  System.out.println(x);
                addActor(actor);
                EventListener eventListener = new TiledMapClickListener(actor);
                actor.addListener(eventListener);
               
            }
        }
        showVector2(matrix);
        Vector2ToCube(matrix);
        System.out.println("Vector 3: ");
        showVector3(matrixCube);
    }
    
/*
    
public void hexpix(int x, int y){
    int size = (int) (178*0.5);
   double q = x * 2/3 / size;
   double r = (-x / 3 + sqrt(3)/3 * y) / size;
    return hex_round(Hex(q, r));
            }  
public void hex_round(h){
    return cube_to_hex(cube_round(hex_to_cube(h)));
}

public void cube_to_hex(h){
    double q = h.x;
    double  r = h.z;
    return Hex(q, r);
            }
public void hex_to_cube(h){
    double  x = h.q;
    double  z = h.r;
    double  y = -x-z;
    return Cube(x, y, z);
            }

public void cube_round(h){
    double rx = round(h.x);
    double ry = round(h.y);
    double rz = round(h.z);

    double x_diff = abs(rx - h.x);
    double y_diff = abs(ry - h.y);
    double z_diff = abs(rz - h.z);

    if (x_diff > y_diff and x_diff > z_diff)
    {
        rx = -ry-rz;
    }    else if( y_diff > z_diff)
    {
        ry = -rx-rz;
    }   else{
        rz = -rx-ry;
     }
    return Cube(rx, ry, rz);
}
*/
}