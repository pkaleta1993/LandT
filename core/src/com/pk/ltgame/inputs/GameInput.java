package com.pk.ltgame.inputs;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.pk.ltgame.stream.InputStream;

/**
 * Klasa odpowiadająca za obsługę myszy.
 * @author pkale
 */
public class GameInput implements InputProcessor {
   
    /**
     * Obiekt odpowiadający za kliknięcia lewego przycisku myszy.
     */
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
          postMC.postMouseClick(x, y);
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