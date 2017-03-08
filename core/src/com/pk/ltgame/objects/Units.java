/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pk.ltgame.LandTerrorGame;

/**
 *
 * @author pkale
 */
public class Units {
    
    private SpriteBatch batch;
    private Texture img;
    private Sprite unit;
   

    /**
     *
     */
    public float move;

    /**
     *
     */
    public int q,

    /**
     *
     */
    r,

    /**
     *
     */
    s;
    

    /**
     *
     */
    public String playerColor;
    private Label unitsCount;
    public int id;
    /**
     *
     */
    protected Stage stage;

    /**
     *
     */
    public int meleeUnits,

    /**
     *
     */
    rangeUnits,

    /**
     *
     */
    specialUnits;

    /**
     *
     * @param id
     * @param q
     * @param r
     * @param s
     * @param meleeUnits
     * @param rangeUnits
     * @param specialUnits
     * @param move
     * @param playerColor
     */
    public Units(int id, int q, int r, int s, int meleeUnits, int rangeUnits, int specialUnits, float move, String playerColor){
        this.id = id;
        this.q = q;
        this.r = r;
        this.s = s;
        this.meleeUnits = meleeUnits;
        this.rangeUnits = rangeUnits;
        this.specialUnits = specialUnits;
        this.move = move;
        this.playerColor = playerColor;
        
        create();
    };
    
    /**
     *
     */
    public void create () {
        
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.img = new Texture(Gdx.files.internal("knight"+playerColor+".png"));
        this.unit = new Sprite(img);
        this.unitsCount =new Label(String.format("%2d/%2d/%2d", meleeUnits, rangeUnits, specialUnits), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Pixmap labelColor = new Pixmap(50, 20, Pixmap.Format.RGB888);
        labelColor.setColor(Color.BLACK);
        labelColor.fill();
        unitsCount.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        stage.addActor(unitsCount);
        System.out.println("Stworzono");
    }

    /**
     *
     * @param x
     * @param y
     */
    public void render (float x, float y) {
      //  Gdx.gl.glClearColor(1, 0, 0, 1);
    //    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
      
       batch.draw(img, x, y, Gdx.graphics.getWidth()/12f, Gdx.graphics.getHeight()/12f);
        batch.end();
        unitsCount.setX(x);
        unitsCount.setY(y);
        stage.act();
        stage.draw();
        
    }

    /**
     *
     */
    public void updateArmy(){
        unitsCount.setText(String.format("%2d/%2d/%2d", meleeUnits, rangeUnits, specialUnits));
    }

    /**
     *
     */
    public void dispose() {
    batch.dispose();
    img.dispose();
}
}
