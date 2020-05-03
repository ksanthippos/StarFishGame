package com.mygdx.game;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Starfish extends BaseActor {

    private boolean collected;

    public Starfish(float x, float y, Stage s) {
        super(x, y, s);
        collected = false;
        loadTexture("starfish.png");

        // boundaries
        setBoundaryPolygon(8);

        Action spin = Actions.rotateBy(30, 1);
        this.addAction(Actions.forever(spin));
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        clearActions();
        addAction(Actions.fadeOut(1));
        addAction(Actions.after(Actions.removeActor()));
    }

}
