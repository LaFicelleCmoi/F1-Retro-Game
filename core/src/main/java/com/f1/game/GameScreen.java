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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

public class GameScreen extends ScreenAdapter {
    private F1Game game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;

    public static final float WORLD_WIDTH = 480f;
    public static final float WORLD_HEIGHT = 800f;

    private PlayerF1 player;
    private List<RivalCar> rivals = new ArrayList<>();
    private List<Obstacle> obstacles = new ArrayList<>();

    private Texture playerTex, rivalTex, obstacleTex;
    private Texture[] startLightsTex;

    private int startPhase = 0;
    private float startTimer = 0, gantryY, spawnTimer = 0, roadOffset = 0, score = 0, bonusTimer = 0, brightness;
    private boolean isRaceStarted = false, isGameOver = false;
    private String bonusText = "";

    private int highScore, keyBoost;
    private Preferences prefs;

    public GameScreen(F1Game game, String playerTextureName, String rivalTextureName) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        playerTex = new Texture(playerTextureName);
        rivalTex = new Texture(rivalTextureName);
        obstacleTex = new Texture("obstacle.png");

        startLightsTex = new Texture[]{
            new Texture("gantry_empty.png"), new Texture("start_1.png"),
            new Texture("start_2.png"), new Texture("start_3.png"),
            new Texture("start_4.png"), new Texture("start_5.png"),
            new Texture("start_go.png")
        };

        prefs = Gdx.app.getPreferences("F1RetroRacerPrefs");
        highScore = prefs.getInteger("highscore", 0);
        brightness = prefs.getFloat("brightness", 1.0f);
        keyBoost = prefs.getInteger("keyBoost", Input.Keys.UP);

        restartGame(); // On utilise directement restart pour ne pas réécrire le code !
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        if (!isRaceStarted) {
            startTimer += delta;
            if (startTimer > 0.8f) {
                startTimer = 0;
                if (++startPhase == 6) isRaceStarted = true;
            }
        } else if (!isGameOver) {
            player.update(delta); // Le joueur gère lui-même ses limites grâce à Math.min et Math.max

            float currentSpeed = Gdx.input.isKeyPressed(keyBoost) ? 800f : 400f;
            score += (currentSpeed / 100f) * delta;

            roadOffset = (roadOffset - currentSpeed * delta) % 100; // Plus besoin de if !
            if (gantryY > -200) gantryY -= currentSpeed * delta;

            spawnTimer += delta;
            if (spawnTimer > (currentSpeed == 800f ? 0.7f : 1.5f)) {
                if (Math.random() < 0.3) obstacles.add(new Obstacle((float)(Math.random() * (WORLD_WIDTH - 50)), WORLD_HEIGHT + 50, obstacleTex));
                else rivals.add(new RivalCar((float)(Math.random() * (WORLD_WIDTH - 50)), WORLD_HEIGHT + 50, rivalTex, 200f));
                spawnTimer = 0;
            }

            for (RivalCar rival : rivals) {
                rival.scroll(currentSpeed, delta);
                if (player.isCollidingWith(rival)) triggerGameOver();

                // ⬅️ NOUVEAU : On utilise la fonction 'isScored()' plutôt qu'une Liste !
                if (rival.getY() < player.getY() && !rival.isScored()) {
                    rival.setScored(true);
                    if (Math.abs(player.getX() - rival.getX()) < 85) {
                        score += 500;
                        bonusTimer = 1.0f;
                        bonusText = "DEPASSEMENT RISQUE ! +500";
                    }
                }
            }

            for (Obstacle obs : obstacles) {
                obs.scroll(currentSpeed, delta);
                if (player.isCollidingWith(obs)) triggerGameOver();
            }


            rivals.removeIf(r -> r.getY() < -100);
            obstacles.removeIf(o -> o.getY() < -100);

        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) restartGame();
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return;
            }
        }

        drawGame(delta);
    }


    private void drawGame(float delta) {
        Gdx.gl.glClearColor(0.15f * brightness, 0.15f * brightness, 0.15f * brightness, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        for (int i = 0; i < 15; i++) shapeRenderer.rect(WORLD_WIDTH / 2f - 5, roadOffset + (i * 100), 10, 50);
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.setColor(brightness, brightness, brightness, 1f);
        batch.begin();

        player.draw(batch);
        for (RivalCar rival : rivals) rival.draw(batch);
        for (Obstacle obs : obstacles) obs.draw(batch);

        if (gantryY > -200) batch.draw(startLightsTex[Math.min(startPhase, 6)], WORLD_WIDTH / 2f - 200, gantryY, 400, 150);

        if (bonusTimer > 0) {
            bonusTimer -= delta;
            font.setColor(Color.CYAN);
            font.getData().setScale(1.0f);
            font.draw(batch, bonusText, player.getX() - 90, player.getY() + 150 + (1f - bonusTimer) * 50);
        }

        font.setColor(Color.YELLOW);
        font.getData().setScale(1.2f);
        font.draw(batch, "Score: " + (int)score, 20, WORLD_HEIGHT - 20);
        font.draw(batch, "Highscore: " + highScore, 20, WORLD_HEIGHT - 50);
        batch.end();

        if (isGameOver) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(new Color(0, 0, 0, 0.75f));
            shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            batch.begin();
            font.setColor(Color.RED);
            font.getData().setScale(3);
            font.draw(batch, "CRASH !", WORLD_WIDTH / 2f - 85, WORLD_HEIGHT / 2f + 100);
            font.setColor(Color.WHITE);
            font.getData().setScale(1.2f);
            font.draw(batch, "Score final : " + (int)score, WORLD_WIDTH / 2f - 90, WORLD_HEIGHT / 2f + 40);
            font.draw(batch, "[ R ] - Rejouer", WORLD_WIDTH / 2f - 70, WORLD_HEIGHT / 2f - 10);
            font.draw(batch, "[ ECHAP ] - Retour Menu", WORLD_WIDTH / 2f - 110, WORLD_HEIGHT / 2f - 50);
            batch.end();
        }
    }

    private void triggerGameOver() {
        isGameOver = true;
        if ((int)score > highScore) {
            highScore = (int)score;
            prefs.putInteger("highscore", highScore);
            prefs.flush();
        }
    }

    private void restartGame() {
        rivals.clear();
        obstacles.clear();
        if(player == null) player = new PlayerF1(WORLD_WIDTH / 2f - 25, 50, playerTex);
        else player.setPosition(WORLD_WIDTH / 2f - 25, 50);
        isGameOver = isRaceStarted = false;
        startPhase = 0;
        startTimer = spawnTimer = score = bonusTimer = 0;
        gantryY = WORLD_HEIGHT - 150;
    }

    @Override
    public void dispose() {
        batch.dispose(); shapeRenderer.dispose(); font.dispose();
        playerTex.dispose(); rivalTex.dispose(); obstacleTex.dispose();
        for(Texture t : startLightsTex) t.dispose();
    }
}
