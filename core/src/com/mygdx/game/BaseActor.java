package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class BaseActor extends Actor {

    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;

    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;
    private float deceleration;
    private float maxSpeed;

    public BaseActor(float x, float y, Stage s) {
        super();
        setPosition(x, y);
        s.addActor(this);

        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        velocityVec = new Vector2(0, 0);
        accelerationVec = new Vector2(0,0);
        acceleration = 0;
        deceleration = 0;
        maxSpeed = 1000;
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

    // ANIMATION
    // ***********************
    // loading from separate img files
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

    // loading from spritesheet
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

    // animation setters
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
    // ***********************

    // PHYSICS
    // speed
    // ***********************
    public void setSpeed(float speed) {
        if (velocityVec.len() == 0) // vector length = 0 --> speed = 0
            velocityVec.set(speed, 0);
        else
            velocityVec.setLength(speed);
    }

    public float getSpeed() {
        return velocityVec.len();
    }

    public void setMotionAngle(float angle) {
        velocityVec.setAngle(angle);
    }

    public float getMotionAngle() {
        return velocityVec.angle();
    }

    public boolean isMoving() {
        return (getSpeed() > 0);
    }

    // acceleration
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void accelerateAtAngle(float angle) {
        accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
    }

    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }

    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }

    public void setMaxSpeed(float speed) {
        this.maxSpeed = speed;
    }

    // apply all above
    public void applyPhysics(float dt) {
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);        // v = a*t
        float speed = getSpeed();

        // if not accelerating --> decelerate (decrease speed)
        if (accelerationVec.len() == 0)
            speed -= deceleration * dt;

        // keeps speed within bounds, set speed and move actor to new coordinates
        speed = MathUtils.clamp(speed, 0, maxSpeed);
        setSpeed(speed);
        moveBy(velocityVec.x * dt, velocityVec.y * dt);     // s = v*t

        // reset acceleration to 0
        accelerationVec.set(0, 0);
    }
    // ***********************










}
