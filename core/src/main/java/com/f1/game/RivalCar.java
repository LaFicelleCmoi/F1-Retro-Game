package com.f1.game;

import com.badlogic.gdx.graphics.Texture;

public class RivalCar extends Vehicle {
    private boolean scored = false;

    public RivalCar(float x, float y, Texture texture, float speed) {
        super(x, y, 50, 100, texture, speed);
    }

    public void scroll(float playerSpeed, float delta) {
        setPosition(getX(), getY() - (getSpeed() + playerSpeed - 400f) * delta);
    }

    public boolean isScored() { return scored; }
    public void setScored(boolean scored) { this.scored = scored; }

    @Override
    public void update(float delta) {}
}
