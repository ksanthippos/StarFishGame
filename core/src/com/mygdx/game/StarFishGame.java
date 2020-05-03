package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class StarFishGame extends GameBeta {

	private Turtle turtle;
	private Starfish starfish;
	private BaseActor ocean;

	@Override
	public void initialize() {

		ocean = new BaseActor(0, 0, mainStage);
		ocean.loadTexture("water.jpg");
		ocean.setSize(800, 600);

		starfish = new Starfish(380, 380, mainStage);
		turtle = new Turtle(20, 20, mainStage);
	}

	@Override
	public void update(float dt) {
		if (turtle.overlaps(starfish) && !starfish.isCollected()) {
			starfish.collect();

			Whirlpool whirlpool = new Whirlpool(0, 0, mainStage);
			whirlpool.centerAtActor(starfish);
			whirlpool.setOpacity(0.25f);

			BaseActor winMessage = new BaseActor(0, 0, mainStage);
			winMessage.loadTexture("you-win.png");
			winMessage.centerAtPosition(400, 300);
			winMessage.setOrigin(0);
			winMessage.addAction(Actions.delay(1));
			winMessage.addAction(Actions.after(Actions.fadeIn(1)));

		}


	}


}
