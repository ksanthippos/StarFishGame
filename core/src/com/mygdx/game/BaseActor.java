package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class BaseActor extends Actor {

    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;

    public BaseActor(float x, float y, Stage s) {
        super();
        setPosition(x, y);
        s.addActor(this);

        animation = null;
        elapsedTime = 0;
        animationPaused = false;
    }

    @Override
    public void act(float dt) {
        super.act(dt);

        if (!animationPaused)
            elapsedTime += dt;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        if (animation != null && isVisible()) {
            batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

    // animation methods
    // ***********************
    public Animation<TextureRegion> loadAnimationFromFiles(String[] filenames, float frameDuration, boolean loop) {
        int fileCount = filenames.length;
        Array<TextureRegion> textureArray = new Array<>();  // ei luokkanotatiota lopussa?

        // adds single images to an array for animation
        for (int i = 0; i < fileCount; i++) {
            String fileName = filenames[i];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);  // WHATS THIS?
            textureArray.add(new TextureRegion(texture));
        }

        // create new animation from array
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

        // check if animation is looped
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            setAnimation(anim);

        return anim;
    }

    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                textureArray.add(temp[i][j]);
            }
        }

        // create new animation from array (identical part with above)
        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);

        // check if animation is looped
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);

        if (this.animation == null)
            setAnimation(anim);

        return anim;
    }

    // loading single textures is also handled via animation method for consistency
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }

    // check if animation is finished
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(elapsedTime);
    }
    // ***********************


    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        TextureRegion tr = animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w/2, h/2);
    }

    public void setAnimationPaused(boolean value) {
        this.animationPaused = value;
    }










}
