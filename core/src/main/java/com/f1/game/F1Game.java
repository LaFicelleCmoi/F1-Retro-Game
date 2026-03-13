package com.f1.game;

import com.badlogic.gdx.Game;

public class F1Game extends Game {
    @Override
    public void create() {
        this.setScreen(new MainMenuScreen(this));
    }
}
