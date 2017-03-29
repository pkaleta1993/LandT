package com.pk.ltgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;

/**
 * Klasa odpowiadająca za HUD ze statystykami gracza.
 * @author pkale
 */

public class GameHUD{

    /**
     * Scena dla obiektu
     */
public Stage stage;
 /**
     * Widok
     */
private final Viewport viewport;

 /**
     * Czas rozgrywki
     */
private int worldTimer;
 /**
     * Zliczanie czasu na podstawie delty
     */
private float timeCount;


    /**
     * Złoto gracza
     */
    public int gold;

    /**
     * Jedzenie gracza
     */
    public int food;

    /**
     * Punkty nauki gracza
     */
    public float techPoints;

    /**
     * Tura gracza
     */
    public  int turn;

    /**
     * Nazwa gracza
     */
    public String player;

private LandTerrorGame game;

private final Table table;
    /**
     * Przycisk do przejścia do następnej tury
     */
public  TextButton turnButton;
private final TextureAtlas atlas;
private final Skin skin;
 /**
     * Etykiety
     */
private final Label timeLabelV, playerLabelV, turnLabelV, timeLabel, goldLabel, playerLabel, foodLabel, techPointsLabel, turnLabel;
 /**
     * Etykiety ze zmianami
     */
private static Label goldLabelV, foodLabelV, techPointsLabelV;

    /**
     * Tworzenie obiektu GameHUD.
     * @param playerColor identyfikator gracza.
     * @param gold posiadane złoto.
     * @param food posiadane jedzenie.
     * @param techPoints posiadane punkty nauki.
     * @param turn aktualna tura.
     */
    public GameHUD (String playerColor, int gold, int food, float techPoints, int turn){
        worldTimer = 0;
        timeCount = 0;
        this.player = playerColor;
        this.gold = gold;
        this.food = food;
        this.techPoints = techPoints;
        this.turn = turn;
        game = new LandTerrorGame();
        viewport = new FitViewport(game.WIDTH, game.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        timeLabelV = new Label(String.format("%5s", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        goldLabelV =new Label(String.format("%1d", gold), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabelV =new Label(String.format("%1s", player), new Label.LabelStyle(new BitmapFont(),Color.valueOf("Ff0000")));
        foodLabelV =new Label(String.format("%1d", food), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        techPointsLabelV =new Label(String.format("%1$.2f", techPoints), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        turnLabelV =new Label(String.format("%1d", turn), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("Czas", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        goldLabel = new Label("Zloto", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label("Gracz", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        foodLabel = new Label("Jedzienie", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        techPointsLabel = new Label("Punkty nauki", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        turnLabel = new Label("Tura", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        turnButton = new TextButton("Nastepna tura", skin,"default");
        table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(playerLabel).expandX();
        table.add(goldLabel).expandX();
        table.add(foodLabel).expandX();
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
        stage.addActor(table);
    }

    /**
     *
     * @param dt pojedynczy czas renderingu
     */
    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            worldTimer++;
            timeLabelV.setText(countTime(worldTimer));
            timeCount = 0;
        }
}
/**
     *
     * @param timer Aktualny czas.
     */
     private String countTime(int timer) {
        return String.format("%02d:%02d", timer / 60, timer % 60);    
    }

    /**
     * Dodaj turę.
     */
    public void addTurn(){
        turn++;
        turnLabelV.setText(String.format("%1d", turn));
    }

    /**
     * Dodaj x złota.
     * @param value Ilość złota.
     */
    public void addGold(int value){
        gold += value;
        goldLabelV.setText(String.format("%1d", gold));
    }

    /**
     * Odejmij x punktów nauki.
     * @param value Ilość punktów nauki.
     */
    public void payTech(float value){
        techPoints -= value;
        techPointsLabelV.setText(String.format("%1$.2f", techPoints));        
    }
    /**
     * Dodaj x jedzenia.
     * @param value Ilość jedzenia.
     */
    public void addFood(int value){
        food += value;
        foodLabelV.setText(String.format("%1d", food));
    }

    /**
     *Dodaj x punktów nauki.
     * @param value Ilość punktów nauki.
     */
    public void addtechPoints(float value){
        techPoints += value;
        techPointsLabelV.setText(String.format("%1$.2f", techPoints));
    }

    /**
     * Zniszczenie sceny.
     */
    public void dispose() { stage.dispose(); }

    /**
     * 
     * @return stan złota.
     */
    public static Label getGoldLabel() {
        return goldLabelV; 
    }

}