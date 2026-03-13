package com.f1.game;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {
    Rectangle getHitbox();
    boolean isCollidingWith(Collidable other);
}
