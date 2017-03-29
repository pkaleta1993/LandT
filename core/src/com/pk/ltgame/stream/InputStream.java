package com.pk.ltgame.stream;

import com.pk.ltgame.map.TiledMapStage;

/**
 * Klasa do przekazywania współrzędnych.
 * @author pkale
 */
public class InputStream {
    
    final TiledMapStage tiledMS = new TiledMapStage();

    /**
     * Wyślij współrzędne.
     * @param x Współrzędna x.
     * @param y Współrzędna y.
     */
    public void postMouseClick(double x, double y)
    {
        tiledMS.mouseClickToHex(x, y);
    }
}
