package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class BaseScreen implements Screen {

    protected Stage mainStage;
    protected Stage uiStage;

    public BaseScreen() {
        mainStage = new Stage();
        uiStage = new Stage();
        initialize();
    }

    public abstract void initialize();

    public abstract void update(float dt);

    public void render(float dt) {
        uiStage.act(dt);
        mainStage.act(dt);
        update(dt);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

}
