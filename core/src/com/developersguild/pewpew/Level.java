package com.developersguild.pewpew;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.developersguild.pewpew.components.AnimationComponent;
import com.developersguild.pewpew.components.BackgroundComponent;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.BoundsComponent;
import com.developersguild.pewpew.components.BulletComponent;
import com.developersguild.pewpew.components.CameraComponent;
import com.developersguild.pewpew.components.EnemyComponent;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.HeightDisposableComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TextureComponent;
import com.developersguild.pewpew.components.TransformComponent;
import com.developersguild.pewpew.systems.RenderingSystem;

/**
 * Created by Vihan on 1/10/2016.
 */
public class Level {

    public static final float SCREEN_HEIGHT = 15;

    public static final float LEVEL_WIDTH = 10;
    public static final float LEVEL_HEIGHT = SCREEN_HEIGHT * 20; // I think the second number is the number of screens

    public static final int LEVEL_STATE_RUNNING = 1;
    public static final int LEVEL_STATE_GAME_OVER = 2;
    public static final int LEVEL_STATE_GAME_WON = 3;
    public static float playerHeight;
    public final RandomXS128 rand;
    public int state;
    public int score;
    private PooledEngine engine;
    private WorldGenerator generator;
    private World world;

    public Level(PooledEngine engine) {
        this.engine = engine;
        this.rand = new RandomXS128();
    }

    public void create(World world) {
        this.world = world;

        Entity player = createPlayer();
        createCamera(player);
        createBackground();

        generator = new WorldGenerator();

        generateObstacles(1.5f * SCREEN_HEIGHT, player);

        this.state = LEVEL_STATE_RUNNING;
        this.score = 0;
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = new CameraComponent();
        camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
        camera.target = target;

        entity.add(camera);

        engine.addEntity(entity);
    }

    private Entity createPlayer() {
        Entity entity = engine.createEntity();

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        BodyComponent body = engine.createComponent(BodyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        animation.animations.put(PlayerComponent.STATE_NORMAL, Assets.shipNormal);

        bounds.bounds.width = PlayerComponent.WIDTH;
        bounds.bounds.height = PlayerComponent.HEIGHT;

        position.pos.set(5.0f, 2.5f, 0.0f);
        position.scale.set(2.0f / 3.0f, 2.0f / 3.0f);

        // Health
        player.maxHealth = (int) PlayerComponent.STARTING_HEALTH;
        player.currentHealth = player.maxHealth;

        // Create player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.pos.x, position.pos.y);
        body.body = world.createBody(bodyDef);
        body.body.setUserData(this);

        // Define a shape with the vertices
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(PlayerComponent.WIDTH / 2.f, PlayerComponent.HEIGHT / 2.f);

        // Create a fixture with the shape
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // Assign shape to body
        body.body.createFixture(fixtureDef);

        // Clean up
        polygon.dispose();

        state.set(PlayerComponent.STATE_NORMAL);

        entity.add(animation);
        entity.add(bounds);
        entity.add(movement);
        entity.add(position);
        entity.add(player);
        entity.add(body);
        entity.add(state);
        entity.add(texture);

        createHealthBar(entity, null);

        engine.addEntity(entity);

        return entity;
    }

    private void createStructure(float x, float y, Entity player) {
        Entity entity = engine.createEntity();

        BodyComponent body = engine.createComponent(BodyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        StructureComponent structure = engine.createComponent(StructureComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        HeightDisposableComponent disposable = engine.createComponent(HeightDisposableComponent.class);

        // Health
        structure.maxHealth = StructureComponent.STARTING_HEALTH;
        structure.currentHealth = structure.maxHealth;
        structure.target = player;

        state.set(StructureComponent.STATE_ALIVE);

        bounds.bounds.width = StructureComponent.WIDTH;
        bounds.bounds.height = StructureComponent.HEIGHT;

        position.pos.set(x, y, 1.0f);

        texture.region = Assets.terrainRegions[rand.nextInt(Assets.TERRAIN_SPRITES)];

        // Create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.pos.x, position.pos.y);
        body.body = world.createBody(bodyDef);
        body.body.setUserData(this);

        // Define a shape with the vertices
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(StructureComponent.WIDTH / 2.f, StructureComponent.HEIGHT / 2.f);

        // Assign shape to body
        body.body.createFixture(polygon, 0.0f);

        // Clean up
        polygon.dispose();

        entity.add(structure);
        entity.add(bounds);
        entity.add(position);
        entity.add(state);
        entity.add(texture);
        entity.add(body);
        entity.add(disposable);

        createHealthBar(entity, disposable);

        engine.addEntity(entity);
    }

    private void createEnemy(float x, float y, Entity player) {
        Entity entity = engine.createEntity();

        BodyComponent body = engine.createComponent(BodyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        MovementComponent mov = engine.createComponent(MovementComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        HeightDisposableComponent disposable = engine.createComponent(HeightDisposableComponent.class);

        // Health
        enemy.maxHealth = EnemyComponent.STARTING_HEALTH;
        enemy.currentHealth = enemy.maxHealth;
        enemy.target = player;

        state.set(EnemyComponent.STATE_ALIVE);

        bounds.bounds.width = EnemyComponent.WIDTH;
        bounds.bounds.height = EnemyComponent.HEIGHT;

        position.pos.set(x, y, 1f);
        position.scale.set(2f, 2f);

        texture.region = Assets.enemyRegions[rand.nextInt(Assets.ENEMY_SPRITES)];

        // Create player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.pos.x, position.pos.y);
        body.body = world.createBody(bodyDef);
        body.body.setUserData(this);

        // Define a shape with the vertices
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(EnemyComponent.WIDTH / 2.f, EnemyComponent.HEIGHT / 2.f);

        // Create a fixture with the shape
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // Assign shape to body
        body.body.createFixture(fixtureDef);

        // Clean up
        polygon.dispose();

        entity.add(body);
        entity.add(bounds);
        entity.add(enemy);
        entity.add(mov);
        entity.add(position);
        entity.add(state);
        entity.add(texture);
        entity.add(disposable);
        createHealthBar(entity, disposable);

        engine.addEntity(entity);
    }

    public void createBullet(Entity origin) {
        Entity entity = engine.createEntity();

        BodyComponent body = engine.createComponent(BodyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        BulletComponent bullet = engine.createComponent(BulletComponent.class);
        MovementComponent mov = engine.createComponent(MovementComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent pos = engine.createComponent(TransformComponent.class);

        bullet.origin = origin;

        state.set(BulletComponent.STATE_NORMAL);

        bounds.bounds.width = BulletComponent.WIDTH;
        bounds.bounds.height = BulletComponent.HEIGHT;

        texture.region = Assets.bulletRegion;
        if (origin.getComponent(PlayerComponent.class) != null) { // If player fired
            texture.color = Color.WHITE;
        } else { // If enemy or anyone else fired
            texture.color = Color.RED;
        }

        // Get origin position
        float x = origin.getComponent(TransformComponent.class).pos.x;
        float y;

        if (origin.getComponent(PlayerComponent.class) != null) { // If player fired
            y = origin.getComponent(TransformComponent.class).pos.y + origin.getComponent(BoundsComponent.class).bounds.height / 2f;
        } else { // If enemy or anyone else fired
            y = origin.getComponent(TransformComponent.class).pos.y - origin.getComponent(BoundsComponent.class).bounds.height / 2f;
        }

        pos.pos.set(x, y, 1f);
        pos.scale.set(1f, 1f);

        // Create player body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.pos.x, pos.pos.y);
        body.body = world.createBody(bodyDef);
        body.body.setBullet(true);
        body.body.setUserData(this);

        // Define a shape with the vertices
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(BulletComponent.WIDTH / 2.f, BulletComponent.HEIGHT / 2.f);

        // Create a fixture with the shape
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygon;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // Assign shape to body
        body.body.createFixture(fixtureDef);

        // Clean up
        polygon.dispose();

        entity.add(body);
        entity.add(bounds);
        entity.add(bullet);
        entity.add(mov);
        entity.add(state);
        entity.add(texture);
        entity.add(pos);

        engine.addEntity(entity);
    }

    private void createHealthBar(Entity target, HeightDisposableComponent disposable) {
        Entity entity = engine.createEntity();

        if (disposable != null) {
            disposable.childEntity = entity;
        }

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        HealthComponent health = engine.createComponent(HealthComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        health.target = target;
        health.targetPos = target.getComponent(TransformComponent.class).pos;
        health.targetPos.y -= target.getComponent(BoundsComponent.class).bounds.height;

        // Determine type of entity
        if (target.getComponent(PlayerComponent.class) != null) {
            health.maxHealth = PlayerComponent.STARTING_HEALTH * HealthComponent.healthMultiplier;
            health.currentHealth = health.maxHealth;
            health.lengthRatio = 2.0f / 3.0f;
            health.widthRatio = 2.0f / 3.0f;
            health.belongsTo = HealthComponent.IS_PLAYER;
            position.scale.set(health.lengthRatio, health.widthRatio);
        } else if (target.getComponent(StructureComponent.class) != null) {
            health.maxHealth = StructureComponent.STARTING_HEALTH * HealthComponent.healthMultiplier;
            health.currentHealth = health.maxHealth;
            health.lengthRatio = 1.0f / 3.0f;
            health.widthRatio = 2.0f / 3.0f;
            health.belongsTo = HealthComponent.IS_STRUCTURE;
            position.scale.set(health.lengthRatio, health.widthRatio);
        } else if (target.getComponent(EnemyComponent.class) != null) {
            health.maxHealth = EnemyComponent.STARTING_HEALTH * HealthComponent.healthMultiplier;
            health.currentHealth = health.maxHealth;
            health.lengthRatio = 2.0f / 3.0f;
            health.widthRatio = 2.0f / 3.0f;
            health.belongsTo = HealthComponent.IS_ENEMY;
            position.scale.set(health.lengthRatio, health.widthRatio);
        }

        texture.region = Assets.healthRegion;

        entity.add(health);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createBackground() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        texture.region = Assets.backgroundRegion;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    //Makes sure that the world is generated up to the given height, and cleans up entities out of bounds
    public void generateObstacles(float heightSoFar, Entity player) {
        generator.provideWorld(heightSoFar, player);
        playerHeight = heightSoFar;
    }

    private class WorldGenerator {

        /**
         * How far up has been pre-generated
         */
        private float height = 8;
        /**
         * This is the X-coordinate of the path that the player can follow.
         * The path is kept clear of all obstacles.
         */
        private float path = 5.0f;
        /**
         * Each step, path += lastDeltaPath. lastDeltaPath itself changes by a random walk.
         */
        private float lastDeltaPath = 0.0f;

        public void provideWorld(float heightNeeded, Entity player) {
            //Make sure generation has made it off screen above the requested height
            while (height < heightNeeded + 1.5f * SCREEN_HEIGHT) {
                //Advance world generation
                height += StructureComponent.HEIGHT;
                float restrictedArea =
                        PlayerComponent.WIDTH * 1.3f        //Generous width
                                - height / LEVEL_HEIGHT * 0.4f//Minus a difficulty scaling term
                                + lastDeltaPath / 2;            //Plus more if the path is changing sharply, so you can still get through
                for (int i = 0; i <= LEVEL_WIDTH / StructureComponent.WIDTH + 1; i++) {
                    float x = i * StructureComponent.WIDTH;
                    //Check that we're not stomping the path
                    if ((x > path + restrictedArea || x < path - restrictedArea)) {
                        if (rand.nextFloat() < 0.2)
                            createStructure(x, height, player);
                    }
                }

                //Generate enemy
                if (rand.nextFloat() < 0.1) {
                    createEnemy(path, height, player);
                }

                //Move the clear path so you can't just fly in a straight line
                path += lastDeltaPath;

                lastDeltaPath += (rand.nextFloat() - 0.5f)//random-walk term, evenly distributed in +-0.5
                        * (1 + height / LEVEL_HEIGHT);            //plus a difficulty scaling term

                //If the path is outside the world, move it back in, and make it go the other way
                if (path < 0) {
                    path = 0.2f;
                    lastDeltaPath *= -1;
                } else if (path > LEVEL_WIDTH) {
                    path = LEVEL_WIDTH - 0.2f;
                    lastDeltaPath *= -1;
                }
            }
            // create enemies
        }
    }

}
