package com.f1.game;

import com.badlogic.gdx.graphics.Texture;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RivalCarTest {

    @Test
    public void testSetPositionUpdatesHitbox() {

        Texture mockTexture = Mockito.mock(Texture.class);
        RivalCar rival = new RivalCar(50f, 100f, mockTexture, 200f);


        rival.setPosition(150f, 300f);


        assertEquals(150f, rival.getBounds().x);
        assertEquals(300f, rival.getBounds().y);
    }
}
