package com.f1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;

public class PlayerF1 extends Vehicle {
    private int keyLeft, keyRight;

    public PlayerF1(float x, float y, Texture texture) {
        super(x, y, 50, 100, texture, 400f);
        Preferences prefs = Gdx.app.getPreferences("F1RetroRacerPrefs");
        keyLeft = prefs.getInteger("keyLeft", Input.Keys.LEFT);
        keyRight = prefs.getInteger("keyRight", Input.Keys.RIGHT);
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(keyLeft)) {
            /
            setPosition(Math.max(0, getX() - getSpeed() * delta), getY());
        }
        if (Gdx.input.isKeyPressed(keyRight)) {

            setPosition(Math.min(430f, getX() + getSpeed() * delta), getY());
        }
    }
}
