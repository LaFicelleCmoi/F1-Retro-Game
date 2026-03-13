package com.f1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Graphics.DisplayMode;

public class OptionsScreen extends ScreenAdapter {
    private F1Game game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; // 💡 NOUVEAU : Le pinceau géométrique !
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 800f;

    private Preferences prefs;
    private boolean isFullscreen;
    private float brightness;

    private int keyLeft;
    private int keyRight;
    private int keyBoost;

    private int rebindState = 0;

    public OptionsScreen(F1Game game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer(); // Initialisation du pinceau
        font = new BitmapFont();
        font.getData().setScale(1.2f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        prefs = Gdx.app.getPreferences("F1RetroRacerPrefs");
        isFullscreen = prefs.getBoolean("fullscreen", false);
        brightness = prefs.getFloat("brightness", 1.0f);

        keyLeft = prefs.getInteger("keyLeft", Input.Keys.LEFT);
        keyRight = prefs.getInteger("keyRight", Input.Keys.RIGHT);
        keyBoost = prefs.getInteger("keyBoost", Input.Keys.UP);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        // --- 1. MODE "ÉCOUTE" POUR CHANGER UNE TOUCHE ---
        if (rebindState != 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                rebindState = 0;
            } else {
                for (int i = 8; i < 256; i++) {
                    if (Gdx.input.isKeyJustPressed(i)) {
                        if (rebindState == 1) keyLeft = i;
                        if (rebindState == 2) keyRight = i;
                        if (rebindState == 3) keyBoost = i;

                        prefs.putInteger("keyLeft", keyLeft);
                        prefs.putInteger("keyRight", keyRight);
                        prefs.putInteger("keyBoost", keyBoost);
                        prefs.flush();

                        rebindState = 0;
                        break;
                    }
                }
            }
        }
        // --- 2. MODE NORMAL (CLICS SOURIS) ---
        else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return;
            }

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                viewport.unproject(touchPoint);

                // Clic Écran
                if (touchPoint.y > 620 && touchPoint.y < 670) {
                    isFullscreen = !isFullscreen;
                    prefs.putBoolean("fullscreen", isFullscreen);
                    prefs.flush();
                    if (isFullscreen) {
                        DisplayMode mode = Gdx.graphics.getDisplayMode();
                        Gdx.graphics.setFullscreenMode(mode);
                    } else {
                        Gdx.graphics.setWindowedMode(480, 800);
                    }
                }

                // Clic Luminosité [-] et [+]
                if (touchPoint.x > 60 && touchPoint.x < 140 && touchPoint.y > 540 && touchPoint.y < 590) {
                    brightness = Math.max(0.2f, brightness - 0.1f);
                    saveBrightness();
                }
                if (touchPoint.x > 340 && touchPoint.x < 420 && touchPoint.y > 540 && touchPoint.y < 590) {
                    brightness = Math.min(2.0f, brightness + 0.1f);
                    saveBrightness();
                }

                // Clics Contrôles (Zones ajustées aux nouveaux boutons)
                if (touchPoint.y > 380 && touchPoint.y < 430) rebindState = 1;
                if (touchPoint.y > 310 && touchPoint.y < 360) rebindState = 2;
                if (touchPoint.y > 240 && touchPoint.y < 290) rebindState = 3;

                // Clic Retour
                if (touchPoint.y > 60 && touchPoint.y < 120) {
                    game.setScreen(new MainMenuScreen(game));
                    dispose();
                    return;
                }
            }
        }

        // --- 3. DESSIN DU MENU ---
        Gdx.gl.glClearColor(0.12f * brightness, 0.12f * brightness, 0.15f * brightness, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // 💡 3A. DESSIN DES FORMES GÉOMÉTRIQUES (Fonds, Boutons)
        // Activation de la transparence (Alpha)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Grand panneau de fond pour "AFFICHAGE"
        shapeRenderer.setColor(new Color(0.2f * brightness, 0.2f * brightness, 0.25f * brightness, 0.8f));
        shapeRenderer.rect(20, 520, WORLD_WIDTH - 40, 180);

        // Grand panneau de fond pour "CONTROLES"
        shapeRenderer.rect(20, 220, WORLD_WIDTH - 40, 260);

        // Dessin des "boutons" pour les touches pour montrer qu'ils sont cliquables
        shapeRenderer.setColor(new Color(0.3f * brightness, 0.3f * brightness, 0.35f * brightness, 1f));
        shapeRenderer.rect(40, 385, WORLD_WIDTH - 80, 40); // Fond touche Gauche
        shapeRenderer.rect(40, 315, WORLD_WIDTH - 80, 40); // Fond touche Droite
        shapeRenderer.rect(40, 245, WORLD_WIDTH - 80, 40); // Fond touche Boost

        // Fond pour le bouton Retour
        shapeRenderer.setColor(new Color(0.8f * brightness, 0.1f * brightness, 0.1f * brightness, 0.3f));
        shapeRenderer.rect(100, 70, WORLD_WIDTH - 200, 45);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND); // Fin de la transparence

        // 💡 3B. DESSIN DU TEXTE PAR DESSUS LES FORMES
        batch.setProjectionMatrix(camera.combined);
        batch.setColor(brightness, brightness, brightness, 1f);
        batch.begin();

        // Titre principal
        font.setColor(Color.YELLOW);
        font.draw(batch, "OPTIONS", WORLD_WIDTH / 2f - 60, 760);

        // --- Section Affichage ---
        font.setColor(Color.CYAN);
        font.draw(batch, "- AFFICHAGE -", WORLD_WIDTH / 2f - 85, 680);

        font.setColor(Color.WHITE);
        String screenText = isFullscreen ? "[ PLEIN ECRAN ]" : "[ MODE FENETRE ]";
        font.draw(batch, screenText, WORLD_WIDTH / 2f - 95, 640);

        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "[-]", 80, 570);
        font.setColor(Color.WHITE);
        font.draw(batch, "Luminosite: " + Math.round(brightness * 100) + "%", 140, 570);
        font.setColor(Color.LIGHT_GRAY);
        font.draw(batch, "[+]", 360, 570);

        // --- Section Contrôles ---
        font.setColor(Color.CYAN);
        font.draw(batch, "- CONTROLES -", WORLD_WIDTH / 2f - 85, 460);

        // Touche Gauche
        font.setColor(Color.WHITE);
        font.draw(batch, "Aller a Gauche", 60, 412);
        font.setColor(rebindState == 1 ? Color.ORANGE : Color.YELLOW);
        String txtLeft = rebindState == 1 ? "Appuyez..." : "[ " + Input.Keys.toString(keyLeft) + " ]";
        font.draw(batch, txtLeft, 280, 412);

        // Touche Droite
        font.setColor(Color.WHITE);
        font.draw(batch, "Aller a Droite", 60, 342);
        font.setColor(rebindState == 2 ? Color.ORANGE : Color.YELLOW);
        String txtRight = rebindState == 2 ? "Appuyez..." : "[ " + Input.Keys.toString(keyRight) + " ]";
        font.draw(batch, txtRight, 280, 342);

        // Touche Boost
        font.setColor(Color.WHITE);
        font.draw(batch, "Activer Boost", 60, 272);
        font.setColor(rebindState == 3 ? Color.ORANGE : Color.YELLOW);
        String txtBoost = rebindState == 3 ? "Appuyez..." : "[ " + Input.Keys.toString(keyBoost) + " ]";
        font.draw(batch, txtBoost, 280, 272);

        // Bouton Retour
        font.setColor(Color.WHITE);
        font.draw(batch, "RETOUR AU MENU", WORLD_WIDTH / 2f - 100, 100);

        batch.end();
    }

    private void saveBrightness() {
        prefs.putFloat("brightness", brightness);
        prefs.flush();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose(); // 💡 Ne pas oublier de nettoyer le pinceau !
        font.dispose();
    }
}
