package com.f1.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends ScreenAdapter {
    private F1Game game;
    private SpriteBatch batch;
    private BitmapFont font;

    private OrthographicCamera camera;
    private Viewport viewport;
    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 800f;

    private Texture uiTitle;
    private Texture uiArrowLeft;
    private Texture uiArrowRight;
    private Texture uiConfirm;
    private Texture uiOptions;
    private Texture[] carCards;

    private String[] carFiles = {"player_f1.png", "rival_car.png", "Ferrari.png", "RedBull.png"};

    private int selectedPlayer = 0;
    private int selectedRival = 1;

    // 💡 La variable de luminosité déclarée au bon endroit
    private float brightness;

    public MainMenuScreen(F1Game game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.2f);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        uiTitle = new Texture("ui_title.png");
        uiArrowLeft = new Texture("ui_arrow_left.png");
        uiArrowRight = new Texture("ui_arrow_right.png");
        uiConfirm = new Texture("ui_confirm.png");
        uiOptions = new Texture("ui_options.png");

        carCards = new Texture[4];
        carCards[0] = new Texture("card_alpine.png");
        carCards[1] = new Texture("card_mclaren.png");
        carCards[2] = new Texture("card_ferrari.png");
        carCards[3] = new Texture("card_redbull.png");

        // 💡 Lecture de la sauvegarde dans le constructeur
        Preferences prefs = Gdx.app.getPreferences("F1RetroRacerPrefs");
        brightness = prefs.getFloat("brightness", 1.0f);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            selectedPlayer = (selectedPlayer - 1 + 4) % 4;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            selectedPlayer = (selectedPlayer + 1) % 4;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            selectedRival = (selectedRival - 1 + 4) % 4;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedRival = (selectedRival + 1) % 4;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            navigateToOptions();
            return;
        }

        boolean startRace = false;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            startRace = true;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            viewport.unproject(touchPoint);

            float optX = WORLD_WIDTH - 60;
            float optY = WORLD_HEIGHT - 60;
            if (touchPoint.x >= optX && touchPoint.x <= optX + 50 &&
                touchPoint.y >= optY && touchPoint.y <= optY + 50) {
                navigateToOptions();
                return;
            }

            if (touchPoint.x >= WORLD_WIDTH / 2f - 100 && touchPoint.x <= WORLD_WIDTH / 2f + 100 &&
                touchPoint.y >= 40 && touchPoint.y <= 100) {
                startRace = true;
            }

            float leftArrowX = WORLD_WIDTH / 2f - 200;
            float arrowY = WORLD_HEIGHT - 350;
            if (touchPoint.x >= leftArrowX - 15 && touchPoint.x <= leftArrowX + 40 + 15 &&
                touchPoint.y >= arrowY - 15 && touchPoint.y <= arrowY + 50 + 15) {
                selectedPlayer = (selectedPlayer - 1 + 4) % 4;
            }

            float rightArrowX = WORLD_WIDTH / 2f + 160;
            if (touchPoint.x >= rightArrowX - 15 && touchPoint.x <= rightArrowX + 40 + 15 &&
                touchPoint.y >= arrowY - 15 && touchPoint.y <= arrowY + 50 + 15) {
                selectedPlayer = (selectedPlayer + 1) % 4;
            }

            float rivalCardX = WORLD_WIDTH / 2f - 90;
            float rivalCardY = WORLD_HEIGHT - 600;
            if (touchPoint.x >= rivalCardX && touchPoint.x <= rivalCardX + 180 &&
                touchPoint.y >= rivalCardY && touchPoint.y <= rivalCardY + 115) {
                selectedRival = (selectedRival + 1) % 4;
            }
        }

        if (startRace) {
            game.setScreen(new GameScreen(game, carFiles[selectedPlayer], carFiles[selectedRival]));
            dispose();
            return;
        }

        // Application de la luminosité au fond
        Gdx.gl.glClearColor(0.12f * brightness, 0.12f * brightness, 0.15f * brightness, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Application de la luminosité aux textures
        batch.setColor(brightness, brightness, brightness, 1f);

        batch.begin();
        batch.draw(uiOptions, WORLD_WIDTH - 60, WORLD_HEIGHT - 60, 50, 50);
        batch.draw(uiTitle, WORLD_WIDTH / 2f - 200, WORLD_HEIGHT - 120, 400, 70);

        font.setColor(Color.CYAN);
        font.draw(batch, "VOTRE F1 (< >)", WORLD_WIDTH / 2f - 70, WORLD_HEIGHT - 180);
        batch.draw(uiArrowLeft, WORLD_WIDTH / 2f - 200, WORLD_HEIGHT - 350, 40, 50);
        batch.draw(carCards[selectedPlayer], WORLD_WIDTH / 2f - 140, WORLD_HEIGHT - 400, 280, 180);
        batch.draw(uiArrowRight, WORLD_WIDTH / 2f + 160, WORLD_HEIGHT - 350, 40, 50);

        font.setColor(Color.RED);
        font.draw(batch, "ADVERSAIRE PRINCIPAL (Clic / Haut-Bas)", WORLD_WIDTH / 2f - 165, WORLD_HEIGHT - 450);
        batch.draw(carCards[selectedRival], WORLD_WIDTH / 2f - 90, WORLD_HEIGHT - 600, 180, 115);

        batch.draw(uiConfirm, WORLD_WIDTH / 2f - 100, 40, 200, 60);
        font.setColor(Color.WHITE);
        font.draw(batch, "ESPACE ou CLIC", WORLD_WIDTH / 2f - 70, 30);

        batch.end();
    }

    private void navigateToOptions() {
        game.setScreen(new OptionsScreen(game));
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        uiTitle.dispose();
        uiArrowLeft.dispose();
        uiArrowRight.dispose();
        uiConfirm.dispose();
        uiOptions.dispose();
        for (Texture tex : carCards) {
            tex.dispose();
        }
    }
}
