package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;

public class StarFishGame extends GameBeta {

	private Turtle turtle;
	private boolean win;
	private int collectedSF;

	private List<Starfish> starfishList;
	private List<Rock> rockList;

	@Override
	public void initialize() {

		// background
		BaseActor ocean = new BaseActor(0, 0, mainStage);
		ocean.loadTexture("water.jpg");
		ocean.setSize(800, 600);
		BaseActor.setWorldBounds(ocean);

		// game objects
		starfishList = new ArrayList<>();
		rockList = new ArrayList<>();

		Starfish s1 = new Starfish(400, 400, mainStage);
		Starfish s2 = new Starfish(500, 100, mainStage);
		Starfish s3 = new Starfish(200, 250, mainStage);

		Rock r1 = new Rock(200, 150, mainStage);
		Rock r2 = new Rock(100, 300, mainStage);

		starfishList.add(s1);
		starfishList.add(s2);
		starfishList.add(s3);
		rockList.add(r1);
		rockList.add(r2);

/*
		NOT WORKING? More code below

		new Starfish(400, 400, mainStage);
		new Starfish(500, 100, mainStage);
		new Starfish(200, 250, mainStage);

		new Rock(200, 150, mainStage);
		new Rock(100, 300, mainStage);*/

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
				BaseActor winMessage = new BaseActor(0, 0, mainStage);
				winMessage.loadTexture("you-win.png");
				winMessage.centerAtPosition(400, 300);
				winMessage.setOrigin(0);
				winMessage.addAction(Actions.delay(1));
				winMessage.addAction(Actions.after(Actions.fadeIn(1)));
			}
		}


		/*
		NOT WORKING? Does not recognize class name "Rock"

		for (BaseActor rockActor: BaseActor.getList(mainStage, "Rock"))
			turtle.preventOverlap(rockActor);

		for (BaseActor starfishActor: BaseActor.getList(mainStage, "Starfish")) {

			Starfish starfish = (Starfish) starfishActor;

			if (turtle.overlaps(starfish) && !starfish.isCollected()) {
				starfish.collect();
				starfish.clearActions();
				starfish.addAction(Actions.fadeOut(1));
				starfish.addAction(Actions.after(Actions.removeActor()));
				Whirlpool whirlpool = new Whirlpool(0, 0, mainStage);
				whirlpool.centerAtActor(starfish);
				whirlpool.setOpacity(0.25f);
			}

			if (BaseActor.count(mainStage, "Starfish") == 0 && !win) {
				win = true;
				BaseActor winMessage = new BaseActor(0, 0, mainStage);
				winMessage.loadTexture("you-win.png");
				winMessage.centerAtPosition(400, 300);
				winMessage.setOrigin(0);
				winMessage.addAction(Actions.delay(1));
				winMessage.addAction(Actions.after(Actions.fadeIn(1)));
			}
		}*/
	}


}
