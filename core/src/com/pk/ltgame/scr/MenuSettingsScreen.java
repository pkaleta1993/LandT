package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;

/**
 * Klasa ekranu ustawień rozgrywki.
 * @author pkale
 */
public class MenuSettingsScreen extends AbstractScreen{
    private Texture splashBGImg;
    private SpriteBatch batch;
    private Viewport viewport;
    private TextureAtlas atlas;

    /**
     * Kontener dla zasobów gry.
     */
    protected Skin skin;
    private LandTerrorGame game;
   
    /**
     * Konstruktor dla ekranu ustawień rozpoczęcia gry.
     * @param game Obiekt gry.
     */
    public MenuSettingsScreen(final LandTerrorGame game)
    {
        super(game);
        this.game = game;
        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);
        batch = new SpriteBatch();
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
        TextButton playButton = new TextButton("Zacznij gre z tymi ustawieniami", skin,"default");
        TextButton backButton = new TextButton("Powrót", skin,"default");
        String[] values = new String[]{"Ludzie", "Nieumarli", "Demony"};
        final SelectBox<String> selectBox = new SelectBox<String>(skin);
        selectBox.setItems(values);
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, selectBox.getSelected()));   
            }
        });
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game)); 
            }
        });
        mainTable.add(playButton);
        mainTable.row();
        mainTable.add(selectBox);
        mainTable.row();
        mainTable.add(backButton);
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