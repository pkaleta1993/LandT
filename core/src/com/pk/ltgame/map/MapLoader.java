/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.cell;

import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.hud.GameHUD;
import com.pk.ltgame.objects.TileBuildings;
/**
 *
 * @author pkale
 */
public class MapLoader extends Image{

    /**
     *
     */
    public TiledMap map;
        
    	//private TiledMapRenderer renderer;
       private HexagonalTiledMapRenderer renderer;
        private OrthographicCamera camera;
       // private TileBuildings buildings;
       // private SpriteBatch batch;
        // private Texture splashgamehudimg;

    /**
     *
     */
        public void createMap(){
           
        map = new TmxMapLoader().load("newm4.tmx");
        //splashgamehudimg = new Texture("gamehud.png");
       TiledMapTileLayer layers = (TiledMapTileLayer)map.getLayers().get(0);
       //System.out.printf(layers.get);
        Cell cell = layers.getCell(0,0);
        System.out.println("Ten cell to: "+cell.toString());
       renderer = new HexagonalTiledMapRenderer(map);
       // renderer = new OrthogonalTiledMapRenderer(map);
     
      // buildings = new TileBuildings(1,1,-2);
     /*
      TODO!!
      Muszę przenieść buildings do TiledMapStage(kwestia wygody - brak przenoszenia i tworzenia nowych klas)
      Pozycje w TiledMapStage są w hexPixArr[][] - pozycję są do środka hexa na osi x oraz całkowita wysokość na osi y, czyli x - 99 oraz y - 172
       Muszę czytać te wartości i od x  odejmować 85(?) oraz od y około 120 oraz pamiętać, by dodawać połowę wysokości dla nieparzystych(mapa odbita jest względem horyzontu.
      
       */
  //buildings.create();
   // buildings.render();
        camera = new OrthographicCamera();
        
	camera.setToOrtho(true, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        

	camera.update();
    
        }
        
    /**
     *
     */
    public void renderMap(){
         Gdx.gl.glClearColor(1, 1, 1, 1);
       Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        
        renderer.setView(camera);
        
        renderer.render();
        
    //    buildings.render(15,45);
        
        
        }

}