/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 * @author pkale
 */
public class TiledMapActor extends Actor {

    private TiledMap tiledMap;

    private TiledMapTileLayer tiledLayer;

    /**
     *
     */
    public TiledMapTileLayer.Cell cell;

    /**
     *
     * @param tiledMap
     * @param tiledLayer
     * @param cell
     */
    public TiledMapActor(TiledMap tiledMap, TiledMapTileLayer tiledLayer, TiledMapTileLayer.Cell cell) {
        //this.tiledMap = tiledMap;
        //this.tiledLayer = tiledLayer;
        this.cell = cell;
    }

    /**
     *
     * @return
     */
    public TiledMapTileLayer.Cell getCell() {
            return cell;
        }
}