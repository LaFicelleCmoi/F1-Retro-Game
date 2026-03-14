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
    private List<RivalCar> rivals;
    private List<Obstacle> obstacles;
    private List<RivalCar> scoredRivals;

    private Texture playerTex;
    private Texture rivalTex;
    private Texture obstacleTex;

    private Texture[] startLightsTex;
    private int startPhase = 0;
    private float startTimer = 0;
    private boolean isRaceStarted = false;
    private float gantryY;

    private float spawnTimer = 0;
    private float roadOffset = 0;

    private float score = 0;
    private int highScore = 0;
    private Preferences prefs;

    private float bonusTimer = 0;
    private String bonusText = "";

    private boolean isGameOver = false;
    private int keyLeft;
    private int keyRight;
    private int keyBoost;
    private float brightness;

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

        startLightsTex = new Texture[7];
        startLightsTex[0] = new Texture("gantry_empty.png");
        startLightsTex[1] = new Texture("start_1.png");
        startLightsTex[2] = new Texture("start_2.png");
        startLightsTex[3] = new Texture("start_3.png");
        startLightsTex[4] = new Texture("start_4.png");
        startLightsTex[5] = new Texture("start_5.png");
        startLightsTex[6] = new Texture("start_go.png");

        gantryY = WORLD_HEIGHT - 150;

        player = new PlayerF1(WORLD_WIDTH / 2f - 25, 50, playerTex);
        rivals = new ArrayList<>();
        obstacles = new ArrayList<>();
        scoredRivals = new ArrayList<>();

        prefs = Gdx.app.getPreferences("F1RetroRacerPrefs");
        highScore = prefs.getInteger("highscore", 0);

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

        if (!isRaceStarted) {
            startTimer += delta;
            if (startTimer > 0.8f) {
                startTimer = 0;
                startPhase++;
                if (startPhase == 6) {
                    isRaceStarted = true;
                }
            }
        }
        else if (!isGameOver) {
            player.update(delta);

            if (player.getX() < 0) player.setPosition(0, player.getY());
            if (player.getX() > WORLD_WIDTH - 50) player.setPosition(WORLD_WIDTH - 50, player.getY());

            float currentSpeed = 400f;
            if (Gdx.input.isKeyPressed(keyBoost)) {
                currentSpeed = 800f;
            }

            score += (currentSpeed / 100f) * delta;

            roadOffset -= currentSpeed * delta;
            if (roadOffset <= -100) roadOffset += 100;

            if (gantryY > -200) {
                gantryY -= currentSpeed * delta;
            }

            spawnTimer += delta;
            float spawnRate = currentSpeed == 800f ? 0.7f : 1.5f;
            if (spawnTimer > spawnRate) {
                if (Math.random() < 0.3) spawnObstacle();
                else spawnRival();
                spawnTimer = 0;
            }

            for (int i = rivals.size() - 1; i >= 0; i--) {
                RivalCar rival = rivals.get(i);
                rival.setPosition(rival.getX(), rival.getY() - (rival.getSpeed() + currentSpeed - 400f) * delta);

                if (player.isCollidingWith(rival)) {
                    triggerGameOver();
                }
                else if (rival.getY() < player.getY() && !scoredRivals.contains(rival)) {
                    scoredRivals.add(rival);
                    float distX = Math.abs(player.getX() - rival.getX());
                    if (distX < 85) {
                        score += 500;
                        bonusTimer = 1.0f;
                        bonusText = "DEPASSEMENT RISQUE ! +500";
                    }
                }

                if (rival.getY() < -100) {
                    scoredRivals.remove(rival);
                    rivals.remove(i);
                }
            }

            for (int i = obstacles.size() - 1; i >= 0; i--) {
                Obstacle obs = obstacles.get(i);
                obs.scroll(currentSpeed * delta);
                if (player.isCollidingWith(obs)) triggerGameOver();
                if (obs.getY() < -100) obstacles.remove(i);
            }
        }
        else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) restartGame();
            if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return;
            }
        }

        Gdx.gl.glClearColor(0.15f * brightness, 0.15f * brightness, 0.15f * brightness, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        for (int i = 0; i < 15; i++) {
            shapeRenderer.rect(WORLD_WIDTH / 2f - 5, roadOffset + (i * 100), 10, 50);
        }
        shapeRenderer.end();

        // Application de la luminosité à toutes les textures (voitures, obstacles...)
        batch.setColor(brightness, brightness, brightness, 1f);
        batch.begin();

        player.draw(batch);
        for (RivalCar rival : rivals) rival.draw(batch);
        for (Obstacle obs : obstacles) obs.draw(batch);

        if (gantryY > -200) {
            batch.draw(startLightsTex[Math.min(startPhase, 6)], WORLD_WIDTH / 2f - 200, gantryY, 400, 150);
        }

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

    private void spawnRival() {
        float randomX = (float) (Math.random() * (WORLD_WIDTH - 50));
        rivals.add(new RivalCar(randomX, WORLD_HEIGHT + 50, rivalTex, 200f));
    }

    private void spawnObstacle() {
        float randomX = (float) (Math.random() * (WORLD_WIDTH - 50));
        obstacles.add(new Obstacle(randomX, WORLD_HEIGHT + 50, obstacleTex));
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
        scoredRivals.clear();
        player.setPosition(WORLD_WIDTH / 2f - 25, 50);
        isGameOver = false;

        isRaceStarted = false;
        startPhase = 0;
        startTimer = 0;
        gantryY = WORLD_HEIGHT - 150;

        bonusTimer = 0;
        spawnTimer = 0;
        score = 0;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        playerTex.dispose();
        rivalTex.dispose();
        obstacleTex.dispose();
        for(Texture t : startLightsTex) {
            t.dispose();
        }
    }
}
