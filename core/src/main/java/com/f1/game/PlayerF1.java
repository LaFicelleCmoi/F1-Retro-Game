package com.f1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PlayerF1 {
    private float x, y;
    private float width = 50;
    private float height = 100;
    private Texture texture;
    private float speed = 400f;


    private int keyLeft;
    private int keyRight;


    private Rectangle bounds;

    public PlayerF1(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);


        Preferences prefs = Gdx.app.getPreferences("F1RetroRacerPrefs");
        keyLeft = prefs.getInteger("keyLeft", Input.Keys.LEFT);
        keyRight = prefs.getInteger("keyRight", Input.Keys.RIGHT);
    }

    public void update(float delta) {

        if (Gdx.input.isKeyPressed(keyLeft)) {
            x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(keyRight)) {
            x += speed * delta;
        }


        bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }


    public float getX() { return x; }
    public float getY() { return y; }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return bounds;
    }


    public boolean isCollidingWith(RivalCar rival) {
        return this.bounds.overlaps(rival.getBounds());
    }

    public boolean isCollidingWith(Obstacle obs) {
        return this.bounds.overlaps(obs.getBounds());
    }
}
