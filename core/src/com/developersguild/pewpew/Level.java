package com.developersguild.pewpew;

import java.util.Random;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.developersguild.pewpew.components.AnimationComponent;
import com.developersguild.pewpew.components.BackgroundComponent;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.BoundsComponent;
import com.developersguild.pewpew.components.CameraComponent;
import com.developersguild.pewpew.components.HealthComponent;
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
    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20; // I think the second number is the number of screens
    public static final int WORLD_STATE_RUNNING = 0;

    public float heightSoFar;
    public int state;
    public int score;

    private PooledEngine engine;

    public Level(PooledEngine engine) {
        this.engine = engine;
    }

    public void create(World world) {
        Entity player = createPlayer(world);
        createCamera(player);
        createBackground();
        generateLevel(world);

        this.heightSoFar = 0;
        this.state = WORLD_STATE_RUNNING;
        this.score = 0;
    }

    private void generateLevel(World world) {
        // create obstacles
        Random rand=new Random();
        //Wgen is supposed to keep this relatively clear of obstacles
        //It changes by +-deltaPathLinear*random +- deltaPathInverse/random
        float path=5.0f;
        for(float height=8; height<WORLD_HEIGHT; height+=StructureComponent.HEIGHT-rand.nextFloat()) {


            //Keep obstacles outside this distance of path
            float restrictedArea=StructureComponent.WIDTH*0.7f - height/WORLD_HEIGHT*0.4f;
            for(int i=0; i<=WORLD_WIDTH/StructureComponent.WIDTH+1; i++) {
                float x=rand.nextFloat() + i*StructureComponent.WIDTH;
                //Make sure not in restricted area, and skip part of the time
                if((x>path+restrictedArea || x<path-restrictedArea) && rand.nextBoolean())
                    //We might want to make more smaller structures
                    createStructure(
                            rand.nextFloat()<0.8f ? 0 : 1,
                            x,
                            height,
                            world);
            }
            //Move the clear path so you can't just fly in a straight line
            path += height/WORLD_HEIGHT*1.5f * (rand.nextBoolean() ? 1 : -1);
            path += (rand.nextBoolean() ? 1 : -1) * 3.0;

            if(path < 0)path=0.2f;
            else if(path>WORLD_WIDTH)path=WORLD_WIDTH-0.2f;
        }
        // create enemies
    }

    private void createCamera(Entity target) {
        Entity entity = engine.createEntity();

        CameraComponent camera = new CameraComponent();
        camera.camera = engine.getSystem(RenderingSystem.class).getCamera();
        camera.target = target;

        entity.add(camera);

        engine.addEntity(entity);
    }

    private Entity createPlayer(World world) {
        Entity entity = engine.createEntity();

        AnimationComponent animation = engine.createComponent(AnimationComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        MovementComponent movement = engine.createComponent(MovementComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        BodyComponent body = engine.createComponent(BodyComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        animation.animations.put(PlayerComponent.STATE_NORMAL, Assets.shipNormal);

        bounds.bounds.width = PlayerComponent.WIDTH;
        bounds.bounds.height = PlayerComponent.HEIGHT;
        Gdx.app.log(Gdx.app.getClass().getName(), bounds.bounds.width + " " + bounds.bounds.height);

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

        createHealthBar(entity);

        engine.addEntity(entity);

        return entity;
    }

    private void createHealthBar(Entity target) {
        Entity entity = engine.createEntity();

        HealthComponent health = engine.createComponent(HealthComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        health.target = target;
        health.targetPos = target.getComponent(TransformComponent.class).pos;
        health.targetPos.y -= target.getComponent(BoundsComponent.class).bounds.height;

        // Determine type of entity
        if (target.getComponent(PlayerComponent.class) != null) {
            health.maxHealth = PlayerComponent.STARTING_HEALTH * health.healthMultiplier;
            health.currentHealth = health.maxHealth;
            health.lengthRatio = 2.0f / 3.0f;
            health.widthRatio = 2.0f / 3.0f;
            health.isPlayer = true;
            position.scale.set(health.lengthRatio, health.widthRatio);   // TODO: Remove player health bar when we have a health bar gui code; this is for testing
        } else if (target.getComponent(StructureComponent.class) != null) {
            health.maxHealth = StructureComponent.STARTING_HEALTH * health.healthMultiplier;
            health.currentHealth = health.maxHealth;
            health.lengthRatio = 96.0f / 95.0f;
            health.widthRatio = 2.0f / 3.0f;
            health.isStructure = true;
            position.scale.set(health.lengthRatio, health.widthRatio);
        }

        texture.region = Assets.healthRegion;

        entity.add(health);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }

    private void createStructure(int size, float x, float y, World world) {
        createStructure(size, x, y, world, (int) StructureComponent.STARTING_HEALTH);
    }

    private void createStructure(int size, float x, float y, World world, int health) {
        Entity entity = new Entity();

        StructureComponent structure = engine.createComponent(StructureComponent.class);
        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        BodyComponent body = engine.createComponent(BodyComponent.class);

        // Health
        structure.maxHealth = health;
        structure.currentHealth = structure.maxHealth;

        bounds.bounds.width = StructureComponent.WIDTH;
        bounds.bounds.height = StructureComponent.HEIGHT;

        position.pos.set(x, y, 1.0f);

        texture.region = Assets.roofRegion;

        state.set(StructureComponent.STATE_ALIVE);

        structure.size = size;
        if (size == StructureComponent.SIZE_SMALL) {
            // small structure
        } else {
            // large structure
        }

        // Create body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.pos.x, position.pos.y);
        body.body = world.createBody(bodyDef);

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

        createHealthBar(entity);

        engine.addEntity(entity);
    }

    private void createBackground() {
        Entity entity = engine.createEntity();

        BackgroundComponent background = engine.createComponent(BackgroundComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);

        texture.region = Assets.backgroundRegion;

        entity.add(background);
        entity.add(position);
        entity.add(texture);

        engine.addEntity(entity);
    }
}
