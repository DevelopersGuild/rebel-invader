package com.developersguild.pewpew.screens;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.PewPew;
import com.developersguild.pewpew.PhysicsListener;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.systems.AnimationSystem;
import com.developersguild.pewpew.systems.BackgroundSystem;
import com.developersguild.pewpew.systems.BoundsSystem;
import com.developersguild.pewpew.systems.BulletSystem;
import com.developersguild.pewpew.systems.CameraSystem;
import com.developersguild.pewpew.systems.EnemySystem;
import com.developersguild.pewpew.systems.HealthSystem;
import com.developersguild.pewpew.systems.HeightDisposableSystem;
import com.developersguild.pewpew.systems.MovementSystem;
import com.developersguild.pewpew.systems.PhysicsSystem;
import com.developersguild.pewpew.systems.PlayerSystem;
import com.developersguild.pewpew.systems.RenderingSystem;
import com.developersguild.pewpew.systems.StateSystem;
import com.developersguild.pewpew.systems.StructureSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vihan on 1/10/2016.
 */
public class GameScreen extends ScreenAdapter {
    static final int GAME_RUNNING = 0;
    static final int GAME_PAUSED = 1;
    static final int GAME_OVER = 2;
    static final int GAME_WON = 3;

    PewPew game;
    Level level;
    Settings settings;
    PooledEngine engine;
    World world;
    PhysicsListener listener;
    OrthographicCamera guiCam;
    Vector3 touchPoint;
    Rectangle pauseBounds;

    private GlyphLayout layout;
    private int state;
    private List<Entity> deadEntities;
    private float currentTime;

    public GameScreen(PewPew game) {
        this.game = game;

        state = GAME_RUNNING;

        engine = new PooledEngine();
        level = new Level(engine);
        world = new World(new Vector2(0, 0), true);
        listener = new PhysicsListener();

        // Testing
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();
        layout = new GlyphLayout();
        deadEntities = new ArrayList<Entity>();
        currentTime = 0f;

        // Add systems
        engine.addSystem(new PlayerSystem(level));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new BackgroundSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new StateSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new StructureSystem(level));
        engine.addSystem(new EnemySystem(level));
        engine.addSystem(new RenderingSystem(game.batch));
        engine.addSystem(new PhysicsSystem(world, engine.getSystem(RenderingSystem.class).getCamera()));
        engine.addSystem(new HealthSystem(this));
        engine.addSystem(new HeightDisposableSystem(this));
        engine.addSystem(new BulletSystem(this));

        // Set camera
        engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderingSystem.class).getCamera());

        // Set PhysicsListener as the entity listener for Ashley and contact listener for Box2D
        engine.addEntityListener(Family.all(BodyComponent.class).get(), listener);
        world.setContactListener(listener);

        // Set continuous physics for bullets
        world.setContinuousPhysics(true);

        level.create(world);

        pauseBounds = new Rectangle(320 - 40 - 5, 480 - 50 - 5, 40, 50);

        resumeSystems();

        Assets.musicMenu.stop();
        Assets.musicGame.play();
    }

    public void update(float deltaTime) {
        //if (deltaTime > 0.1f) deltaTime = 0.1f;

        engine.update(deltaTime);

        switch (state) {
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
            case GAME_WON:
                updateGameOver();
                break;
        }
    }

    private void updateRunning(float deltaTime) {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
                state = GAME_PAUSED;
                pauseSystems();
                Assets.click.play();
                return;
            }
        }

        Application.ApplicationType appType = Gdx.app.getType();
        currentTime += deltaTime;

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX = 0.0f;

        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            if (Math.abs(Gdx.input.getAccelerometerX()) > 0.8f) accelX = Gdx.input.getAccelerometerX();
            if (Gdx.input.justTouched()) playerShoot();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) accelX = 2.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.D)) accelX = -2.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) playerShoot();
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) missileShoot();
        }

        engine.getSystem(PlayerSystem.class).setAccelX(accelX);

        if (level.state == Level.LEVEL_STATE_GAME_OVER) {
            state = GAME_OVER;
            pauseSystems();
        }

        if (level.state == Level.LEVEL_STATE_GAME_WON) {
            state = GAME_WON;
            pauseSystems();
        }

        //Kill off any dead entities
        for (Entity e : deadEntities) {
            engine.removeEntity(e);
        }
        deadEntities.clear();
    }

    private void updatePaused() {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseBounds.contains(touchPoint.x, touchPoint.y)) {
                resume();
                Assets.click.play();
                return;
            }
        }
    }

    private void updateGameOver() {
        if (Gdx.input.justTouched()) {
            resumeSystems();
            game.setScreen(new MainMenuScreen(game));
            settings.addScore(level.score);
            settings.save();
        }
    }

    private void playerShoot() {
        PlayerComponent player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0).getComponent(PlayerComponent.class);
        if (player.shootTimer <= currentTime) {
            player.shootTimer = currentTime + PlayerComponent.FIRE_RATE;
            engine.getSystem(PlayerSystem.class).requestBullet();
            Assets.shot.play(0.3f);
        }
    }

    private void missileShoot() {
        PlayerComponent player = engine.getEntitiesFor(Family.all(PlayerComponent.class).get()).get(0).getComponent(PlayerComponent.class);
        if (player.shootTimer <= currentTime) {
            player.shootTimer = currentTime + PlayerComponent.FIRE_RATE;
            engine.getSystem(PlayerSystem.class).requestMissile();
        }
    }

    public void draw() {
        guiCam.update();
        game.batch.setProjectionMatrix(guiCam.combined);
        game.batch.begin();
        updateScore(level.score);
        switch (state) {
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
            case GAME_WON:
                presentWin();
                break;
        }
        game.batch.end();
    }

    private void presentRunning() {
        game.batch.draw(Assets.pauseButton, 320 - 40 - 5, 480 - 50 - 5, 40, 50);
    }

    private void presentPaused() {
        game.batch.draw(Assets.playButton, 320 - 40 - 5, 480 - 50 - 5, 40, 50);
    }

    private void presentGameOver() {
        //game.batch.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
        String gameOver = "Game Over";
        layout.setText(Assets.font, gameOver);
        float gameOverWidth = layout.width;
        float gameOverHeight = layout.height;
        Assets.font.draw(game.batch, gameOver, 160 - gameOverWidth / 2, 240 - gameOverHeight / 2);
        Assets.musicGame.stop();
    }

    private void presentWin() {
        String gameOver = "YOU WIN!";
        layout.setText(Assets.font, gameOver);
        float gameOverWidth = layout.width;
        float gameOverHeight = layout.height;
        Assets.font.draw(game.batch, gameOver, 160 - gameOverWidth / 2, 240 - gameOverHeight / 2);
    }

    public void updateScore(int score) {
        String scoreText;
        scoreText = Integer.toString(score);
        layout.setText(Assets.font, scoreText);
        Assets.font.draw(game.batch, scoreText, 0 + 10, 480 - 10);
    }


    private void pauseSystems() {
        // RenderingSystem not included
        engine.getSystem(PlayerSystem.class).setProcessing(false);
        engine.getSystem(StructureSystem.class).setProcessing(false);
        engine.getSystem(EnemySystem.class).setProcessing(false);
        engine.getSystem(MovementSystem.class).setProcessing(false);
        engine.getSystem(BoundsSystem.class).setProcessing(false);
        engine.getSystem(StateSystem.class).setProcessing(false);
        engine.getSystem(AnimationSystem.class).setProcessing(false);
        engine.getSystem(BackgroundSystem.class).setProcessing(false);
        engine.getSystem(CameraSystem.class).setProcessing(false);
        engine.getSystem(HealthSystem.class).setProcessing(false);
        engine.getSystem(PhysicsSystem.class).setProcessing(false);
        engine.getSystem(HeightDisposableSystem.class).setProcessing(false);
        engine.getSystem(BulletSystem.class).setProcessing(false);
        Assets.musicGame.pause();
    }

    private void resumeSystems() {
        // RenderingSystem not included
        engine.getSystem(PlayerSystem.class).setProcessing(true);
        engine.getSystem(MovementSystem.class).setProcessing(true);
        engine.getSystem(StructureSystem.class).setProcessing(true);
        engine.getSystem(EnemySystem.class).setProcessing(true);
        engine.getSystem(BoundsSystem.class).setProcessing(true);
        engine.getSystem(StateSystem.class).setProcessing(true);
        engine.getSystem(AnimationSystem.class).setProcessing(true);
        engine.getSystem(CameraSystem.class).setProcessing(true);
        engine.getSystem(BackgroundSystem.class).setProcessing(true);
        engine.getSystem(HealthSystem.class).setProcessing(true);
        engine.getSystem(PhysicsSystem.class).setProcessing(true);
        engine.getSystem(HeightDisposableSystem.class).setProcessing(true);
        engine.getSystem(BulletSystem.class).setProcessing(true);
        Assets.musicGame.play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public void pause() {
        if (state == GAME_RUNNING) {
            state = GAME_PAUSED;
            pauseSystems();
        }
    }

    @Override
    public void resume() {
        if (state == GAME_PAUSED) {
            state = GAME_RUNNING;
            resumeSystems();
        }
    }

    public void markEntityForRemoval(Entity e) {
        if (e.getComponent(BodyComponent.class) != null) {
            world.destroyBody(e.getComponent(BodyComponent.class).body);
        }
        deadEntities.add(e);
    }

    public Level getLevel() {
        return level;
    }
}
