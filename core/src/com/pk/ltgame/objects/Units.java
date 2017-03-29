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

/**
 * Klasa odpowiedzialna za tworzenie jednostek.
 * @author pkale
 */
public class Units {
    
    private SpriteBatch batch;
    private Texture img;
    private Sprite unit;
   

    /**
     * Ilość punktów ruchu.
     */
    public float move;

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
     * Kolor gracza.
     */
    public String playerColor;
    private Label unitsCount;

    /**
     * Identyfikator obiektu.
     */
    public int id;
    /**
     * Scena dla jednostki.
     */
    private Stage stage;

    /**
     * Ilość jednostek bliskiego zasięgu.
     */
    public int meleeUnits,

    /**
     * Ilość jednostek dalekiego zasięgu.
     */
    rangeUnits,

    /**
     * Ilość jednostek specjalnych.
     */
    specialUnits;

    /**
     * Rasa jednostki.
     */
    public String race;
    
    /**
     * Tworzenie jednostek.
     * @param id Identyfikator obiektów.
     * @param q Koordynat q.
     * @param r Koordynat r.
     * @param s Koordynat s.
     * @param meleeUnits Ilość jednostek bliskiego zasięgu.
     * @param rangeUnits Ilość jednostek dalekiego zasięgu.
     * @param specialUnits Ilość jednostek specjalnych.
     * @param move Ilość punktów ruchu.
     * @param playerColor Kolor gracza.
     * @param race Rasa gracza do którego jednostka należy. 
     */
    public Units(int id, int q, int r, int s, int meleeUnits, int rangeUnits, int specialUnits, float move, String playerColor, String race){
        this.id = id;
        this.q = q;
        this.r = r;
        this.s = s;
        this.meleeUnits = meleeUnits;
        this.rangeUnits = rangeUnits;
        this.specialUnits = specialUnits;
        this.move = move;
        this.playerColor = playerColor;
        this.race = race;
        create();
    };
    
    /**
     * Tworzenie jednostki i etykiety dla ilość jednostek.
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
    }

    /**
     * Renderowanie obiektu na określonych współrzędnych.
     * @param x Miejsce na osi x.
     * @param y Miejsce na osi y.
     */
    public void render (float x, float y) {
        batch.begin();
        batch.draw(img, x, y, Gdx.graphics.getWidth()/12f, Gdx.graphics.getHeight()/12f);
        batch.end();
        unitsCount.setX(x);
        unitsCount.setY(y);
        stage.act();
        stage.draw();   
    }

    /**
     * Aktualizacja ilości jednostek.
     */
    public void updateArmy(){
        unitsCount.setText(String.format("%2d/%2d/%2d", meleeUnits, rangeUnits, specialUnits));
    }

    /**
     * Zniszczenie obrazów.
     */
    public void dispose() {
    batch.dispose();
    img.dispose();
}
}
