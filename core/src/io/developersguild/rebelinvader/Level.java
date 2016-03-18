package io.developersguild.rebelinvader;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import io.developersguild.rebelinvader.components.AnimationComponent;
import io.developersguild.rebelinvader.components.BackgroundComponent;
import io.developersguild.rebelinvader.components.BodyComponent;
import io.developersguild.rebelinvader.components.BoundsComponent;
import io.developersguild.rebelinvader.components.BulletComponent;
import io.developersguild.rebelinvader.components.CameraComponent;
import io.developersguild.rebelinvader.components.EnemyComponent;
import io.developersguild.rebelinvader.components.ExplosionComponent;
import io.developersguild.rebelinvader.components.HealthComponent;
import io.developersguild.rebelinvader.components.HeightDisposableComponent;
import io.developersguild.rebelinvader.components.MissileComponent;
import io.developersguild.rebelinvader.components.MovementComponent;
import io.developersguild.rebelinvader.components.PlayerComponent;
import io.developersguild.rebelinvader.components.PowerComponent;
import io.developersguild.rebelinvader.components.StateComponent;
import io.developersguild.rebelinvader.components.StructureComponent;
import io.developersguild.rebelinvader.components.TextureComponent;
import io.developersguild.rebelinvader.components.TransformComponent;
import io.developersguild.rebelinvader.systems.RenderingSystem;
import io.developersguild.rebelinvader.wgen.WorldGenerator;

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
        createNebula();
        createStars();

        generator = new WorldGenerator(this);

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
        createPowerBar(entity, null);

        engine.addEntity(entity);

        return entity;
    }

    public void createStructure(float x, float y, Entity player, TextureRegion region) {
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
        position.scale.set(1f, 1f);

        texture.region = region;

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

    public void createEnemy(float x, float y, Entity player, int textureIdx) {
        Entity entity = engine.createEntity();

        // Clamp x so the enemy doesn't get spawned off-screen
        if(x < 1.0f) x = 1.0f;
        else if(x > 9.0f) x = 9.0f;

//        System.out.println("Enemy at " + x + ", " + y);

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

        texture.region = Assets.enemyRegions[textureIdx];

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
            // Add randomized horizontal velocity to the bullet
            float randomDegree = MathUtils.random(-BulletComponent.HORIZONTAL_SHIFT_DEGREE, BulletComponent.HORIZONTAL_SHIFT_DEGREE);
            float randomRadian = randomDegree * MathUtils.degreesToRadians;
            bullet.HORIZONTAL_VELOCITY = BulletComponent.PLAYER_BULLET_VELOCITY * (float) Math.tan(randomRadian);
        } else { // If enemy or anyone else fired
            y = origin.getComponent(TransformComponent.class).pos.y - origin.getComponent(BoundsComponent.class).bounds.height / 2f;
        }

        pos.pos.set(x, y, 1f);
        pos.scale.set(1f, 1f);

        // Create bullet body
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

    public void createMissile(Entity origin) {
        Entity entity = engine.createEntity();

        BodyComponent body = engine.createComponent(BodyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MissileComponent missile = engine.createComponent(MissileComponent.class);
        MovementComponent mov = engine.createComponent(MovementComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent pos = engine.createComponent(TransformComponent.class);

        missile.origin = origin;

        state.set(MissileComponent.STATE_NORMAL);

        missile.accelerator = 0;
        missile.currentTime = 0;

        bounds.bounds.width = MissileComponent.WIDTH;
        bounds.bounds.height = MissileComponent.HEIGHT;

        texture.region = Assets.missileRegion;

        // Get origin position
        float x = origin.getComponent(TransformComponent.class).pos.x;
        float y = origin.getComponent(TransformComponent.class).pos.y + origin.getComponent(BoundsComponent.class).bounds.height / 2f;

        pos.pos.set(x, y, 1f);
        pos.scale.set(1f, 1f);

        // Create missile body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.pos.x, pos.pos.y);
        body.body = world.createBody(bodyDef);
        body.body.setBullet(true);
        body.body.setUserData(this);

        // Define a shape with the vertices
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(MissileComponent.WIDTH / 2.f, MissileComponent.HEIGHT / 2.f);

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
        entity.add(missile);
        entity.add(mov);
        entity.add(state);
        entity.add(texture);
        entity.add(pos);

        engine.addEntity(entity);
    }

    public void createExplosion(Entity origin) {
        Entity entity = engine.createEntity();

        BodyComponent body = engine.createComponent(BodyComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        ExplosionComponent explosion = engine.createComponent(ExplosionComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent pos = engine.createComponent(TransformComponent.class);

        explosion.origin = origin;

        explosion.currentTime = 0;

        bounds.bounds.width = ExplosionComponent.WIDTH;
        bounds.bounds.height = ExplosionComponent.HEIGHT;

        texture.region = Assets.explosionTexRegion;

        // Get origin position
        float x = origin.getComponent(TransformComponent.class).pos.x;
        float y = origin.getComponent(TransformComponent.class).pos.y + origin.getComponent(BoundsComponent.class).bounds.height / 2f;

        float scaleRatio = 2.0f;

        pos.pos.set(x, y, 1f);
        pos.scale.set(scaleRatio, scaleRatio);

        // Create explosion body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(pos.pos.x, pos.pos.y);
        body.body = world.createBody(bodyDef);
        //body.body.setBullet(true);
        body.body.setUserData(this);

        // Define a shape with the vertices
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(bounds.bounds.width / 2f * scaleRatio, bounds.bounds.height / 2f * scaleRatio);

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
        entity.add(explosion);
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
        health.doRender = false;

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

        if (target.getComponent(PlayerComponent.class) != null)
            texture.region = Assets.healthRegionGreen;
        else texture.region = Assets.healthRegionRed;

        entity.add(health);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createPowerBar(Entity target, HeightDisposableComponent disposable) {
        Entity entity = engine.createEntity();

        if (disposable != null) {
            disposable.childEntity = entity;
        }

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        PowerComponent power = engine.createComponent(PowerComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        power.target = target;
        power.targetPos = target.getComponent(TransformComponent.class).pos;
        power.targetPos.y -= target.getComponent(BoundsComponent.class).bounds.height;

        // Determine type of entity
        if (target.getComponent(PlayerComponent.class) != null) {
            power.maxPower = PowerComponent.MAX_POWER;
            power.currentPower = 0;
            power.lengthRatio = 2.0f / 3.0f;
            power.widthRatio = 2.0f / 3.0f;
            position.scale.set(power.lengthRatio, power.widthRatio);
        }

        // Prevent overlap with health bar
        power.targetPos.y -= power.widthRatio;

        // Need a different texture
        texture.region = Assets.healthRegionBlue;

        entity.add(power);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createNebula() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        background.type = BackgroundComponent.TYPE_NEBULA;

        texture.region = Assets.bgNebulaRegion;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createStars() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);

        background.type = BackgroundComponent.TYPE_STARS;

        texture.region = Assets.bgStarsRegion;

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
}
