package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen {

    private Turtle turtle;
    private boolean win;
    private int collectedSF;
    private List<Starfish> starfishList;
    private List<Rock> rockList;

    @Override
    public void initialize() {
        // background
        BaseActor ocean = new BaseActor(0, 0, mainStage);
        ocean.loadTexture("water-border.jpg");
        ocean.setSize(1200, 900);
        BaseActor.setWorldBounds(ocean);

        // game objects
        starfishList = new ArrayList<>();
        rockList = new ArrayList<>();

        Starfish s1 = new Starfish(400, 400, mainStage);
        Starfish s2 = new Starfish(500, 100, mainStage);
        Starfish s3 = new Starfish(200, 250, mainStage);
        Starfish s4 = new Starfish(600, 570, mainStage);
        Starfish s5 = new Starfish(800, 440, mainStage);
        Starfish s6 = new Starfish(1000, 800, mainStage);

        Rock r1 = new Rock(200, 150, mainStage);
        Rock r2 = new Rock(100, 300, mainStage);
        Rock r3 = new Rock(300, 300, mainStage);
        Rock r4 = new Rock(400, 600, mainStage);

        starfishList.add(s1);
        starfishList.add(s2);
        starfishList.add(s3);
        starfishList.add(s4);
        starfishList.add(s5);
        starfishList.add(s6);
        rockList.add(r1);
        rockList.add(r2);
        rockList.add(r3);
        rockList.add(r4);

        turtle = new Turtle(20, 20, mainStage);
        collectedSF = 0;
        win = false;

    }

    @Override
    public void update(float dt) {
        for (Rock rock: rockList)
            turtle.preventOverlap(rock);

        for (Starfish starfish: starfishList) {

            if (turtle.overlaps(starfish) && !starfish.isCollected()) {
                starfish.collect();
                collectedSF++;
                starfish.clearActions();
                starfish.addAction(Actions.fadeOut(1));
                starfish.addAction(Actions.after(Actions.removeActor()));
                Whirlpool whirlpool = new Whirlpool(0, 0, mainStage);
                whirlpool.centerAtActor(starfish);
                whirlpool.setOpacity(0.25f);
            }

            if (collectedSF == starfishList.size() && !win) {
                win = true;
                BaseActor winMessage = new BaseActor(0, 0, uiStage);
                winMessage.loadTexture("you-win.png");
                winMessage.centerAtPosition(400, 300);
                winMessage.setOrigin(0);
                winMessage.addAction(Actions.delay(1));
                winMessage.addAction(Actions.after(Actions.fadeIn(1)));
            }
        }
    }
}
