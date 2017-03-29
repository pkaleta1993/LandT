package com.pk.ltgame.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;
import java.util.ArrayList;

/**
 * Klasa odpowiadająca za HUD dotyczący technologii.
 * @author pkale
 */   
public class TechHUD {

    /**
     * Lista technologi niewykupionych oraz wykupionych
     */
    public ArrayList<Boolean> technology;
    private final LandTerrorGame game;

    /**
     * Kontener dla zasobów gry.
     */
    protected Skin skin;
    private final TextureAtlas atlas;

    /**
     * Obiekty ImageButton.
     */
    public  ImageButton buttonDouble, buttonBuildings, buttonUnits, buttonSave;

    /**
     * Scena dla obiektu.
     */
    public Stage stage;
    private final Viewport viewport;

    /**
     * Punkty nauki.
     */
    public float techPoints;

    /**
     * Tabela dla obiektów ImageButton.
     */
    public Table table;

    /**
     * Tworzenie obiektu TechHUD.
     * @param tech Lista technologii.
     * @param techP Liczba posiadanych punktów nauki.
     */
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
        table.setWidth(50);
        table.setHeight(150);
        table.setX(Gdx.graphics.getWidth()-table.getWidth()-10);
        table.setY(-100);
        stage.addActor(table);
        checkOnLoad();
    }

    /**
     *
     * @return Aktualna liczba punktów nauki.
     */
    public float getTech(){
        return this.techPoints;
    }
    
    /**
     * Usuwanie możliwości odblokowywania technologii już wykupionych.
     */
    public void checkOnLoad(){
        if(this.technology.get(0) == true) deleteTechnology(this.buttonDouble);
        if(this.technology.get(1) == true) deleteTechnology(this.buttonBuildings);
        if(this.technology.get(2) == true) deleteTechnology(this.buttonUnits);
    }

    /**
     *Dodaj x punktów nauki.
     * @param techPoints Ilość punktów nauki.
     */
    public void updateTech(float techPoints){
        this.techPoints += techPoints;
    }
    
    /**
     * Odejmij x punktów nauki.
     * @param techpoints Ilość punktów nauki.
     */
    public void payByTechPoints(float techpoints){
        this.techPoints -= techpoints;
    }

    /**
     * Ustaw technologię x jako wykupioną.
     * @param index Indeks technologii.
     */
    public void setTrue(int index){
        technology.set(index, Boolean.TRUE);
    }

    /**
     * Usuń przycisk do wykupienia danej technologii.
     * @param button Nazwa przycisku.
     */
    public void deleteTechnology(ImageButton button){
        table.removeActor(table.getChildren().get(table.getChildren().indexOf(button, true)));
    }
    
}
