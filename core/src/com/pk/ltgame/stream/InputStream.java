/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.stream;

import com.pk.ltgame.map.TiledMapStage;

/**
 *
 * @author pkale
 */
public class InputStream {
    final TiledMapStage tiledMS = new TiledMapStage();
    public void postMouseClick(double x, double y)
    {
        
        tiledMS.mouseClickToHex(x, y);
    }
}
