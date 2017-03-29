package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;

/**
 * Klasa ekranu ustawień.
 * @author pkale
 */
public class SettingsScreen extends AbstractScreen{
    private Texture splashBGImg;
    private SpriteBatch batch;
    private final Viewport viewport;
    private final TextureAtlas atlas;
    private Label labelMusic, labelMusicSwitch;
    private Slider musicVolume;
    private CheckBox musicSwitch;
    private TextButton save,back;
    private Preferences p;
    
    /**
     * Kontener dla zasobów gry.
     */
    protected Skin skin;
    private final LandTerrorGame game;
   
    /**
     * Konstruktor ekranu ustawień.
     * @param game Obiekt gry.
     */
    public SettingsScreen(final LandTerrorGame game)
    {
        super(game);
        this.game = game;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void show() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.top();
        p = Gdx.app.getPreferences("settings");
        float volume =  p.getFloat("volume");
        boolean switchOff = p.getBoolean("switch");
        labelMusic = new Label(String.format("%5s", "Music Volume: "), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        labelMusicSwitch = new Label(String.format("%5s", "Music Off: "), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        musicVolume = new Slider(0.1f, 1f, 0.1f, false, skin);
        musicSwitch = new CheckBox("", skin);
        back = new TextButton("back", skin, "default");
        save = new TextButton("save", skin, "default");
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game)); 
            }
        }); 
        save.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                p.putFloat("volume", musicVolume.getValue());
                p.putBoolean("switch", musicSwitch.isChecked());
                p.flush();
                game.setVolume(musicVolume.getValue());
                game.turnOffMusic(musicSwitch.isChecked());
            }
        });
        mainTable.setY(mainTable.getY()-50);
        mainTable.add(labelMusic);
        mainTable.add(musicVolume);
        mainTable.row();
        mainTable.add(labelMusicSwitch);
        mainTable.add(musicSwitch);
        mainTable.row();
        mainTable.add(back);
        mainTable.add(save);
        if(volume > 0) musicVolume.setValue(volume); else musicVolume.setValue(1f);
        if(switchOff == true) musicSwitch.toggle();
        stage.addActor(mainTable);
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();
        spriteBatch.end();
        stage.act();
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }
}