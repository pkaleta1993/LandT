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
     * Tworzenie obsługi kliknięcia dla pól.
     * @param actor Aktor dla pola.
     */
    public TiledMapClickListener(TiledMapActor actor) {
        this.actor = actor;
       
    }

    /**
     * Obsługa kliknięcia.
     * @param event Zdarzenie kliknięcia.
     * @param x Parametr x.
     * @param y Parametr y.
     */
    @Override
    public void clicked(InputEvent event, float x, float y) {
        
        if(actor!=null && actor.getCell() != null){
          
        }
       
        
    }
}
