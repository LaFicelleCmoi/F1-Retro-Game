package com.f1.game.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.f1.game.F1Game; //

public class Lwjgl3Launcher {
    public static void main(String[] args) {



        new Lwjgl3Application(new F1Game(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("F1 Retro Racer");
        configuration.setWindowedMode(800, 600);
        configuration.disableAudio(true); // ⬅️ AJOUTE CETTE LIGNE ICI !
        return configuration;
    }
}
