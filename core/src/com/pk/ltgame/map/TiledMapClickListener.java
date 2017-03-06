/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 *
 * @author pkale
 */
public class TiledMapClickListener  extends ClickListener {

    private TiledMapActor actor;

    /**
     *
     * @param actor
     */
    public TiledMapClickListener(TiledMapActor actor) {
        this.actor = actor;
       
    }

    /**
     *
     * @param event
     * @param x
     * @param y
     */
    @Override
    public void clicked(InputEvent event, float x, float y) {
        
        if(actor!=null && actor.getCell() != null){
          
                 //   System.out.println(this.actor.getWidth());
                 //   System.out.println(this.actor.getX()+ " is X.");
                  //  System.out.println(actor.getY() + " is Y.");
                  //         System.out.println(actor.cell + " has been clicked.");

                  //  System.out.println(actor.getZIndex()+ " is ZIndex.");
                  //  System.out.println(actor.cell.getTile().getOffsetY() + " has been clicked.");
                  //  System.out.println(actor.cell.getTile().getOffsetX() + " has been clicked.");
       
        }
       
        
    }
}
