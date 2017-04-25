package com.pk.ltgame.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Klasa odpowiadająca za ładowanie mapy.
 * @author pkale
 */
public class MapLoader extends Image{

    /**
     * Obiekt mapy.
     */
    public TiledMap map;
    private HexagonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    
    /**
     * Tworzenie mapy.
     */
    public void createMap(){   
        map = new TmxMapLoader().load("Worldmap.tmx");
        TiledMapTileLayer layers = (TiledMapTileLayer)map.getLayers().get(0);
        Cell cell = layers.getCell(0,0);
        renderer = new HexagonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
        camera.setToOrtho(true, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.update();
        
    }
        
    /**
     * Renderowanie mapy.
     */
    public void renderMap(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        
        }

}