package com.f1.game;

import com.badlogic.gdx.graphics.Texture;

public class Obstacle extends Entity {

    public Obstacle(float x, float y, Texture texture) {
        super(x, y, 40, 40, texture);
    }

    public void scroll(float playerSpeed, float delta) {
        setPosition(getX(), getY() - playerSpeed * delta);
    }

    @Override
    public void update(float delta) {}
}
