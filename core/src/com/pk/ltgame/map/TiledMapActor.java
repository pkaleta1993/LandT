package com.pk.ltgame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Klasa odpowiadająca za aktorów mapy.
 * @author pkale
 */
public class TiledMapActor extends Actor {

    private TiledMap tiledMap;

    private TiledMapTileLayer tiledLayer;

    /**
     * Pole mapy.
     */
    public TiledMapTileLayer.Cell cell;

    /**
     * Tworzenie aktora dla pola mapy.
     * @param cell Pole.
     */
    public TiledMapActor(TiledMapTileLayer.Cell cell) {
        this.cell = cell;
    }

    /**
     * Pobiera to pole.
     * @return Pole.
     */
    public TiledMapTileLayer.Cell getCell() {
            return cell;
        }
}