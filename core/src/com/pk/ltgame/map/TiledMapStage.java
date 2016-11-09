/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import static java.lang.Math.sqrt;


/**
 *
 * @author pkale
 */
public class TiledMapStage extends Stage {
    
    private TiledMap tiledMap;
    
    public static Vector2[][] matrix; 
    public static Vector2[][] matrixPoints; 
    public static Vector3[][] matrixCube; 
    private final float size=99;
    
    public TiledMapStage(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        
        for (MapLayer layer : tiledMap.getLayers()) {
            TiledMapTileLayer tiledLayer = (TiledMapTileLayer)layer;
            createActorsForLayer(tiledLayer);
        }
        
    }
    public TiledMapStage() {
        
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

    public void pixToHex(int x, int y){
      System.out.println("Przesylane x: " + x +" i y: " + y + " size: " + size);  
    double q = x * 2/3 / size;
    double r = (-x / 3 + sqrt(3)/3 * y) / size;
    System.out.println("q: " + q +" i r: " + r);
   // Vector3[] vec = new Vector3[1];
    //vec[0] =  new Vector3((float)q, (float)(-q-r),(float)r);
        Vector2[] vecph = new Vector2[1];
    vecph[0] = new Vector2((float)q,(float)r);
    //cube_round(vec[0]);
    //return hex_round(Hex(q, r))
     
   cube_to_hex(cube_round(hextocube(vecph[0])));
    }
    
   public Vector3 hextocube(Vector2 v){
     float x = v.x;
    float z = v.y;
    float y = -x-z;
      Vector3[] vec = new Vector3[1];
    vec[0] =  new Vector3(x, y,z);
    return vec[0];
   }
 private Vector2 cube_to_hex(Vector3 v){
    float q = v.x;
    float r = v.z;
    Vector2[] vec = new Vector2[1];
    vec[0] = new Vector2(q,r);
     System.out.println("Po cube_to_hex mamy q = " + q + " oraz r = " + r);
    return vec[0];
   
}
private Vector3 cube_round(Vector3 v){
    
    
    int q = (Math.round(v.x));
    int r = (Math.round(v.y));
    int s = (Math.round(v.z));

    double q_diff = Math.abs(q-v.x);
    double r_diff = Math.abs(r-v.y);
    double s_diff = Math.abs(s-v.z);
   

  if (q_diff > r_diff && q_diff > s_diff)
        {
            q = -r - s;
        }
        else
            if (r_diff > s_diff)
            {
                r = -q - s;
            }
            else
            {
                s = -q - r;
            }
       // return new Hex(q, r, s);
       System.out.println("Nowe q: " + q + "Nowe r: " + r + "Nowe s: " + s );
        Vector3[] vec = new Vector3[1];
         vec[0] =  new Vector3(q, r,s);
            return vec[0];
            }


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
    
    private void revOffsetCoord(Vector2[][] m){
        Vector2[][] x = new Vector2[10][10];
         for(int xX=0;xX<m.length;xX++)
        {
            for(int xY=0;xY<m.length;xY++) 
            {
            x[xX][xY] = m[xX][xY];
        }
        }
            
        for(int tX=0;tX<m.length;tX++)
        {
            for(int tY=0;tY<m.length;tY++) 
            {
            m[tX][tY] = x[tX][m.length-1-tY];
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
        private Vector2 getAxialFromCube(Vector3 m)
    {
       Vector2[] vec = new Vector2[1];
        int q = (int) m.x;
        int r = (int) m.z;
        vec[0] = new Vector2(q,r);
        
        return vec[0];

    }
        /*
     private Vector2 hexToPix(Vector2 m)
    {
       //System.out.println("Wektor z getAxiala; X: " + m.x + " natomiast Y: " + m.y);
      Vector2[] vec = new Vector2[1];
        // System.out.println("ROZMIAR SIZE W HEXTOPIX(); size: " + size);  
      double x = size * 3/2 * m.x;
      double y = size * sqrt(3) * (m.y + m.x/2);
      m.x = (float) x;
      m.y = (float) y;
      return m;
      
    }
     */    
        
          private Vector2 hexToPix(Vector2 m)
    {
       //System.out.println("Wektor z getAxiala; X: " + m.x + " natomiast Y: " + m.y);
      Vector2[] vec = new Vector2[1];
        // System.out.println("ROZMIAR SIZE W HEXTOPIX(); size: " + size);  
      double x = size * 3/2 * m.x;
      double y = size * sqrt(3) * (m.y + m.x/2);
      m.x =  (float) x;
      m.y = (float) y;
    //  m.x = (float) Math.pow(m.x, -1);
    //  m.y = (float) Math.pow(m.y, -1);
      return m;
        
     
      
    }
          
    private void createHexToPixelArr(Vector3[][] m)
    {
        matrixPoints = new Vector2[m.length][m.length];

         for(int tX=0;tX<m.length;tX++)
        {
            for(int tY=0;tY<m.length;tY++) 
            {
                Vector2 getHTP =  hexToPix(getAxialFromCube(matrixCube[tX][tY]));
                
                System.out.println("CreateHTPA x:" + getHTP.x + " oraz Y: " + getHTP.y);
 matrixPoints[tX][tY] = getHTP;
            }
 
        }
    }
       
    private void createFractionalHexToPixelArr(Vector2[][] m){
        
    }  
    
    private void Vector2ToCube(Vector2[][] m) { // convert odd-q offset to cube
        // String xS ="";
        // String yS="";
       //  String zS="";
        
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
        /*
                Na odd-q
          int x = (int) m[tX][tY].y;
        int z = (int) (m[tX][tY].x - (m[tX][tY].y - ((int)m[tX][tY].y%2))/2);
        int y = -x-z;
                Niżej na even-q
       
        int x = (int) m[tX][tY].y;
        int z = (int) (m[tX][tY].x - (m[tX][tY].y + ((int)m[tX][tY].y%2))/2);
        int y = -x-z;     
                */
        // xS = xS + x;
      //  yS = yS +y;
      //  zS = zS +z;
                
        int x = (int) m[tX][tY].y;
        int z = (int) (m[tX][tY].x - (m[tX][tY].y - ((int)m[tX][tY].y%2))/2);
        int y = -x-z;
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
     // size = tiledLayer.getTileWidth()/2;
      System.out.println("Rozmiar tile'a: " + size);
      
      matrix = new Vector2[tiledLayer.getWidth()][tiledLayer.getHeight()];
        for (int x = 0; x < tiledLayer.getWidth(); x++) {
            for (int y = 0; y < tiledLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
                TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
                //matrix = new Vector2(x,y);
                matrix[x][y] = new Vector2(x,y);
               
            //   actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(), tiledLayer.getTileWidth(),
              //         tiledLayer.getTileHeight());
                //System.out.println("X: " + actor.getX());
                //System.out.println("Y: " + actor.getY());
              //  tileX = tiledLayer.getWidth();
               // System.out.println("Szerokosc : " + tileX);
               //  System.out.println("Wysokosc : " + tileY);
              //  System.out.println(x);
                addActor(actor);
              //  actor.addListener(new InputListener() {
    
  // public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
     //       System.out.println("down");
     //       return true;
   // }
      //          });
              //  EventListener eventListener = new TiledMapClickListener(actor);
              //  actor.addListener(eventListener);
               
            }
        }
     //  revOffsetCoord(matrix);
        showVector2(matrix);
        
        Vector2ToCube(matrix);
        System.out.println("Vector 3: ");
        showVector3(matrixCube);
       createHexToPixelArr(matrixCube);
      showVector2(matrixPoints);
        System.out.println("Myszka X: "+ Gdx.input.getX( )+ " A Y:" + Gdx.input.getY());
      //  Vector2 getHTP =  hexToPix(getAxialFromCube(matrixCube[0][2]));
       //System.out.println("x: " + getHTP.x + " oraz y: "+ getHTP.y);
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