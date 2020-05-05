package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen {

    private Turtle turtle;

    private boolean win;
    private boolean lose;
    private int collectedSF;

    private List<Starfish> starfishList;
    private List<Rock> rockList;
    private List<Shark> sharkList;

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
        sharkList = new ArrayList<>();

        // rocks & starfishes placed in fixed locations
        Rock r1 = new Rock(300, 200, mainStage);
        Rock r2 = new Rock(100, 300, mainStage);
        Rock r3 = new Rock(400, 500, mainStage);
        Rock r4 = new Rock(600, 700, mainStage);

        rockList.add(r1);
        rockList.add(r2);
        rockList.add(r3);
        rockList.add(r4);

        Starfish s1 = new Starfish(200, 300, mainStage);
        Starfish s2 = new Starfish(500, 800, mainStage);
        Starfish s3 = new Starfish(900, 600, mainStage);
        Starfish s4 = new Starfish(700, 100, mainStage);
        Starfish s5 = new Starfish(300, 400, mainStage);

        starfishList.add(s1);
        starfishList.add(s2);
        starfishList.add(s3);
        starfishList.add(s4);
        starfishList.add(s5);

        // sharks placed randomly
        for (int i = 0; i < 5; i++) {
            int x = MathUtils.random(2, 10)*100;
            int y = MathUtils.random(2, 10)*100;
            Shark shark = new Shark(x, y, mainStage);
            shark.setMaxSpeed(50);
            sharkList.add(shark);
        }

        turtle = new Turtle(20, 20, mainStage);
        collectedSF = 0;
        win = false;
        lose = false;
    }

    @Override
    public void update(float dt) {

        for (Shark shark: sharkList) {
            if (turtle.overlaps(shark) && !lose && !win) {
                lose = true;
                turtle.remove();
                BaseActor loseMessage = new BaseActor(0, 0, uiStage);
                loseMessage.loadTexture("game-over.png");
                loseMessage.centerAtPosition(400, 300);
                loseMessage.setOrigin(0);
                loseMessage.addAction(Actions.delay(1));
                loseMessage.addAction(Actions.after(Actions.fadeIn(1)));
            }
        }

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

    @Override
    public void dispose() {
        super.dispose();
    }
}
