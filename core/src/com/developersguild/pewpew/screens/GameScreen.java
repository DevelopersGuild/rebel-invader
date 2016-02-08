package com.developersguild.pewpew.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.PewPew;
import com.developersguild.pewpew.PhysicsListener;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.systems.AnimationSystem;
import com.developersguild.pewpew.systems.BackgroundSystem;
import com.developersguild.pewpew.systems.BoundsSystem;
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

/**
 * Created by Vihan on 1/10/2016.
 */
public class GameScreen extends ScreenAdapter {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_OVER = 3;

    PewPew game;
    Level level;
    PooledEngine engine;
    World world;
    PhysicsListener listener;

    //Testing -- not sure why it's initialized here
    private GlyphLayout layout = new GlyphLayout(); // from ashley-superjumper
    OrthographicCamera guiCam;
    Vector3 touchPoint;

    private int state;
    
    private List<Entity> deadEntities=new ArrayList<Entity>();
    
    public GameScreen(PewPew game) {
        this.game = game;

        // TODO: Change to ready when updateRunning() and updateReady() are both done
        state = GAME_RUNNING;

        engine = new PooledEngine();
        level = new Level(engine);
        world = new World(new Vector2(0, 0), true);
        listener = new PhysicsListener();

        // Testing
        guiCam = new OrthographicCamera(320, 480);
        guiCam.position.set(320 / 2, 480 / 2, 0);
        touchPoint = new Vector3();

        // Add systems
        engine.addSystem(new PlayerSystem(level));
        engine.addSystem(new CameraSystem());
        engine.addSystem(new BackgroundSystem());
        engine.addSystem(new MovementSystem());
        engine.addSystem(new BoundsSystem());
        engine.addSystem(new StateSystem());
        engine.addSystem(new AnimationSystem());
        engine.addSystem(new StructureSystem());
        engine.addSystem(new EnemySystem());
        engine.addSystem(new RenderingSystem(game.batch));
        engine.addSystem(new PhysicsSystem(world, engine.getSystem(RenderingSystem.class).getCamera()));
        engine.addSystem(new HealthSystem());
        engine.addSystem(new HeightDisposableSystem(this));

        // Set camera
        engine.getSystem(BackgroundSystem.class).setCamera(engine.getSystem(RenderingSystem.class).getCamera());

        // Set PhysicsListener as the entity listener for Ashley and contact listener for Box2D
        engine.addEntityListener(Family.all(BodyComponent.class).get(), listener);
        world.setContactListener(listener);

        level.create(world);

        // TODO: Change to pauseSystems() once first state is READY
        resumeSystems();
    }

    public void update(float deltaTime) {
        if (deltaTime > 0.1f) deltaTime = 0.1f;

        engine.update(deltaTime);
        
        switch (state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady() {
        if (Gdx.input.justTouched()) {
            state = GAME_RUNNING;
            resumeSystems();
        }
    }

    private void updateRunning(float deltaTime) {
        Application.ApplicationType appType = Gdx.app.getType();

        // should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
        float accelX = 0.0f;

        if (appType == Application.ApplicationType.Android || appType == Application.ApplicationType.iOS) {
            accelX = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) accelX = 2.0f;
            if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) accelX = -2.0f;
        }

        engine.getSystem(PlayerSystem.class).setAccelX(accelX);

        if (level.state == Level.LEVEL_STATE_GAME_OVER) {
            state = GAME_OVER;
            pauseSystems();
        }
        
        //Kill off any dead entities
        for(Entity e:deadEntities) {
        	engine.removeEntity(e);
        }
    }

    private void updateGameOver() {
        if (Gdx.input.justTouched()) {
            resumeSystems();
            game.setScreen(new GameScreen(game));
        }
    }

    public void draw() {
        guiCam.update();
        game.batch.setProjectionMatrix(guiCam.combined);
        game.batch.begin();
        switch (state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        game.batch.end();
    }

    private void presentReady() {
        // In Super Jumper:
        // game.batch.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
    }

    private void presentRunning() {
        // nothing yet
    }

    private void presentGameOver() {
        //game.batch.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
        String gameOver = "Game Over";
        layout.setText(Assets.font, gameOver);
        float gameOverWidth = layout.width;
        float gameOverHeight = layout.height;
        Assets.font.draw(game.batch, gameOver, 160 - gameOverWidth / 2, 240 - gameOverHeight / 2);
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
    
    public void markEntityForRemoval(Entity e){
    	deadEntities.add(e);
    }
}
