package com.f1.game;
import com.badlogic.gdx.graphics.Texture;

public abstract class Vehicle extends Entity {
    private float speed;

    public Vehicle(float x, float y, float width, float height, Texture texture, float speed) {
        super(x, y, width, height, texture);
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
