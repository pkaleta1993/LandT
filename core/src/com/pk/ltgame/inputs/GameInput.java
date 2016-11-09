/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.inputs;

/**
 *
 * @author pkale
 */



import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.pk.ltgame.map.TiledMapStage;
import com.pk.ltgame.stream.InputStream;

public class GameInput implements InputProcessor {
   
 public final InputStream postMC = new InputStream();
   

    @Override
 public boolean keyDown (int keycode) {
      return false;
   }

    @Override
   public boolean keyUp (int keycode) {
      return false;
   }

    @Override
   public boolean keyTyped (char character) {
      return false;
   }

    @Override
   public boolean touchDown (int x, int y, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
          // TODO
          
          System.out.println("X: " + x + "oraz Y: " +y );
          postMC.postMouseClick(x, y);
         // pixToHex(x, y);
        
          return true;     
      }

      return false;
   }

    @Override
   public boolean touchUp (int x, int y, int pointer, int button) {
      return false;
   }

    @Override
   public boolean touchDragged (int x, int y, int pointer) {
      return false;
   }

    @Override
   public boolean mouseMoved (int x, int y) {
      return false;
   }

    @Override
   public boolean scrolled (int amount) {
      return false;
   }
}