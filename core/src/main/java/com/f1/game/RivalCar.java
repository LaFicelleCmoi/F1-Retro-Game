package com.f1.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class RivalCar {
    private float x, y;
    private float width = 50;  // Largeur de l'adversaire
    private float height = 100; // Hauteur de l'adversaire
    private Texture texture;
    private float speed;

    // 💡 La fameuse boîte de collision !
    private Rectangle bounds;

    public RivalCar(float x, float y, Texture texture, float speed) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.speed = speed;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        // On déplace la boîte avec la voiture
        this.bounds.setPosition(x, y);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getSpeed() { return speed; }

    // 💡 La méthode que ton PlayerF1 cherchait !
    public Rectangle getBounds() {
        return bounds;
    }
}
