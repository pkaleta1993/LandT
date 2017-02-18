/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;
import com.pk.ltgame.scr.GameScreen;

/**
 *
 * @author pkale
 */

public class GameHUD{

public Stage stage;
private Viewport viewport;

//score && time tracking variables
private int worldTimer;
private float timeCount;
private static int score;
private static int gold;
private int food;
private int techPoints;
private static int turn;
private String player;
private boolean timeUp;
private LandTerrorGame game;
private TextButton turnButton;
private TextureAtlas atlas;
private Skin skin;
//private Color pColor;
//Scene2D Widgets
private Label timeLabelV, playerLabelV, foodLabelV, techPointsLabelV, turnLabelV, timeLabel, goldLabel, playerLabel, foodLabel, techPointsLabel, turnLabel;
private static Label goldLabelV;

public GameHUD (String playerColor, int gold, int food, int techPoints, int turn){
    //define tracking variables
    worldTimer = 0;
    timeCount = 0;
   
    this.player = playerColor;
   this.gold = gold;
   this.food = food;
   this.techPoints = techPoints;
   this.turn = turn;
   
    //Color pColor   = (Color) Color.class.getField("red").get(null);
    //Color pColor = Color..web(player);
    game = new LandTerrorGame();
    //setup the HUD viewport using a new camera seperate from gamecam
    //define stage using that viewport and games spritebatch
    viewport = new FitViewport(game.WIDTH, game.HEIGHT, new OrthographicCamera());
    stage = new Stage(viewport);
    
    //define labels using the String, and a Label style consisting of a font and color
    timeLabelV = new Label(String.format("%5s", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    goldLabelV =new Label(String.format("%1d", gold), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    playerLabelV =new Label(String.format("%1s", player), new Label.LabelStyle(new BitmapFont(),Color.valueOf("Ff0000")));
    foodLabelV =new Label(String.format("%1d", food), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    techPointsLabelV =new Label(String.format("%1d", techPoints), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    turnLabelV =new Label(String.format("%1d", turn), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    timeLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    goldLabel = new Label("Gold", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    playerLabel = new Label("Player", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    foodLabel = new Label("Food", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    techPointsLabel = new Label("Tech points", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    turnLabel = new Label("Turn", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
  atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

    turnButton = new TextButton("Next turn", skin,"default");
    
    //define a table used to organize hud's labels
    turnButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
           GameHUD.turn++;
           turnLabelV.setText(String.format("%1d", GameHUD.turn));
            }
        });
    Table table = new Table();
    table.top();
    table.setFillParent(true);

    //add labels to table, padding the top, and giving them all equal width with expandX
  //  table.add(goldLabel).expandX().padTop(10);
    table.add(playerLabel).expandX();
    
    table.add(goldLabel).expandX();
      table.add(foodLabel).expandX();
    //table.add(timeLabel).expandX().padTop(10);
      table.add(techPointsLabel).expandX();
        table.add(turnLabel).expandX();
    table.add(timeLabel).width(50).expandX();
      table.add(turnButton);
     
    table.row();
    
      table.add(playerLabelV).expandX();
      
    table.add(goldLabelV).expandX();
    table.add(foodLabelV).expandX();
    table.add(techPointsLabelV).expandX();
    table.add(turnLabelV).expandX();
    table.add(timeLabelV).width(50).expandX();
  

    //add table to the stage
    stage.addActor(table);
   
}

public void update(float dt){
    timeCount += dt;
    if(timeCount >= 1){
        if (timeUp == false) {
            worldTimer++;
        } 
      // timeLabelV.setText(String.format("%05s", countTime(worldTimer)));
      timeLabelV.setText(countTime(worldTimer));
      // countTime(worldTimer);
        timeCount = 0;
    }
}

private String countTime(int timer) {
    //timeLabelV.setText(countTime(worldTimer));
     return String.format("%02d:%02d", timer / 60, timer % 60);    
}

public static void addGold(int value){
    gold += value;
    goldLabelV.setText(String.format("%06d", gold));
}

public void dispose() { stage.dispose(); }

public boolean isTimeUp() { return timeUp; }


public static Label getScoreLabel() {
    return goldLabelV;
}

public static Integer getScore() {
    return score;
}
}