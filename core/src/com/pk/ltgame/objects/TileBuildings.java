package com.pk.ltgame.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
 * Klasa odpowiadająca za budynki.
 * @author pkale
 */
public final class TileBuildings {
    SpriteBatch batch;

    /**
     * Tekstura dla budynku.
     */
    public Texture img;
    Sprite tileBuilding;

    /**
     * Scena dla budynków.
     */
    private Stage stage;
    float x;

    /**
     * Koordynat q.
     */
    public int q,

    /**
     * Koordynat r.
     */
    r,

    /**
     * Koordynat s.
     */
    s;

    /**
     * Ilość aktualnych punktów życia budynku.
     */
    public float HP;

    /**
     * Ilość maksymalnych punktów życia budynku.
     */
    public int maxHP;

    /**
     * Ilość dostarczanego złota na turę.
     */
    public int dayGold,

    /**
     * Ilość dostarcznego jedzenia na turę.
     */
    dayFood;

    /**
     * Rasa do której należy budynek.
     */
    public  String race;

    /**
     * Ilość dostarczanych punktów nauki na turę.
     */
    public float dayTechPoints;
    private ProgressBar bar;
    private ProgressBarStyle barStyle;
    private TextureRegionDrawable barTexture;
    private TextureAtlas atlas;

    /**
     * Identyfikator budynku.
     */
    public int id;

    /**
     * Kontener dla tekstur.
     */
    protected Skin skin;
    float y;

    /**
     * Kolor gracza do którego należy budynek.
     */
    public String playerColor;

    /**
     * Nazwa tekstury.
     */
    public String textureName;
    
    /**
     * Tworzenie obiektu dla budynków.
     * @param id Identyfikator obiektu.
     * @param q Koordynat q.
     * @param r Koordynat r.
     * @param s Koorsynat s.
     * @param maxHP Maksymalne punkty życia.
     * @param hp Aktualne punkty życia.
     * @param dayGold Dzienne dostarczane złoto.
     * @param dayFood Dziennie dostarczane jedzenie.
     * @param dayTechPoints Dziennie dostarczane punkty nauki.
     * @param playerColor Kolor gracza.
     * @param textureName Nazwa tekstury.
     * @param race Rasa.
     */
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
        create(textureName);
    };
   

    /**
     *  Tworzenie obrazu dla budynku, punktów życia i dodanie tych obiektów na scene.
     * @param textureName Nazwa tekstury.
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
    }

    /**
     * Aktualizacja tekstury obiektu.
     */
    public void updateTexture(){
        this.img = new Texture(Gdx.files.internal(this.textureName+playerColor+race+".png"));
        this.tileBuilding.setRegion(img);
        
    }
    
    /**
     * Aktualizacja wyświetlanych na pasku punktów życia.
     * @param progress Wartość dla punktów życia.
     */
    public void updateFillBar(float progress){
        this.bar.setValue(progress);
    }

    /**
     * Renderowanie obiektu na określonych współrzędnych.
     * @param x Miejsce na osi x.
     * @param y Miejsce na osi y.
     */
    public void render (float x, float y) {
        this.x = x;
        this.y = y;
        batch.begin();
        batch.draw(img, x, y, 198, 172);
        bar.setX(x+108/2);
        bar.setY(y);
        batch.end();
        stage.act();
        stage.draw();
     
    }
    
    /**
     * Zniszczenie obrazów.
     */
    public void dispose() {
    batch.dispose();
    img.dispose();
}
}
