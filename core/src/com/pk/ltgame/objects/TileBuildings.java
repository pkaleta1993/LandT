/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 * @author pkale
 */
public final class TileBuildings {
    SpriteBatch batch;
    public Texture img;
    Sprite tileBuilding;
    protected Stage stage;
    float x;
    public int q,r,s;
    public float HP;
    public int maxHP;
    public int dayGold, dayFood;
    public  String race;
    public float dayTechPoints;
    private ProgressBar bar;
    private ProgressBarStyle barStyle;
    private TextureRegionDrawable barTexture;
    private TextureAtlas atlas;
    public int id;
    protected Skin skin;
    float y;
    public String playerColor;
    public String textureName;
    
    
    
    public TileBuildings(int id, int q, int r, int s, int maxHP, float hp, int dayGold, int dayFood, float dayTechPoints, String playerColor, String textureName, String race){
        this.id = id;
        this.q = q;
        this.r = r;
        this.s = s;
        this.HP = hp;
        this.maxHP = maxHP;
        this.playerColor = playerColor;
        this.textureName = textureName;
        this.dayFood = dayFood;
        this.dayGold = dayGold;
        this.dayTechPoints = dayTechPoints;
        this.race = race;
         System.out.println("///////////Rasa: "+race);
        System.out.println("///////////Rasa: "+this.race);
        
        create(textureName);
    };
    /*
    *Creates a new SpriteBatch() and update sprite with the given textureName
    */
    public void create (String textureName) {
        batch = new SpriteBatch();
        stage = new Stage();
        
        img = new Texture(Gdx.files.internal(textureName+playerColor+race+".png"));
        tileBuilding = new Sprite(img);
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        barTexture = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("progress.png"))));
        
        barStyle =  new ProgressBarStyle(skin.newDrawable("white", Color.DARK_GRAY), barTexture);
        barStyle.knobBefore = barStyle.knob;
        bar = new ProgressBar(0, 1, 0.1f, false, barStyle);
        bar.setWidth(80);
        
        bar.setHeight(5);
        bar.setValue(1f);
        stage.addActor(bar);
        System.out.println("Stworzono");
    }
    public void updateTexture(){
        this.img = new Texture(Gdx.files.internal(this.textureName+playerColor+race+".png"));
        this.tileBuilding.setRegion(img);
        
    }
    
    public void updateFillBar(float progress){
        this.bar.setValue(progress);
    }
    public void render (float x, float y) {
      //  Gdx.gl.glClearColor(1, 0, 0, 1);
    //    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.x = x;
        this.y = y;
        batch.begin();
        //castle.draw(batch);
       batch.draw(img, x, y, 198, 172);
       bar.setX(x+108/2);
       bar.setY(y);
        batch.end();
        stage.act();
        stage.draw();
        
        
    }
    
    
    public void dispose() {
    batch.dispose();
    img.dispose();
}
}
