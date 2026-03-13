package com.f1.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity implements Collidable {

    protected float x, y;
    protected float width, height;
    protected Texture texture;
    protected Rectangle hitbox;

    public Entity(float x, float y, float width, float height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.hitbox = new Rectangle(x, y, width, height);
    }


    public abstract void update(float delta);

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public boolean isCollidingWith(Collidable other) {
        return this.hitbox.overlaps(other.getHitbox());
    }


    public float getX() { return x; }
    public float getY() { return y; }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.hitbox.setPosition(x, y);
    }
}
