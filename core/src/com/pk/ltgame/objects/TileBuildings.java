/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author pkale
 */
public class TileBuildings {
    SpriteBatch batch;
    Texture img;
    Sprite castle;
    float x;
    public int q,r,s;
    float y;
    String playerColor;
    public TileBuildings(int q, int r, int s, String playerColor){
        this.q = q;
        this.r = r;
        this.s = s;
        this.playerColor = playerColor;
        create();
    };
    
    public void create () {
        batch = new SpriteBatch();
       
        img = new Texture(Gdx.files.internal("cas.png"));
        castle = new Sprite(img);
        
        System.out.println("Stworzono");
    }

    public void render (float x, float y) {
      //  Gdx.gl.glClearColor(1, 0, 0, 1);
    //    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.x = x;
        this.y = y;
        batch.begin();
        //castle.draw(batch);
       batch.draw(img, x, y, Gdx.graphics.getWidth()/6f, Gdx.graphics.getHeight()/6f);
        batch.end();
        
    }
    
    public void dispose() {
    batch.dispose();
    img.dispose();
}
}
