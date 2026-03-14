package com.f1.game;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObstacleTest {

    @Test
    public void testScrollUpdatesPositionAndHitbox() {
        Texture mockTexture = Mockito.mock(Texture.class);


        Obstacle obstacle = new Obstacle(100f, 500f, mockTexture);

        obstacle.scroll(150f);

        assertEquals(350f, obstacle.getY(), "La position Y de l'obstacle doit être mise à jour");


        assertEquals(350f, obstacle.getBounds().y, "La hitbox doit se déplacer avec l'obstacle");
    }
}
