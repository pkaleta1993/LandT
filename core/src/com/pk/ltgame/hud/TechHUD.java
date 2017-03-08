/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.players.HumanPlayer;
import java.util.ArrayList;

/**
 *
 * @author pkale
 */

   
public class TechHUD {
    public ArrayList<Boolean> technology;
    private LandTerrorGame game;
    protected Skin skin;
    private TextureAtlas atlas;
    public  ImageButton buttonDouble, buttonBuildings, buttonUnits, buttonSave;
    public Stage stage;
    private Viewport viewport;
    public float techPoints;
    public Table table;
    public TechHUD(ArrayList<Boolean> tech, float techP){
    this.techPoints = techP;
    this.technology = tech;
     game = new LandTerrorGame();
    viewport = new FitViewport(game.WIDTH*2, game.HEIGHT*2, new OrthographicCamera());
    stage = new Stage(viewport);
    atlas = new TextureAtlas("tech.atlas");
    skin = new Skin(Gdx.files.internal("tech.json"), atlas);
    buttonDouble = new ImageButton(skin,"double");
    buttonBuildings = new ImageButton(skin, "buildings");
    buttonUnits = new ImageButton(skin, "units");
    buttonDouble.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
    buttonBuildings.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
    buttonUnits.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
    buttonSave = new ImageButton(skin, "save");
    buttonSave.setSize(Gdx.graphics.getWidth()/30f, Gdx.graphics.getWidth()/30f);
    /*
     buttonBuildings.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(techPoints >=2) payByTechPoints(2);
            }
           
        });
     buttonUnits.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(techPoints >=2) payByTechPoints(2);
            }
           
        });

*/
    table = new Table();
    table.top();
    table.setFillParent(true);
    table.add(buttonDouble);
    table.row();
    table.add(buttonBuildings);
    table.row();
    table.add(buttonUnits);
    table.row();
    table.row().expand(0,10);
    
    table.add(buttonSave);
   // table.setSize(50,150);
    table.setWidth(50);
    table.setHeight(150);
    table.setX(Gdx.graphics.getWidth()-table.getWidth()-10);
    table.setY(-100);
    stage.addActor(table);
    checkOnLoad();
    
    }
    public float getTech(){
    return this.techPoints;
    }
    
    public void checkOnLoad(){
        if(this.technology.get(0) == true) deleteTechnology(this.buttonDouble);
        if(this.technology.get(1) == true) deleteTechnology(this.buttonBuildings);
        if(this.technology.get(2) == true) deleteTechnology(this.buttonUnits);
    }
    public void updateTech(float techPoints){
        this.techPoints += techPoints;
    }
    
    public void payByTechPoints(float techpoints){
        this.techPoints -= techpoints;
    }
    public void setTrue(int index){
        technology.set(index, Boolean.TRUE);
    }
    public void deleteTechnology(ImageButton button){
        table.removeActor(table.getChildren().get(table.getChildren().indexOf(button, true)));
    }
    
}
