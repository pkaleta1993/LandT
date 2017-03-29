package com.pk.ltgame.scr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pk.ltgame.LandTerrorGame;

/**
 * Klasa ekranu menu.
 * @author pkale
 */
public class MenuScreen extends AbstractScreen{
    private final Texture splashBGImg;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private final TextureAtlas atlas;
    
    /**
     * Kontener dla zasobów gry.
     */
    protected Skin skin;
    private LandTerrorGame game;
   
    /**
     * Konstruktor ekranu menu.
     * @param game Obiekt gry.
     */
    public MenuScreen(LandTerrorGame game)
    {
        super(game);
        this.game = game;
        atlas = new TextureAtlas("menuskin.atlas");
        skin = new Skin(Gdx.files.internal("menuskin.json"), atlas);
        splashBGImg = new Texture("menuBG.png");
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
        TextButton playButton = new TextButton("Nowa gra", skin,"container_green");
        TextButton loadButton = new TextButton("Kontynuacja", skin,"container_green");
        TextButton settingsButton = new TextButton("Ustawienia", skin,"container_green");
        TextButton exitButton = new TextButton("Wyjście", skin,"container_green");
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuSettingsScreen(game));            
            }
        });
        loadButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileHandle save = Gdx.files.internal("data/save");
                if(save.exists()){
                    game.setScreen(new GameScreen(game, save));            
                }
            }
        });
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        playButton.setWidth(200);
        loadButton.setWidth(200);
        settingsButton.setWidth(200);
        exitButton.setWidth(200);
        mainTable.setFillParent(true);;
        mainTable.row();
        mainTable.add(playButton).width(200).padTop(20);
        mainTable.row();
        mainTable.add(loadButton).width(200).padTop(20);
        mainTable.row();
        mainTable.add(settingsButton).width(200).padTop(20);
        mainTable.row();
        mainTable.add(exitButton).width(200).padTop(20);
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();
        spriteBatch.draw(splashBGImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        splashBGImg.dispose();
    }
}