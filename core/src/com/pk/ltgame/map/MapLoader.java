/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import com.pk.ltgame.LandTerrorGame;
/**
 *
 * @author pkale
 */
public class MapLoader extends Image{
        private TiledMap map;
    	//private TiledMapRenderer renderer;
        private HexagonalTiledMapRenderer renderer;
        private OrthographicCamera camera;
        
        public void createMap(){
           
        map = new TmxMapLoader().load("map8.tmx");
        MapLayers layers = map.getLayers();
        renderer = new HexagonalTiledMapRenderer(map,2);
        //renderer = new OrthogonalTiledMapRenderer(map, 2);
        camera = new OrthographicCamera();
	camera.setToOrtho(false, (1024/768)*2048, 2048);
	camera.update();
        }
        
        public void renderMap(){
         Gdx.gl.glClearColor(1, 1, 1, 1);
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        }

}