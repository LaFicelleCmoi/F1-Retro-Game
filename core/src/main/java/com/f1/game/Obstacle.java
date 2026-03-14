package com.f1.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private float x, y;
    private float width = 40;
    private float height = 40;
    private Texture texture;


    private Rectangle bounds;

    public Obstacle(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void scroll(float amount) {
        this.y -= amount;

        this.bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }

    // 💡 Et la méthode pour l'obstacle !
    public Rectangle getBounds() {
        return bounds;
    }
}
